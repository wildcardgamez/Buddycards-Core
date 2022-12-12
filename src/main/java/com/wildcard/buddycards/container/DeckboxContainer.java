package com.wildcard.buddycards.container;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.wildcard.buddycards.Buddycards;
import com.wildcard.buddycards.menu.DeckboxMenu;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class DeckboxContainer extends AbstractContainerScreen<DeckboxMenu> {

<<<<<<< Updated upstream
    public DeckboxContainer(int id, Inventory playerInv) {
        this(id, playerInv, new DeckboxInventory(playerInv.getSelected()));
    }

    public DeckboxContainer(int id, Inventory playerInv, DeckboxInventory inv) {
        super(BuddycardsMisc.DECKBOX_CONTAINER.get(), id);
        checkContainerSize(inv, inv.getContainerSize());
        this.inventory = inv;

        //Set up slots for deckbox
        for (int y = 0; y < 2; y++) {
            for (int x = 0; x < 9; x++) {
                this.addSlot(new DeckSlot(inventory, x + (y * 9), 8 + x * 18, 18 + y * 18));
            }
        }
        //Set up slots for inventory
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 9; x++) {
                this.addSlot(new InvSlot(playerInv, x + (y * 9) + 9, 8 + x * 18, 68 + y * 18));
            }
        }
        //Set up slots for hotbar
        for (int x = 0; x < 9; x++) {
            this.addSlot(new InvSlot(playerInv, x, 8 + x * 18, 126));
        }
=======
    private static final ResourceLocation TEXTURE1 = new ResourceLocation(Buddycards.MOD_ID, "textures/gui/deckbox.png");

    public DeckboxContainer(DeckboxMenu container, Inventory playerInventory, Component title) {
        super(container, playerInventory, title);
        this.leftPos = 0;
        this.topPos = 0;
        this.imageWidth = 176;
        this.imageHeight = 150;
>>>>>>> Stashed changes
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
    }

    @Override
    protected void renderBg(PoseStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        //Place the texture for the gui
        RenderSystem._setShaderTexture(0, TEXTURE1);
        blit(matrixStack, leftPos, topPos, 0, 0, imageWidth, imageHeight, 256, 256);
    }
}
