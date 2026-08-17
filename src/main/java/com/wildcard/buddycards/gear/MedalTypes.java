package com.wildcard.buddycards.gear;

import com.google.common.collect.Multimap;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;

import java.util.Optional;

public enum MedalTypes implements IMedalTypes{
    BASE_SET(null, (map, mod) -> {
        map.put(Attributes.MOVEMENT_SPEED, new AttributeModifier(ResourceLocation.withDefaultNamespace("speed"), .015 * (mod + 1), AttributeModifier.Operation.ADD_VALUE));
    }),
    NETHER_SET((player, mod) -> {
        player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 300, 0, true, false));
    }, (map, mod) -> {
        if (mod > 0)
            map.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(ResourceLocation.withDefaultNamespace("damage"), 1, AttributeModifier.Operation.ADD_VALUE));
        if (mod > 1)
            map.put(Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier(ResourceLocation.withDefaultNamespace("kbr"), 0.1 * (mod - 1), AttributeModifier.Operation.ADD_VALUE));
    }),
    END_SET((player, mod) -> {
        if (player.hasEffect(MobEffects.LEVITATION) && mod > 0) {
            player.removeEffect(MobEffects.LEVITATION);
        }
    }, (map, mod) -> {
        map.put(Attributes.SAFE_FALL_DISTANCE, new AttributeModifier(ResourceLocation.withDefaultNamespace("fall"), mod > 0 ? 12 : 8, AttributeModifier.Operation.ADD_VALUE));
        if (mod > 1)
            map.put(Attributes.ARMOR, new AttributeModifier(ResourceLocation.withDefaultNamespace("armor"), (mod - 1), AttributeModifier.Operation.ADD_VALUE));
    }),
    CAVE_SET((player, mod) -> {
        if (mod > 0) {
            if (player.hasEffect(MobEffects.DARKNESS))
                player.removeEffect(MobEffects.DARKNESS);
            if (player.hasEffect(MobEffects.POISON))
                player.removeEffect(MobEffects.POISON);
            player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 300, 0, true, false));
        }
    }, (map, mod) -> {
        map.put(Attributes.ATTACK_SPEED, new AttributeModifier(ResourceLocation.withDefaultNamespace("haste"), mod > 1 ? .1 * mod : 0.1, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
    });

    MedalTypes(MedalTick effect, MedalAttributes attributes) {
        this.effect = Optional.ofNullable(effect);
        this.attributes = Optional.ofNullable(attributes);
    }

    private final Optional<MedalTick> effect;
    private final Optional<MedalAttributes> attributes;

    @Override
    public void effectTick(LivingEntity player, int mod) {
        effect.ifPresent(medalTick -> medalTick.applyEffect(player, mod));
    }

    @Override
    public void applyAttributes(Multimap<Holder<Attribute>, AttributeModifier> map, int mod) {
        attributes.ifPresent(medalAttributes -> medalAttributes.applyAttributes(map, mod));
    }

    interface MedalTick {
        void applyEffect(LivingEntity player, int mod);
    }

    interface MedalAttributes {
        void applyAttributes(Multimap<Holder<Attribute>, AttributeModifier> map, int mod);
    }
}
