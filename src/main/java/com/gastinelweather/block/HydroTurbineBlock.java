package com.gastinelweather.block;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class HydroTurbineBlock extends Block {

    private static final VoxelShape SHAPE = Block.box(2.0, 0.0, 2.0, 14.0, 16.0, 14.0);

    public HydroTurbineBlock(Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!level.isClientSide) {
            boolean inWater = level.getBlockState(pos.above()).is(Blocks.WATER) ||
                              level.getBlockState(pos.north()).is(Blocks.WATER) ||
                              level.getBlockState(pos.south()).is(Blocks.WATER);

            int fePerTick = inWater ? 40 : 0;

            player.sendSystemMessage(Component.literal("§8=== §3§lHydrolienne Immergée (Hélice pour l'eau) §8==="));
            player.sendSystemMessage(Component.literal(
                String.format("§7Courant hydraulique: %s §7| Production: §a+%d FE/t",
                    inWater ? "§bOptimal (Immergée)" : "§cInactif (Placer dans l'eau)", fePerTick)
            ));
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }
}
