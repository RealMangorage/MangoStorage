package org.mangorage.mangostorage.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.NotNull;
import org.mangorage.mangostorage.fluid.FluidBox;
import org.mangorage.mangostorage.menu.TransmutationMenu;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class TransmutationScreen extends BaseContainerScreen<TransmutationMenu> {
    private static final ResourceLocation INVENTORY = ResourceLocation.fromNamespaceAndPath("mangostorage", "textures/gui/container/transmutation.png");
    private final List<FluidBox> boxes = new ArrayList<>();

    public TransmutationScreen(TransmutationMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);

        int amount = 10;
        FluidBox box = new FluidBox(
                10,
                55,
                28,
                70,
                () -> this.leftPos,
                () -> this.topPos
        );

        boxes.add(box);
        for (int i = 1; i < amount; i++) {
            box = box.createRight(2);
            boxes.add(box);
        }
    }

    @Override
    public ResourceLocation getInventoryLocation() {
        return INVENTORY;
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(graphics, pMouseX, pMouseY, pPartialTick);
        super.renderTooltip(graphics, pMouseX, pMouseY);

        AtomicInteger integer = new AtomicInteger();
        boxes.forEach(box -> {
            var id = integer.getAndIncrement();
            var capacity = getMenu().getFluidHandler().getTankCapacity(id);
            var stack = getMenu().getFluidHandler().getFluidInTank(id);

            box.renderWithOutline(
                    graphics,
                    stack.isEmpty() ? Fluids.WATER : stack.getFluid(),
                    FluidBox.Style.VERTICAL,
                    stack.getAmount(),
                    capacity,
                    Color.WHITE.getRGB()
            );

            box.renderTooltip(graphics, minecraft.font, Component.translatable(stack.getTranslationKey()), pMouseX, pMouseY);
        });
    }
}
