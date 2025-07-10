package com.wildcard.buddycards.item;

import com.wildcard.buddycards.Buddycards;
import com.wildcard.buddycards.savedata.BuddycardCollectionSaveData;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BlankZylexMedalItem extends BlankBuddysteelMedalItem {
    public BlankZylexMedalItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("item." + Buddycards.MOD_ID + ".blank_zylex_medal.desc", ChatFormatting.GRAY));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        InteractionHand cardHand = hand == InteractionHand.MAIN_HAND ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
        if(level instanceof ServerLevel serverLevel && stack.getItem() instanceof BlankZylexMedalItem && player.getItemInHand(cardHand).getItem() instanceof BuddycardItem card) {
            ZylexSetMedalItem medalItem = card.getSet().getZylexMedal();
            if(medalItem != null) {
                if (BuddycardCollectionSaveData.get(serverLevel).checkPlayerGradeSetCompleted(player.getUUID(), card.getSet())) {
                    player.displayClientMessage(Component.translatable("item.buddycards.blank_zylex_medal.success").append(Component.translatable(card.getSet().getDescriptionId())), true);
                    ItemStack medal = new ItemStack(medalItem);
                    CompoundTag nbt = new CompoundTag();
                    nbt.putString("player", player.getName().getString());
                    nbt.putInt("day", (int)(level.getDayTime()/24000));
                    medal.setTag(nbt);
                    return new InteractionResultHolder<>(InteractionResult.SUCCESS, medal);
                } else
                    player.displayClientMessage(Component.translatable("item.buddycards.blank_zylex_medal.fail"), true);
            }
            else
                player.displayClientMessage(Component.translatable("item.buddycards.blank_buddysteel_medal.fizzle"), true);
        }
        return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
    }
}
