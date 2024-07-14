package org.mangorage.mangostorage.menu;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nullable;

public abstract class AbstractBaseMenu extends AbstractContainerMenu {
    private final Inventory inventory;
    @Nullable
    private final BlockPos blockPos;


    // Client
    public AbstractBaseMenu(MenuType<?> menuType, int containerId, Inventory inventory, FriendlyByteBuf buf) {
        super(menuType, containerId);
        this.inventory = inventory;
        this.blockPos = buf.readBlockPos();
        setup();
    }

    public AbstractBaseMenu(MenuType<?> menuType, int containerId, Inventory inventory) {
        super(menuType, containerId);
        this.inventory = inventory;
        this.blockPos = null;
        setup();
    }

    public BlockPos getBlockPos() {
        return blockPos;
    }

    public void setup() {
        for (int l = 0; l < 3; l++) {
            for (int j1 = 0; j1 < 9; j1++) {
                this.addSlot(new Slot(inventory, j1 + (l + 1) * 9, 8 + j1 * 18, 84 + l * 18));
            }
        }

        for (int i1 = 0; i1 < 9; i1++) {
            this.addSlot(new Slot(inventory, i1, 8 + i1 * 18, 142));
        }
    }
}
