package com.wildcard.buddycards.util;

import com.wildcard.buddycards.Buddycards;
import com.wildcard.buddycards.entity.EnderlingEntity;
import com.wildcard.buddycards.registries.BuddycardsAttributes;
import com.wildcard.buddycards.registries.BuddycardsEntities;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;

@Mod(value = Buddycards.MOD_ID)
@EventBusSubscriber(modid = Buddycards.MOD_ID)
public class MobAttributeHandler {
    @SubscribeEvent
    public static void setupAttributes(EntityAttributeCreationEvent event) {
        event.put(BuddycardsEntities.ENDERLING.get(), EnderlingEntity.setupAttributes());
    }

    @SubscribeEvent
    public static void modifyAttributes(EntityAttributeModificationEvent event) {
        event.add(EntityType.PLAYER, BuddycardsAttributes.BUDDY_LUCK);
        event.add(EntityType.PLAYER, BuddycardsAttributes.FOIL_LUCK);
        event.add(EntityType.PLAYER, BuddycardsAttributes.GRADING_LUCK);
        event.add(EntityType.PLAYER, BuddycardsAttributes.BUDDY_BONUS);
        event.add(EntityType.PLAYER, BuddycardsAttributes.FOIL_BONUS);
        event.add(EntityType.PLAYER, BuddycardsAttributes.GRADING_BONUS);
    }
}
