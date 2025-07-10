package com.wildcard.buddycards.container;

import com.wildcard.buddycards.item.BuddycardItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

public class BinderItemHandler extends ItemStackHandler {
    public BinderItemHandler(ItemStack binderIn, int pageAmtIn, int stackModIn) {
        super(32 * pageAmtIn);
        binder = binderIn;
        pageAmt = pageAmtIn;
        slotLimit = 32 * (1 + stackModIn);
        if (binder.hasTag() && binder.getTag().contains("Items"))
            deserializeNBT(binder.getTag());
    }

    protected ItemStack binder;
    protected final int pageAmt;
    protected final int slotLimit;

    @Override
    public int getSlotLimit(int slot) {
        return slotLimit;
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return stack.getItem() instanceof BuddycardItem;
    }

    public int getPageAmt() {
        return pageAmt;
    }

    public ItemStack getBinder() {
        return binder;
    }

    public void saveAndClose() {
        binder.setTag(serializeNBT());
    }
}
