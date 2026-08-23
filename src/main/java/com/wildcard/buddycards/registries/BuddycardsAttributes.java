package com.wildcard.buddycards.registries;

import com.wildcard.buddycards.Buddycards;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class BuddycardsAttributes {
    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(BuiltInRegistries.ATTRIBUTE, Buddycards.MOD_ID);

    public static void registerAttributes(IEventBus eventBus) {
        ATTRIBUTES.register(eventBus);
    }

    public static final DeferredHolder<Attribute, Attribute> BUDDY_LUCK = ATTRIBUTES.register("buddy_luck", () -> new RangedAttribute(makeDescriptionId("buddy_luck"), 0, -1024, 1024));
    public static final DeferredHolder<Attribute, Attribute> FOIL_LUCK = ATTRIBUTES.register("foil_luck", () -> new RangedAttribute(makeDescriptionId("foil_luck"), 0, -1024, 1024));
    public static final DeferredHolder<Attribute, Attribute> GRADING_LUCK = ATTRIBUTES.register("grading_luck", () -> new RangedAttribute(makeDescriptionId("grading_luck"), 0, -1024, 1024));

    public static final DeferredHolder<Attribute, Attribute> BUDDY_BONUS = ATTRIBUTES.register("buddy_bonus", () -> new RangedAttribute(makeDescriptionId("buddy_bonus"), 0, -1024, 1024));
    public static final DeferredHolder<Attribute, Attribute> FOIL_BONUS = ATTRIBUTES.register("foil_bonus", () -> new RangedAttribute(makeDescriptionId("foil_bonus"), 0, -1024, 1024));
    public static final DeferredHolder<Attribute, Attribute> GRADING_BONUS = ATTRIBUTES.register("grading_bonus", () -> new RangedAttribute(makeDescriptionId("grading_bonus"), 0, -1024, 1024));

    public static String makeDescriptionId(String name) {
        return "attribute.name." + Buddycards.MOD_ID + "." + name;
    }
}
