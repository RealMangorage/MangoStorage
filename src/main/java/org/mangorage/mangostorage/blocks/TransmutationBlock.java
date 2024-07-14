package org.mangorage.mangostorage.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;
import org.mangorage.mangostorage.blocks.entities.ITransmutationBlockEntity;
import org.mangorage.mangostorage.blocks.entities.TransmutationBlockEntity;
import org.mangorage.mangostorage.core.Registration;

public class TransmutationBlock extends AbstractMenuBlock implements EntityBlock {
    public TransmutationBlock(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new TransmutationBlockEntity(pPos, pState);
    }


    @Nullable
    @SuppressWarnings("unchecked")
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return (BlockEntityTicker<T>) createTicker();
    }

    public <T extends BlockEntity & ITransmutationBlockEntity> BlockEntityTicker<T> createTicker() {
        return (pLevel, pPos, pState, pBlockEntity) -> {
            pBlockEntity.tick();
        };
    }
}
