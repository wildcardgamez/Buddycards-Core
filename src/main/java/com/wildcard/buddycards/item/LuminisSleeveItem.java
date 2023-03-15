package com.wildcard.buddycards.item;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class LuminisSleeveItem extends Item {
    public LuminisSleeveItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(new TranslatableComponent(getDescriptionId() + ".desc").withStyle(ChatFormatting.GRAY));
    }

    public boolean tryFoil(ItemStack card, ItemStack sleeves, Player player, Level level) {
        CompoundTag nbt = card.getOrCreateTag().copy();
        if(level instanceof ServerLevel && card.getItem() instanceof BuddycardItem && !nbt.contains("foil")) {
            ItemStack newCard = new ItemStack(card.getItem());
            BuddycardItem.setShiny(newCard);
            sleeves.shrink(1);
            card.shrink(1);
            ItemHandlerHelper.giveItemToPlayer(player, newCard);
            return true;
        }
        return false;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        InteractionHand cardHand = hand == InteractionHand.MAIN_HAND ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
        if(level instanceof ServerLevel && player.getItemInHand(cardHand).getItem() instanceof BuddycardItem &&
                tryFoil(player.getItemInHand(cardHand), player.getItemInHand(hand), player, level))
            return new InteractionResultHolder<>(InteractionResult.SUCCESS, player.getItemInHand(hand));
        return super.use(level, player, hand);
    }
}
