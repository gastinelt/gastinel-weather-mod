package com.gastinelweather.weather;

import org.joml.Vector3f;

/**
 * Utilitaires mathématiques déterministes pour le moteur météo.
 * Porté de Vercora (C++) vers Java.
 */
public final class MathUtils {

    public static final float PI = 3.14159265358979323846f;
    public static final float TWO_PI = 2.0f * PI;

    private MathUtils() {}

    public static float clamp(float value, float min, float max) {
        if (Float.isNaN(value)) return min;
        return Math.max(min, Math.min(max, value));
    }

    public static float clamp01(float value) {
        return clamp(value, 0.0f, 1.0f);
    }

    public static float mix(float a, float b, float t) {
        return a + t * (b - a);
    }

    public static float safeFinite(float value, float fallback) {
        return Float.isFinite(value) ? value : fallback;
    }

    public static float smootherStep(float t) {
        t = clamp01(t);
        return t * t * t * (t * (t * 6.0f - 15.0f) + 10.0f);
    }

    /**
     * Hachage lowbias32 de Chris Wellons : mélange complet et déterministe.
     */
    public static int hashU32(int x) {
        x ^= x >>> 16;
        x *= 0x7feb352d;
        x ^= x >>> 15;
        x *= 0x846ca68b;
        x ^= x >>> 16;
        return x;
    }

    public static float nodeGradient(int node, int seed) {
        int h = hashU32((node * 0x9E3779B9) ^ seed);
        // Conversion en float dans [-1, 1)
        long unsignedH = Integer.toUnsignedLong(h);
        return (float) (unsignedH * (2.0 / 4294967296.0) - 1.0);
    }

    public static float gradientNoise1D(float x, int seed) {
        float floored = (float) Math.floor(x);
        int i0 = (int) floored;
        float f = x - floored;

        float g0 = nodeGradient(i0, seed);
        float g1 = nodeGradient(i0 + 1, seed);

        float n0 = g0 * f;
        float n1 = g1 * (f - 1.0f);

        return 2.0f * (n0 + (n1 - n0) * smootherStep(f));
    }

    public static float fbm1D(float x, int seed, int octaves) {
        octaves = Math.max(1, Math.min(8, octaves));

        float sum = 0.0f;
        float amplitude = 1.0f;
        float frequency = 1.0f;
        float total = 0.0f;

        for (int i = 0; i < octaves; ++i) {
            sum += amplitude * gradientNoise1D(x * frequency, seed + i * 0x9E3779B9);
            total += amplitude;
            amplitude *= 0.5f;
            frequency *= 2.0f;
        }

        return total > 0.0f ? sum / total : 0.0f;
    }

    /**
     * Modèle astronomique de Cooper (1969) pour la déclinaison solaire.
     */
    public static float solarDeclinationDegrees(float dayOfYear) {
        return 23.44f * (float) Math.sin(TWO_PI * (284.0f + dayOfYear) / 365.0f);
    }

    /**
     * Convertit un cap compas (0° = Nord, 90° = Est, 180° = Sud, 270° = Ouest)
     * en vecteur directionnel unitaire dans le repère Minecraft (+X = Est, +Z = Sud).
     */
    public static Vector3f compassDirection(float degrees) {
        float rad = (float) Math.toRadians(degrees);
        return new Vector3f((float) Math.sin(rad), 0.0f, -(float) Math.cos(rad));
    }

    /**
     * Formule officielle internationale JAG/TI (Joint Action Group for Temperature Indices)
     * pour le refroidissement éolien (Wind Chill).
     */
    public static float windChillC(float temperatureC, float windMps) {
        float t = safeFinite(temperatureC, 0.0f);
        float v = Math.max(0.0f, safeFinite(windMps, 0.0f));
        float kmh = v * 3.6f;

        if (t > 10.0f || kmh < 4.8f) {
            return t;
        }

        double p = Math.pow(kmh, 0.16);
        float chilled = (float) (13.12 + 0.6215 * t - 11.37 * p + 0.3965 * t * p);
        return Math.min(chilled, t);
    }
}
