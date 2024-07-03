package org.mangorage.mangostorage.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;
import org.mangorage.mangostorage.blocks.entities.TransmutationBlockEntity;
import org.mangorage.mangostorage.menu.TransmutationMenu;

public class TransmutationBlock extends Block implements EntityBlock {
    public TransmutationBlock(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new TransmutationBlockEntity(pPos, pState);
    }


    @Nullable
    @Override
    protected MenuProvider getMenuProvider(BlockState pState, Level pLevel, BlockPos pPos) {
        return new SimpleMenuProvider(
                (id, inventory, player) -> new TransmutationMenu(id, inventory),
                Component.literal("Test")
        );
    }

    @Override
    public InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult result) {
        if (!level.isClientSide && player instanceof ServerPlayer plr) {
            plr.openMenu(getMenuProvider(state, level, pos));
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

}
