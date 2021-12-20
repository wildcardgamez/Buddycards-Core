package com.wildcard.buddycards.item;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Rarity;

public enum MedalTypes {
    EMPTY((player, mod) -> {}),
    BASE_SET((player, mod) -> {
        player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 300, mod, true, false));
    }),
    NETHER_SET((player, mod) -> {
        player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 300, 0, true, false));
        if(mod > 0 && player.isOnFire()) {
            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 1800, 0, true, false));
            if (mod > 1)
                player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 300, 0, true, false));
        }
    }),
    END_SET((player, mod) -> {
        player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 300, mod / 2, true, false));
        if (player.hasEffect(MobEffects.LEVITATION) && mod > 0)
            player.removeEffect(MobEffects.LEVITATION);
    });

    MedalTypes(MedalEffect effect) {
        this.effect = effect;
    }
    private final MedalEffect effect;

    public void applyEffect(Player player, int mod) {
        effect.applyEffect(player, mod);
    }

    interface MedalEffect {
        void applyEffect(Player player, int mod);
    }
}
