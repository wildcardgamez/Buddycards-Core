package com.wildcard.buddycards.item;

import com.wildcard.buddycards.savedata.BuddycardCollectionSaveData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ChargedBuddysteelPowerMeterItem extends DescriptionItem {
    public ChargedBuddysteelPowerMeterItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        InteractionHand cardHand = hand == InteractionHand.MAIN_HAND ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
        if(level instanceof ServerLevel serverLevel && stack.getItem() instanceof ChargedBuddysteelPowerMeterItem) {
            if(player.getItemInHand(cardHand).getItem() instanceof BuddycardItem card) {
                //Specific set
                BuddycardCollectionSaveData.Fraction cardsCollected = BuddycardCollectionSaveData.get(serverLevel).checkPlayerPerfectSetCompletion(player.getUUID(), card.getSet());
                player.displayClientMessage(Component.translatable(card.getSet().getDescriptionId())
                        .append(Component.translatable("item.buddycards.charged_buddysteel_power_meter.cards_collected"))
                        .append(cardsCollected.top + "/" + cardsCollected.bottom), true);
            }
            else {
                //Nonspecific set
                BuddycardCollectionSaveData.Fraction cardsCollected = BuddycardCollectionSaveData.get(serverLevel).checkPlayerPerfectTotalCompletion((player.getUUID()));
                player.displayClientMessage(Component.translatable("item.buddycards.charged_buddysteel_power_meter.total_cards_collected")
                        .append(cardsCollected.top + "/" + cardsCollected.bottom), true);
                CompoundTag nbt = stack.getOrCreateTag();
                nbt.putInt("power", (int) (11 * (float) cardsCollected.top / cardsCollected.bottom));
            }
        }
        return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
    }
}
