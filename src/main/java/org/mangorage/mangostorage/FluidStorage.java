package org.mangorage.mangostorage;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;

public class FluidStorage implements IFluidHandler {
    private final List<FluidTank> tanks = new LinkedList<>();

    public FluidStorage(int capacity, int tanks) {
        for (int i = 0; i < tanks; i++) {
            this.tanks.add(new FluidTank(capacity));
        }
    }

    public FluidTank getTank(int tank) {
        return tanks.get(tank);
    }

    @Override
    public int getTanks() {
        return tanks.size();
    }

    @Override
    public @NotNull FluidStack getFluidInTank(int tank) {
        return getTank(tank).getFluid();
    }

    @Override
    public int getTankCapacity(int tank) {
        return getTank(tank).getTankCapacity(tank);
    }

    @Override
    public boolean isFluidValid(int tank, @NotNull FluidStack stack) {
        return getTank(tank).isFluidValid(stack);
    }


    @Override
    public int fill(FluidStack resource, FluidAction action) {
        int totalFilled = 0;

        for (FluidTank tank : tanks) {
            int filled = tank.fill(resource, action);
            resource.shrink(filled);
            totalFilled += filled;

            if (resource.isEmpty()) {
                break;
            }
        }

        return totalFilled;
    }

    @Override
    public @NotNull FluidStack drain(FluidStack resource, FluidAction action) {
        FluidStack totalDrained = FluidStack.EMPTY;

        for (FluidTank tank : tanks) {
            FluidStack drained = tank.drain(resource, action);

            if (!drained.isEmpty()) {
                if (totalDrained.isEmpty()) {
                    totalDrained = drained.copy();
                } else {
                    totalDrained.grow(drained.getAmount());
                }

                resource.shrink(drained.getAmount());

                if (resource.isEmpty()) {
                    break;
                }
            }
        }

        return totalDrained;
    }

    @Override
    public @NotNull FluidStack drain(int maxDrain, FluidAction action) {
        FluidStack totalDrained = FluidStack.EMPTY;

        for (FluidTank tank : tanks) {
            FluidStack drained = tank.drain(maxDrain, action);

            if (!drained.isEmpty()) {
                if (totalDrained.isEmpty()) {
                    totalDrained = drained.copy();
                } else {
                    totalDrained.grow(drained.getAmount());
                }

                maxDrain -= drained.getAmount();

                if (maxDrain <= 0) {
                    break;
                }
            }
        }

        return totalDrained;
    }
}
