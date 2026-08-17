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
    public boolean trySleeve(ItemStack card, ItemStack sleeves, Player player, Level level) {
        if(level instanceof ServerLevel && card.getItem() instanceof BuddycardItem && card.get(BuddycardsComponents.BUDDYCARD_FOIL) == 0) {
            ItemStack newCard = card.split(1);
            newCard.set(BuddycardsComponents.BUDDYCARD_FOIL, 2);
            sleeves.shrink(1);
            ItemHandlerHelper.giveItemToPlayer(player, newCard);
            return true;
        }
        return false;
    }
}
