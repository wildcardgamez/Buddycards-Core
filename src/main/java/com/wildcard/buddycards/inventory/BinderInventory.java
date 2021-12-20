package com.wildcard.buddycards.inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;

public class BinderInventory extends SimpleContainer {
    public BinderInventory(int slots, ItemStack binderIn) {
        super(slots);
        binder = binderIn;
    }

    public BinderInventory(int slots, boolean isEnder) {
        super(slots);
        ender = isEnder;
    }

    public ItemStack binder;
    public boolean ender;

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
            ListTag list = nbt.getList("Items", CompoundTag.TAG_COMPOUND);
            for(int i = 0; i < list.size(); i++) {
                CompoundTag compoundnbt = list.getCompound(i);
                int k = compoundnbt.getByte("Slot") & 255;
                if (k >= 0 && k < this.getContainerSize()) {
                    this.setItem(k, ItemStack.of(compoundnbt));
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
            CompoundTag nbt;
            if(binder.hasTag())
                nbt = binder.getTag();
            else
                nbt = new CompoundTag();
            ListTag list = new ListTag();
            for(int i = 0; i < this.getContainerSize(); i++) {
                ItemStack itemstack = this.getItem(i);
                if (!itemstack.isEmpty()) {
                    CompoundTag compoundnbt = new CompoundTag();
                    compoundnbt.putByte("Slot", (byte)i);
                    itemstack.save(compoundnbt);
                    list.add(compoundnbt);
                }
            }
            nbt.put("Items", list);
            binder.setTag(nbt);
        }
    }
}
