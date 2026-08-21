package com.wildcard.buddycards.item;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.ItemHandlerHelper;

import javax.annotation.Nullable;

public abstract class SleeveItem extends DescriptionItem {
    public SleeveItem(Properties properties) {
        super(properties);
        CONSUME = true;
    }

    public SleeveItem(Properties properties, boolean consume) {
        super(properties);
        CONSUME = consume;
    }

    public final boolean CONSUME;

    public abstract boolean canSleeve(ItemStack card, ItemStack sleeves);

    public abstract ItemStack sleeveResult(ItemStack card, ItemStack sleeves, @Nullable Player player, Level level);

    public boolean trySleeve(ItemStack card, ItemStack sleeves, @Nullable Player player, Level level) {
        if(level instanceof ServerLevel && canSleeve(card, sleeves)) {
            ItemHandlerHelper.giveItemToPlayer(player, sleeveResult(card, sleeves, player, level));
            if(CONSUME)
                sleeves.shrink(1);
            card.shrink(1);
            return true;
        }
        return false;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        InteractionHand cardHand = hand == InteractionHand.MAIN_HAND ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
        if(level instanceof ServerLevel && player.getItemInHand(cardHand).getItem() instanceof BuddycardItem &&
                trySleeve(player.getItemInHand(cardHand), player.getItemInHand(hand), player, level))
            return new InteractionResultHolder<>(InteractionResult.SUCCESS, player.getItemInHand(hand));
        return super.use(level, player, hand);
    }
}
