package com.wildcard.buddycards.battles;

import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.UUID;

public class Boardstate {
    public Boardstate(int size) {
        cards = new ItemStack[size];
    }

    public Boardstate enemy;
    public UUID player;
    public final ItemStack[] cards;
}
