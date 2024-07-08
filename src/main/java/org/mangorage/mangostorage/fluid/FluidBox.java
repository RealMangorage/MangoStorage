package org.mangorage.mangostorage.fluid;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.material.Fluid;
import org.mangorage.mangostorage.RenderHelper;

import java.util.List;
import java.util.function.Supplier;

public class FluidBox {
    public enum Style {
        HORIZONTAL,
        VERTICAL
    }

    private final int width;
    private final int height;
    private final int posX;
    private final int posY;
    private final Supplier<Integer> leftPos;
    private final Supplier<Integer> topPos;

    public FluidBox(final int width, final int height, final int posX, final int poxY, Supplier<Integer> leftPos, Supplier<Integer> topPos) {
        this.width = width;
        this.height = height;
        this.posX = posX;
        this.posY = poxY;
        this.leftPos = leftPos;
        this.topPos = topPos;
    }

    private int getLeftPos() {
        return leftPos.get();
    }

    private int getTopPos() {
        return topPos.get();
    }

    public FluidBox createRight(int offset) {
        return new FluidBox(
                width,
                height,
                offset + (posX + width),
                posY,
                leftPos,
                topPos
        );
    }

    public FluidBox createDown(int offset) {
        return new FluidBox(
                width,
                height,
                posX,
                offset + (posY + height),
                leftPos,
                topPos
        );
    }

    private static int getPercentage(int capacity, int amount, int number) {
        return Math.min(Math.max((int) (amount * ((float) number / capacity)), 1), number);
    }

    public void render(GuiGraphics graphics, Fluid fluid, Style style, int amount, int total) {
        RenderHelper.renderFluid(
                graphics,
                fluid,
                posX,
                posY,
                style == Style.HORIZONTAL ? getPercentage(total, amount, width) : width,
                style == Style.VERTICAL ? getPercentage(total, amount, height) : height
        );
    }

    public void renderWithOutline(GuiGraphics graphics, Fluid fluid, Style style, int amount, int total, int color) {
        RenderHelper.renderFluid(
                graphics,
                fluid,
                (posX + getLeftPos()),
                (posY + getTopPos()),
                style == Style.HORIZONTAL ? getPercentage(total, amount, width) : width,
                style == Style.VERTICAL ? getPercentage(total, amount, height) : height
        );

        graphics.renderOutline(
                (posX + getLeftPos()),
                (posY + getTopPos()) - height,
                width,
                height,
                color
        );
    }

    public void renderTooltip(GuiGraphics graphics, Font font, Component tooltip, int mouseX, int mouseY) {
        if ((mouseX > (posX + getLeftPos()) && mouseX < ((posX + getLeftPos()) + width)) && (mouseY > ((posY + getTopPos()) - height) && mouseY < (posY + getTopPos()))) {
            graphics.renderComponentTooltip(
                    font,
                    List.of(tooltip),
                    mouseX,
                    mouseY
            );
        }
    }
}
