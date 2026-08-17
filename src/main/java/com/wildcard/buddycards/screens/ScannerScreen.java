package com.wildcard.buddycards.screens;

import com.wildcard.buddycards.Buddycards;
import com.wildcard.buddycards.menu.ScannerMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

public class ScannerScreen extends AbstractContainerScreen<ScannerMenu> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(Buddycards.MOD_ID, "textures/gui/scanner.png");
    private static final ResourceLocation SLOT = ResourceLocation.fromNamespaceAndPath(Buddycards.MOD_ID, "container/scanner/card_slot");
    private static final ResourceLocation PACK = ResourceLocation.fromNamespaceAndPath(Buddycards.MOD_ID, "container/scanner/pack_slot");

    public ScannerScreen(ScannerMenu menu, Inventory playerInv, Component title) {
        super(menu, playerInv, title);
        this.width = 176;
        this.height = 186;
        this.leftPos = 0;
        this.topPos = 0;
    }

    @Override
    protected void init() {
        super.init();
        this.addRenderableWidget(new ScannerPageButton(leftPos + 128, topPos - 5, false, btn -> this.sendButtonPress(0)));
        this.addRenderableWidget(new ScannerPageButton(leftPos + 163, topPos - 5, true, btn -> this.sendButtonPress(1)));
    }

    private void sendButtonPress(int buttonId) {
        this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, buttonId);
    }

    @Override
    public void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        guiGraphics.blit(TEXTURE, leftPos, topPos, 0, 0, width, height, 256, 256);
    }

    @Override
    public void renderLabels(GuiGraphics guiGraphics, int MouseX, int MouseY) {
        guiGraphics.drawString(font, Component.translatable("item.buddycards.buddycard.set_" + menu.getCurrentSet().getName()), 8, 6, 10021119, false);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        super.renderBackground(guiGraphics, mouseX, mouseY, delta);
        super.render(guiGraphics, mouseX, mouseY, delta);
        //Go through every card
        for (int i = 0; i < menu.getCurrentSet().getCards().size(); i++) {
            int x = leftPos + 9 + (i % 9 * 18), y = topPos + 18 + (i / 9 * 18);
            //Leave a blank slot
            guiGraphics.blitSprite(SLOT, x, y, 16, 16);
            if (menu.hasPercentages()) {
                //Make the fake bar
                guiGraphics.fill(RenderType.guiOverlay(), x, y + 2, x + 2, y + 14, 0xFF1b1824);
                double percent = menu.getCompletionForCard(i);
                //Only fill the bar if there is stuff to fill
                if (percent > 0) {
                    int color = percent < .2 ? 0xFF617bc2 : percent < .5 ? 0xFF98e8ff : percent != 1 ? 0xFFffef98 : 0xFFfc83ff;
                    guiGraphics.fill(RenderType.guiOverlay(), x, y + 13 - (int) (percent * 11), x + 2, y + 14, color);
                }
            }
        }
        //Place the pack
        guiGraphics.blitSprite(PACK, leftPos + 144, topPos - 8, 18, 24);
        guiGraphics.blitSprite(ResourceLocation.fromNamespaceAndPath(Buddycards.MOD_ID, "container/scanner/packs/" + menu.getCurrentSet().getName()), leftPos + 145, topPos - 4, 16, 16);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderTooltip(GuiGraphics guiGraphics, int x, int y) {
        if (this.hoveredSlot != null) {
            ItemStack itemstack = this.hoveredSlot.getItem();
            guiGraphics.renderTooltip(this.font, menu.getCollectionTooltip(this.hoveredSlot.index + 1), itemstack.getTooltipImage(), itemstack, x, y);
        }
    }

    public static class ScannerPageButton extends Button {
        private static final ResourceLocation PAGE_FORWARD_HIGHLIGHTED_SPRITE = ResourceLocation.fromNamespaceAndPath(Buddycards.MOD_ID, "container/scanner/page_forward_highlighted");
        private static final ResourceLocation PAGE_FORWARD_SPRITE = ResourceLocation.fromNamespaceAndPath(Buddycards.MOD_ID, "container/scanner/page_forward");
        private static final ResourceLocation PAGE_BACKWARD_HIGHLIGHTED_SPRITE = ResourceLocation.fromNamespaceAndPath(Buddycards.MOD_ID, "container/scanner/page_backward_highlighted");
        private static final ResourceLocation PAGE_BACKWARD_SPRITE = ResourceLocation.fromNamespaceAndPath(Buddycards.MOD_ID, "container/scanner/page_backward");

        protected ScannerPageButton(int x, int y, boolean isForward, OnPress onPress) {
            super(x, y, 15, 18, CommonComponents.EMPTY, onPress, DEFAULT_NARRATION);
            this.isForward = isForward;
        }

        private final boolean isForward;

        public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
            ResourceLocation texture = this.isForward ? this.isHoveredOrFocused() ? PAGE_FORWARD_HIGHLIGHTED_SPRITE : PAGE_FORWARD_SPRITE : this.isHoveredOrFocused() ? PAGE_BACKWARD_HIGHLIGHTED_SPRITE : PAGE_BACKWARD_SPRITE;
            guiGraphics.blitSprite(texture, this.getX(), this.getY(), 15, 18);
        }
    }
}
