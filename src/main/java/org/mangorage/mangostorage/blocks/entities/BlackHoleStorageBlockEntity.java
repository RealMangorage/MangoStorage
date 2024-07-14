package org.mangorage.mangostorage.blocks.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.mangorage.mangostorage.core.Registration;
import org.mangorage.mangostorage.menu.BlackHoleStorageUnitMenu;

public class BlackHoleStorageBlockEntity extends BlockEntity implements IBlockEntityWithMenu<BlackHoleStorageUnitMenu> {
    private final IItemHandler handler = new ItemStackHandler(2);

    public BlackHoleStorageBlockEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
    }

    public BlackHoleStorageBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(Registration.BLACK_HOLE_STORAGE_UNIT_BLOCK_ENTITY.get(), pPos, pBlockState);
    }


    @Override
    public BlackHoleStorageUnitMenu create(int containerid, Inventory inventory, Player player) {
        return new BlackHoleStorageUnitMenu(containerid, inventory, handler);
    }
}
