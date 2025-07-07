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
        this.leftPos = 0;
        this.topPos = 0;
        this.imageWidth = 176;
        this.imageHeight = 204;
        TEXTURE = menu.getTexture();
    }

    public final ResourceLocation TEXTURE;

    @Override
    protected void init() {
        super.init();
        this.addRenderableWidget(new ImageButton(leftPos - 18, topPos + 52, 15, 18, 176, 0, 18, TEXTURE, btn -> {
            this.sendButtonPress(0);
        }));
        this.addRenderableWidget(new ImageButton(leftPos + 176, topPos + 52, 15, 18, 191, 0, 18, TEXTURE, btn -> {
            this.sendButtonPress(1);
        }));
    }

    private void sendButtonPress(int buttonId) {
        this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, buttonId);
    }

    @Override
    protected void renderLabels(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY) {
        //Draw the name of the binder and the inventory titles
        pGuiGraphics.drawString(font, title, 8, 6, 4210752, false);
        pGuiGraphics.drawString(font, Component.literal(this.menu.getCurrentPage() + "/" + this.menu.getPageAmt()), 140, 6, 4210752, false);
        pGuiGraphics.drawString(font, playerInventoryTitle, 8, 110, 4210752, false);
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        //Place the texture for the binder gui
        int size = menu.getItems().size();
        assert this.minecraft != null;
        this.renderBackground(pGuiGraphics);
        pGuiGraphics.blit(TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight);
    }
}
