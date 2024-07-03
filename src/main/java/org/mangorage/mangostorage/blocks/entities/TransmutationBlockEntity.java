package org.mangorage.mangostorage.blocks.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.mangorage.mangostorage.core.Registration;

public class TransmutationBlockEntity extends BlockEntity {
    public TransmutationBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(Registration.TRANSMUTATION_BLOCK_ENTITY.get(), pPos, pBlockState);
    }
}
