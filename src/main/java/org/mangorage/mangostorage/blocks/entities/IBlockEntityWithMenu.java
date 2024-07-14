package org.mangorage.mangostorage.blocks.entities;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;

public interface IBlockEntityWithMenu<T extends AbstractContainerMenu> {
    T create(int containerid, Inventory inventory, Player player);
}
