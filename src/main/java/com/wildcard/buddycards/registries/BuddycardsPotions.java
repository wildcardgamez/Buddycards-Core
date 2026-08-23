package com.wildcard.buddycards.registries;

import com.wildcard.buddycards.Buddycards;
import com.wildcard.buddycards.effect.AttributeMobEffect;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.alchemy.Potion;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class BuddycardsPotions {
    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(BuiltInRegistries.MOB_EFFECT, Buddycards.MOD_ID);
    public static final DeferredRegister<Potion> POTIONS = DeferredRegister.create(BuiltInRegistries.POTION, Buddycards.MOD_ID);

    public static void registerPotions(IEventBus eventBus) {
        EFFECTS.register(eventBus);
        POTIONS.register(eventBus);
    }

    public static final DeferredHolder<MobEffect, MobEffect> GRADING_LUCK = EFFECTS.register("grading_luck_effect", () ->
        new AttributeMobEffect(MobEffectCategory.BENEFICIAL, 16548863).addAttributeModifier(BuddycardsAttributes.GRADING_LUCK, Buddycards.buddycardsLocation("grading_luck"), 1, AttributeModifier.Operation.ADD_VALUE));
    public static final DeferredHolder<MobEffect, MobEffect> FOIL_LUCK = EFFECTS.register("foil_luck_effect", () ->
            new AttributeMobEffect(MobEffectCategory.BENEFICIAL, 16765009).addAttributeModifier(BuddycardsAttributes.FOIL_LUCK, Buddycards.buddycardsLocation("foil_luck"), 1, AttributeModifier.Operation.ADD_VALUE));

    public static final DeferredHolder<Potion, Potion> GRADING_LUCK_POTION = POTIONS.register("grading_luck_potion", () -> new Potion(new MobEffectInstance(GRADING_LUCK, 1200)));
    public static final DeferredHolder<Potion, Potion> GRADING_LUCK_POTION_LONG = POTIONS.register("grading_luck_potion_long", () -> new Potion(new MobEffectInstance(GRADING_LUCK, 3000)));
    public static final DeferredHolder<Potion, Potion> GRADING_LUCK_POTION_STRONG = POTIONS.register("grading_luck_potion_strong", () -> new Potion(new MobEffectInstance(GRADING_LUCK, 1200, 1)));
    public static final DeferredHolder<Potion, Potion> FOIL_LUCK_POTION = POTIONS.register("foil_luck_potion", () -> new Potion(new MobEffectInstance(FOIL_LUCK, 1200)));
    public static final DeferredHolder<Potion, Potion> FOIL_LUCK_POTION_LONG = POTIONS.register("foil_luck_potion_long", () -> new Potion(new MobEffectInstance(FOIL_LUCK, 3000)));
    public static final DeferredHolder<Potion, Potion> FOIL_LUCK_POTION_STRONG = POTIONS.register("foil_luck_potion_strong", () -> new Potion(new MobEffectInstance(FOIL_LUCK, 1200, 1)));
}
