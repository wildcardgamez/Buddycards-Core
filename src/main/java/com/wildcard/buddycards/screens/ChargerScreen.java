package com.wildcard.buddycards.screens;

import com.wildcard.buddycards.Buddycards;
import com.wildcard.buddycards.menu.ChargerMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class ChargerScreen extends AbstractContainerScreen<ChargerMenu> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(Buddycards.MOD_ID, "textures/gui/buddysteel_charger.png");

    public ChargerScreen(ChargerMenu menu, Inventory inv, Component title) {
        super(menu, inv, title);
        this.leftPos = 0;
        this.topPos = 0;
        this.imageWidth = 176;
        this.imageHeight = 168;
    }

    @Override
    protected void renderLabels(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY) {
        pGuiGraphics.drawString(font, title, 8, 6, 4210752, false);
        pGuiGraphics.drawString(font, playerInventoryTitle,8, 74, 4210752, false);
    }
    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        //Place the texture for the gui
        this.renderBackground(pGuiGraphics);
        pGuiGraphics.blit(TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight);
        int progress = menu.getProgress();
        if(progress > 0)
            pGuiGraphics.blit(TEXTURE, leftPos + 52, topPos + 37, 176, 0, progress, 16);
    }
}
