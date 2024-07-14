package org.mangorage.mangostorage.recipe;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

import java.util.List;

public record TransmuteProduceRecipe(List<FluidStack> required, ItemStack output) {
    public boolean drainFromStorage(IFluidHandler fluidHandler, IFluidHandler.FluidAction action) {
        List<FluidStack> copiedList = List.copyOf(required).stream()
                .map(FluidStack::copy) // ensure we copy it!
                .toList();

        int emptied = 0;
        for (FluidStack stack : copiedList) {
            fluidHandler.drain(stack, action);
            if (stack.isEmpty())
                emptied++;
            else
                break;
        }

        return emptied == required().size();
    }
}
