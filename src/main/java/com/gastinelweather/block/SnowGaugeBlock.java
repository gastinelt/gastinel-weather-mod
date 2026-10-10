package com.gastinelweather.block;

import com.gastinelweather.GastinelWeatherMod;
import com.gastinelweather.integration.MinecraftHeightSampler;
import com.gastinelweather.weather.HeightSampler;
import com.gastinelweather.weather.WeatherSample;
import com.gastinelweather.weather.WeatherSystem;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
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

public class SnowGaugeBlock extends Block {

    private static final VoxelShape SHAPE = Block.box(4.0, 0.0, 4.0, 12.0, 16.0, 12.0);

    public SnowGaugeBlock(Properties properties) {
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
            WeatherSample sample = system.sampleAt(vPos, gameHours, terrain);

            float snowHeightCm = (sample.temperatureC <= 1.0f) ? sample.wetness * 25.0f : 0.0f;

            player.sendSystemMessage(Component.literal("§8=== §b§lNiveomètre Laser / Manteau Neigeux §8==="));
            player.sendSystemMessage(Component.literal(
                String.format("§fHauteur de neige estimée: §b%.1f cm §7| Température au sol: §e%.1f°C",
                    snowHeightCm, sample.temperatureC)
            ));
            player.sendSystemMessage(Component.literal(
                String.format("§7Chute de neige en cours: %s §7(Limite pluie/neige: §f1.0°C§7)",
                    sample.snowing ? "§bOUI (" + String.format("%.1f", sample.precipitationMmPerHour) + " mm/h)" : "§7NON")
            ));
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }
}
