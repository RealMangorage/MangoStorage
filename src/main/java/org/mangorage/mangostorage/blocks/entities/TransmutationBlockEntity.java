package org.mangorage.mangostorage.blocks.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mangorage.mangostorage.FluidStorage;
import org.mangorage.mangostorage.core.Registration;
import org.mangorage.mangostorage.recipe.TransmuteRecipe;

import java.util.ArrayList;
import java.util.List;

public class TransmutationBlockEntity extends BlockEntity implements ITransmutationBlockEntity {

    private final IItemHandler inventory = new ItemStackHandler(3);
    private final FluidStorage fluidHandler = new FluidStorage(1000, 10);

    private final LazyOptional<IItemHandler> PUBLIC_LAZY_ITEM = LazyOptional.of(() -> new IItemHandler() {
        @Override
        public int getSlots() {
            return 1;
        }

        @Override
        public @NotNull ItemStack getStackInSlot(int slot) {
            return slot > getSlots() ? ItemStack.EMPTY : inventory.getStackInSlot(slot);
        }

        @Override
        public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
            return slot > getSlots() ? ItemStack.EMPTY : inventory.insertItem(slot, stack, simulate);
        }

        @Override
        public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
            return slot > getSlots() ? ItemStack.EMPTY : inventory.extractItem(slot, amount, simulate);
        }

        @Override
        public int getSlotLimit(int slot) {
            return inventory.getSlotLimit(slot);
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return inventory.isItemValid(slot, stack);
        }
    });

    private final List<TransmuteRecipe> RECIPES = new ArrayList<>();

    public TransmutationBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(Registration.TRANSMUTATION_BLOCK_ENTITY.get(), pPos, pBlockState);
        RECIPES.add(
                new TransmuteRecipe(
                        Items.ACACIA_LOG,
                        new FluidStack(
                                Fluids.LAVA,
                                10
                        )
                )
        );
        RECIPES.add(
                new TransmuteRecipe(
                        Items.CACTUS,
                        new FluidStack(
                                Fluids.WATER,
                                6
                        )
                )
        );
    }

    @Override
    public void onLoad() {
        fluidHandler.getTank(0).fill(
                new FluidStack(Fluids.WATER, 200),
                IFluidHandler.FluidAction.EXECUTE
        );
        setChanged();
    }

    @Override
    public void tick() {
        if (level.isClientSide()) return;
        var item = inventory.extractItem(0, 1, true);
        if (item.isEmpty()) return;

        TransmuteRecipe recipe = null;
        for (TransmuteRecipe transmuteRecipe : RECIPES) {
            if (transmuteRecipe.isValid(item)) {
                recipe = transmuteRecipe;
                break;
            }
        }

        if (recipe == null) return;

        item = inventory.extractItem(0, 1, false);

        var tanks = fluidHandler.getTanks();
        var stackTest = recipe.output().copy();
        int amount = stackTest.getAmount();
        for (int i = 0; i < tanks; i++) {
            amount-=fluidHandler.getTank(i).fill(stackTest, IFluidHandler.FluidAction.SIMULATE);
        }
        if (amount <= 0) {
            FluidStack stack = recipe.output().copy();
            int amountLeft = stack.getAmount();
            for (int i = 0; i < tanks; i++) {
                amountLeft -= fluidHandler.getTank(i).fill(stack, IFluidHandler.FluidAction.EXECUTE);
                if (amountLeft <= 0)
                    break;
            }
            if (!item.isEmpty()) {
                item.shrink(1);
                inventory.insertItem(0, item, false);
            }
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            setChanged();
        } else {
            if (!item.isEmpty()) {
                inventory.insertItem(0, item, false);
            }
            setChanged();
        }

    }

    public IItemHandler getInventory() {
        return inventory;
    }

    public IFluidHandler getFluidHandler() {
        setChanged();
        return fluidHandler;
    }

    @Override
    protected void loadAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.loadAdditional(pTag, pRegistries);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider pRegistries) {
        super.saveAdditional(pTag, pRegistries);
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection connection, ClientboundBlockEntityDataPacket pkt, HolderLookup.Provider lookup) {
        handleUpdateTag(pkt.getTag(), lookup);
    }

    @Override
    public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider holders) {
        if (tag.contains("fluids", 9)) {
            ListTag list = (ListTag) tag.get("fluids");
            for (int i = 0; i < list.size(); i++) {
                fluidHandler.getTank(i).readFromNBT(list.getCompound(i));
            }
        }
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider pRegistries) {
        CompoundTag tag = new CompoundTag();
        ListTag listTag = new ListTag();
        for (int i = 0; i < fluidHandler.getTanks(); i++) {
            listTag.add(fluidHandler.getTank(i).writeToNBT(new CompoundTag()));
        }
        tag.put("fluids", listTag);
        return tag;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER)
            return PUBLIC_LAZY_ITEM.cast();
        return LazyOptional.empty();
    }
}
