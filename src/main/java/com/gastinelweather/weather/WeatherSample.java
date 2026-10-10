package com.gastinelweather.weather;

import org.joml.Vector3f;

/**
 * Échantillon météo microclimatique spatialisé en un point (x, y, z) du monde.
 */
public class WeatherSample {

    // --- Température ---
    public float temperatureC = 0.0f;
    public float lapseC = 0.0f;          // Gradient altitudinal (< 0 en altitude)
    public float aspectC = 0.0f;         // Ensoleillement adret / ubac (> 0 si chauffé)
    public float inversionC = 0.0f;      // Inversion thermique de cuvette (<= 0)
    public float windChillC = 0.0f;      // Température ressentie au vent

    // --- Vent Horizontal ---
    public float windMps = 0.0f;
    public Vector3f windVector = new Vector3f(0.0f);

    // --- Composantes Verticales ---
    public float verticalMps = 0.0f;
    public float ridgeLiftMps = 0.0f;    // Pente dynamique
    public float thermalMps = 0.0f;      // Convection thermique
    public float subsidenceMps = 0.0f;   // Rabattant sous le vent
    public float turbulence = 0.0f;      // Turbulence [0, 1]

    // --- Humidité & Précipitations ---
    public float cloudCover = 0.0f;
    public float precipitationMmPerHour = 0.0f;
    public boolean snowing = false;
    public float wetness = 0.0f;         // Mouillure du sol [0, 1]

    public float getWindSpeedKmh() {
        return windMps * 3.6f;
    }

    public String describe() {
        return String.format(
            "Local : %.1f°C (Ressenti %.1f°C | Alt %.1f°C, Versant %+.1f°C, Inversion %+.1f°C) | Vent = %.1f m/s (%.0f km/h) | Vertical = %+.2f m/s (Thermique %+.2f, Pente %+.2f) | %s | Sol = %.0f%% mouillé",
            temperatureC, windChillC, lapseC, aspectC, inversionC,
            windMps, getWindSpeedKmh(), verticalMps, thermalMps, ridgeLiftMps,
            (snowing ? "NEIGE (" + String.format("%.1f", precipitationMmPerHour) + " mm/h)" : (precipitationMmPerHour > 0.05f ? "PLUIE (" + String.format("%.1f", precipitationMmPerHour) + " mm/h)" : "SEC")),
            wetness * 100.0f
        );
    }
}
