package com.gastinelweather.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CableBlock extends Block {

    public enum CableType {
        NETWORK_INDOOR,
        NETWORK_OUTDOOR,
        NETWORK_UNDERGROUND,
        ELECTRIC_INDOOR,
        ELECTRIC_UNDERGROUND
    }

    private static final VoxelShape THIN_SHAPE = Block.box(6.0, 6.0, 6.0, 10.0, 10.0, 10.0);
    private static final VoxelShape THICK_SHAPE = Block.box(4.0, 4.0, 4.0, 12.0, 12.0, 12.0);

    private final CableType cableType;

    public CableBlock(Properties properties, CableType cableType) {
        super(properties);
        this.cableType = cableType;
    }

    public CableType getCableType() {
        return cableType;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return (cableType == CableType.NETWORK_UNDERGROUND || cableType == CableType.ELECTRIC_UNDERGROUND)
                ? THICK_SHAPE : THIN_SHAPE;
    }
}
