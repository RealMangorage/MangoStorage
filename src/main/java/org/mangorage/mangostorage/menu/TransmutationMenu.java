package org.mangorage.mangostorage.menu;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.mangorage.mangostorage.FluidStorage;
import org.mangorage.mangostorage.blocks.entities.ITransmutationBlockEntity;
import org.mangorage.mangostorage.blocks.entities.TransmutationBlockEntity;
import org.mangorage.mangostorage.core.Registration;

public class TransmutationMenu extends AbstractContainerMenu {
    private final Inventory inventory;
    private final IFluidHandler fluidHandler;

    public TransmutationMenu(int containenrId, Inventory inventory, FriendlyByteBuf buf) {
        super(Registration.TRANSMUTATION_MENU.get(), containenrId);
        this.inventory = inventory;
        var be = inventory.player.level().getBlockEntity(buf.readBlockPos());
        if (be != null && be instanceof ITransmutationBlockEntity transmutationBlockEntity) {
            this.fluidHandler = transmutationBlockEntity.getFluidHandler();
            setup(transmutationBlockEntity.getInventory());
        } else {
            this.fluidHandler = new FluidStorage(1000, 10);
            setup(new ItemStackHandler(3));
        }
    }

    public TransmutationMenu(int pContainerId, Inventory inventory, IItemHandler handler, IFluidHandler fluidHandler) {
        super(Registration.TRANSMUTATION_MENU.get(), pContainerId);
        this.inventory = inventory;
        this.fluidHandler = fluidHandler;
        setup(handler);
    }

    private void setup(IItemHandler handler) {
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
                        36
                )
        );

        this.addSlot(
                new SlotItemHandler(
                        handler,
                        1,
                        152,
                        18 * 1
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
    public void clicked(int pSlotId, int pButton, ClickType pClickType, Player pPlayer) {
        super.clicked(pSlotId, pButton, pClickType, pPlayer);
    }

    @Override
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(pIndex);
        if (slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            EquipmentSlot equipmentslot = pPlayer.getEquipmentSlotForItem(itemstack);
            if (pIndex == 0) {
                if (!this.moveItemStackTo(itemstack1, 9, 45, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(itemstack1, itemstack);
            } else if (pIndex >= 1 && pIndex < 5) {
                if (!this.moveItemStackTo(itemstack1, 9, 45, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (pIndex >= 5 && pIndex < 9) {
                if (!this.moveItemStackTo(itemstack1, 9, 45, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (equipmentslot.getType() == EquipmentSlot.Type.HUMANOID_ARMOR && !this.slots.get(8 - equipmentslot.getIndex()).hasItem()) {
                int i = 8 - equipmentslot.getIndex();
                if (!this.moveItemStackTo(itemstack1, i, i + 1, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (equipmentslot == EquipmentSlot.OFFHAND && !this.slots.get(45).hasItem()) {
                if (!this.moveItemStackTo(itemstack1, 45, 46, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (pIndex >= 9 && pIndex < 36) {
                if (!this.moveItemStackTo(itemstack1, 36, 45, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (pIndex >= 36 && pIndex < 45) {
                if (!this.moveItemStackTo(itemstack1, 9, 36, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, 9, 45, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY, itemstack);
            } else {
                slot.setChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(pPlayer, itemstack1);
            if (pIndex == 0) {
                pPlayer.drop(itemstack1, false);
            }
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
