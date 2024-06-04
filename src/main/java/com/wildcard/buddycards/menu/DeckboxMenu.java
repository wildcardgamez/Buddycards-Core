package com.wildcard.buddycards.menu;

import com.wildcard.buddycards.container.DeckboxContainer;
import com.wildcard.buddycards.item.BuddycardItem;
import com.wildcard.buddycards.item.BuddycardReprintItem;
import com.wildcard.buddycards.registries.BuddycardsMisc;
import com.wildcard.buddycards.util.ConfigManager;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;

public class DeckboxMenu extends AbstractContainerMenu {
    private final DeckboxContainer inventory;

    public DeckboxMenu(int id, Inventory playerInv) {
        this(id, playerInv, new DeckboxContainer(playerInv.getSelected()));
    }

    public DeckboxMenu(int id, Inventory playerInv, DeckboxContainer inv) {
        super(BuddycardsMisc.DECKBOX_CONTAINER.get(), id);
        checkContainerSize(inv, inv.getContainerSize());
        this.inventory = inv;

        //Set up slots for deckbox
        for (int y = 0; y < 2; y++) {
            for (int x = 0; x < 8; x++) {
                this.addSlot(new DeckSlot(inventory, x + (y * 8), 8 + x * 18, 18 + y * 18));
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

        this.inventory.startOpen(playerInv.player);
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    public static class DeckSlot extends Slot {
        public DeckSlot(Container inventoryIn, int index, int xPosition, int yPosition) {
            super(inventoryIn, index, xPosition, yPosition);
        }

        //Only let cards go into card slots if allowed in deck
        @Override
        public boolean mayPlace(ItemStack stack) {
            if((stack.getItem() instanceof BuddycardItem item)) {
                item = item.getOriginal();
                Rarity r = item.getRarity();
                //Each rarity has a limit on how many can be in the deck
                int max = r.equals(Rarity.COMMON) ? ConfigManager.deckLimitCommon.get() : r.equals(Rarity.UNCOMMON) ? ConfigManager.deckLimitUncommon.get() : r.equals(Rarity.RARE) ? ConfigManager.deckLimitRare.get() : ConfigManager.deckLimitEpic.get();
                for (int i = 0; i < 16; i++)
                    //Check for limit and that the card is properly implemented with an ability
                    if (!container.getItem(i).isEmpty() && item.equals(((BuddycardItem)container.getItem(i).getItem()).getOriginal())) {
                        if (--max <= 0 || ((BuddycardItem)container.getItem(i).getItem()).getOriginal().getAbilities().size() > 0) return false;
                    }
                return true;
            }
            return false;
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
