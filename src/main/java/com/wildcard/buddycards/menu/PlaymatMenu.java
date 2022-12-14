package com.wildcard.buddycards.menu;

import com.wildcard.buddycards.container.PlaymatContainer;
import com.wildcard.buddycards.registries.BuddycardsMisc;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class PlaymatMenu extends AbstractContainerMenu {
    PlaymatContainer playerContainer;
    PlaymatContainer opponentContainer;

    public PlaymatMenu(int id, Inventory playerInv) {
        this(id, playerInv, new PlaymatContainer(new PlaymatContainer()));
    }

    public PlaymatMenu(int id, Inventory playerInv, PlaymatContainer container) {
        super(BuddycardsMisc.PLAYMAT_CONTAINER.get(), id);
        this.playerContainer = container;
        opponentContainer = playerContainer.opponent;
        this.addSlot(new DeckSlot(opponentContainer, 0, 143, 18));
        this.addSlot(new DeckSlot(this.playerContainer, 0, 143, 64));
        for (int i = 0; i < 3; i++) {
            this.addSlot(new CardSlot(this.playerContainer, 1+i, 80 + (18 * i), 64));
        }
        for (int i = 0; i < 3; i++) {
            this.addSlot(new BattlefieldSlot(this.playerContainer, 4+i, 80 + (18 * i), 36));
        }
        for (int i = 0; i < 3; i++) {
            this.addSlot(new OpponentBattlefieldSlot(opponentContainer, 4+i, 80 + (18 * i), 18));
        }
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
