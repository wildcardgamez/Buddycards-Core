package com.wildcard.buddycards.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.wildcard.buddycards.Buddycards;
import com.wildcard.buddycards.battles.BattleComponent;
import com.wildcard.buddycards.battles.TextureBattleIcon;
import com.wildcard.buddycards.battles.BuddycardBattleIcon;
import com.wildcard.buddycards.battles.IBattleIcon;
import com.wildcard.buddycards.menu.PlaymatMenu;

import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class PlaymatScreen extends AbstractContainerScreen<PlaymatMenu> {
    public static final ResourceLocation TEXTURE1 = new ResourceLocation(Buddycards.MOD_ID, "textures/gui/playmat.png");
    public static final ResourceLocation smallFont = new ResourceLocation("buddycards", "smallnumbers");
    public PlaymatScreen(PlaymatMenu container, Inventory playerInventory, Component title) {
        super(container, playerInventory, title);
        this.imageWidth = 176;
        this.imageHeight = 88;
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
        int selectedSlot = this.menu.selectedSlot.get();
        if (selectedSlot != AbstractContainerMenu.SLOT_CLICKED_OUTSIDE) {
            ItemStack stack = this.menu.container.getItem(selectedSlot);
            if (!stack.isEmpty()) {
                PoseStack posestack = RenderSystem.getModelViewStack();
                posestack.pushPose();
                posestack.translate(0.0D, 0.0D, 32.0D);
                RenderSystem.applyModelViewMatrix();
                this.setBlitOffset(200);
                this.itemRenderer.blitOffset = 200.0F;
                this.itemRenderer.renderAndDecorateItem(stack, mouseX - 8, mouseY - 8);
                this.setBlitOffset(0);
                this.itemRenderer.blitOffset = 0.0F;
                posestack.popPose();
            }
        }
        this.renderTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(PoseStack matrixStack, int x, int y) {
        //Draw the name of the playmat and the inventory titles
        this.font.draw(matrixStack, title, 8.0f, 6.0f, 4210752);

        this.font.draw(matrixStack, Integer.toString(this.menu.opponentEnergy.get()), 20.0f, 22.0f, 4210752);
        this.font.draw(matrixStack, Integer.toString(this.menu.opponentHealth.get()), 51.0f, 22.0f, 4210752);
        this.font.draw(matrixStack, Integer.toString(this.menu.energy.get()), 20.0f, 40.0f, 4210752);
        this.font.draw(matrixStack, Integer.toString(this.menu.health.get()), 51.0f, 40.0f, 4210752);
        renderBattleLog(matrixStack, x, y);
    }

    private void renderBattleLog(PoseStack poseStack, int mouseX, int mouseY) {
        int height = 6;
        ResourceLocation lastDraw = null;
        for (BattleComponent battleComponent : getMenu().getBattleLog()) {
            int leftShift = -103;
            for (IBattleIcon battleIcon : battleComponent.getBattleIcons()) {
                if (battleIcon instanceof BuddycardBattleIcon buddycardBattleIcon) {
                    itemRenderer.renderAndDecorateItem(this.minecraft.player, buddycardBattleIcon.getItem().getDefaultInstance(), leftShift - 4, height - 2, 0);
                    lastDraw = null;
                } else if (battleIcon instanceof TextureBattleIcon t) {
                    if (t.texture() != IBattleIcon.NO_RENDER) {
                        if (t.texture() != lastDraw) {
                            lastDraw = t.texture();
                            RenderSystem._setShaderTexture(0, lastDraw);
                        }
                        blit(poseStack, leftShift, height, t.texturePosX(), t.texturePosY(), battleIcon.width(), 12);
                        poseStack.pushPose();
                        RenderSystem.disableDepthTest();
                        for (TextureBattleIcon.BattleInfo battleInfo : t.info()) {
                            MutableComponent text = new TextComponent(battleInfo.display() + "").withStyle(style -> style.withFont(smallFont));
                            this.font.draw(poseStack, text, leftShift + battleInfo.x() - (battleInfo.isLeftAligned() ? 0 : this.font.width(text)), height + battleInfo.y(), battleInfo.color());
                        }
                        RenderSystem.enableDepthTest();
                        poseStack.popPose();
                    }
                }
                leftShift += battleIcon.width() + 2;
            }
            height += 12 + 2;
        }
        int xMouseScreen = mouseX - leftPos;
        int yMouseScreen = mouseY - topPos;
        int tooltipRow = (yMouseScreen - 6) / 14;
        List<BattleComponent> battleLog = menu.getBattleLog();
        if (tooltipRow >= 0 && tooltipRow < battleLog.size() && yMouseScreen > 0) {
            if (mouseX > leftPos - 103 && mouseX < leftPos) {
                renderTooltip(poseStack, battleLog.get(tooltipRow).getHoverText(), xMouseScreen, yMouseScreen);
            }
        }
    }

    @Override
    protected void renderBg(PoseStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        //Place the texture for the gui
        RenderSystem._setShaderTexture(0, TEXTURE1);
        blit(matrixStack, leftPos, topPos, 0, 0, imageWidth, imageHeight);
        int battleLogLength = getMenu().getBattleLog().size();
        if (battleLogLength > 0) {
            int height = 0;
            blit(matrixStack, leftPos - 108, topPos + height, 0, 88, 108, 5);
            height += 5;
            for (int i = 0; i < battleLogLength; i++) {
                blit(matrixStack, leftPos - 108, topPos + height, 0, 98, 108, 14);
                height += 14;
            }
            blit(matrixStack, leftPos - 108, topPos + height, 0, 93, 108, 5);
        }
    }
}
