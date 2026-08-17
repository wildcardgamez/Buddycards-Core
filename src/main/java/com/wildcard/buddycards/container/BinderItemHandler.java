package com.wildcard.buddycards.container;

import com.wildcard.buddycards.item.BuddycardItem;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

public class BinderItemHandler extends ItemStackHandler {
    public BinderItemHandler(ItemStack binderIn, int pageAmtIn, boolean large, HolderLookup.Provider provider) {
        super((large ? 72 : 32) * pageAmtIn);
        binder = binderIn;
        pageAmt = pageAmtIn;
        if (binder.has(DataComponents.CONTAINER))
            binder.get(DataComponents.CONTAINER).copyInto(stacks);
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
        binder.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(stacks));
    }
}
