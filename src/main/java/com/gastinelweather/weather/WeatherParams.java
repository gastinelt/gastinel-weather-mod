package com.gastinelweather.weather;

/**
 * Paramètres et constantes météorologiques du modèle.
 * Regroupe tous les réglages physiques et d'équilibrage de la simulation.
 */
public class WeatherParams {

    // --- 1. Temps et Saison ---
    public float startDayOfYear = 135.0f;       // Mi-mai (printemps)
    public float latitudeDegrees = 45.0f;       // 45° Nord (climat tempéré / alpin)

    // --- 2. Température de référence (Niveau de la mer) ---
    public float seaLevelAnnualMeanC = 11.0f;
    public float seasonalAmplitudeC = 9.0f;
    public float seasonalPeakDay = 205.0f;      // Fin juillet
    public float seasonalHarmonic2C = 1.2f;
    public float diurnalAmplitudeC = 8.0f;
    public float diurnalPeakHour = 15.0f;       // Maximum thermique à 15h
    public float diurnalHarmonic2C = 1.0f;
    public float cloudDiurnalDamping = 0.60f;
    public float lapseRateCPerKm = 6.5f;        // Gradient altitudinal adiabatique standard

    // Altitude de référence (niveau de la mer Minecraft)
    public float seaLevelAltitudeBlocks = 64.0f;

    // --- 3. Perturbations et Masses d'Air ---
    public float frontPeriodDays = 3.5f;        // Période caractéristique des fronts météo
    public int frontOctaves = 3;
    public float frontNoiseGain = 2.2f;
    public float frontTemperatureAmplitudeC = 4.0f;
    public float meanCloudCover = 0.45f;
    public float cloudAmplitude = 0.50f;
    public float precipitationCloudThreshold = 0.72f;
    public float precipitationMaxMmPerHour = 6.0f;
    public float snowThresholdC = 1.0f;         // Neige sous 1°C

    // --- 4. Vent Synoptique ---
    public float meanWindFromDegrees = 270.0f;  // Régime dominant d'Ouest
    public float windDirectionSwingDegrees = 75.0f;
    public float baseWindMps = 3.0f;            // Brise permanente minimale
    public float frontWindAmplitudeMps = 9.0f;
    public float windAltitudeGainMpsPerKm = 4.0f;
    // Renfort du vent par la météo Minecraft (pluie, orage) : vent x (1 + rainWindBoost x pluie + thunderWindBoost x orage).
    // Le shader GastinelWind recopie ces deux valeurs (lib/wind.glsl) : les modifier des deux côtés.
    public float rainWindBoost = 0.5f;
    public float thunderWindBoost = 0.8f;

    // --- 5. Spatialisation & Ensoleillement (Adret/Ubac) ---
    public float aspectBonusC = 4.0f;
    public float cloudAspectDamping = 0.85f;
    public float aspectFullDaylightSin = 0.25f;
    public float aspectIncidenceReference = 0.35f;
    public float inversionMaxC = -6.0f;
    public float inversionMixingWindMps = 4.0f;

    // --- 6. Sondage Amont & Relief ---
    public float upwindNearMeters = 30.0f;      // Blocs Minecraft
    public float upwindMidMeters = 75.0f;
    public float upwindFarMeters = 200.0f;
    public float downwindMeters = 15.0f;
    public float lateralMeters = 40.0f;

    public float exposureReliefMeters = 20.0f;
    public float exposureWindGain = 0.80f;
    public float shelterWindReduction = 0.75f;
    public float venturiReliefMeters = 25.0f;
    public float venturiWindGain = 0.70f;
    public float maxLocalWindMps = 35.0f;

    // --- Composantes Verticales & Vol ---
    public float ridgeLiftEfficiency = 0.55f;
    public float maxRidgeLiftMps = 6.0f;
    public float leeSubsidenceMps = -3.0f;
    public float leeTurbulenceMax = 1.0f;
    public float turbulenceFullWindMps = 12.0f;
    public float thermalMaxMps = 3.5f;
    public float thermalPeakHour = 14.0f;
    public float thermalHalfWidthHours = 4.5f;
    public float cloudThermalDamping = 0.80f;

    // --- Précipitations Orographiques & Sol ---
    public float orographicGain = 0.90f;
    public float orographicFullSlope = 0.35f;
    public float orographicCloudGain = 0.15f;
    public float precipitationAltitudeGainPerKm = 0.35f;
    public float wetnessMemoryHours = 6.0f;
    public int wetnessSamples = 8;
    public float wetnessSaturationMmPerHour = 3.0f;
    public float dryingTimeHours = 4.0f;
}
