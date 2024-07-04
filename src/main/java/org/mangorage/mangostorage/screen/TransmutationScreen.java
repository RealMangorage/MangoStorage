package org.mangorage.mangostorage.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.NotNull;
import org.mangorage.mangostorage.fluid.FluidBox;
import org.mangorage.mangostorage.menu.TransmutationMenu;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class TransmutationScreen extends AbstractContainerScreen<TransmutationMenu> {
    private static final ResourceLocation INVENTORY = ResourceLocation.fromNamespaceAndPath("mangostorage", "textures/gui/container/transmutation.png");
    private final List<FluidBox> boxes = new ArrayList<>();

    public TransmutationScreen(TransmutationMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);

        int amount = 10;
        FluidBox box = new FluidBox(
                10,
                55,
                195,
                125
        );

        boxes.add(box);
        for (int i = 1; i < amount; i++) {
            box = box.createRight(2);
            boxes.add(box);
        }
    }

    @Override
    protected void slotClicked(Slot pSlot, int pSlotId, int pMouseButton, ClickType pType) {
        super.slotClicked(pSlot, pSlotId, pMouseButton, pType);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(graphics, pMouseX, pMouseY, pPartialTick);
        super.renderTooltip(graphics, pMouseX, pMouseY);

        AtomicInteger integer = new AtomicInteger();
        boxes.forEach(box -> {
            var id = integer.getAndIncrement();
            var capacity = menu.getFluidHandler().getTankCapacity(id);
            var stack = menu.getFluidHandler().getFluidInTank(id);

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

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        int i = this.leftPos;
        int j = this.topPos;
        pGuiGraphics.blit(INVENTORY, i, j, 0, 0, this.imageWidth, this.imageHeight);
    }
}
