package org.mangorage.mangostorage.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;
import org.mangorage.mangostorage.menu.BlackHoleStorageUnitMenu;

import java.awt.*;

public class BlackHoleStorageUnitScreen extends BaseContainerScreen<BlackHoleStorageUnitMenu> {
    public BlackHoleStorageUnitScreen(BlackHoleStorageUnitMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(graphics, pMouseX, pMouseY, pPartialTick);
        super.renderTooltip(graphics, pMouseX, pMouseY);

        ItemStack stack = Items.ACACIA_SAPLING.getDefaultInstance();

        graphics.renderItem(
                stack,
                20,
                20
        );

        graphics.drawString(
                minecraft.font,
                "10000",
                20,
                30,
                Color.BLUE.getRGB()
        );
    }
}
