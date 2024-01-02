package com.wildcard.buddycards.savedata;

import com.wildcard.buddycards.Buddycards;
import com.wildcard.buddycards.core.BuddycardSet;
import com.wildcard.buddycards.core.BuddycardsAPI;
import com.wildcard.buddycards.item.BuddycardItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.*;

public class BuddycardCollectionSaveData extends SavedData {
    private static final Map<UUID, Map<String, Set<Integer>>> CARD_LISTS = new HashMap<>();

    public BuddycardCollectionSaveData() {
    }

    public BuddycardCollectionSaveData(CompoundTag nbt) {
        ListTag list = nbt.getList("collections", Tag.TAG_COMPOUND);
        for (int i = 0; i < list.size(); i++) {
            //For every player...
            CompoundTag playerDataNbt = list.getCompound(i);
            ListTag setsNbt = playerDataNbt.getList("sets", Tag.TAG_COMPOUND);
            Map<String, Set<Integer>> playerData = new HashMap<>();
            for (int j = 0; j < setsNbt.size(); j++) {
                //For every set...
                CompoundTag setNbt = setsNbt.getCompound(j);
                ListTag cardsNbt = setNbt.getList("cards", Tag.TAG_COMPOUND);
                Set<Integer> cards = new HashSet<>();
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
    public static BuddycardCollectionSaveData getPerfect(ServerLevel world) {
        return world.getDataStorage().computeIfAbsent(BuddycardCollectionSaveData::new, BuddycardCollectionSaveData::new, Buddycards.MOD_ID + "_perfect_collections");
    }

    @Override
    public CompoundTag save(CompoundTag nbt) {
        ListTag list = new ListTag();
        for (Map.Entry<UUID, Map<String, Set<Integer>>> i: CARD_LISTS.entrySet()) {
            //For every player...
            CompoundTag playerData = new CompoundTag();
            ListTag sets = new ListTag();
            for (Map.Entry<String, Set<Integer>> j: i.getValue().entrySet()) {
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
        int foundCards = 0;
        if (CARD_LISTS.containsKey(uuid) && CARD_LISTS.get(uuid).containsKey(setName))
            foundCards += CARD_LISTS.get(uuid).get(setName).size();
        return new Fraction(foundCards, set.getCards().size());
    }

    public Fraction checkPlayerTotalCompletion(UUID uuid) {
        var playerCollection = CARD_LISTS.get(uuid);
        if(playerCollection != null) {
            int foundCards = 0;
            int totalCards = 0;
            for (BuddycardSet set : BuddycardsAPI.getAllCardsets()) {
                var playerSet = playerCollection.getOrDefault(set.getName(), new HashSet<>());
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

    public void addPlayerCardFound(UUID uuid, BuddycardItem item) {
        addPlayerCardFound(uuid, item.getSet(), item.getCardNumber());
    }

    public void addPlayerCardFound(UUID uuid, BuddycardSet set, int cardNumber) {
        CARD_LISTS.computeIfAbsent(uuid, key -> new HashMap<>())
                .computeIfAbsent(set.getName(), key -> new HashSet<>())
                .add(cardNumber);
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
