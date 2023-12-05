package com.wildcard.buddycards.container;

import com.wildcard.buddycards.item.DeckboxItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class DeckboxContainer extends SimpleContainer {
    public DeckboxContainer(ItemStack deck) {
        super(18);
        deckbox = deck;
    }

    public ItemStack deckbox;

    @Override
    public void startOpen(Player player)
    {
        //Set all slots in the deckbox as empty by default
        for(int i = 0; i < this.getContainerSize(); i++) {
            setItem(i, ItemStack.EMPTY);
        }
        if(deckbox.hasTag())
        {
            //If the deckbox has nbt data, turn it into items
            CompoundTag nbt = deckbox.getTag();
            ListTag list = nbt.getList("Items", Tag.TAG_COMPOUND);
            for(int i = 0; i < list.size(); i++) {
                CompoundTag compoundnbt = list.getCompound(i);
                int k = compoundnbt.getInt("Slot");
                if (k < this.getContainerSize()) {
                    this.setItem(k, ItemStack.of(compoundnbt));
                }
            }
        }
    }

    public void startOpen()
    {
        //Set all slots in the deckbox as empty by default
        for(int i = 0; i < this.getContainerSize(); i++) {
            setItem(i, ItemStack.EMPTY);
        }
        if(deckbox.hasTag())
        {
            //If the deckbox has nbt data, turn it into items
            CompoundTag nbt = deckbox.getTag();
            ListTag list = nbt.getList("Items", Tag.TAG_COMPOUND);
            for(int i = 0; i < list.size(); i++) {
                CompoundTag compoundnbt = list.getCompound(i);
                int k = compoundnbt.getInt("Slot");
                if (k < this.getContainerSize()) {
                    this.setItem(k, ItemStack.of(compoundnbt));
                }
            }
        }
    }

    @Override
    public void stopOpen(Player player)
    {
        if(!deckbox.isEmpty())
        {
            //When the deckbox has cards in it, turn them into nbt data and put them in the deckbox
            CompoundTag nbt = deckbox.getOrCreateTag();
            CompoundTag deck = new CompoundTag();
            ListTag list = new ListTag();
            for(int i = 0; i < this.getContainerSize(); i++) {
                ItemStack itemstack = this.getItem(i);
                if (!itemstack.isEmpty()) {
                    CompoundTag compoundnbt = new CompoundTag();
                    compoundnbt.putInt("Slot", i);
                    itemstack.save(compoundnbt);
                    list.add(compoundnbt);
                    String name = itemstack.getDisplayName().getString();
                    if(deck.contains(name))
                        deck.putInt(name, deck.getInt(name) + 1);
                    else
                        deck.putInt(name, 1);
                }
            }
            nbt.put("Items", list);
            nbt.put("Deck", deck);
            deckbox.setTag(nbt);
        }
        DeckboxItem.updateFull(deckbox);
    }

    public void saveStats(boolean win) {
        CompoundTag nbt = deckbox.getTag();
        ListTag list = nbt.getList("Items", Tag.TAG_COMPOUND);
        for(int i = 0; i < list.size(); i++) {
            ItemStack card = ItemStack.of(list.getCompound(i));
            if(card.hasTag() && card.getTag().contains("wins")) {
                CompoundTag cardNbt = card.getTag();
                if(win)
                    cardNbt.putInt("wins", 1 + cardNbt.getInt("wins"));
                else
                    cardNbt.putInt("loss", 1 + cardNbt.getInt("loss"));
                card.setTag(cardNbt);
                CompoundTag compoundnbt = new CompoundTag();
                compoundnbt.putInt("Slot", i);
                card.save(compoundnbt);
                list.set(i, compoundnbt);
            }
        }
        nbt.put("Items", list);
        deckbox.setTag(nbt);
    }
}
