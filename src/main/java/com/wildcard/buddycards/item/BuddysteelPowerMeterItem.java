package com.wildcard.buddycards.item;

import com.ibm.icu.impl.Pair;
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

public class BuddysteelPowerMeterItem extends Item {
    public BuddysteelPowerMeterItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        InteractionHand cardHand = hand == InteractionHand.MAIN_HAND ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
        if(level instanceof ServerLevel serverLevel && stack.getItem() instanceof BuddysteelPowerMeterItem) {
            if(player.getItemInHand(cardHand).getItem() instanceof BuddycardItem card) {
                //Specific set
                Pair<Integer, Integer> cardsCollected = BuddycardCollectionSaveData.get(serverLevel).checkPlayerSetCompletion(player.getUUID(), card.getSet());
                player.displayClientMessage(new TranslatableComponent("item." + Buddycards.MOD_ID + ".buddycard.set_" + card.getSet())
                        .append(new TranslatableComponent("item.buddycards.buddysteel_power_meter.cards_collected"))
                        .append(cardsCollected.first + "/" + cardsCollected.second), true);
            }
            else {
                //Nonspecific set
                Pair<Integer, Integer> cardsCollected = BuddycardCollectionSaveData.get(serverLevel).checkPlayerTotalCompletion(player.getUUID());
                player.displayClientMessage(new TranslatableComponent("item.buddycards.buddysteel_power_meter.total_cards_collected")
                        .append(cardsCollected.first + "/" + cardsCollected.second), true);
                CompoundTag nbt = stack.getOrCreateTag();
                nbt.putInt("power", (int) (11 * (float) cardsCollected.first / cardsCollected.second));
            }
        }
        return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(new TranslatableComponent("item." + Buddycards.MOD_ID + ".buddysteel_power_meter.desc").withStyle(ChatFormatting.GRAY));
    }
}
