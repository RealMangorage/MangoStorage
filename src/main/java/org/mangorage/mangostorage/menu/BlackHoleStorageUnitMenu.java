package org.mangorage.mangostorage.menu;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.Nullable;
import org.mangorage.mangostorage.core.Registration;

public class BlackHoleStorageUnitMenu extends AbstractBaseMenu {

    public BlackHoleStorageUnitMenu(int containerId, Inventory inventory, FriendlyByteBuf buf) {
        super(Registration.BLACK_HOLE_UNIT_MENU.get(), containerId, inventory, buf);
        setupSlots(new ItemStackHandler(2));
    }

    public BlackHoleStorageUnitMenu(int containerId, Inventory inventory, IItemHandler handler) {
        super(Registration.BLACK_HOLE_UNIT_MENU.get(), containerId, inventory);
        setupSlots(handler);
    }

    private void setupSlots(IItemHandler handler) {
        this.addSlot(
                new SlotItemHandler(
                        handler,
                        0,
                        8,
                        36
                )
        );
        this.addSlot(
                new SlotItemHandler(
                        handler,
                        1,
                        152,
                        36
                )
        );
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
