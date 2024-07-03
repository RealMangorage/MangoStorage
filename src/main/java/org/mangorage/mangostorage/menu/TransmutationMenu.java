package org.mangorage.mangostorage.menu;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.mangorage.mangostorage.core.Registration;

public class TransmutationMenu extends AbstractContainerMenu {
    private final Inventory inventory;

    public TransmutationMenu(int pContainerId, Inventory inventory) {
        super(Registration.TRANSMUTATION_MENU.get(), pContainerId);
        this.inventory = inventory;
    }

    @Override
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        return null;
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return true;
    }


}
