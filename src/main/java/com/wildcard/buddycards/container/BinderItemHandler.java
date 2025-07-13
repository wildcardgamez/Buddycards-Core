package com.wildcard.buddycards.container;

import com.wildcard.buddycards.item.BuddycardItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

public class BinderItemHandler extends ItemStackHandler {
    public BinderItemHandler(ItemStack binderIn, int pageAmtIn) {
        super(32 * pageAmtIn);
        binder = binderIn;
        pageAmt = pageAmtIn;
        if (binder.hasTag() && binder.getTag().contains("Inventory"))
            deserializeNBT(binder.getTag().getCompound("Inventory"));
    }

    protected ItemStack binder;
    protected final int pageAmt;

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
        CompoundTag nbt = binder.getOrCreateTag();
        nbt.put("Inventory", serializeNBT());
        binder.setTag(nbt);
    }
}
