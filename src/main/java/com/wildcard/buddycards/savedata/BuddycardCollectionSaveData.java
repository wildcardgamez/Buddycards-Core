package com.wildcard.buddycards.savedata;

import com.wildcard.buddycards.Buddycards;
import com.wildcard.buddycards.core.BuddycardSet;
import com.wildcard.buddycards.core.BuddycardsAPI;
import com.wildcard.buddycards.item.BuddycardItem;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.*;

public class BuddycardCollectionSaveData extends SavedData {
    private static final Map<UUID, Map<String, NonNullList<CardCollectionData>>> CARD_LISTS = new HashMap<>();

    public BuddycardCollectionSaveData() {
    }

    public BuddycardCollectionSaveData(CompoundTag nbt) {
        Set<String> uuids = nbt.getAllKeys();
        for (String uuid : uuids) {
            //For every player...
            CompoundTag playerDataNbt = nbt.getCompound(uuid);
            Set<String> sets = playerDataNbt.getAllKeys();
            Map<String, NonNullList<CardCollectionData>> playerData = new HashMap<>();
            for (String set : sets) {
                //For every set...
                CompoundTag setDataNbt = playerDataNbt.getCompound(set);
                BuddycardSet cardSet = BuddycardsAPI.findSet(set);
                if (cardSet != null) {
                    NonNullList<CardCollectionData> setCollection = newSetData(cardSet);
                    for (int i = 0; i < cardSet.getCards().size(); i++) {
                        //For every card...
                        CardCollectionData cardData = setCollection.get(i);
                        if (setDataNbt.contains(String.valueOf(i))) {
                            CompoundTag cardNbt = setDataNbt.getCompound(String.valueOf(i));
                            for (int j = 0; j < 4; j++) {
                                //Set the values of each variant
                                CompoundTag tagNbt = cardNbt.getCompound(String.valueOf(j));
                                for (int k = 0; k < 6; k++) {
                                    cardData.variants[j][k] = tagNbt.getBoolean(String.valueOf(k));
                                }
                            }
                        }
                    }
                    playerData.put(set, setCollection);
                }
            }
            CARD_LISTS.put(UUID.fromString(uuid), playerData);
        }
    }

    public static BuddycardCollectionSaveData get(ServerLevel world) {
        return world.getDataStorage().computeIfAbsent(BuddycardCollectionSaveData::new, BuddycardCollectionSaveData::new, Buddycards.MOD_ID + "_collections");
    }

    @Override
    public CompoundTag save(CompoundTag nbt) {
        for (Map.Entry<UUID, Map<String, NonNullList<CardCollectionData>>> i: CARD_LISTS.entrySet()) {
            //For every player...
            CompoundTag playerData = new CompoundTag();
            for (Map.Entry<String, NonNullList<CardCollectionData>> j: i.getValue().entrySet()) {
                //For every set...
                CompoundTag set = new CompoundTag();
                NonNullList<CardCollectionData> setArray = j.getValue();
                for (int k = 0; k < setArray.size(); k++) {
                    //For every card
                    CompoundTag card = new CompoundTag();
                    for (int l = 0; l < 4; l++) {
                        //For every foil
                        CompoundTag foil = new CompoundTag();
                        for (int m = 0; m < 6; m++) {
                            //For every grade
                            foil.putBoolean(String.valueOf(m), setArray.get(k).variants[l][m]);
                        }
                        card.put(String.valueOf(l), foil);
                    }
                    set.put(String.valueOf(k), card);
                }
                playerData.put(j.getKey(), set);
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
            NonNullList<CardCollectionData> playerSet = CARD_LISTS.get(uuid).getOrDefault(set.getName(), newSetData(set));
            for (BuddycardItem card : set.getCards()) {
                if (card.shouldLoad()) {
                    totalCards++;
                    if (playerSet.get(card.getCardNumber()-1).variants[0][0])
                        foundCards++;
                }
            }
        }
        return new Fraction(foundCards, totalCards);
    }

    public Fraction checkPlayerTotalCompletion(UUID uuid) {
        Map<String, NonNullList<CardCollectionData>> playerCollection = CARD_LISTS.get(uuid);
        if(playerCollection != null) {
            int foundCards = 0, totalCards = 0;
            for (BuddycardSet set : BuddycardsAPI.getAllCardsets()) {
                NonNullList<CardCollectionData> playerSet = playerCollection.getOrDefault(set.getName(), newSetData(set));
                for(BuddycardItem card : set.getCards()) {
                    if(card.shouldLoad()) {
                        totalCards++;
                        if(playerSet.get(card.getCardNumber()-1).variants[0][0])
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
            NonNullList<CardCollectionData> playerSet = CARD_LISTS.get(uuid).getOrDefault(set.getName(), newSetData(set));
            for (BuddycardItem card : set.getCards()) {
                if(card.shouldLoad()) {
                    for (int i = 1; i < 4; i++) {
                        totalCards++;
                        if (playerSet.get(card.getCardNumber()-1).variants[i][0])
                            foundCards++;
                    }
                }
            }
        }
        return new Fraction(foundCards, totalCards);
    }

    public Fraction checkPlayerFoilTotalCompletion(UUID uuid) {
        Map<String, NonNullList<CardCollectionData>> playerCollection = CARD_LISTS.get(uuid);
        if(playerCollection != null) {
            int foundCards = 0, totalCards = 0;
            for (BuddycardSet set : BuddycardsAPI.getAllCardsets()) {
                NonNullList<CardCollectionData> playerSet = playerCollection.getOrDefault(set.getName(), newSetData(set));
                for(BuddycardItem card : set.getCards()) {
                    if(card.shouldLoad()) {
                        for (int i = 1; i < 4; i++) {
                            totalCards++;
                            if (playerSet.get(card.getCardNumber()-1).variants[i][0])
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
            NonNullList<CardCollectionData> playerSet = CARD_LISTS.get(uuid).getOrDefault(set.getName(), newSetData(set));
            for (BuddycardItem card : set.getCards()) {
                if(card.shouldLoad()) {
                    for (int i = 1; i < 6; i++) {
                        totalCards++;
                        if (playerSet.get(card.getCardNumber()-1).variants[0][i])
                            foundCards++;
                    }
                }
            }
        }
        return new Fraction(foundCards, totalCards);
    }

    public Fraction checkPlayerGradeTotalCompletion(UUID uuid) {
        Map<String, NonNullList<CardCollectionData>> playerCollection = CARD_LISTS.get(uuid);
        if(playerCollection != null) {
            int foundCards = 0, totalCards = 0;
            for (BuddycardSet set : BuddycardsAPI.getAllCardsets()) {
                NonNullList<CardCollectionData> playerSet = playerCollection.getOrDefault(set.getName(), newSetData(set));
                for(BuddycardItem card : set.getCards()) {
                    if(card.shouldLoad()) {
                        for (int i = 1; i < 6; i++) {
                            totalCards++;
                            if (playerSet.get(card.getCardNumber()-1).variants[0][i])
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
            NonNullList<CardCollectionData> playerSet = CARD_LISTS.get(uuid).getOrDefault(set.getName(), newSetData(set));
            for (BuddycardItem card : set.getCards()) {
                if(card.shouldLoad()) {
                    for (int i = 1; i < 4; i++) {
                        for (int j = 1; j < 6; j++) {
                            totalCards++;
                            if (playerSet.get(card.getCardNumber()-1).variants[i][j])
                                foundCards++;
                        }
                    }
                }
            }
        }
        return new Fraction(foundCards, totalCards);
    }

    public Fraction checkPlayerPerfectTotalCompletion(UUID uuid) {
        Map<String, NonNullList<CardCollectionData>> playerCollection = CARD_LISTS.get(uuid);
        if(playerCollection != null) {
            int foundCards = 0, totalCards = 0;
            for (BuddycardSet set : BuddycardsAPI.getAllCardsets()) {
                NonNullList<CardCollectionData> playerSet = playerCollection.getOrDefault(set.getName(), newSetData(set));
                for(BuddycardItem card : set.getCards()) {
                    if(card.shouldLoad()) {
                        for (int i = 1; i < 4; i++) {
                            for (int j = 1; j < 6; j++) {
                                totalCards++;
                                if (playerSet.get(card.getCardNumber()-1).variants[i][j])
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

    public void addPlayerCardFound(UUID uuid, ItemStack stack) {
        if (stack.getItem() instanceof BuddycardItem item) {
            int foil = 0,  grade = 0;
            if (stack.hasTag()) {
                CompoundTag nbt = stack.getTag();
                if (nbt.contains("foil"))
                    foil = nbt.getInt("foil");
                if (nbt.contains("grade"))
                    grade = nbt.getInt("grade");
            }
            addPlayerCardFound(uuid, item.getSet(), item.getCardNumber(), foil, grade);
        }
    }

    public void addPlayerCardFound(UUID uuid, BuddycardItem item, int foil, int grade) {
        addPlayerCardFound(uuid, item.getSet(), item.getCardNumber(), foil, grade);
    }

    public void addPlayerCardFound(UUID uuid, BuddycardSet set, int cardNumber, int foil, int grade) {
        NonNullList<CardCollectionData> setList = CARD_LISTS.computeIfAbsent(uuid, key -> new HashMap<>())
                .computeIfAbsent(set.getName(), key -> newSetData(set));
        CardCollectionData data = setList.get(cardNumber-1);
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

    private static NonNullList<CardCollectionData> newSetData(BuddycardSet set) {
        NonNullList<CardCollectionData> data = NonNullList.withSize(set.getCards().size(), new CardCollectionData());
        for (int i = 1; i < set.getCards().size(); i++) {
            data.set(i, new CardCollectionData());
        }
        return data;
    }
}
