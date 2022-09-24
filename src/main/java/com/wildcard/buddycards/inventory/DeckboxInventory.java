package com.wildcard.buddycards.inventory;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class DeckboxInventory extends SimpleContainer {
    public DeckboxInventory(ItemStack deck) {
        super(16);
        deckbox = deck;
    }

    public ItemStack deckbox;

    @Override
    public void startOpen(Player player)
    {
        if(deckbox.hasTag())
        {
            //If the deckbox has nbt data, turn it into items
            CompoundTag nbt = deckbox.getTag();
            ListTag list = nbt.getList("Items", CompoundTag.TAG_COMPOUND);
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
            ListTag list = new ListTag();
            HashMap<String, Integer> deckList = new HashMap<>();
            for(int i = 0; i < this.getContainerSize(); i++) {
                ItemStack itemstack = this.getItem(i);
                if (!itemstack.isEmpty()) {
                    CompoundTag compoundnbt = new CompoundTag();
                    compoundnbt.putInt("Slot", i);
                    itemstack.save(compoundnbt);
                    list.add(compoundnbt);
                    String name = itemstack.getDisplayName().getString();
                    if(deckList.containsKey(name))
                        deckList.put(name, 1 + deckList.get(name));
                    else
                        deckList.put(name, 1);
                }
            }
            CompoundTag deck = new CompoundTag();
            for (Map.Entry<String, Integer> entry : deckList.entrySet()) {
                deck.putInt(entry.getKey(), entry.getValue());
            }
            nbt.put("Deck", deck);
            nbt.put("Items", list);
            deckbox.setTag(nbt);
        }
    }
}
