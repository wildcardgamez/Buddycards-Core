package com.wildcard.buddycards.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.wildcard.buddycards.Buddycards;
import com.wildcard.buddycards.menu.BinderMenu;
import com.wildcard.buddycards.menu.PlaymatMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class BinderScreen extends AbstractContainerScreen<BinderMenu> {
    public BinderScreen(BinderMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        TEXTURE = menu.getTexture();
        LARGE = menu.isLarge();
        this.leftPos = 0;
        this.topPos = 0;
        this.imageWidth = LARGE ? 247 : 176;
        this.imageHeight = LARGE ? 240 : 204;
    }

    public final ResourceLocation TEXTURE;
    public final boolean LARGE;

    @Override
    protected void init() {
        super.init();
        this.addRenderableWidget(new ImageButton(leftPos - 15, topPos + (LARGE ? 70 : 52), 15, 18, (LARGE ? 247 : 176), 0, 18, TEXTURE, LARGE ? 512 : 256, LARGE ? 512 : 256, btn -> {
            this.sendButtonPress(0);
        }));
        this.addRenderableWidget(new ImageButton(leftPos + (LARGE ? 248 : 176), topPos + (LARGE ? 70 : 52), 15, 18, (LARGE ? 263 : 191), 0, 18, TEXTURE, LARGE ? 512 : 256, LARGE ? 512 : 256, btn -> {
            this.sendButtonPress(1);
        }));
    }

    private void sendButtonPress(int buttonId) {
        this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, buttonId);
    }

    @Override
    protected void renderLabels(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY) {
        //Draw the name of the binder and the inventory titles
        pGuiGraphics.drawString(font, title, LARGE ? 44 : 8, 6, 4210752, false);
        pGuiGraphics.drawString(font, Component.literal(this.menu.getCurrentPage() + "/" + this.menu.getPageAmt()), LARGE ? 176 : 140, 6, 4210752, false);
        pGuiGraphics.drawString(font, playerInventoryTitle, LARGE ? 44 : 8, LARGE ? 146 : 110, 4210752, false);
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        assert this.minecraft != null;
        pGuiGraphics.blit(TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight, LARGE ? 512 : 256, LARGE ? 512 : 256);
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float delta) {
        super.renderBackground(pGuiGraphics);
        super.render(pGuiGraphics, pMouseX, pMouseY, delta);
        renderTooltip(pGuiGraphics, pMouseX, pMouseY);
    }
}
