package com.gastinelweather.block;

import com.gastinelweather.GastinelWeatherMod;
import com.gastinelweather.integration.MinecraftHeightSampler;
import com.gastinelweather.weather.HeightSampler;
import com.gastinelweather.weather.SynopticState;
import com.gastinelweather.weather.WeatherSample;
import com.gastinelweather.weather.WeatherSystem;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.joml.Vector3f;

public class Vp2StationBlock extends Block {

    private static final VoxelShape SHAPE = Block.box(3.0, 0.0, 3.0, 13.0, 16.0, 13.0);

    public Vp2StationBlock(Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!level.isClientSide) {
            WeatherSystem system = GastinelWeatherMod.getWeatherSystem();
            float gameHours = level.getDayTime() / 1000.0f;
            Vector3f vPos = new Vector3f(pos.getX(), pos.getY(), pos.getZ());
            HeightSampler terrain = new MinecraftHeightSampler(level);

            SynopticState sky = system.synopticAt(gameHours);
            WeatherSample sample = system.sampleAt(vPos, sky, terrain);

            level.playSound(null, pos, SoundEvents.NOTE_BLOCK_CHIME.value(), SoundSource.BLOCKS, 0.8f, 1.2f);

            player.sendSystemMessage(Component.literal("§8=== §6§lStation Météo Vantage Pro 2 (VP2) §8==="));
            player.sendSystemMessage(Component.literal(
                String.format("§eTempérature: §f%.1f°C §7(Ressenti: §b%.1f°C§7) | Humidité: §a%.0f%%",
                    sample.temperatureC, sample.windChillC, (1.0f - sky.cloudCover * 0.4f) * 65.0f)
            ));
            player.sendSystemMessage(Component.literal(
                String.format("§bVent Moyen: §f%.1f km/h §7(§e%.0f° %s§7) | Rafales: §c%.1f km/h",
                    sample.getWindSpeedKmh(), sky.windFromDegrees, getCompassName(sky.windFromDegrees), sample.getWindSpeedKmh() * 1.35f)
            ));
            player.sendSystemMessage(Component.literal(
                String.format("§3Précipitations: §f%.1f mm/h §7| Cumul pluie sol: §b%.1f mm",
                    sample.precipitationMmPerHour, sample.wetness * 12.0f)
            ));
            player.sendSystemMessage(Component.literal(
                String.format("§6Rayonnement Solaire: §f%.0f W/m² §7| Index UV: §e%.1f",
                    sky.daylight * 850.0f, sky.daylight * 9.5f)
            ));
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    private static String getCompassName(float deg) {
        if (deg >= 337.5 || deg < 22.5) return "N";
        if (deg < 67.5) return "NE";
        if (deg < 112.5) return "E";
        if (deg < 157.5) return "SE";
        if (deg < 202.5) return "S";
        if (deg < 247.5) return "SO";
        if (deg < 292.5) return "O";
        return "NO";
    }
}
