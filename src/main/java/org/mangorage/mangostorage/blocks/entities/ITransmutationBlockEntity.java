package org.mangorage.mangostorage.blocks.entities;

import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;

public interface ITransmutationBlockEntity {
    void tick();
    IItemHandler getInventory();
    IFluidHandler getFluidHandler();
}
