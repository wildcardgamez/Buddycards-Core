package com.wildcard.buddycards.screens;

import com.wildcard.buddycards.Buddycards;
import com.wildcard.buddycards.menu.DeckboxMenu;
import com.wildcard.buddycards.util.ConfigManager;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import java.util.List;

public class DeckboxScreen extends AbstractContainerScreen<DeckboxMenu> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(Buddycards.MOD_ID, "textures/gui/deckbox.png");

    public DeckboxScreen(DeckboxMenu container, Inventory playerInventory, Component title) {
        super(container, playerInventory, title);
        this.leftPos = 0;
        this.topPos = 0;
        this.imageWidth = 176;
        this.imageHeight = 150;
    }

    @Override
    protected void renderLabels(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY) {
        //Draw the name of the deck and the inventory titles
        pGuiGraphics.drawString(font, title, 8, 6, 4210752, false);
        pGuiGraphics.drawString(font, playerInventoryTitle, 8, 56, 4210752, false);
        if (pMouseX >= 157 + leftPos && pMouseX <= 172 + leftPos && pMouseY >= 22 + topPos && pMouseY <= 33 + topPos) {
            List<Component> tooltips = List.of(Component.translatable("item.buddycards.buddycard.deckbuilding_limit"),
                    Component.literal("" + ConfigManager.deckLimitCommon.get()).append(Component.translatable("item.buddycards.buddycard.deckbuilding_limit.common")).withStyle(ChatFormatting.GRAY),
                    Component.literal("" + ConfigManager.deckLimitUncommon.get()).append(Component.translatable("item.buddycards.buddycard.deckbuilding_limit.uncommon")).withStyle(ChatFormatting.GRAY),
                    Component.literal("" + ConfigManager.deckLimitRare.get()).append(Component.translatable("item.buddycards.buddycard.deckbuilding_limit.rare")).withStyle(ChatFormatting.GRAY),
                    Component.literal("" + ConfigManager.deckLimitEpic.get()).append(Component.translatable("item.buddycards.buddycard.deckbuilding_limit.epic")).withStyle(ChatFormatting.GRAY));
            pGuiGraphics.renderComponentTooltip(font, tooltips, pMouseX - leftPos, pMouseY - topPos);
        }
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        //Place the texture for the gui
        pGuiGraphics.blit(TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight);
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float delta) {
        super.renderBackground(pGuiGraphics);
        super.render(pGuiGraphics, pMouseX, pMouseY, delta);
        renderTooltip(pGuiGraphics, pMouseX, pMouseY);
    }
}
