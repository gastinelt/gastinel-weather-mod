package com.gastinelweather.block;

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

public class NetworkRouterBlock extends Block {

    private static final VoxelShape SHAPE = Block.box(2.0, 0.0, 4.0, 14.0, 4.0, 12.0);

    public NetworkRouterBlock(Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!level.isClientSide) {
            level.playSound(null, pos, SoundEvents.UI_BUTTON_CLICK.value(), SoundSource.BLOCKS, 0.5f, 1.8f);
            player.sendSystemMessage(Component.literal("§8[§2Box / Routeur Réseau Gigabit§8] §aLiaison: 1000 Mbps §7| Ports Ethernet: §e4 Connectés §7| WiFi: §bActif 5GHz"));
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }
}
