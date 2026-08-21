package com.wildcard.buddycards.item;

import com.wildcard.buddycards.registries.BuddycardsComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.ItemHandlerHelper;

public class LuminisSleeveItem extends SleeveItem {
    public LuminisSleeveItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean canSleeve(ItemStack card, ItemStack sleeves) {
        return card.getItem() instanceof BuddycardItem && card.get(BuddycardsComponents.BUDDYCARD_FOIL) == 0;
    }

    @Override
    public ItemStack sleeveResult(ItemStack card, ItemStack sleeves, Player player, Level level) {
        ItemStack newCard = card.copyWithCount(1);
        newCard.set(BuddycardsComponents.BUDDYCARD_FOIL, 2);
        return newCard;
    }
}
