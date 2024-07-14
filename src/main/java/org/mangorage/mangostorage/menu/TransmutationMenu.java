package org.mangorage.mangostorage.menu;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.mangorage.mangostorage.FluidStorage;
import org.mangorage.mangostorage.blocks.entities.ITransmutationBlockEntity;
import org.mangorage.mangostorage.core.Registration;

public class TransmutationMenu extends AbstractBaseMenu {
    private final IFluidHandler fluidHandler;

    public TransmutationMenu(int containerId, Inventory inventory, FriendlyByteBuf buf) {
        super(Registration.TRANSMUTATION_MENU.get(), containerId, inventory, buf);
        var be = inventory.player.level().getBlockEntity(getBlockPos());
        if (be != null && be instanceof ITransmutationBlockEntity transmutationBlockEntity) {
            this.fluidHandler = transmutationBlockEntity.getFluidHandler();
            setup(transmutationBlockEntity.getInventory());
        } else {
            this.fluidHandler = new FluidStorage(1000, 10);
            setup(new ItemStackHandler(3));
        }
    }

    public TransmutationMenu(int pContainerId, Inventory inventory, IItemHandler handler, IFluidHandler fluidHandler) {
        super(Registration.TRANSMUTATION_MENU.get(), pContainerId, inventory);
        this.fluidHandler = fluidHandler;
        setup(handler);
    }

    private void setup(IItemHandler handler) {
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
                        18
                )
        );

        this.addSlot(
                new SlotItemHandler(
                        handler,
                        2,
                        152,
                        18 * 3
                )
        );
    }

    public IFluidHandler getFluidHandler() {
        return fluidHandler;
    }

    @Override
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(pIndex);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (pIndex < 39 && pIndex > 35) {
                if (!this.moveItemStackTo(itemstack1, 0, 36, true)) {
                    return ItemStack.EMPTY;
                }
            }

            if (pIndex >= 0 && pIndex < 36) {
                if (!this.moveItemStackTo(itemstack1, 36, 37, false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (itemstack1.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(pPlayer, itemstack1);
        }

        return itemstack;
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
