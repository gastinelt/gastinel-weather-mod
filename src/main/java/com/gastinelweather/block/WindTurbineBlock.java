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

public class WindTurbineBlock extends Block {

    private static final VoxelShape SHAPE = Block.box(3.0, 0.0, 3.0, 13.0, 16.0, 13.0);

    public WindTurbineBlock(Properties properties) {
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

            // Puissance éolienne P proportionnelle à v^3 (loi de Betz simplifiée)
            float v = sample.windMps;
            int generatedFE = (int) Math.min(250.0f, 0.08f * v * v * v);

            player.sendSystemMessage(Component.literal("§8=== §a§lGénérateur Éolien Aérodynamique §8==="));
            player.sendSystemMessage(Component.literal(
                String.format("§fVent local capté: §b%.1f m/s §7(§e%.1f km/h§7) | Altitude: §f%d m",
                    v, sample.getWindSpeedKmh(), pos.getY())
            ));
            player.sendSystemMessage(Component.literal(
                String.format("§2Production Énergie: §a§l+%d FE/t §7(Rendement: §f%.0f%%§7)",
                    generatedFE, Math.min(100.0f, (v / 25.0f) * 100.0f))
            ));
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }
}
