package org.mangorage.mangostorage.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.NotNull;
import org.mangorage.mangostorage.fluid.FluidBox;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class TransmutationScreen extends Screen {
    private final List<FluidBox> boxes = new ArrayList<>();


    protected TransmutationScreen() {
        super(Component.literal("Test"));

        int sizeX = 5;
        int sizeY = 5;

        FluidBox currentXBox = null;
        FluidBox currentYBox = null;

        for (int x = 0; x < sizeX; x++) {
            currentYBox = currentYBox == null ? new FluidBox(10, 50, 100, 50) : currentYBox.createRight(2);
            currentXBox = null;
            boxes.add(currentYBox);
            for (int y = 0; y < sizeY - 1; y++) {
                currentXBox = currentXBox == null ? currentYBox.createDown(2) : currentXBox.createDown(2);
                boxes.add(currentXBox);
            }
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void render(@NotNull GuiGraphics GuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        AtomicInteger integer = new AtomicInteger();
        boxes.forEach(box -> {
            box.renderWithOutline(
                    GuiGraphics,
                    Fluids.WATER,
                    FluidBox.Style.VERTICAL,
                    1500,
                    1000,
                    Color.WHITE.getRGB()
            );
            box.renderTooltip(GuiGraphics, minecraft.font, Component.literal("Box -> " + integer.addAndGet(1)), pMouseX, pMouseY);
        });
    }
}
