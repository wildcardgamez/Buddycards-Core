package com.wildcard.buddycards.container;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class BinderContainer extends SimpleContainer {
    public BinderContainer(ItemStack binderIn, int pageAmtIn, int stackModIn) {
        super(32 * pageAmtIn);
        binder = binderIn;
        pageAmt = pageAmtIn;
        stackMod = stackModIn;
    }

    public ItemStack binder;
    public final int pageAmt;
    public final int stackMod;

    @Override
    public void startOpen(Player player)
    {
        //Set all slots in the binder as empty by default
        for(int i = 0; i < this.getContainerSize(); i++) {
            setItem(i, ItemStack.EMPTY);
        }

        if(binder.hasTag())
        {
            //If the binder has nbt data, turn it into items
            CompoundTag nbt = binder.getTag();
            ListTag list = nbt.getList("Items", Tag.TAG_COMPOUND);
            for(int i = 0; i < list.size(); i++) {
                CompoundTag compoundnbt = list.getCompound(i);
                int k = compoundnbt.getByte("Slot") & 255;
                if (k < this.getContainerSize()) {
                    setItem(k, ItemStack.of(compoundnbt));
                }
            }
        }
    }

    @Override
    public void stopOpen(Player player)
    {
        if(!binder.isEmpty())
        {
            //When the binder has cards in it, turn them into nbt data and put them in the binder
            CompoundTag nbt = binder.getOrCreateTag();
            ListTag list = new ListTag();
            for (int i = 0; i < getContainerSize(); i++) {
                ItemStack itemstack = getItem(i);
                if (!itemstack.isEmpty()) {
                    CompoundTag compoundnbt = new CompoundTag();
                    compoundnbt.putByte("Slot", (byte) i);
                    itemstack.save(compoundnbt);
                    list.add(compoundnbt);
                }
            }
            nbt.put("Items", list);
            binder.setTag(nbt);
        }
    }

    @Override
    public int getMaxStackSize() {
        return (1 + stackMod) * 64;
    }

}
