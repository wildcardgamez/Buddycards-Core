package com.wildcard.buddycards.item;

import com.wildcard.buddycards.Buddycards;
import com.wildcard.buddycards.savedata.BuddycardCollectionSaveData;
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
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BlankBuddysteelMedalItem extends Item {
    public BlankBuddysteelMedalItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(new TranslatableComponent("item." + Buddycards.MOD_ID + ".blank_buddysteel_medal.desc").withStyle(ChatFormatting.GRAY));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        InteractionHand cardHand = hand == InteractionHand.MAIN_HAND ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
        if(level instanceof ServerLevel serverLevel && stack.getItem() instanceof BlankBuddysteelMedalItem && player.getItemInHand(cardHand).getItem() instanceof BuddycardItem card) {
            if(BuddysteelSetMedalItem.MEDALS.containsKey(card.getSet()) && BuddysteelSetMedalItem.MEDALS.get(card.getSet()).REQUIREMENT.shouldLoad()) {
                if (BuddycardCollectionSaveData.get(serverLevel).checkPlayerSetCompleted(player.getUUID(), card.getSet())) {
                    player.displayClientMessage(new TranslatableComponent("item.buddycards.blank_buddysteel_medal.success").append(new TranslatableComponent("item." + Buddycards.MOD_ID + ".buddycard.set_" + card.getSet())), true);
                    ItemStack medal = new ItemStack(BuddysteelSetMedalItem.MEDALS.get(card.getSet()));
                    CompoundTag nbt = new CompoundTag();
                    nbt.putString("player", player.getName().getString());
                    nbt.putInt("day", (int)(level.getDayTime()/24000));
                    medal.setTag(nbt);
                    return new InteractionResultHolder<>(InteractionResult.SUCCESS, medal);
                } else
                    player.displayClientMessage(new TranslatableComponent("item.buddycards.blank_buddysteel_medal.fail"), true);
            }
            else
                player.displayClientMessage(new TranslatableComponent("item.buddycards.blank_buddysteel_medal.fizzle"), true);
        }
        return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
    }
}
