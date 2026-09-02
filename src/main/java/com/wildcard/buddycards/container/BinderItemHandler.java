package com.wildcard.buddycards.container;

import com.wildcard.buddycards.item.BuddycardItem;
import com.wildcard.buddycards.item.component.BinderContents;
import com.wildcard.buddycards.registries.BuddycardsComponents;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

public class BinderItemHandler extends ItemStackHandler {
    public BinderItemHandler(ItemStack binderIn, int pageAmtIn, boolean large, HolderLookup.Provider provider) {
        super((large ? 72 : 32) * pageAmtIn);
        binder = binderIn;
        pageAmt = pageAmtIn;
        if (binder.has(BuddycardsComponents.BINDER))
            binder.get(BuddycardsComponents.BINDER).copyInto(stacks);
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
        binder.set(BuddycardsComponents.BINDER, BinderContents.fromItems(stacks));
    }
}
