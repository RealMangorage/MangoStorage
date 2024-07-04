package org.mangorage.mangostorage.menu;

import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.mangorage.mangostorage.core.Registration;
import org.mangorage.mangostorage.fluid.FluidBox;

public class TransmutationMenu extends AbstractContainerMenu {
    private final Inventory inventory;

    public TransmutationMenu(int containenrId, Inventory inventory) {
        this(containenrId, inventory, new ItemStackHandler(3));
    }

    public TransmutationMenu(int pContainerId, Inventory inventory, IItemHandler handler) {
        super(Registration.TRANSMUTATION_MENU.get(), pContainerId);
        this.inventory = inventory;

        for (int l = 0; l < 3; l++) {
            for (int j1 = 0; j1 < 9; j1++) {
                this.addSlot(new Slot(inventory, j1 + (l + 1) * 9, 8 + j1 * 18, 84 + l * 18));
            }
        }

        for (int i1 = 0; i1 < 9; i1++) {
            this.addSlot(new Slot(inventory, i1, 8 + i1 * 18, 142));
        }

        this.addSlot(
                new SlotItemHandler(
                        handler,
                        0,
                        8,
                        32
                )
        ).set(Items.IRON_AXE.getDefaultInstance());

        this.addSlot(
                new SlotItemHandler(
                        handler,
                        1,
                        152,
                        18 * 1
                )
        ).set(Items.AMETHYST_SHARD.getDefaultInstance());

        this.addSlot(
                new SlotItemHandler(
                        handler,
                        2,
                        152,
                        18 * 3
                )
        ).set(Items.BAMBOO.getDefaultInstance());
    }

    @Override
    public void clicked(int pSlotId, int pButton, ClickType pClickType, Player pPlayer) {
        super.clicked(pSlotId, pButton, pClickType, pPlayer);
    }

    @Override
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        return null;
    }

    @Override
    public boolean canDragTo(Slot pSlot) {
        return true;
    }

    @Override
    public boolean canTakeItemForPickAll(ItemStack pStack, Slot pSlot) {
        return true;
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return true;
    }


}
