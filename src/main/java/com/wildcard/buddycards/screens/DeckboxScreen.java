package com.wildcard.buddycards.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.wildcard.buddycards.Buddycards;
import com.wildcard.buddycards.menu.DeckboxMenu;
import com.wildcard.buddycards.util.ConfigManager;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class DeckboxScreen extends AbstractContainerScreen<DeckboxMenu> {
    private static final ResourceLocation TEXTURE1 = new ResourceLocation(Buddycards.MOD_ID, "textures/gui/deckbox.png");

    public DeckboxScreen(DeckboxMenu container, Inventory playerInventory, Component title) {
        super(container, playerInventory, title);
        this.leftPos = 0;
        this.topPos = 0;
        this.imageWidth = 176;
        this.imageHeight = 150;
    }

    @Override
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(PoseStack matrixStack, int x, int y) {
        //Draw the name of the deck and the inventory titles
        this.font.draw(matrixStack, title, 8.0f, 6.0f, 4210752);
        this.font.draw(matrixStack, playerInventoryTitle,8.0f, 56.0f, 4210752);
        if (x >= 157 + leftPos && x <= 188 +leftPos && y >= 22 + topPos && y <= 33 + topPos) {
            List<Component> tooltips = List.of(new TranslatableComponent("item.buddycards.buddycard.deckbuilding_limit"),
                    new TextComponent("" + ConfigManager.deckLimitCommon.get()).append(new TranslatableComponent("item.buddycards.buddycard.deckbuilding_limit.common")).withStyle(ChatFormatting.GRAY),
                    new TextComponent("" + ConfigManager.deckLimitUncommon.get()).append(new TranslatableComponent("item.buddycards.buddycard.deckbuilding_limit.uncommon")).withStyle(ChatFormatting.GRAY),
                    new TextComponent("" + ConfigManager.deckLimitRare.get()).append(new TranslatableComponent("item.buddycards.buddycard.deckbuilding_limit.rare")).withStyle(ChatFormatting.GRAY),
                    new TextComponent("" + ConfigManager.deckLimitEpic.get()).append(new TranslatableComponent("item.buddycards.buddycard.deckbuilding_limit.epic")).withStyle(ChatFormatting.GRAY));
            renderComponentTooltip(matrixStack, tooltips, x - leftPos, y - topPos);
        }
    }

    @Override
    protected void renderBg(PoseStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        //Place the texture for the gui
        RenderSystem._setShaderTexture(0, TEXTURE1);
        blit(matrixStack, leftPos, topPos, 0, 0, imageWidth, imageHeight, 256, 256);
    }
}
