package com.wildcard.buddycards.util;

import com.wildcard.buddycards.Buddycards;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.HashMap;

public class SleeveStickerManager {
    public static final HashMap<String, ArrayList<Integer>> SLEEVE_STICKER_MAP = new HashMap<>();

    public void addSleeveType(String nbtKey) {
        SLEEVE_STICKER_MAP.put(nbtKey, new ArrayList<>());
    }

    public void addSleeveValue(String nbtKey, int value) {
        SLEEVE_STICKER_MAP.get(nbtKey).add(value);
    }

    public ResourceLocation getSleeveModel(String nbtKey, int value) {
        return new ResourceLocation(Buddycards.MOD_ID, "sleeve_stickers/" + nbtKey + "/" + value);
    }
}
