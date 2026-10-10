package com.gastinelweather.block;

import com.gastinelweather.GastinelWeatherMod;
import com.gastinelweather.integration.MinecraftHeightSampler;
import com.gastinelweather.weather.HeightSampler;
import com.gastinelweather.weather.WeatherSample;
import com.gastinelweather.weather.WeatherSystem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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

public class AnemometerBlock extends Block {

    private static final VoxelShape SHAPE = Block.box(4.0, 0.0, 4.0, 12.0, 16.0, 12.0);

    public AnemometerBlock(Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public boolean isSignalSource(BlockState state) {
        return true;
    }

    @Override
    public int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        if (level instanceof Level l) {
            WeatherSystem system = GastinelWeatherMod.getWeatherSystem();
            float gameHours = l.getDayTime() / 1000.0f;
            Vector3f vPos = new Vector3f(pos.getX(), pos.getY(), pos.getZ());
            HeightSampler terrain = new MinecraftHeightSampler(l);
            WeatherSample sample = system.sampleAt(vPos, gameHours, terrain);

            // Signal redstone 0 à 15 (15 = vent à 60 km/h ou plus)
            int power = (int) Math.min(15.0f, (sample.getWindSpeedKmh() / 60.0f) * 15.0f);
            return Math.max(0, power);
        }
        return 0;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!level.isClientSide) {
            WeatherSystem system = GastinelWeatherMod.getWeatherSystem();
            float gameHours = level.getDayTime() / 1000.0f;
            Vector3f vPos = new Vector3f(pos.getX(), pos.getY(), pos.getZ());
            HeightSampler terrain = new MinecraftHeightSampler(level);
            WeatherSample sample = system.sampleAt(vPos, gameHours, terrain);

            player.sendSystemMessage(Component.literal(
                String.format("§8[§bAnémomètre à Coupelles§8] §fVent: §e%.1f km/h §7(%.1f m/s) | Signal Redstone: §c%d/15",
                    sample.getWindSpeedKmh(), sample.windMps, getSignal(state, level, pos, Direction.UP))
            ));
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }
}
