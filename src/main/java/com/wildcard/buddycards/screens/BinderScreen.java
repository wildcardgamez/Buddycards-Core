package com.wildcard.buddycards.screens;

import com.wildcard.buddycards.Buddycards;
import com.wildcard.buddycards.menu.BinderMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;

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
        this.addRenderableWidget(new BinderPageButton(leftPos - 15, topPos + (LARGE ? 70 : 52), false, btn -> this.sendButtonPress(0)));
        this.addRenderableWidget(new BinderPageButton(leftPos + (LARGE ? 248 : 176), topPos + (LARGE ? 70 : 52), true, btn -> this.sendButtonPress(1)));
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
        pGuiGraphics.blit(TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight, 256, 256);
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float delta) {
        super.renderBackground(pGuiGraphics, pMouseX, pMouseY, delta);
        super.render(pGuiGraphics, pMouseX, pMouseY, delta);
        renderTooltip(pGuiGraphics, pMouseX, pMouseY);
    }

    @Override
    protected void renderSlotHighlight(GuiGraphics guiGraphics, Slot slot, int mouseX, int mouseY, float partialTick) {
        if (slot.isHighlightable()) {
            renderSlotHighlight(guiGraphics, slot.x, slot.y, 0, this.getSlotColor(slot.index), slot instanceof BinderMenu.BinderSlot);
        }
    }

    public static void renderSlotHighlight(GuiGraphics guiGraphics, int x, int y, int blitOffset, int color, boolean isCard) {
        if (isCard)
            guiGraphics.fillGradient(RenderType.guiOverlay(), x + 4, y + 2, x + 12, y + 14, color, color, blitOffset);
        else
            guiGraphics.fillGradient(RenderType.guiOverlay(), x, y, x + 16, y + 16, color, color, blitOffset);
    }

    public static class BinderPageButton extends Button {
        private static final ResourceLocation PAGE_FORWARD_HIGHLIGHTED_SPRITE = ResourceLocation.fromNamespaceAndPath(Buddycards.MOD_ID, "container/binder/page_forward_highlighted");
        private static final ResourceLocation PAGE_FORWARD_SPRITE = ResourceLocation.fromNamespaceAndPath(Buddycards.MOD_ID, "container/binder/page_forward");
        private static final ResourceLocation PAGE_BACKWARD_HIGHLIGHTED_SPRITE = ResourceLocation.fromNamespaceAndPath(Buddycards.MOD_ID, "container/binder/page_backward_highlighted");
        private static final ResourceLocation PAGE_BACKWARD_SPRITE = ResourceLocation.fromNamespaceAndPath(Buddycards.MOD_ID, "container/binder/page_backward");

        protected BinderPageButton(int x, int y, boolean isForward, OnPress onPress) {
            super(x, y, 15, 18, CommonComponents.EMPTY, onPress, DEFAULT_NARRATION);
            this.isForward = isForward;
        }

        private final boolean isForward;

        public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
            ResourceLocation texture = this.isForward ? this.isHoveredOrFocused() ? PAGE_FORWARD_HIGHLIGHTED_SPRITE : PAGE_FORWARD_SPRITE : this.isHoveredOrFocused() ? PAGE_BACKWARD_HIGHLIGHTED_SPRITE : PAGE_BACKWARD_SPRITE;
            guiGraphics.blitSprite(texture, this.getX(), this.getY(), 15, 18);
        }

        public void playDownSound(SoundManager handler) {
            handler.play(SimpleSoundInstance.forUI(SoundEvents.BOOK_PAGE_TURN, 1.0F));
        }
    }
}
