package com.wildcard.buddycards.battles.game;

import net.minecraft.nbt.CompoundTag;

public class BattleCardState {
    
    public int power = 0;
    public BattleStatusEffect status;
    
    public BattleCardState(int power, BattleStatusEffect status) {
        this.power = power;
        this.status = status;
    }
    
    public void save(CompoundTag tag) {
        tag.putInt("power", power);
        tag.putInt("status", status.ordinal());
    }
    
    public static BattleCardState load(CompoundTag tag) {
        return new BattleCardState(tag.getInt("power"), BattleStatusEffect.values()[tag.getInt("status")]);
    }

    public BattleAbility getStatusEffect() {
        return status.getAbility();
    }
}