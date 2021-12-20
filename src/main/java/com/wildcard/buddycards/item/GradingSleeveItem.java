package com.wildcard.buddycards.item;

import com.wildcard.buddycards.Buddycards;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
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

public class GradingSleeveItem extends Item {
    public GradingSleeveItem(Properties properties, float[] odds) {
        super(properties);
        ODDS = odds;
    }

    public final float[] ODDS;

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(new TranslatableComponent(getDescriptionId() + ".desc").withStyle(ChatFormatting.GRAY));
    }

    public boolean tryGrade(ItemStack card, ItemStack sleeves, Player player, Level level) {
        CompoundTag nbt = card.getOrCreateTag().copy();
        if(card.getItem() instanceof BuddycardItem && !nbt.contains("grade")) {
            int grade;
            float rand = level.getRandom().nextFloat();
            for (grade = 1; grade < 5; grade++) {
                if(rand < ODDS[grade-1])
                    break;
                rand -= ODDS[grade-1];
            }
            nbt.putInt("grade", grade);
            ItemStack newCard = new ItemStack(card.getItem());
            newCard.setTag(nbt);
            sleeves.shrink(1);
            card.shrink(1);
            ItemHandlerHelper.giveItemToPlayer(player, newCard);
            if(grade == 5)
                player.playSound(SoundEvents.UI_TOAST_CHALLENGE_COMPLETE, 1, 1);
            return true;
        }
        return false;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        InteractionHand cardHand = hand == InteractionHand.MAIN_HAND ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
        if(level instanceof ServerLevel && player.getItemInHand(cardHand).getItem() instanceof BuddycardItem &&
            tryGrade(player.getItemInHand(cardHand), player.getItemInHand(hand), player, level))
            return new InteractionResultHolder<>(InteractionResult.SUCCESS, player.getItemInHand(hand));
        return super.use(level, player, hand);
    }
}
