package com.wildcard.buddycards.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.wildcard.buddycards.Buddycards;
import com.wildcard.buddycards.menu.PlaymatMenu;

import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class PlaymatScreen extends AbstractContainerScreen<PlaymatMenu> {
    private static final ResourceLocation TEXTURE1 = new ResourceLocation(Buddycards.MOD_ID, "textures/gui/playmat.png");

    public PlaymatScreen(PlaymatMenu container, Inventory playerInventory, Component title) {
        super(container, playerInventory, title);
        this.imageWidth = 176;
        this.imageHeight = 132;
    }
    
    @Override
    protected void init() {
        super.init();
        this.addRenderableWidget(new ImageButton(leftPos + 141, topPos + 40, 20, 18, 176, 0, 18, TEXTURE1, btn -> {
            this.sendButtonPress(PlaymatMenu.ButtonIds.END_TURN);
        }));
    }
    
    private void sendButtonPress(int buttonId) {
        this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, buttonId);
    }

    @Override
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(PoseStack matrixStack, int x, int y) {
        //Draw the name of the playmat and the inventory titles
        this.font.draw(matrixStack, title, 8.0f, 6.0f, 4210752);
        this.font.draw(matrixStack, getMenu().getBattleLog(), 8.0f, 89.0f, 4210752);
        this.font.draw(matrixStack, Integer.valueOf(this.menu.opponentEnergy.get()).toString(), 20.0f, 22.0f, 4210752);
        this.font.draw(matrixStack, Integer.valueOf(this.menu.opponentHealth.get()).toString(), 51.0f, 22.0f, 4210752);
        this.font.draw(matrixStack, Integer.valueOf(this.menu.energy.get()).toString(), 20.0f, 40.0f, 4210752);
        this.font.draw(matrixStack, Integer.valueOf(this.menu.health.get()).toString(), 51.0f, 40.0f, 4210752);
    }

    @Override
    protected void renderBg(PoseStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        //Place the texture for the gui
        RenderSystem._setShaderTexture(0, TEXTURE1);
        blit(matrixStack, leftPos, topPos, 0, 0, imageWidth, imageHeight, 256, 256);
    }
}
