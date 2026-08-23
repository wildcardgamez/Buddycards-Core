package com.wildcard.buddycards.screens;

import com.wildcard.buddycards.Buddycards;
import com.wildcard.buddycards.menu.ChargerMenu;
import com.wildcard.buddycards.menu.GraderMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class ChargerScreen extends AbstractContainerScreen<ChargerMenu> {
    private static final ResourceLocation TEXTURE = Buddycards.buddycardsLocation("textures/gui/buddysteel_charger.png");
    private static final ResourceLocation PROGRESS = Buddycards.buddycardsLocation("container/charger/progress");

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
        pGuiGraphics.blit(TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight);
        int progress = menu.getProgress();
        if(progress > 0)
            pGuiGraphics.blitSprite(PROGRESS, 72, 16, 0, 0, leftPos + 52, topPos + 37, progress, 16);
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float delta) {
        super.renderBackground(pGuiGraphics, pMouseX,pMouseY, delta);
        super.render(pGuiGraphics, pMouseX, pMouseY, delta);
        renderTooltip(pGuiGraphics, pMouseX, pMouseY);
    }
}
