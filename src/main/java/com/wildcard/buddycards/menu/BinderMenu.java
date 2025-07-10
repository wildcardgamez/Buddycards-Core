package com.wildcard.buddycards.menu;

import com.wildcard.buddycards.container.BinderItemHandler;
import com.wildcard.buddycards.item.BuddycardBinderItem;
import com.wildcard.buddycards.item.BuddycardItem;
import com.wildcard.buddycards.registries.BuddycardsMisc;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class BinderMenu extends AbstractContainerMenu {

    private final BinderItemHandler handler;
    private final DataSlot pageData = DataSlot.standalone();
    private int page;

    public BinderMenu(int id, Inventory playerInv) {
        this(id, playerInv, new BinderItemHandler(playerInv.getSelected(), 3 + EnchantmentHelper.getItemEnchantmentLevel(BuddycardsMisc.EXTRA_PAGE.get(), playerInv.getSelected()), EnchantmentHelper.getItemEnchantmentLevel(BuddycardsMisc.THICK_POCKETS.get(), playerInv.getSelected())));
    }

    public BinderMenu(int id, Inventory playerInv, BinderItemHandler handler) {
        super(BuddycardsMisc.BINDER_MENU.get(), id);
        this.handler = handler;
        //Set up slots for binder
        this.addDataSlot(pageData);
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 8; x++) {
                this.addSlot(new BinderSlot(this.handler, x + (y * 8), (x < 4 ? 8 : 26) + x * 18, 26 + y * 18));
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
        if (buttonId == 0 && 0 < page) {
            page -= 1;
            pageData.set(page);
            broadcastChanges();
            return true;
        }
        else if (buttonId == 1 && page + 1 < handler.getPageAmt()) {
            page += 1;
            pageData.set(page);
            broadcastChanges();
            return true;
        }
        return false;
    }

    public class BinderSlot extends SlotItemHandler {
        public BinderSlot(IItemHandler inventoryIn, int index, int xPosition, int yPosition) {
            super(inventoryIn, index, xPosition, yPosition);
        }

        //Only let cards go into card slots
        @Override
        public boolean mayPlace(ItemStack stack) {
            return stack.getItem() instanceof BuddycardItem;
        }

        @Override
        public ItemStack getItem() {
            return this.getItemHandler().getStackInSlot(getRealSlotIndex());
        }

        @Override
        public void set(ItemStack stack) {
            ((IItemHandlerModifiable) this.getItemHandler()).setStackInSlot(getRealSlotIndex(), stack);
            this.setChanged();
        }

        @Override
        public int getMaxStackSize() {
            return this.getItemHandler().getSlotLimit(getRealSlotIndex());
        }

        @Override
        public int getMaxStackSize(@NotNull ItemStack stack)
        {
            int index = getRealSlotIndex();
            ItemStack maxAdd = stack.copy();
            int maxInput = stack.getMaxStackSize();
            maxAdd.setCount(maxInput);
            IItemHandler handler = this.getItemHandler();
            ItemStack currentStack = handler.getStackInSlot(index);
            if (handler instanceof IItemHandlerModifiable) {
                IItemHandlerModifiable handlerModifiable = (IItemHandlerModifiable) handler;
                handlerModifiable.setStackInSlot(index, ItemStack.EMPTY);
                ItemStack remainder = handlerModifiable.insertItem(index, maxAdd, true);
                handlerModifiable.setStackInSlot(index, currentStack);
                return maxInput - remainder.getCount();
            }
            else
            {
                ItemStack remainder = handler.insertItem(index, maxAdd, true);
                int current = currentStack.getCount();
                int added = maxInput - remainder.getCount();
                return current + added;
            }
        }

        @Override
        public boolean mayPickup(Player playerIn) {
            return !this.getItemHandler().extractItem(getRealSlotIndex(), 1, true).isEmpty();
        }

        @Override
        @NotNull
        public ItemStack remove(int amt) {
            return this.getItemHandler().extractItem(getRealSlotIndex(), amt, false);
        }

        public int getRealSlotIndex() {
            return (page * 32) + getSlotIndex();
        }
    }
    public class InvSlot extends Slot {
        public InvSlot(Container inventoryIn, int index, int xPosition, int yPosition) {
            super(inventoryIn, index, xPosition, yPosition);
        }

        //Only let the stack move if it isn't the open binder
        @Override
        public boolean mayPickup(Player player) {
            return !(this.getItem().getItem() instanceof BuddycardBinderItem);
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return !(this.getItem().getItem() instanceof BuddycardBinderItem);
        }
    }

    @Override
    public void removed(Player player) {
        //Run the code to check the inventory and convert to nbt
        handler.saveAndClose();
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
        return ((BuddycardBinderItem) handler.getBinder().getItem()).getBinderTexture();
    }

    public int getPageAmt() {
        return handler.getPageAmt();
    }

    public int getCurrentPage() {
        return pageData.get() + 1;
    }
}
