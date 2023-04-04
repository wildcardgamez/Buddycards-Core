package com.wildcard.buddycards.battles.game;

import net.minecraft.nbt.CompoundTag;

public class BattleCardState {
    
    public int power = 0;
    
    public BattleCardState(int power) {
        this.power = power;
    }
    
    public void save(CompoundTag tag) {
        tag.putInt("power", power);
    }
    
    public static BattleCardState load(CompoundTag tag) {
        int power = tag.getInt("power");
        return new BattleCardState(power);
    }
    
}