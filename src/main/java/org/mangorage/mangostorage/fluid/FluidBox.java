package org.mangorage.mangostorage.fluid;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.material.Fluid;
import org.mangorage.mangostorage.RenderHelper;

import java.util.List;

public class FluidBox {
    public enum Style {
        HORIZONTAL,
        VERTICAL
    }

    private final int width;
    private final int height;
    private final int posX;
    private final int posY;

    public FluidBox(final int width, final int height, final int posX, final int poxY) {
        this.width = width;
        this.height = height;
        this.posX = posX;
        this.posY = poxY;
    }

    public FluidBox createRight(int offset) {
        return new FluidBox(
                width,
                height,
                offset + (posX + width),
                posY
        );
    }

    public FluidBox createDown(int offset) {
        return new FluidBox(
                width,
                height,
                posX,
                offset + (posY + height)
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
                posX,
                posY,
                style == Style.HORIZONTAL ? getPercentage(total, amount, width) : width,
                style == Style.VERTICAL ? getPercentage(total, amount, height) : height
        );

        graphics.renderOutline(
                posX,
                posY - height,
                width,
                height,
                color
        );
    }

    public void renderTooltip(GuiGraphics graphics, Font font, Component tooltip, int mouseX, int mouseY) {
        if ( (mouseX > posX && mouseX < posX + width) && (mouseY > posY - height && mouseY < posY ) ) {
            graphics.renderComponentTooltip(
                    font,
                    List.of(tooltip),
                    mouseX,
                    mouseY
            );
        }
    }
}
