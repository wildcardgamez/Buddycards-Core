package com.wildcard.buddycards.savedata;

import com.wildcard.buddycards.Buddycards;
import com.wildcard.buddycards.core.BuddycardSet;
import com.wildcard.buddycards.core.BuddycardsAPI;
import com.wildcard.buddycards.item.BuddycardItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PerfectBuddycardCollectionSaveData extends SavedData {
    private final static HashMap<UUID, HashMap<String, ArrayList<Integer>>> CARD_LISTS = new HashMap<>();

    public PerfectBuddycardCollectionSaveData() {
    }

    public PerfectBuddycardCollectionSaveData(CompoundTag nbt) {
        ListTag list = nbt.getList("collections", CompoundTag.TAG_COMPOUND);
        for (int i = 0; i < list.size(); i++) {
            //For every player...
            CompoundTag playerDataNbt = list.getCompound(i);
            ListTag setsNbt = playerDataNbt.getList("sets", CompoundTag.TAG_COMPOUND);
            HashMap<String, ArrayList<Integer>> playerData = new HashMap<>();
            for (int j = 0; j < setsNbt.size(); j++) {
                //For every set...
                CompoundTag setNbt = setsNbt.getCompound(j);
                ListTag cardsNbt = setNbt.getList("cards", CompoundTag.TAG_COMPOUND);
                ArrayList<Integer> cards = new ArrayList<>();
                for(int k = 0; k < cardsNbt.size(); k++) {
                    //For every card...
                    CompoundTag cardNbt = cardsNbt.getCompound(k);
                    cards.add(cardNbt.getInt("number"));
                }
                playerData.put(setNbt.getString("name"), cards);
            }
            CARD_LISTS.put(playerDataNbt.getUUID("uuid"), playerData);
        }
    }

    public static PerfectBuddycardCollectionSaveData get(ServerLevel world) {
        return world.getDataStorage().computeIfAbsent(PerfectBuddycardCollectionSaveData::new, PerfectBuddycardCollectionSaveData::new, Buddycards.MOD_ID + "_perfect_collections");
    }

    @Override
    public CompoundTag save(CompoundTag nbt) {
        ListTag list = new ListTag();
        for (Map.Entry<UUID, HashMap<String, ArrayList<Integer>>> i: CARD_LISTS.entrySet()) {
            //For every player...
            CompoundTag playerData = new CompoundTag();
            ListTag sets = new ListTag();
            for (Map.Entry<String, ArrayList<Integer>> j: i.getValue().entrySet()) {
                //For every set...
                CompoundTag set = new CompoundTag();
                ListTag cards = new ListTag();
                for (int k: j.getValue()) {
                    //For every card...
                    CompoundTag card = new CompoundTag();
                    card.putInt("number", k);
                    cards.add(card);
                }
                set.putString("name", j.getKey());
                set.put("cards", cards);
                sets.add(set);
            }
            playerData.putUUID("uuid", i.getKey());
            playerData.put("sets", sets);
            list.add(playerData);
        }
        nbt.put("collections", list);
        return nbt;
    }

    public boolean checkPlayerSetCompleted(UUID uuid, BuddycardSet set) {
        return checkPlayerSetCompletion(uuid, set).calc() >= 1;
    }

    public Fraction checkPlayerSetCompletion(UUID uuid, BuddycardSet set) {
        String setName = set.getName();
        int foundCards = 0, totalCards = set.getCards().size();
        if (CARD_LISTS.containsKey(uuid) && CARD_LISTS.get(uuid).containsKey(setName))
            foundCards += CARD_LISTS.get(uuid).get(setName).size();
        return new Fraction(foundCards, totalCards);
    }

    public Fraction checkPlayerTotalCompletion(UUID uuid) {
        var playerCollection = CARD_LISTS.get(uuid);
        if(playerCollection != null) {
            int foundCards = 0;
            int totalCards = 0;
            for (BuddycardSet set : BuddycardsAPI.getAllCardsets()) {
                var playerSet = playerCollection.getOrDefault(set.getName(), new ArrayList<>());
                for(BuddycardItem card : set.getCards()) {
                    if(card.shouldLoad()) {
                        totalCards++;
                        if(playerSet.contains(card.getCardNumber()))
                            foundCards++;
                    }
                }
            }
            return new Fraction(foundCards, totalCards);
        }
        return new Fraction(0, BuddycardsAPI.getAllCards().stream().filter(BuddycardItem::shouldLoad).toList().size());
    }

    public void addPlayerCardFound(UUID uuid, BuddycardSet set, int cardNumber) {
        String setName = set.getName();
        if(!CARD_LISTS.containsKey(uuid))
            CARD_LISTS.put(uuid, new HashMap<>());
        if(!CARD_LISTS.get(uuid).containsKey(setName))
            CARD_LISTS.get(uuid).put(setName, new ArrayList<>());
        if(!CARD_LISTS.get(uuid).get(setName).contains(cardNumber))
            CARD_LISTS.get(uuid).get(setName).add(cardNumber);
        setDirty();
    }

    public static class Fraction {
        public final int top;
        public final int bottom;

        Fraction (int i, int j) {
            top = i;
            bottom = j;
        }

        public float calc () {
            return (float)top/bottom;
        }
    }
}
