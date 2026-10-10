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

public class UltrasonicAnemometerBlock extends Block {

    private static final VoxelShape SHAPE = Block.box(3.0, 0.0, 3.0, 13.0, 15.0, 13.0);

    public UltrasonicAnemometerBlock(Properties properties) {
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

            player.sendSystemMessage(Component.literal("§8=== §3§lAnémomètre Statique à Ultrasons (3D) §8==="));
            player.sendSystemMessage(Component.literal(
                String.format("§bVent Horizontal: §f%.2f m/s §7(§e%.1f km/h§7)", sample.windMps, sample.getWindSpeedKmh())
            ));
            player.sendSystemMessage(Component.literal(
                String.format("§aVitesse Verticale (W): §f%+.2f m/s §7(Thermiques: §6%+.2f§7, Pente: §e%+.2f§7, Rabattant: §c%+.2f§7)",
                    sample.verticalMps, sample.thermalMps, sample.ridgeLiftMps, sample.subsidenceMps)
            ));
            player.sendSystemMessage(Component.literal(
                String.format("§7Indice de Turbulence: §f%.0f%% §7| Direction vecteur: §8(X: %.1f, Z: %.1f)",
                    sample.turbulence * 100.0f, sample.windVector.x, sample.windVector.z)
            ));
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }
}
