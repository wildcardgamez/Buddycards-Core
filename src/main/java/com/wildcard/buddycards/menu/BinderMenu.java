package com.wildcard.buddycards.menu;

import com.wildcard.buddycards.container.BinderContainer;
import com.wildcard.buddycards.item.BuddycardBinderItem;
import com.wildcard.buddycards.item.BuddycardItem;
import com.wildcard.buddycards.registries.BuddycardsMisc;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

public class BinderMenu extends AbstractContainerMenu {

    private final BinderContainer binderInv;

    public BinderMenu(int id, Inventory playerInv) {
        this(id, playerInv, new BinderContainer(playerInv.getSelected(), 3 + EnchantmentHelper.getItemEnchantmentLevel(BuddycardsMisc.EXTRA_PAGE.get(), playerInv.getSelected())));
    }

    public BinderMenu(int id, Inventory playerInv, BinderContainer binderInv) {
        super(BuddycardsMisc.BINDER_CONTAINER.get(), id);
        checkContainerSize(binderInv, binderInv.getContainerSize());
        this.binderInv = binderInv;
        //Set up slots for binder
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 8; x++) {
                this.addSlot(new BinderSlot(this.binderInv, x + (y * 8), (x < 4 ? 8 : 26) + x * 18, 26 + y * 18));
            }
        }
        //Set up slots for inventory
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 9; x++) {
                this.addSlot(new InvSlot(playerInv, x + (y * 9) + 9, 8 + x * 18, 122 + y * 18));
            }
        }
        //Set up slots for hotbar
        for (int x = 0; x < 9; x++) {
            this.addSlot(new InvSlot(playerInv, x, 8 + x * 18, 180));
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public boolean clickMenuButton(Player player, int buttonId) {
        if (buttonId == 0 && 0 < binderInv.currentPage)
            binderInv.lastPage();
        else if (buttonId == 1 && binderInv.currentPage < binderInv.pageAmt)
            binderInv.nextPage();
        return false;
    }

    public static class BinderSlot extends Slot {
        public BinderSlot(Container inventoryIn, int index, int xPosition, int yPosition) {
            super(inventoryIn, index, xPosition, yPosition);
        }

        //Only let cards go into card slots
        @Override
        public boolean mayPlace(ItemStack stack) {
            return stack.getItem() instanceof BuddycardItem;
        }
    }
    public class InvSlot extends Slot {
        public InvSlot(Container inventoryIn, int index, int xPosition, int yPosition) {
            super(inventoryIn, index, xPosition, yPosition);
        }

        //Only let the stack move if it isn't the open binder
        @Override
        public boolean mayPickup(Player player) {
            return !(this.getItem().equals(binderInv.binder));
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return !(this.getItem().equals(binderInv.binder));
        }
    }

    @Override
    public void removed(Player player) {
        //Run the code to check the inventory and convert to nbt
        binderInv.stopOpen(player);
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

    public ResourceLocation getTexture() {
        return ((BuddycardBinderItem) binderInv.binder.getItem()).getBinderTexture();
    }

    public int getPageAmt() {
        return binderInv.pageAmt;
    }

    public int getCurrentPage() {
        return binderInv.currentPage + 1;
    }
}
