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
    private static final Map<UUID, Map<String, ArrayList<CardCollectionData>>> CARD_LISTS = new HashMap<>();

    public BuddycardCollectionSaveData() {
    }

    public BuddycardCollectionSaveData(CompoundTag nbt) {
        Set<String> uuids = nbt.getAllKeys();
        for (String uuid : uuids) {
            //For every player...
            CompoundTag playerDataNbt = nbt.getCompound(uuid);
            Set<String> sets = playerDataNbt.getAllKeys();
            Map<String, ArrayList<CardCollectionData>> playerData = new HashMap<>();
            for (String set : sets) {
                //For every set...
                CompoundTag setNbt = playerDataNbt.getCompound(set);
                Set<String> cards = setNbt.getAllKeys();
                ArrayList<CardCollectionData> setCollection = new ArrayList<>();
                int size = Objects.requireNonNull(BuddycardsAPI.findSet(set)).getCards().size();
                for (int i = 0; i < size; i++) {
                    //For every card...
                    CompoundTag cardNbt = playerDataNbt.getCompound(String.valueOf(i));
                    CardCollectionData cardData = new CardCollectionData();
                    for (int j = 0; j < 4; j++) {
                        //Set the values of each variant
                        CompoundTag tagNbt = cardNbt.getCompound(String.valueOf(j));
                        for (int k = 0; k < 6; k++) {
                            cardData.variants[j][k] = tagNbt.getBoolean(String.valueOf(k));
                        }
                    }
                    setCollection.set(i, cardData);
                }
                playerData.put(setNbt.getString("name"), setCollection);
            }
            CARD_LISTS.put(UUID.fromString(uuid), playerData);
        }
    }

    public static BuddycardCollectionSaveData get(ServerLevel world) {
        return world.getDataStorage().computeIfAbsent(BuddycardCollectionSaveData::new, BuddycardCollectionSaveData::new, Buddycards.MOD_ID + "_collections");
    }

    @Override
    public CompoundTag save(CompoundTag nbt) {
        for (Map.Entry<UUID, Map<String, ArrayList<CardCollectionData>>> i: CARD_LISTS.entrySet()) {
            //For every player...
            CompoundTag playerData = new CompoundTag();
            for (Map.Entry<String, ArrayList<CardCollectionData>> j: i.getValue().entrySet()) {
                //For every set...
                CompoundTag set = new CompoundTag();
                ArrayList<CardCollectionData> setArray = j.getValue();
                for (int k = 0; k < setArray.size(); k++) {
                    //For every card
                    CompoundTag card = new CompoundTag();
                    for (int l = 0; l < 4; l++) {
                        //For every foil
                        CompoundTag foil = new CompoundTag();
                        for (int m = 0; m < 6; m++) {
                            //For every grade
                            foil.putBoolean(String.valueOf(l), setArray.get(k).variants[l][m]);
                        }
                        card.put(String.valueOf(l), foil);
                    }
                    set.put(String.valueOf(k), card);
                }
                set.put(j.getKey(), set);
            }
            nbt.put(i.getKey().toString(), playerData);
        }
        return nbt;
    }

    public boolean checkPlayerSetCompleted(UUID uuid, BuddycardSet set) {
        return checkPlayerSetCompletion(uuid, set).calc() >= 1;
    }

    public Fraction checkPlayerSetCompletion(UUID uuid, BuddycardSet set) {
        int foundCards = 0, totalCards = 0;
        if (CARD_LISTS.get(uuid) != null) {
            ArrayList<CardCollectionData> playerSet = CARD_LISTS.get(uuid).getOrDefault(set.getName(), new ArrayList<>());
            for (BuddycardItem card : set.getCards()) {
                if (card.shouldLoad()) {
                    totalCards++;
                    if (playerSet.get(card.getCardNumber()).variants[0][0])
                        foundCards++;
                }
            }
        }
        return new Fraction(foundCards, totalCards);
    }

    public Fraction checkPlayerTotalCompletion(UUID uuid) {
        Map<String, ArrayList<CardCollectionData>> playerCollection = CARD_LISTS.get(uuid);
        if(playerCollection != null) {
            int foundCards = 0, totalCards = 0;
            for (BuddycardSet set : BuddycardsAPI.getAllCardsets()) {
                ArrayList<CardCollectionData> playerSet = playerCollection.getOrDefault(set.getName(), new ArrayList<>());
                for(BuddycardItem card : set.getCards()) {
                    if(card.shouldLoad()) {
                        totalCards++;
                        if(playerSet.get(card.getCardNumber()).variants[0][0])
                            foundCards++;
                    }
                }
            }
            return new Fraction(foundCards, totalCards);
        }
        return new Fraction(0, BuddycardsAPI.getAllCards().stream().filter(BuddycardItem::shouldLoad).toList().size());
    }

    public boolean checkPlayerFoilSetCompleted(UUID uuid, BuddycardSet set) {
        return checkPlayerFoilSetCompletion(uuid, set).calc() >= 1;
    }

    public Fraction checkPlayerFoilSetCompletion(UUID uuid, BuddycardSet set) {
        int foundCards = 0, totalCards = 0;
        if (CARD_LISTS.get(uuid) != null) {
            ArrayList<CardCollectionData> playerSet = CARD_LISTS.get(uuid).getOrDefault(set.getName(), new ArrayList<>());
            for (BuddycardItem card : set.getCards()) {
                if(card.shouldLoad()) {
                    for (int i = 1; i < 4; i++) {
                        totalCards++;
                        if (playerSet.get(card.getCardNumber()).variants[i][0])
                            foundCards++;
                    }
                }
            }
        }
        return new Fraction(foundCards, totalCards);
    }

    public Fraction checkPlayerFoilTotalCompletion(UUID uuid) {
        Map<String, ArrayList<CardCollectionData>> playerCollection = CARD_LISTS.get(uuid);
        if(playerCollection != null) {
            int foundCards = 0, totalCards = 0;
            for (BuddycardSet set : BuddycardsAPI.getAllCardsets()) {
                ArrayList<CardCollectionData> playerSet = playerCollection.getOrDefault(set.getName(), new ArrayList<>());
                for(BuddycardItem card : set.getCards()) {
                    if(card.shouldLoad()) {
                        for (int i = 1; i < 4; i++) {
                            totalCards++;
                            if (playerSet.get(card.getCardNumber()).variants[i][0])
                                foundCards++;
                        }
                    }
                }
            }
            return new Fraction(foundCards, totalCards);
        }
        return new Fraction(0, BuddycardsAPI.getAllCards().stream().filter(BuddycardItem::shouldLoad).toList().size());
    }

    public boolean checkPlayerGradeSetCompleted(UUID uuid, BuddycardSet set) {
        return checkPlayerGradeSetCompletion(uuid, set).calc() >= 1;
    }

    public Fraction checkPlayerGradeSetCompletion(UUID uuid, BuddycardSet set) {
        int foundCards = 0, totalCards = 0;
        if (CARD_LISTS.get(uuid) != null) {
            ArrayList<CardCollectionData> playerSet = CARD_LISTS.get(uuid).getOrDefault(set.getName(), new ArrayList<>());
            for (BuddycardItem card : set.getCards()) {
                if(card.shouldLoad()) {
                    for (int i = 1; i < 6; i++) {
                        totalCards++;
                        if (playerSet.get(card.getCardNumber()).variants[0][i])
                            foundCards++;
                    }
                }
            }
        }
        return new Fraction(foundCards, totalCards);
    }

    public Fraction checkPlayerGradeTotalCompletion(UUID uuid) {
        Map<String, ArrayList<CardCollectionData>> playerCollection = CARD_LISTS.get(uuid);
        if(playerCollection != null) {
            int foundCards = 0, totalCards = 0;
            for (BuddycardSet set : BuddycardsAPI.getAllCardsets()) {
                ArrayList<CardCollectionData> playerSet = playerCollection.getOrDefault(set.getName(), new ArrayList<>());
                for(BuddycardItem card : set.getCards()) {
                    if(card.shouldLoad()) {
                        for (int i = 1; i < 6; i++) {
                            totalCards++;
                            if (playerSet.get(card.getCardNumber()).variants[0][i])
                                foundCards++;
                        }
                    }
                }
            }
            return new Fraction(foundCards, totalCards);
        }
        return new Fraction(0, BuddycardsAPI.getAllCards().stream().filter(BuddycardItem::shouldLoad).toList().size());
    }

    public boolean checkPlayerPerfectSetCompleted(UUID uuid, BuddycardSet set) {
        return checkPlayerPerfectSetCompletion(uuid, set).calc() >= 1;
    }

    public Fraction checkPlayerPerfectSetCompletion(UUID uuid, BuddycardSet set) {
        int foundCards = 0, totalCards = 0;
        if (CARD_LISTS.get(uuid) != null) {
            ArrayList<CardCollectionData> playerSet = CARD_LISTS.get(uuid).getOrDefault(set.getName(), new ArrayList<>());
            for (BuddycardItem card : set.getCards()) {
                if(card.shouldLoad()) {
                    for (int i = 1; i < 4; i++) {
                        for (int j = 1; j < 6; j++) {
                            totalCards++;
                            if (playerSet.get(card.getCardNumber()).variants[i][j])
                                foundCards++;
                        }
                    }
                }
            }
        }
        return new Fraction(foundCards, totalCards);
    }

    public Fraction checkPlayerPerfectTotalCompletion(UUID uuid) {
        Map<String, ArrayList<CardCollectionData>> playerCollection = CARD_LISTS.get(uuid);
        if(playerCollection != null) {
            int foundCards = 0, totalCards = 0;
            for (BuddycardSet set : BuddycardsAPI.getAllCardsets()) {
                ArrayList<CardCollectionData> playerSet = playerCollection.getOrDefault(set.getName(), new ArrayList<>());
                for(BuddycardItem card : set.getCards()) {
                    if(card.shouldLoad()) {
                        for (int i = 1; i < 4; i++) {
                            for (int j = 1; j < 6; j++) {
                                totalCards++;
                                if (playerSet.get(card.getCardNumber()).variants[i][j])
                                    foundCards++;
                            }
                        }
                    }
                }
            }
            return new Fraction(foundCards, totalCards);
        }
        return new Fraction(0, BuddycardsAPI.getAllCards().stream().filter(BuddycardItem::shouldLoad).toList().size());
    }

    public void addPlayerCardFound(UUID uuid, BuddycardItem item, int foil, int grade) {
        addPlayerCardFound(uuid, item.getSet(), item.getCardNumber(), foil, grade);
    }

    public void addPlayerCardFound(UUID uuid, BuddycardSet set, int cardNumber, int foil, int grade) {
        ArrayList<CardCollectionData> setList = CARD_LISTS.computeIfAbsent(uuid, key -> new HashMap<>())
                .computeIfAbsent(set.getName(), key -> new ArrayList<>());
        CardCollectionData data = setList.get(cardNumber) != null ? setList.get(cardNumber) : new CardCollectionData();
        data.variants[0][0] = true;
        if (foil != 0)
            data.variants[foil][0] = true;
        if(grade != 0) {
            data.variants[0][grade] = true;
            if (foil != 0)
                data.variants[foil][grade] = true;
        }
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

    public static class CardCollectionData {
        public final boolean[][] variants;

        public CardCollectionData() {
            variants = new boolean[4][6];
        }
    }
}
