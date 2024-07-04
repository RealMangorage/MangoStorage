package org.mangorage.mangostorage.recipe;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public record TransmuteRecipe(Item input, FluidStack output) {
    public boolean isValid(ItemStack stack) {
        return stack.is(input);
    }
}
