package com.wildcard.buddycards.container;

import com.wildcard.buddycards.inventory.BinderInventory;
import com.wildcard.buddycards.item.BuddycardItem;
import com.wildcard.buddycards.registries.BuddycardsMisc;
import com.wildcard.buddycards.savedata.EnderBinderSaveData;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class DeckboxContainer extends AbstractContainerMenu {
    private final BinderInventory inventory;

    public DeckboxContainer(int id, Inventory playerInv) {
        this(id, playerInv, new BinderInventory(18, playerInv.getSelected()));
    }

    public DeckboxContainer(int id, Inventory playerInv, BinderInventory binderInv) {
        super(BuddycardsMisc.DECKBOX_CONTAINER.get(), id);
        checkContainerSize(binderInv, binderInv.getContainerSize());
        this.inventory = binderInv;

        //Set up slots for deckbox
        for (int y = 0; y < 6; y++) {
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

        @Override
        public int getMaxStackSize() {
            return 1;
        }
    }
    public class InvSlot extends Slot {
        public InvSlot(Container inventoryIn, int index, int xPosition, int yPosition) {
            super(inventoryIn, index, xPosition, yPosition);
        }

        //Only let the stack move if it isn't the open binder
        @Override
        public boolean mayPickup(Player player) {
            return !(this.getItem().equals(inventory.binder));
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return !(this.getItem().equals(inventory.binder));
        }
    }

    @Override
    public void removed(Player player) {
        //Run the code to check the inventory and convert to nbt
        if(!inventory.ender)
            inventory.stopOpen(player);
        else
            EnderBinderSaveData.get(player.createCommandSourceStack().getLevel()).setDirty();
        super.removed(player);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = slots.get(index);
        if(slot.hasItem())
        {
            stack = slot.getItem().copy();
            if (index < 18)
                if(!this.moveItemStackTo(slot.getItem(), 18, slots.size(), true))
                    return ItemStack.EMPTY;
            else if(!this.moveItemStackTo(slot.getItem(), 0, 18, false))
                return ItemStack.EMPTY;
            if(slot.getItem().isEmpty())
                slot.set(ItemStack.EMPTY);
            else
                slot.setChanged();
        }
        return stack;
    }
}
