package org.mangorage.mangostorage.blocks.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.mangorage.mangostorage.FluidStorage;
import org.mangorage.mangostorage.core.Registration;
import org.mangorage.mangostorage.recipe.TransmuteRecipe;

import java.util.ArrayList;
import java.util.List;

public class TransmutationBlockEntity extends BlockEntity implements ITransmutationBlockEntity {

    private final IItemHandler inventory = new ItemStackHandler(3);
    private final FluidStorage fluidHandler = new FluidStorage(1000, 10);

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
        var item = inventory.extractItem(0, 1, false);
        if (item.isEmpty()) return;

        TransmuteRecipe recipe = null;
        for (TransmuteRecipe transmuteRecipe : RECIPES) {
            if (transmuteRecipe.isValid(item)) {
                recipe = transmuteRecipe;
                break;
            }
        }

        if (recipe != null) {
            var failed = false;
            var tanks = fluidHandler.getTanks();
            for (int i = 0; i < tanks; i++) {
                var result = fluidHandler.getTank(i).fill(recipe.output().copy(), IFluidHandler.FluidAction.SIMULATE);
                if (result != recipe.output().getAmount()) {
                    failed = true;
                    break;
                }
            }
            if (!failed) {
                for (int i = 0; i < tanks; i++) {
                    fluidHandler.getTank(i).fill(recipe.output().copy(), IFluidHandler.FluidAction.EXECUTE);
                }
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            } else {
                if (!item.isEmpty()) {
                    item.shrink(1);
                    inventory.insertItem(0, item, false);
                }

                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
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

    @Override
    public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider holders) {
        if (tag.contains("fluids", ListTag.TAG_LIST)) {
            var list = tag.getList("fluids", ListTag.TAG_LIST);
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
}
