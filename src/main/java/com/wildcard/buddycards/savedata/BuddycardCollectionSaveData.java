package com.wildcard.buddycards.savedata;

import com.ibm.icu.impl.Pair;
import com.wildcard.buddycards.Buddycards;
import com.wildcard.buddycards.item.BuddycardItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.*;

public class BuddycardCollectionSaveData extends SavedData {
    private final static HashMap<UUID, HashMap<String, ArrayList<Integer>>> CARD_LISTS = new HashMap<>();

    public BuddycardCollectionSaveData() {
    }

    public BuddycardCollectionSaveData(CompoundTag nbt) {
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

    public static BuddycardCollectionSaveData get(ServerLevel world) {
        return world.getDataStorage().computeIfAbsent(BuddycardCollectionSaveData::new, BuddycardCollectionSaveData::new, Buddycards.MOD_ID + "_collections");
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

    public boolean checkPlayerSetCompleted(UUID uuid, String setName) {
        return checkPlayerSetCompletionPercent(uuid, setName) >= 1;
    }

    public float checkPlayerSetCompletionPercent(UUID uuid, String setName) {
        Pair<Integer, Integer> pair = checkPlayerSetCompletion(uuid, setName);
        return (float) pair.first / pair.second;
    }

    public float checkPlayerTotalCompletionPercent(UUID uuid) {
        Pair<Integer, Integer> pair = checkPlayerTotalCompletion(uuid);
        return (float) pair.first / pair.second;
    }

    public Pair<Integer, Integer> checkPlayerSetCompletion(UUID uuid, String setName) {
        int foundCards = 0, totalCards = BuddycardItem.CARD_LIST.get(setName).size();
        if (CARD_LISTS.containsKey(uuid) && CARD_LISTS.get(uuid).containsKey(setName))
            foundCards += CARD_LISTS.get(uuid).get(setName).size();
        return Pair.of(foundCards, totalCards);
    }

    public Pair<Integer, Integer> checkPlayerTotalCompletion(UUID uuid) {
        int totalCards = 0, foundCards = 0;
        for (String setName: BuddycardItem.CARD_LIST.keySet()) {
            totalCards += BuddycardItem.CARD_LIST.get(setName).size();
            if(CARD_LISTS.containsKey(uuid) && CARD_LISTS.get(uuid).containsKey(setName))
                foundCards += CARD_LISTS.get(uuid).get(setName).size();
        }
        return Pair.of(foundCards, totalCards);
    }

    public void addPlayerCardFound(UUID uuid, String setName, int cardNumber) {
        if(!CARD_LISTS.containsKey(uuid))
            CARD_LISTS.put(uuid, new HashMap<>());
        if(!CARD_LISTS.get(uuid).containsKey(setName))
            CARD_LISTS.get(uuid).put(setName, new ArrayList<>());
        if(!CARD_LISTS.get(uuid).get(setName).contains(cardNumber))
            CARD_LISTS.get(uuid).get(setName).add(cardNumber);
        setDirty();
    }
}
