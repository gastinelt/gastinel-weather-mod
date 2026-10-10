package com.gastinelweather.weather;

import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

/**
 * Système météorologique déterministe pur (porté de Vercora).
 * Météo = f(Position(x, y, z), Instant t, Seed).
 */
public class WeatherSystem {

    public static final int DEFAULT_SEED = 0x5EA50E;

    private final WeatherParams params;
    private final int seed;

    public WeatherSystem() {
        this(new WeatherParams(), DEFAULT_SEED);
    }

    public WeatherSystem(WeatherParams params, int seed) {
        this.params = params != null ? params : new WeatherParams();
        this.seed = seed;
    }

    public WeatherParams getParams() {
        return params;
    }

    public int getSeed() {
        return seed;
    }

    // =======================================================================
    // COUCHE 1 : État synoptique macro (0D, purement temporel)
    // =======================================================================

    private float frontNoise(float gameHours, int channel) {
        float periodHours = Math.max(1.0f, params.frontPeriodDays * 24.0f);
        int channelSeed = seed ^ MathUtils.hashU32(channel * 0x85EBCA6B + 1);
        float raw = MathUtils.fbm1D(gameHours / periodHours, channelSeed, params.frontOctaves);
        return MathUtils.clamp(raw * params.frontNoiseGain, -1.0f, 1.0f);
    }

    /**
     * Renfort du vent par la pluie et l'orage Minecraft (niveaux [0, 1] du monde).
     * Même formule dans le shader GastinelWind (gwStormFactor) pour que l'herbe et l'anémomètre soient d'accord.
     */
    public float stormWindFactor(float rainLevel, float thunderLevel) {
        float rain = MathUtils.clamp01(MathUtils.safeFinite(rainLevel, 0.0f));
        float thunder = MathUtils.clamp01(MathUtils.safeFinite(thunderLevel, 0.0f));
        return 1.0f + params.rainWindBoost * rain + params.thunderWindBoost * thunder;
    }

    /**
     * Même terrain, sans la pluie/l'orage Minecraft : la météo vanilla n'est pas prévisible,
     * les prévisions au-delà de l'heure en cours ne l'utilisent donc pas.
     */
    private static HeightSampler withoutVanillaWeather(HeightSampler terrain) {
        return terrain == null ? null : terrain::heightAt;
    }

    public SynopticState synopticAt(float gameHours) {
        SynopticState sky = new SynopticState();
        sky.gameHours = MathUtils.safeFinite(gameHours, 0.0f);

        // --- Calendrier ---
        sky.timeOfDayHours = (float) ((sky.gameHours % 24.0 + 24.0) % 24.0);
        sky.dayOfYear = (float) (((params.startDayOfYear + sky.gameHours / 24.0) % 365.0 + 365.0) % 365.0);

        // --- Position Solaire ---
        float declination = MathUtils.solarDeclinationDegrees(sky.dayOfYear);
        float latRad = (float) Math.toRadians(params.latitudeDegrees);
        float decRad = (float) Math.toRadians(declination);
        float hourAngle = (float) Math.toRadians(15.0f * (sky.timeOfDayHours - 12.0f));

        float sinElevation = MathUtils.clamp(
            (float) (Math.sin(latRad) * Math.sin(decRad) + Math.cos(latRad) * Math.cos(decRad) * Math.cos(hourAngle)),
            -1.0f, 1.0f
        );
        float elevation = (float) Math.asin(sinElevation);
        sky.sunElevationDegrees = (float) Math.toDegrees(elevation);

        float cosElevation = (float) Math.cos(elevation);
        float azimuth = 180.0f; // Plein sud
        if (cosElevation > 1e-4f && (float) Math.cos(latRad) > 1e-4f) {
            float cosAzimuth = MathUtils.clamp(
                (float) ((Math.sin(decRad) - sinElevation * Math.sin(latRad)) / (cosElevation * Math.cos(latRad))),
                -1.0f, 1.0f
            );
            azimuth = (float) Math.toDegrees(Math.acos(cosAzimuth));
            if (hourAngle > 0.0f) {
                azimuth = 360.0f - azimuth;
            }
        }
        sky.sunAzimuthDegrees = azimuth;

        Vector3f horizontal = MathUtils.compassDirection(azimuth);
        sky.sunDirection = new Vector3f(horizontal.x * cosElevation, sinElevation, horizontal.z * cosElevation);
        if (sky.sunDirection.lengthSquared() > 1e-6f) {
            sky.sunDirection.normalize();
        } else {
            sky.sunDirection.set(0.0f, 1.0f, 0.0f);
        }
        sky.daylight = Math.max(0.0f, sinElevation);

        // --- Nébulosité (Canal 1) ---
        float cloudNoise = frontNoise(sky.gameHours, 1);
        sky.cloudCover = MathUtils.clamp01(params.meanCloudCover + params.cloudAmplitude * cloudNoise);

        // --- Température de Référence (Niveau Mer) ---
        float seasonPhase = MathUtils.TWO_PI * (sky.dayOfYear - params.seasonalPeakDay) / 365.0f;
        float seasonal = params.seasonalAmplitudeC * (float) Math.cos(seasonPhase) +
                         params.seasonalHarmonic2C * (float) Math.cos(2.0f * seasonPhase);

        float diurnalPhase = MathUtils.TWO_PI * (sky.timeOfDayHours - params.diurnalPeakHour) / 24.0f;
        float cloudDamping = 1.0f - params.cloudDiurnalDamping * sky.cloudCover;
        float diurnal = (params.diurnalAmplitudeC * (float) Math.cos(diurnalPhase) +
                         params.diurnalHarmonic2C * (float) Math.cos(2.0f * diurnalPhase)) * cloudDamping;

        float airMass = params.frontTemperatureAmplitudeC * frontNoise(sky.gameHours, 0);
        sky.seaLevelTemperatureC = params.seaLevelAnnualMeanC + seasonal + diurnal + airMass;

        // --- Vent Synoptique (Canaux 2 et 3) ---
        float windNoise = Math.abs(frontNoise(sky.gameHours, 2));
        sky.seaLevelWindMps = Math.max(0.0f, params.baseWindMps + params.frontWindAmplitudeMps * windNoise);

        float directionNoise = frontNoise(sky.gameHours, 3);
        sky.windFromDegrees = (params.meanWindFromDegrees + params.windDirectionSwingDegrees * directionNoise) % 360.0f;
        if (sky.windFromDegrees < 0.0f) sky.windFromDegrees += 360.0f;

        // --- Précipitations ---
        float threshold = MathUtils.clamp(params.precipitationCloudThreshold, 0.0f, 0.999f);
        if (sky.cloudCover > threshold) {
            float excess = (sky.cloudCover - threshold) / (1.0f - threshold);
            sky.precipitationMmPerHour = params.precipitationMaxMmPerHour * MathUtils.clamp01(excess);
        } else {
            sky.precipitationMmPerHour = 0.0f;
        }

        return sky;
    }

    // =======================================================================
    // COUCHE 2 : Spatialisation Micro-Climatique (3D)
    // =======================================================================

    public float aspectSignal(Vector3f normal, Vector3f sunDirection, boolean sunUp) {
        float incidence = sunUp ? Math.max(0.0f, normal.dot(sunDirection)) : 0.0f;
        float flatIncidence = Math.max(0.0f, sunDirection.y);
        return MathUtils.clamp((incidence - flatIncidence) / Math.max(1e-3f, params.aspectIncidenceReference), -1.0f, 1.0f);
    }

    public WeatherSample sampleAt(Vector3f position, float gameHours, HeightSampler terrain) {
        return sampleAt(position, synopticAt(gameHours), terrain);
    }

    public WeatherSample sampleAt(Vector3f position, SynopticState sky, HeightSampler terrain) {
        WeatherSample out = new WeatherSample();

        float x = MathUtils.safeFinite(position.x, 0.0f);
        float y = MathUtils.safeFinite(position.y, params.seaLevelAltitudeBlocks);
        float z = MathUtils.safeFinite(position.z, 0.0f);

        // Altitude relative par rapport au niveau de la mer
        float altitudeKm = (y - params.seaLevelAltitudeBlocks) / 1000.0f;
        out.cloudCover = sky.cloudCover;

        Vector3f normal = new Vector3f(0.0f, 1.0f, 0.0f);
        if (terrain != null) {
            normal = terrain.normalAt(x, z, 2.0f);
        }

        // 1. Température : Gradient altitudinal
        out.lapseC = -params.lapseRateCPerKm * altitudeKm;

        // Adret / Ubac
        float signal = aspectSignal(normal, sky.sunDirection, sky.daylight > 0.0f);
        float sunStrength = MathUtils.clamp01(sky.daylight / Math.max(1e-3f, params.aspectFullDaylightSin));
        out.aspectC = params.aspectBonusC * signal * sunStrength * (1.0f - params.cloudAspectDamping * sky.cloudCover);

        // 2. Vent Local & Sondage Amont
        float referenceWind = Math.max(0.0f, sky.seaLevelWindMps + params.windAltitudeGainMpsPerKm * altitudeKm);
        if (terrain != null) {
            referenceWind *= stormWindFactor(terrain.rainLevel(), terrain.thunderLevel());
        }
        Vector3f fromWind = MathUtils.compassDirection(sky.windFromDegrees);
        Vector3f flow = new Vector3f(fromWind).negate();

        float speedFactor = 1.0f;
        float exposureSignal = 0.0f;
        float slopeAlongFlow = 0.0f;

        if (terrain != null) {
            float h0 = terrain.heightAt(x, z);
            float hNear = terrain.heightAt(x + fromWind.x * params.upwindNearMeters, z + fromWind.z * params.upwindNearMeters);
            float hMid = terrain.heightAt(x + fromWind.x * params.upwindMidMeters, z + fromWind.z * params.upwindMidMeters);
            float hFar = terrain.heightAt(x + fromWind.x * params.upwindFarMeters, z + fromWind.z * params.upwindFarMeters);

            float relief = Math.max(1.0f, params.exposureReliefMeters);
            float shelter = Math.max(
                MathUtils.clamp01((hNear - h0) / relief),
                Math.max(MathUtils.clamp01((hMid - h0) / relief), MathUtils.clamp01((hFar - h0) / relief))
            );
            float dominance = Math.min(
                MathUtils.clamp01((h0 - hNear) / relief),
                Math.min(MathUtils.clamp01((h0 - hMid) / relief), MathUtils.clamp01((h0 - hFar) / relief))
            );

            exposureSignal = MathUtils.clamp(dominance - shelter, -1.0f, 1.0f);
            speedFactor = 1.0f + (exposureSignal > 0.0f ? exposureSignal * params.exposureWindGain : exposureSignal * params.shelterWindReduction);

            // Venturi
            Vector3f lateral = new Vector3f(flow.z, 0.0f, -flow.x);
            float hLeft = terrain.heightAt(x + lateral.x * params.lateralMeters, z + lateral.z * params.lateralMeters);
            float hRight = terrain.heightAt(x - lateral.x * params.lateralMeters, z - lateral.z * params.lateralMeters);
            float gap = Math.min(hLeft, hRight) - h0;
            float venturiSignal = MathUtils.clamp01(gap / Math.max(1.0f, params.venturiReliefMeters));
            float venturiWeight = MathUtils.clamp01(0.5f + 0.5f * exposureSignal);
            speedFactor *= 1.0f + params.venturiWindGain * venturiSignal * venturiWeight;

            slopeAlongFlow = (h0 - hNear) / Math.max(1.0f, params.upwindNearMeters);
        }

        out.windMps = MathUtils.clamp(referenceWind * speedFactor, 0.0f, params.maxLocalWindMps);
        out.windVector = new Vector3f(flow).mul(out.windMps);

        // 3. Composantes Verticales
        if (slopeAlongFlow > 0.0f) {
            out.ridgeLiftMps = Math.min(params.maxRidgeLiftMps, params.ridgeLiftEfficiency * slopeAlongFlow * out.windMps);
        }

        float leeFactor = MathUtils.clamp01(-exposureSignal);
        float windIntensity = MathUtils.clamp01(referenceWind / Math.max(1.0f, params.turbulenceFullWindMps));
        out.subsidenceMps = params.leeSubsidenceMps * leeFactor * windIntensity;
        out.turbulence = MathUtils.clamp01(params.leeTurbulenceMax * leeFactor * windIntensity);

        // Thermiques
        float incidence = (sky.daylight > 0.0f) ? Math.max(0.0f, normal.dot(sky.sunDirection)) : 0.0f;
        if (sky.daylight > 0.0f && incidence > 0.0f) {
            float hourOffset = (sky.timeOfDayHours - params.thermalPeakHour) / Math.max(0.5f, params.thermalHalfWidthHours);
            float timeWindow = Math.max(0.0f, 1.0f - hourOffset * hourOffset);
            float cloudFactor = 1.0f - params.cloudThermalDamping * sky.cloudCover;
            out.thermalMps = params.thermalMaxMps * incidence * timeWindow * Math.max(0.0f, cloudFactor);
        }

        out.verticalMps = out.ridgeLiftMps + out.thermalMps + out.subsidenceMps;

        // 4. Précipitations Orographiques & Sol
        float orographicSignal = MathUtils.clamp01(slopeAlongFlow / Math.max(1e-3f, params.orographicFullSlope));
        float orographic = (1.0f + params.orographicGain * orographicSignal) * (1.0f + params.precipitationAltitudeGainPerKm * altitudeKm);

        out.precipitationMmPerHour = Math.max(0.0f, sky.precipitationMmPerHour * orographic);
        out.cloudCover = MathUtils.clamp01(sky.cloudCover + params.orographicCloudGain * orographicSignal);

        out.temperatureC = sky.seaLevelTemperatureC + out.lapseC + out.aspectC;
        out.inversionC = 0.0f; // Piégeage local si terrain analysé

        out.snowing = out.precipitationMmPerHour > 0.05f && out.temperatureC <= params.snowThresholdC;
        out.wetness = groundWetness(sky.gameHours, y, orographic);
        out.windChillC = MathUtils.windChillC(out.temperatureC, out.windMps);

        return out;
    }

    public float groundWetness(float gameHours, float altitudeMeters, float orographicFactor) {
        float now = MathUtils.safeFinite(gameHours, 0.0f);
        int samples = Math.max(1, Math.min(32, params.wetnessSamples));
        float window = Math.max(0.1f, params.wetnessMemoryHours);
        float drying = Math.max(0.1f, params.dryingTimeHours);
        float oro = Math.max(0.0f, MathUtils.safeFinite(orographicFactor, 1.0f));
        float altitudeKm = (MathUtils.safeFinite(altitudeMeters, params.seaLevelAltitudeBlocks) - params.seaLevelAltitudeBlocks) / 1000.0f;
        float altitudeGain = 1.0f + params.precipitationAltitudeGainPerKm * altitudeKm;

        float wettest = 0.0f;
        for (int i = 0; i < samples; ++i) {
            float age = window * (i + 0.5f) / (float) samples;
            SynopticState past = synopticAt(now - age);
            float precip = past.precipitationMmPerHour * oro * altitudeGain;
            float soaked = MathUtils.clamp01(precip / Math.max(0.01f, params.wetnessSaturationMmPerHour));
            float dried = (float) Math.exp(-age / drying);
            wettest = Math.max(wettest, soaked * dried);
        }
        return MathUtils.clamp01(wettest);
    }

    // =======================================================================
    // MODULE DE PRÉDICTION MÉTÉOROLOGIQUE (Fonction pure & déterministe)
    // =======================================================================

    public record HourlyForecast(float gameHours, float timeOfDayHours, float temperatureC, float windKmh, float precipitationMm, float cloudPercent, boolean willRain, boolean willSnow) {}

    public List<HourlyForecast> predictHourlyForecast(float currentHours, int hoursAhead, Vector3f pos, HeightSampler terrain) {
        List<HourlyForecast> list = new ArrayList<>(hoursAhead);
        for (int h = 0; h <= hoursAhead; h++) {
            float t = currentHours + h;
            WeatherSample sample = sampleAt(pos, t, h == 0 ? terrain : withoutVanillaWeather(terrain));
            SynopticState sky = synopticAt(t);
            list.add(new HourlyForecast(
                t,
                sky.timeOfDayHours,
                sample.temperatureC,
                sample.getWindSpeedKmh(),
                sample.precipitationMmPerHour,
                sample.cloudCover * 100.0f,
                sample.precipitationMmPerHour > 0.05f && !sample.snowing,
                sample.snowing
            ));
        }
        return list;
    }

    public record DailyForecast(int dayNumber, float minTempC, float maxTempC, float maxWindKmh, float totalRainMm, float maxCloudPercent, boolean hasRain, boolean hasSnow) {}

    public List<DailyForecast> predictDailyForecast(float currentHours, int daysAhead, Vector3f pos, HeightSampler terrain) {
        List<DailyForecast> list = new ArrayList<>(daysAhead);
        int currentDay = (int) (currentHours / 24.0f);
        HeightSampler calm = withoutVanillaWeather(terrain);

        for (int d = 0; d < daysAhead; d++) {
            int targetDay = currentDay + d;
            float minT = 999.0f;
            float maxT = -999.0f;
            float maxWind = 0.0f;
            float totalRain = 0.0f;
            float maxCloud = 0.0f;
            boolean hasRain = false;
            boolean hasSnow = false;

            for (int h = 0; h < 24; h += 2) {
                float t = targetDay * 24.0f + h;
                WeatherSample s = sampleAt(pos, t, calm);
                minT = Math.min(minT, s.temperatureC);
                maxT = Math.max(maxT, s.temperatureC);
                maxWind = Math.max(maxWind, s.getWindSpeedKmh());
                maxCloud = Math.max(maxCloud, s.cloudCover * 100.0f);
                if (s.precipitationMmPerHour > 0.05f) {
                    totalRain += s.precipitationMmPerHour * 2.0f;
                    if (s.snowing) hasSnow = true;
                    else hasRain = true;
                }
            }

            list.add(new DailyForecast(targetDay, minT, maxT, maxWind, totalRain, maxCloud, hasRain, hasSnow));
        }
        return list;
    }
}
