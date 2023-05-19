package com.wildcard.buddycards.battles.game;

import com.google.common.collect.ImmutableListMultimap;

public class BattleAbility {
    
    public final BattleEvent event;
    public final BattleAbilityFunc ability;
    public final String name;
    
    public BattleAbility(BattleEvent event, String name, BattleAbilityFunc ability) {
        this.event = event;
        this.name = name;
        this.ability = ability;
    }
    
    public ImmutableListMultimap<BattleEvent, BattleAbility> single() {
        return new Builder().add(this).build();
    }
    
    /**
     * Battle ability function (game, slot, target, source) -> {}<br>
     * First argument <b>game</b> is a {@linkplain BattleGame}<br>
     * Other arguments are BattleGame slots, 012=P1, 345=P2<br>
     * <b>slot</b> is the slot the event is firing for<br>
     * <b>target</b> is the slot the event targeted (such as the card taking damage or dying)<br>
     * <b>source</b> is the slot that caused the event (such as the card that killed another or was played)<br>
     */
    @FunctionalInterface
    public static interface BattleAbilityFunc {
        public boolean trigger(BattleGame game, int slot, int target, int source);
    }
    
    public static class Builder {
        
        private ImmutableListMultimap.Builder<BattleEvent, BattleAbility> abilityMap = ImmutableListMultimap.builder();
        
        public Builder() {}
        
        public Builder add(BattleAbility ability) {
            abilityMap.put(ability.event, ability);
            return this;
        }
        
        public ImmutableListMultimap<BattleEvent, BattleAbility> build() {
            return abilityMap.build();
        }
        
    }
    
}