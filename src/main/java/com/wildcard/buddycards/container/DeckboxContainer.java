package com.wildcard.buddycards.container;

import com.wildcard.buddycards.inventory.DeckboxInventory;
import com.wildcard.buddycards.item.BuddycardItem;
import com.wildcard.buddycards.registries.BuddycardsMisc;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class DeckboxContainer extends AbstractContainerMenu {
    private final DeckboxInventory inventory;

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
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    public static class DeckSlot extends Slot {
        public DeckSlot(Container inventoryIn, int index, int xPosition, int yPosition) {
            super(inventoryIn, index, xPosition, yPosition);
        }

        //Only let cards go into card slots
        @Override
        public boolean mayPlace(ItemStack stack) {
            return stack.getItem() instanceof BuddycardItem;
        }

        //Only 1 card per slot
        @Override
        public int getMaxStackSize() {
            return 1;
        }
    }
    public class InvSlot extends Slot {
        public InvSlot(Container inventoryIn, int index, int xPosition, int yPosition) {
            super(inventoryIn, index, xPosition, yPosition);
        }

        //Only let the stack move if it isn't the open deckbox
        @Override
        public boolean mayPickup(Player player) {
            return !(this.getItem().equals(inventory.deckbox));
        }
    }

    @Override
    public void removed(Player player) {
        inventory.stopOpen(player);
        super.removed(player);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = slots.get(index);
        if(slot.hasItem())
        {
            stack = slot.getItem().copy();
            if (index < slots.size() - 36)
            {
                if(!this.moveItemStackTo(slot.getItem(), slots.size() - 36, slots.size(), true))
                    return ItemStack.EMPTY;
            }
            else if(!this.moveItemStackTo(slot.getItem(), 0, slots.size() - 36, false))
                return ItemStack.EMPTY;
            if(slot.getItem().isEmpty())
                slot.set(ItemStack.EMPTY);
            else
                slot.setChanged();
        }
        return stack;
    }
}
