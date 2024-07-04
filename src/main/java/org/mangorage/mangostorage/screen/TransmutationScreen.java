package org.mangorage.mangostorage.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.network.chat.Component;
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
        AtomicInteger integer = new AtomicInteger();

        boxes.forEach(box -> {
            box.renderWithOutline(
                    graphics,
                    Fluids.WATER,
                    FluidBox.Style.VERTICAL,
                    1500,
                    1000,
                    Color.WHITE.getRGB()
            );
            box.renderTooltip(graphics, minecraft.font, Component.literal("Box -> " + integer.addAndGet(1)), pMouseX, pMouseY);
        });
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {

    }
}
