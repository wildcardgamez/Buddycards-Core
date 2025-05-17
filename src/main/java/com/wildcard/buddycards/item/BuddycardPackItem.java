package com.wildcard.buddycards.item;

import com.wildcard.buddycards.registries.BuddycardsItems;
import com.wildcard.buddycards.savedata.BuddycardCollectionSaveData;
import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.ItemHandlerHelper;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.List;
import java.util.Optional;

public abstract class BuddycardPackItem extends Item {
    public BuddycardPackItem(int amount, int foils, SimpleWeightedRandomList<Rarity> rarityWeights, Properties properties) {
        super(properties);
        CARD_AMT = amount;
        FOIL_AMT = foils;
        this.rarityWeights = rarityWeights;

        if (this.rarityWeights.isEmpty()) {
            throw new IllegalArgumentException("No rarity weights provided");
        }
    }

    protected final int CARD_AMT;
    protected final int FOIL_AMT;
    protected final SimpleWeightedRandomList<Rarity> rarityWeights;

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (level instanceof ServerLevel serverLevel) {
            //Prematurely delete the pack item so the card items can go in the same slot
            player.getItemInHand(hand).shrink(1);
            //Roll each card and throw it into a list
            NonNullList<ItemStack> cards = NonNullList.create();
            int foilAmt = !CuriosApi.getCuriosHelper().findCurios(player, BuddycardsItems.LUMINIS_RING.get()).isEmpty() ? FOIL_AMT + 1 : FOIL_AMT;
            for (int i = 0; i < CARD_AMT; i++) {
                ItemStack card = new ItemStack(rollCard(level.getRandom()));
                //If its one of the last ones that needs foil, make it foil
                if (i >= CARD_AMT - foilAmt)
                    BuddycardItem.setShiny(card);
                cards.add(card);
            }
            //Give each card to the player and put their collection data in
            cards.forEach(card -> {
                ItemHandlerHelper.giveItemToPlayer(player, card);
                BuddycardCollectionSaveData.get(serverLevel).addPlayerCardFound(player.getUUID(), ((BuddycardItem) card.getItem()).getSet(), ((BuddycardItem) card.getItem()).getCardNumber());
            });
        }
        //Return success every time because we shrink it ourselves instead of using consume
        return InteractionResultHolder.success(player.getItemInHand(hand));
    }

    public BuddycardItem rollCard(RandomSource random) {
        Optional<Rarity> optional = rarityWeights.getRandomValue(random);
        return optional
                .map(this::getPossibleCards)
                .map(cards -> cards.get(random.nextInt(cards.size())))
                .orElseThrow(() -> new IllegalStateException("Cardpack " + getDescriptionId() + " does not contain cards for rarity"));
    }

    public abstract List<BuddycardItem> getPossibleCards(Rarity rarity);
}
