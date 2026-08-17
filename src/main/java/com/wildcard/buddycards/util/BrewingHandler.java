package com.wildcard.buddycards.util;

import com.wildcard.buddycards.Buddycards;
import com.wildcard.buddycards.registries.BuddycardsItems;
import com.wildcard.buddycards.registries.BuddycardsPotions;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent;

@Mod(value = Buddycards.MOD_ID)
@EventBusSubscriber(modid = Buddycards.MOD_ID)
public class BrewingHandler {
    @SubscribeEvent
    public static void onRegisterBrewingRecipes(RegisterBrewingRecipesEvent event) {
        event.getBuilder().addMix(Potions.AWKWARD, BuddycardsItems.ZYLEX_NUGGET.get(), BuddycardsPotions.GRADING_LUCK_POTION);
        event.getBuilder().addMix(BuddycardsPotions.GRADING_LUCK_POTION, Items.REDSTONE, BuddycardsPotions.GRADING_LUCK_POTION_LONG);
        event.getBuilder().addMix(BuddycardsPotions.GRADING_LUCK_POTION, Items.GLOWSTONE_DUST, BuddycardsPotions.GRADING_LUCK_POTION_STRONG);
        event.getBuilder().addMix(BuddycardsPotions.GRADING_LUCK_POTION, Items.FERMENTED_SPIDER_EYE, BuddycardsPotions.FOIL_LUCK_POTION);
        event.getBuilder().addMix(BuddycardsPotions.FOIL_LUCK_POTION, Items.REDSTONE, BuddycardsPotions.FOIL_LUCK_POTION_LONG);
        event.getBuilder().addMix(BuddycardsPotions.FOIL_LUCK_POTION, Items.GLOWSTONE_DUST, BuddycardsPotions.FOIL_LUCK_POTION_STRONG);
    }
}
