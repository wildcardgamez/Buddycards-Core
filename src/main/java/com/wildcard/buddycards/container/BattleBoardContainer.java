package com.wildcard.buddycards.container;

import com.wildcard.buddycards.block.entity.BattleBoardBlockEntity;
import com.wildcard.buddycards.registries.BuddycardsMisc;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

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

        //Only 1 card per slot
        @Override
        public int getMaxStackSize() {
            return 1;
        }
    }

    public static class BattlefieldSlot extends CardSlot {
        public BattlefieldSlot(Container inventoryIn, int index, int xPosition, int yPosition) {
            super(inventoryIn, index, xPosition, yPosition);
        }

        //Once a card enters the battlefield, you cant move it
        @Override
        public boolean mayPickup(Player player) {
            return false;
        }
    }

    public static class OpponentBattlefieldSlot extends BattlefieldSlot {
        public OpponentBattlefieldSlot(Container inventoryIn, int index, int xPosition, int yPosition) {
            super(inventoryIn, index, xPosition, yPosition);
        }

        //You cannot play cards on the opponents battlefield
        @Override
        public boolean mayPlace(ItemStack stack) {
            return false;
        }
    }

    public static class DeckSlot extends Slot {
        public DeckSlot(Container inventoryIn, int index, int xPosition, int yPosition) {
            super(inventoryIn, index, xPosition, yPosition);
        }

        //You cannot move the deck
        @Override
        public boolean mayPickup(Player player) {
            return false;
        }
    }
}
