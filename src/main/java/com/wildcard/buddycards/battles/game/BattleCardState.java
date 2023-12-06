package com.wildcard.buddycards.battles.game;

import net.minecraft.nbt.CompoundTag;

import java.util.ArrayList;

public class BattleCardState {
    
    public int power = 0;
    public int status = 0;
    
    public BattleCardState(int power, int status) {
        this.power = power;
        this.status = status;
    }
    
    public void save(CompoundTag tag) {
        tag.putInt("power", power);
        tag.putInt("status", status);
    }
    
    public static BattleCardState load(CompoundTag tag) {
        return new BattleCardState(tag.getInt("power"), tag.getInt("status"));
    }

    public BattleAbility getStatusEffect() {
        if(status == 0 || BattleStatusEffect.EFFECTS.get(status) == null)
            return null;
        return BattleStatusEffect.EFFECTS.get(status).getAbility();
    }
}