package com.wildcard.buddycards.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.wildcard.buddycards.Buddycards;
import com.wildcard.buddycards.container.BinderContainer;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class BinderScreen extends AbstractContainerScreen<BinderContainer> {

    private static final ResourceLocation TEXTURE1 = new ResourceLocation(Buddycards.MOD_ID, "textures/gui/binder.png");
    private static final ResourceLocation TEXTURE2 = new ResourceLocation(Buddycards.MOD_ID, "textures/gui/binder2.png");
    private static final ResourceLocation TEXTURE3 = new ResourceLocation(Buddycards.MOD_ID, "textures/gui/binder3.png");
    private static final ResourceLocation TEXTURE4 = new ResourceLocation(Buddycards.MOD_ID, "textures/gui/binder4.png");

    public BinderScreen(BinderContainer container, Inventory playerInventory, Component title) {
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
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(PoseStack matrixStack, int x, int y) {
        //Draw the name of the binder and the inventory titles
        this.font.draw(matrixStack, title, 8.0f, 6.0f, 4210752);
        int size = menu.getItems().size();
        if (size == 90)
            this.font.draw(matrixStack, playerInventoryTitle,8.0f, 128.0f, 4210752);
        else if (size == 108)
            this.font.draw(matrixStack, playerInventoryTitle,35.0f, 128.0f, 4210752);
        else if (size == 132)
            this.font.draw(matrixStack, playerInventoryTitle,35.0f, 164.0f, 4210752);
        else if (size == 156)
            this.font.draw(matrixStack, playerInventoryTitle,35.0f, 200.0f, 4210752);
    }

    @Override
    protected void renderBg(PoseStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        //Place the texture for the binder gui
        int size = menu.getItems().size();
        assert this.minecraft != null;
        if (size == 90) {
            RenderSystem._setShaderTexture(0, TEXTURE1);
            blit(matrixStack, leftPos, topPos, 0, 0, imageWidth, imageHeight, 256, 256);
        }
        else if (size == 108) {
            RenderSystem._setShaderTexture(0, TEXTURE2);
            blit(matrixStack, leftPos, topPos, 0, 0, imageWidth, imageHeight, 256, 256);
        }
        else if (size == 132) {
            RenderSystem._setShaderTexture(0, TEXTURE3);
            blit(matrixStack, leftPos, topPos, 0, 0, imageWidth, imageHeight, 512, 512);
        }
        else if (size == 156) {
            RenderSystem._setShaderTexture(0, TEXTURE4);
            blit(matrixStack, leftPos, topPos, 0, 0, imageWidth, imageHeight, 512, 512);
        }
    }
}
