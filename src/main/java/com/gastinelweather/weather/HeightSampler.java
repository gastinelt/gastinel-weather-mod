package com.gastinelweather.weather;

import org.joml.Vector3f;

/**
 * Interface pour échantillonner la hauteur et la géométrie du terrain (Minecraft ou simulé).
 */
@FunctionalInterface
public interface HeightSampler {

    /**
     * Renvoie la hauteur de la surface du terrain aux coordonnées monde (x, z).
     */
    float heightAt(float worldX, float worldZ);

    /**
     * Calcule la normale unitaire de la surface par différences finies.
     */
    default Vector3f normalAt(float worldX, float worldZ, float sampleStep) {
        float s = sampleStep > 0.0f ? sampleStep : 2.0f;

        float dhdx = (heightAt(worldX + s, worldZ) - heightAt(worldX - s, worldZ)) / (2.0f * s);
        float dhdz = (heightAt(worldX, worldZ + s) - heightAt(worldX, worldZ - s)) / (2.0f * s);

        Vector3f normal = new Vector3f(-dhdx, 1.0f, -dhdz);
        if (normal.lengthSquared() > 1e-6f) {
            normal.normalize();
        } else {
            normal.set(0.0f, 1.0f, 0.0f);
        }
        return normal;
    }

    /**
     * Niveau de pluie Minecraft [0, 1] du monde échantillonné (0 hors d'un vrai monde).
     */
    default float rainLevel() {
        return 0.0f;
    }

    /**
     * Niveau d'orage Minecraft [0, 1] du monde échantillonné (0 hors d'un vrai monde).
     */
    default float thunderLevel() {
        return 0.0f;
    }

    /**
     * Sampler par défaut plat (niveau de la mer).
     */
    static HeightSampler flat(float defaultHeight) {
        return (x, z) -> defaultHeight;
    }
}
