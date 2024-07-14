package org.mangorage.mangostorage.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.mangorage.mangostorage.blocks.entities.BlackHoleStorageBlockEntity;

public class BlackHoleStorageBlock extends AbstractMenuBlock implements EntityBlock {
    public BlackHoleStorageBlock(Properties p_49795_) {
        super(p_49795_);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new BlackHoleStorageBlockEntity(pPos, pState);
    }
}
