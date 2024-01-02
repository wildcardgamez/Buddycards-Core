package com.wildcard.buddycards.item;

import com.wildcard.buddycards.registries.BuddycardsItems;
import com.wildcard.buddycards.savedata.BuddycardCollectionSaveData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.ItemHandlerHelper;
import top.theillusivec4.curios.api.CuriosApi;

public class BattleSleeveItem extends DescriptionItem {
    public BattleSleeveItem(Properties properties) {
        super(properties);
    }

    public boolean trySleeve(ItemStack card, ItemStack sleeves, Player player, Level level) {
        CompoundTag nbt = card.getOrCreateTag().copy();
        if(level instanceof ServerLevel serverLevel && card.getItem() instanceof BuddycardItem buddycardItem && !nbt.contains("wins")) {
            nbt.putInt("wins", 0);
            nbt.putInt("loss", 0);
            ItemStack newCard = new ItemStack(card.getItem());
            newCard.setTag(nbt);
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
                trySleeve(player.getItemInHand(cardHand), player.getItemInHand(hand), player, level))
            return new InteractionResultHolder<>(InteractionResult.SUCCESS, player.getItemInHand(hand));
        return super.use(level, player, hand);
    }
}
