package org.mangorage.mangostorage.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.util.Lazy;
import org.jetbrains.annotations.Nullable;
import org.mangorage.mangostorage.blocks.entities.IBlockEntityWithMenu;

public abstract class AbstractMenuBlock extends Block {
    private final Lazy<Component> COMPONENT = Lazy.of(() -> Component.translatable("block.%s.menu".replaceFirst("%s", builtInRegistryHolder().getRegisteredName().replaceAll(":", "."))));

    public AbstractMenuBlock(Properties p_49795_) {
        super(p_49795_);
    }

    @Nullable
    @Override
    protected final MenuProvider getMenuProvider(BlockState pState, Level pLevel, BlockPos pPos) {
        var be = pLevel.getBlockEntity(pPos);
        if (be == null) return null;

        if (be instanceof IBlockEntityWithMenu<?> entity) {
            return new SimpleMenuProvider(
                    entity::create,
                    COMPONENT.get()
            );
        }

        return null;
    }

    @Override
    public InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult result) {
        if (!level.isClientSide && player instanceof ServerPlayer plr) {
            plr.openMenu(getMenuProvider(state, level, pos), pos);
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }
}
