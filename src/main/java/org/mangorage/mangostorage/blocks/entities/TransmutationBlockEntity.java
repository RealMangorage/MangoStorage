package org.mangorage.mangostorage.blocks.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.mangorage.mangostorage.core.Registration;

public class TransmutationBlockEntity extends BlockEntity {

    private final IItemHandler inventory = new ItemStackHandler(3);

    public TransmutationBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(Registration.TRANSMUTATION_BLOCK_ENTITY.get(), pPos, pBlockState);
    }

    public IItemHandler getInventory() {
        return inventory;
    }
}
