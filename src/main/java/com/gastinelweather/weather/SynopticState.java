package com.gastinelweather.weather;

import org.joml.Vector3f;

/**
 * État synoptique macro du monde à un instant donné (indépendant de la position 3D).
 */
public class SynopticState {

    public float gameHours = 0.0f;
    public float timeOfDayHours = 12.0f;
    public float dayOfYear = 0.0f;

    public float seaLevelTemperatureC = 0.0f;
    public float windFromDegrees = 270.0f;
    public float seaLevelWindMps = 0.0f;
    public float cloudCover = 0.0f;               // [0, 1]
    public float precipitationMmPerHour = 0.0f;

    public float sunElevationDegrees = 0.0f;
    public float sunAzimuthDegrees = 180.0f;
    public float daylight = 0.0f;                 // [0, 1]
    public Vector3f sunDirection = new Vector3f(0.0f, 1.0f, 0.0f);

    public boolean isRaining() {
        return precipitationMmPerHour > 0.05f;
    }

    public String describe() {
        int day = (int) (gameHours / 24.0f);
        return String.format(
            "Jour +%d %.1fh : Température réf = %.1f°C | Vent = %.1f m/s (%.0f km/h) de %.0f° | Nuages = %.0f%% | Pluie = %.1f mm/h | Soleil = %.1f°",
            day, timeOfDayHours, seaLevelTemperatureC, seaLevelWindMps, seaLevelWindMps * 3.6f,
            windFromDegrees, cloudCover * 100.0f, precipitationMmPerHour, sunElevationDegrees
        );
    }
}
