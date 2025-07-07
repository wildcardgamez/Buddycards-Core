package com.wildcard.buddycards.container;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;

public class BinderContainer extends SimpleContainer {
    public BinderContainer(ItemStack binderIn, int pageAmtIn) {
        super(32);
        binder = binderIn;
        pageAmt = pageAmtIn;
        cards = NonNullList.withSize(pageAmt * 32, ItemStack.EMPTY);
        currentPage = 0;
    }

    public ItemStack binder;
    public final int pageAmt;
    public final NonNullList<ItemStack> cards;
    public int currentPage;

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
                    cards.set(k, ItemStack.of(compoundnbt));
                }
            }
            for (int i =  0; i < 32; i++)
                setItem(i, cards.get(i));
        }
    }

    @Override
    public void stopOpen(Player player)
    {
        savePage();
        if(!binder.isEmpty())
        {
            //When the binder has cards in it, turn them into nbt data and put them in the binder
            CompoundTag nbt = binder.getOrCreateTag();
            ListTag list = new ListTag();
            for (int j = 0; j < 32 * pageAmt; j++) {
                ItemStack itemstack = cards.get(j);
                if (!itemstack.isEmpty()) {
                    CompoundTag compoundnbt = new CompoundTag();
                    compoundnbt.putByte("Slot", (byte) j);
                    itemstack.save(compoundnbt);
                    list.add(compoundnbt);
                }
            }
            nbt.put("Items", list);
            binder.setTag(nbt);
        }
    }

    public void savePage() {
        for (int i =  0; i < 32; i++) {
            cards.set((32 * currentPage) + i, getItem(i));
        }
    }

    public void goToPage(int pageNum) {
        savePage();
        currentPage = pageNum;
        for (int i =  0; i < 32; i++) {
            setItem(i, cards.get((32 * currentPage) + i));
        }
    }

    public void nextPage() {
        goToPage(currentPage + 1);
    }

    public void lastPage() {
        goToPage(currentPage - 1);
    }
}
