package com.gastinelweather.integration;

import com.gastinelweather.weather.HeightSampler;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;

/**
 * Adaptateur HeightSampler pour le monde Minecraft en cours d'exécution.
 */
public class MinecraftHeightSampler implements HeightSampler {

    private final Level level;

    public MinecraftHeightSampler(Level level) {
        this.level = level;
    }

    // Pluie et orage du monde (globaux, comme rainStrength/thunderStrength côté shader).
    @Override
    public float rainLevel() {
        return level == null ? 0.0f : level.getRainLevel(1.0f);
    }

    @Override
    public float thunderLevel() {
        return level == null ? 0.0f : level.getThunderLevel(1.0f);
    }

    @Override
    public float heightAt(float worldX, float worldZ) {
        if (level == null) {
            return 64.0f;
        }

        int x = (int) Math.floor(worldX);
        int z = (int) Math.floor(worldZ);

        if (!level.hasChunkAt(new BlockPos(x, 64, z))) {
            return level.getSeaLevel();
        }

        try {
            return (float) level.getHeight(Heightmap.Types.MOTION_BLOCKING, x, z);
        } catch (Exception e) {
            return level.getSeaLevel();
        }
    }
}
