package org.mangorage.mangostorage.recipe;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public record TransmuteProduceRecipe(List<FluidStack> required, ItemStack output) { }
