package com.wildcard.buddycards.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.wildcard.buddycards.Buddycards;
import com.wildcard.buddycards.menu.BinderMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class BinderScreen extends AbstractContainerScreen<BinderMenu> {

    private static final ResourceLocation TEXTURE1 = new ResourceLocation(Buddycards.MOD_ID, "textures/gui/binder.png");
    private static final ResourceLocation TEXTURE2 = new ResourceLocation(Buddycards.MOD_ID, "textures/gui/binder2.png");
    private static final ResourceLocation TEXTURE3 = new ResourceLocation(Buddycards.MOD_ID, "textures/gui/binder3.png");
    private static final ResourceLocation TEXTURE4 = new ResourceLocation(Buddycards.MOD_ID, "textures/gui/binder4.png");

    public BinderScreen(BinderMenu container, Inventory playerInventory, Component title) {
        super(container, playerInventory, title);
        //set up sizes for the gui
        int size = container.getItems().size();
        if (size == 90) {
            this.leftPos = 0;
            this.topPos = 0;
            this.imageWidth = 176;
            this.imageHeight = 222;
            return;
        }
        else if (size == 108) {
            this.leftPos = 0;
            this.topPos = 0;
            this.imageWidth = 230;
            this.imageHeight = 222;
            return;
        }
        else if (size == 132) {
            this.leftPos = 0;
            this.topPos = 0;
            this.imageWidth = 230;
            this.imageHeight = 258;
            return;
        }
        else if (size == 156) {
            this.leftPos = 0;
            this.topPos = 0;
            this.imageWidth = 230;
            this.imageHeight = 294;
            return;
        }
        this.leftPos = 0;
        this.topPos = 0;
        this.imageWidth = 176;
        this.imageHeight = 222;
    }

    @Override
    protected void renderLabels(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY) {
        //Draw the name of the binder and the inventory titles
        pGuiGraphics.drawString(font, title, 8, 6, 4210752, false);
        int size = menu.getItems().size();
        if (size == 90)
            pGuiGraphics.drawString(font, playerInventoryTitle, 8, 128, 4210752, false);
        else if (size == 108)
            pGuiGraphics.drawString(font, playerInventoryTitle, 35, 128, 4210752, false);
        else if (size == 132)
            pGuiGraphics.drawString(font, playerInventoryTitle, 35, 164, 4210752, false);
        else if (size == 156)
            pGuiGraphics.drawString(font, playerInventoryTitle, 35, 200, 4210752, false);
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        //Place the texture for the binder gui
        int size = menu.getItems().size();
        assert this.minecraft != null;
        this.renderBackground(pGuiGraphics);
        if (size == 90)
            pGuiGraphics.blit(TEXTURE1, leftPos, topPos, 0, 0, imageWidth, imageHeight);
        else if (size == 108)
            pGuiGraphics.blit(TEXTURE2, leftPos, topPos, 0, 0, imageWidth, imageHeight);
        else if (size == 132)
            pGuiGraphics.blit(TEXTURE3, leftPos, topPos, 0, 0, imageWidth, imageHeight);
        else if (size == 156)
            pGuiGraphics.blit(TEXTURE4, leftPos, topPos, 0, 0, imageWidth, imageHeight);
    }
}
