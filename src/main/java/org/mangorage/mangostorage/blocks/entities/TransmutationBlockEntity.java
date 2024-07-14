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
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
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
import org.mangorage.mangostorage.handler.WrappedItemHandler;
import org.mangorage.mangostorage.menu.TransmutationMenu;
import org.mangorage.mangostorage.recipe.TransmuteConsumeRecipe;
import org.mangorage.mangostorage.recipe.TransmuteProduceRecipe;
import org.mangorage.mangostorage.util.ChangedStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class TransmutationBlockEntity extends BlockEntity implements ITransmutationBlockEntity, IBlockEntityWithMenu<TransmutationMenu> {

    private static final Logger log = LoggerFactory.getLogger(TransmutationBlockEntity.class);
    private final IItemHandler inventory = new ItemStackHandler(3);
    private final FluidStorage fluidHandler = new FluidStorage(1000, 10);

    private final LazyOptional<IItemHandler> PUBLIC_LAZY_ITEM = LazyOptional.of(() ->
            WrappedItemHandler.of()
                    .set(0, WrappedItemHandler.Mode::isInsert)
                    .set(2, WrappedItemHandler.Mode::isExtract)
                    .build(inventory)
    );

    private final List<TransmuteConsumeRecipe> CONSUME_RECIPES = new ArrayList<>();
    private final List<TransmuteProduceRecipe> PRODUCE_RECIPES = new ArrayList<>();

    public TransmutationBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(Registration.TRANSMUTATION_BLOCK_ENTITY.get(), pPos, pBlockState);
        CONSUME_RECIPES.add(
                new TransmuteConsumeRecipe(
                        Items.ACACIA_LOG,
                        new FluidStack(
                                Fluids.LAVA,
                                10
                        )
                )
        );
        CONSUME_RECIPES.add(
                new TransmuteConsumeRecipe(
                        Items.CACTUS,
                        new FluidStack(
                                Fluids.WATER,
                                6
                        )
                )
        );
        PRODUCE_RECIPES.add(
                new TransmuteProduceRecipe(
                        List.of(
                                new FluidStack(Fluids.WATER, 10),
                                new FluidStack(Fluids.LAVA, 4)
                        ),
                        new ItemStack(Items.OBSIDIAN, 2)
                )
        );
    }

    @Override
    public void onLoad() {
        fluidHandler.fill(new FluidStack(Fluids.WATER, 500), IFluidHandler.FluidAction.EXECUTE);
        fluidHandler.fill(new FluidStack(Fluids.LAVA, 500), IFluidHandler.FluidAction.EXECUTE);
    }

    @Override
    public void tick() {
        if (!getLevel().isClientSide()) {
            var changed = ChangedStatus.of(() -> level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3));
            changed.setChanged(consumeInput());
            changed.setChanged(produceOutput());
            changed.finalizeChanged();
        } else {

        }
    }

    private TransmuteProduceRecipe getProduceRecipe() {
        for (TransmuteProduceRecipe produceRecipe : PRODUCE_RECIPES) {
            if (produceRecipe.drainFromStorage(getFluidHandler(), IFluidHandler.FluidAction.SIMULATE)) {
                return produceRecipe;
            }
        }
        return null;
    }

    private TransmuteConsumeRecipe getConsumeRecipe(ItemStack itemStack) {
        for (TransmuteConsumeRecipe consumeRecipe : CONSUME_RECIPES) {
            if (consumeRecipe.isValid(itemStack) && fluidHandler.fill(consumeRecipe.output().copy(), IFluidHandler.FluidAction.SIMULATE) == consumeRecipe.output().getAmount()) {
                return consumeRecipe;
            }
        }
        return null;
    }

    private boolean consumeInput() {
        var item = inventory.getStackInSlot(0);
        if (item.isEmpty()) return false;
        var recipe = getConsumeRecipe(item);
        if (recipe != null) {
            inventory.extractItem(0, 1, false);
            getFluidHandler().fill(recipe.output().copy(), IFluidHandler.FluidAction.EXECUTE);
            return true;
        }
        return false;
    }

    private boolean produceOutput() {
        var recipe = getProduceRecipe();
        if (recipe != null) {
            var output = recipe.output().copy();
            if (inventory.insertItem(2, output, true).isEmpty()) {
                recipe.drainFromStorage(getFluidHandler(), IFluidHandler.FluidAction.EXECUTE);
                inventory.insertItem(2, output, false);
                return true;
            }
        }
        return false;
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

    @Override
    public TransmutationMenu create(int containerid, Inventory inventory, Player player) {
        return new TransmutationMenu(containerid, inventory, getInventory(), getFluidHandler());
    }
}
