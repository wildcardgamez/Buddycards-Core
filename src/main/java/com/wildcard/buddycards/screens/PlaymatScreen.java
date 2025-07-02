package com.wildcard.buddycards.screens;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.wildcard.buddycards.Buddycards;
import com.wildcard.buddycards.battles.BattleComponent;
import com.wildcard.buddycards.battles.BuddycardBattleIcon;
import com.wildcard.buddycards.battles.IBattleIcon;
import com.wildcard.buddycards.battles.TextureBattleIcon;
import com.wildcard.buddycards.battles.game.BattleGame;
import com.wildcard.buddycards.battles.game.BattleStatusEffect;
import com.wildcard.buddycards.item.BuddycardItem;
import com.wildcard.buddycards.menu.PlaymatMenu;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class PlaymatScreen extends AbstractContainerScreen<PlaymatMenu> {
    public static final ResourceLocation TEXTURE1 = new ResourceLocation(Buddycards.MOD_ID, "textures/gui/playmat.png");
    public static final ResourceLocation smallFont = new ResourceLocation("buddycards", "smallnumbers");
    private float scrollPosition = 0;
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
        scrollPosition = 0;
    }

    private void sendButtonPress(int buttonId) {
        this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, buttonId);
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(pGuiGraphics);
        super.render(pGuiGraphics, mouseX, mouseY, partialTicks);
        int selectedSlot = this.menu.selectedSlot.get();
        if (selectedSlot != AbstractContainerMenu.SLOT_CLICKED_OUTSIDE) {
            ItemStack stack = this.menu.container.getItem(selectedSlot);
            if (!stack.isEmpty()) {
                PoseStack posestack = RenderSystem.getModelViewStack();
                posestack.pushPose();
                posestack.translate(0.0D, 0.0D, 32.0D);
                RenderSystem.applyModelViewMatrix();
                //this.setBlitOffset(200);
                //this.itemRenderer.blitOffset = 200.0F;
                pGuiGraphics.renderItem(stack, mouseX - 8, mouseY - 8);
                //this.setBlitOffset(0);
                //this.itemRenderer.blitOffset = 0.0F;
                posestack.popPose();
            }
        }
        this.renderTooltip(pGuiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY) {
        //Draw the name of the playmat and the inventory titles
        pGuiGraphics.drawString(font, title, 8, 6, 4210752, false);
        pGuiGraphics.drawString(font, Integer.toString(this.menu.opponentEnergy.get()), 20, 22, 4210752, false);
        pGuiGraphics.drawString(font, Integer.toString(this.menu.opponentHealth.get()), 51, 22, 4210752, false);
        pGuiGraphics.drawString(font, Integer.toString(this.menu.energy.get()), 20, 40, 4210752, false);
        pGuiGraphics.drawString(font, Integer.toString(this.menu.health.get()), 51, 40, 4210752, false);
        renderBattleLog(pGuiGraphics, pMouseX, pMouseY);
        renderCardPower(pGuiGraphics);
    }

    private void renderBattleLog(GuiGraphics pGuiGraphics, int mouseX, int mouseY) {
        int scale = (int)minecraft.getWindow().getGuiScale();
        ScrollerData data = ScrollerData.fromScreen(this);
        RenderSystem.enableScissor((leftPos-103) * scale, (topPos-data.drawableHeight()+imageHeight - 7)*scale, 98 * scale, (data.drawableHeight()+4)*scale);

        int height = 6;
        ResourceLocation lastDraw = null;
        for (BattleComponent battleComponent : getMenu().getBattleLog()) {
            int leftShift = -103;
            for (IBattleIcon battleIcon : battleComponent.getBattleIcons()) {
                if (battleIcon instanceof BuddycardBattleIcon buddycardBattleIcon) {
                    pGuiGraphics.renderItem(this.minecraft.player, buddycardBattleIcon.getItem().getDefaultInstance(), leftShift - 4, height - 2 - data.scrollerOffset(scrollPosition), 0);
                    lastDraw = null;
                } else if (battleIcon instanceof TextureBattleIcon t) {
                    if (t.texture() != lastDraw) {
                        lastDraw = t.texture();
                        RenderSystem._setShaderTexture(0, lastDraw);
                    }
                    pGuiGraphics.blit(TEXTURE1, leftShift, height - data.scrollerOffset(scrollPosition), t.texturePosX(), t.texturePosY(), battleIcon.width(), 12);
                    //This pop and push might not work?
                    pGuiGraphics.pose().pushPose();
                    RenderSystem.disableDepthTest();
                    for (TextureBattleIcon.BattleInfo battleInfo : t.info()) {
                        MutableComponent text = Component.literal(battleInfo.display()).withStyle(style -> style.withFont(smallFont));
                        pGuiGraphics.drawString(font, text, leftShift + battleInfo.x() - (battleInfo.isLeftAligned() ? 0 : this.font.width(text)), height + battleInfo.y() - data.scrollerOffset(scrollPosition), battleInfo.color());
                    }
                    RenderSystem.enableDepthTest();
                    pGuiGraphics.pose().popPose();
                }
                leftShift += battleIcon.width() + 2;
            }
            height += 12 + 2;
        }
        int xMouseScreen = mouseX - leftPos;
        int yMouseScreen = mouseY - topPos;
        int tooltipRow = (yMouseScreen - 6 + data.scrollerOffset(scrollPosition)) / 14;
        List<BattleComponent> battleLog = menu.getBattleLog();
        RenderSystem.disableScissor();
        if (tooltipRow >= 0 && tooltipRow < battleLog.size() && yMouseScreen > 0) {
            if (mouseX > leftPos - 103 && mouseX < leftPos) {
                pGuiGraphics.renderTooltip(font, battleLog.get(tooltipRow).getHoverText(), xMouseScreen, yMouseScreen);
            }
        }
    }

    private void renderCardPower(GuiGraphics pGuiGraphics) {
        BattleGame game = getMenu().container.game;
        for (int i = 0; i < 6; i++) {
            Slot slot = getMenu().getSlot( i + 5);
            ItemStack itemStack = slot.getItem();
            if (!(itemStack.getItem() instanceof BuddycardItem card)) {
                continue;
            }
            int position = menu.entity.p1 ? i : BattleGame.opposite(i);
            int power = game.state[position].power;
            ChatFormatting color;
            if (power == 0) {
                color = ChatFormatting.RED;
            } else if (power >= card.getPower()) {
                color = ChatFormatting.GREEN;
            } else {
                color = ChatFormatting.YELLOW;
            }
            MutableComponent text = Component.literal(power + "").withStyle(style -> style.withFont(smallFont));
            pGuiGraphics.drawString(font, text, slot.x + 13, slot.y + (i < 3 ? 8 : -1), Objects.requireNonNull(color.getColor()));
            if(!game.state[position].status.equals(BattleStatusEffect.EMPTY)) {
                BattleStatusEffect status = game.state[position].status;
                text = Component.literal(Character.toString('\u0ED0' + status.ordinal())).withStyle(style -> style.withFont(smallFont));
                pGuiGraphics.drawString(font, text, slot.x + 13, slot.y + (i < 3 ? 2 : 5), status.getColor());
            }
        }
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics pGuiGraphics, float partialTicks, int mouseX, int mouseY) {
        //Place the texture for the gui
        RenderSystem._setShaderTexture(0, TEXTURE1);
        pGuiGraphics.blit(TEXTURE1, leftPos, topPos, 0, 0, imageWidth, imageHeight);
        int battleLogLength = getMenu().getBattleLog().size();
        if (battleLogLength > 0) {
            ScrollerData scrollerData = ScrollerData.fromScreen(this);
            int height = 0;
            pGuiGraphics.blit(TEXTURE1, leftPos - 108, topPos + height, 0, 88, 108, 5);
            if (scrollerData.needsScroller()) {
                pGuiGraphics.blit(TEXTURE1, leftPos - 118, topPos + height, 108, 88, 10, 5);
            }
            height += 5;
            while (height < scrollerData.drawableHeight() + 5) {
                int drawnHeight = Math.min(14, scrollerData.drawableHeight() - height + 5);
                pGuiGraphics.blit(TEXTURE1, leftPos - 108, topPos + height, 0, 98, 108, drawnHeight);
                if (scrollerData.needsScroller()) {
                    pGuiGraphics.blit(TEXTURE1, leftPos - 118, topPos + height, 108, 98, 10, drawnHeight);
                }
                height += drawnHeight;
            }
            pGuiGraphics.blit(TEXTURE1, leftPos - 108, topPos + height, 0, 93, 108, 5);
            if (scrollerData.needsScroller()) {
                pGuiGraphics.blit(TEXTURE1, leftPos - 118, topPos + height, 108, 93, 10, 5);
                pGuiGraphics.blit(TEXTURE1, leftPos - 115, topPos + (int)scrollPosition + 3, 118, 98, 4, 14);
            }
        }
    }

    @Override
    public boolean mouseClicked(double x, double y, int mouse) {
        if (updateScrollPosition(x,y, mouse)) {
            return true;
        }
        return super.mouseClicked(x, y, mouse);
    }

    @Override
    public boolean mouseDragged(double x, double y, int mouse, double x2, double y2) {
        if (updateScrollPosition(x,y, mouse)) {
            return true;
        }
        return super.mouseDragged(x, y, mouse, x2, y2);
    }

    @Override
    public boolean mouseScrolled(double x, double y, double amount) {
        ScrollerData data = ScrollerData.fromScreen(this);
        if (data.needsScroller() && x >= leftPos - 123 && x < leftPos && y >= topPos && y <= height) {
            float scrollRatio = (data.scrollerPosMax - data.scrollerPosMin) / ((data.requiredHeight - data.availableHeight) * .065f);
            scrollPosition = Mth.clamp(scrollPosition - (float) amount * scrollRatio, 0, data.scrollerPosMax - data.scrollerPosMin);
            return true;
        }
        return super.mouseScrolled(x, y, amount);
    }

    private boolean updateScrollPosition(double x, double y, int mouse) {
        ScrollerData data = ScrollerData.fromScreen(this);
        if (mouse == InputConstants.MOUSE_BUTTON_LEFT && data.needsScroller()) {
            if (x  >= leftPos - 123 && x <=leftPos - 103
                    && y >= topPos && y <= height) {
                scrollPosition = Mth.clamp((float)y - data.scrollerPosMin, 0, data.scrollerPosMax - data.scrollerPosMin);
                return true;
            }
        }
        return false;
    }

    private record ScrollerData(int requiredHeight, int availableHeight, int scrollerPosMin, int scrollerPosMax) {

        private static ScrollerData fromScreen(PlaymatScreen screen) {
            return new ScrollerData(screen.getMenu().getBattleLog().size() * 14, screen.height - screen.topPos - 10, screen.topPos + 10, screen.height - 3 - 7);
        }
        private int drawableHeight() {
            return Math.min(availableHeight, requiredHeight);
        }
        private boolean needsScroller() {
            return requiredHeight > availableHeight;
        }

        private int scrollerOffset(float scrollerPos) {
            if (!needsScroller())
                return 0;
            float offset = scrollerPos / (scrollerPosMax - scrollerPosMin);
            return (int)((requiredHeight - availableHeight)*offset);
        }
    }
}
