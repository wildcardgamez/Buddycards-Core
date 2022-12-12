package com.wildcard.buddycards.container;

<<<<<<< Updated upstream:src/main/java/com/wildcard/buddycards/container/BattleBoardContainer.java
import com.wildcard.buddycards.block.entity.BattleBoardBlockEntity;
import com.wildcard.buddycards.registries.BuddycardsMisc;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
=======
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.wildcard.buddycards.Buddycards;
import com.wildcard.buddycards.menu.PlaymatMenu;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
>>>>>>> Stashed changes:src/main/java/com/wildcard/buddycards/container/PlaymatContainer.java
import net.minecraft.world.entity.player.Inventory;

<<<<<<< Updated upstream:src/main/java/com/wildcard/buddycards/container/BattleBoardContainer.java
public class BattleBoardContainer extends AbstractContainerMenu {
    IItemHandler handler;
    IItemHandler opponentHandler;
    BattleBoardBlockEntity entity;
    BattleBoardBlockEntity opponentEntity;

    public BattleBoardContainer(int id, Inventory playerInv, BlockPos pos) {
        super(BuddycardsMisc.BATTLE_BOARD_CONTAINER.get(), id);
        entity = (BattleBoardBlockEntity) playerInv.player.level.getBlockEntity(pos);
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    public static class CardSlot extends Slot {
        public CardSlot(Container inventoryIn, int index, int xPosition, int yPosition) {
            super(inventoryIn, index, xPosition, yPosition);
        }
=======
public class PlaymatContainer extends AbstractContainerScreen<PlaymatMenu> {

    private static final ResourceLocation TEXTURE1 = new ResourceLocation(Buddycards.MOD_ID, "textures/gui/battlescreen.png");
>>>>>>> Stashed changes:src/main/java/com/wildcard/buddycards/container/PlaymatContainer.java

    public PlaymatContainer(PlaymatMenu container, Inventory playerInventory, Component title) {
        super(container, playerInventory, title);
        this.leftPos = 0;
        this.topPos = 0;
        this.imageWidth = 176;
        this.imageHeight = 88;
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
    }

    @Override
    protected void renderBg(PoseStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        //Place the texture for the gui
        RenderSystem._setShaderTexture(0, TEXTURE1);
        blit(matrixStack, leftPos, topPos, 0, 0, imageWidth, imageHeight, 256, 256);
    }
}
