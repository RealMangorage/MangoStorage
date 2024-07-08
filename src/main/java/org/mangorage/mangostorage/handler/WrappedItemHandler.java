package org.mangorage.mangostorage.handler;


import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectSortedMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectSortedMaps;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class WrappedItemHandler implements IItemHandler {

    public enum Mode {
        EXTRACT,
        INSERT,
        UNDEFINED;

        public boolean isExtract() {
            return this == EXTRACT;
        }

        public boolean isInsert() {
            return this == INSERT;
        }
    }

    public interface ILogic {
        boolean isValid(Mode mode);
    }

    public static Builder of() {
        return new Builder();
    }


    public static class Builder {
        private final Int2ObjectMap<ILogic> logicList = new Int2ObjectLinkedOpenHashMap<>();

        private Builder() {
        }

        public Builder set(int slot, ILogic logic) {
            if(logicList.containsKey(slot)) return this;
            logicList.put(slot, logic);
            return this;
        }

        public IItemHandler build(IItemHandler handler) {
            for (int i = 0; i < handler.getSlots(); i++) {
                set(i, m -> false);
            }
            return new WrappedItemHandler(
                    logicList.int2ObjectEntrySet().stream()
                            .sorted(Comparator.comparingInt(Int2ObjectMap.Entry::getIntKey))
                            .map(Map.Entry::getValue)
                            .toList(),
                    handler
            );
        }
    }

    private final List<ILogic> logicList;
    private final IItemHandler handler;

    private WrappedItemHandler(List<ILogic> logicList, IItemHandler handler) {
        this.logicList = logicList;
        this.handler = handler;
    }

    @Override
    public int getSlots() {
        return handler.getSlots();
    }

    @Override
    public @NotNull ItemStack getStackInSlot(int slot) {
        return handler.getStackInSlot(slot);
    }

    @Override
    public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        return logicList.get(slot).isValid(Mode.INSERT) ? handler.insertItem(slot, stack, simulate) : stack;
    }

    @Override
    public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
        return logicList.get(slot).isValid(Mode.EXTRACT) ? handler.extractItem(slot, amount, simulate) : ItemStack.EMPTY;
    }

    @Override
    public int getSlotLimit(int slot) {
        return handler.getSlotLimit(slot);
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return handler.isItemValid(slot, stack);
    }
}
