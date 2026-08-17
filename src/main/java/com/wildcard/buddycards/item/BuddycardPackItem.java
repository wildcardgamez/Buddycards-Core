package com.wildcard.buddycards.item;

import com.wildcard.buddycards.registries.BuddycardsAttributes;
import com.wildcard.buddycards.registries.BuddycardsItems;
import com.wildcard.buddycards.util.ConfigManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

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

    private static final float[] GRADING_ODDS = new float[]{0.4f, 0.3f, 0.225f, 0.073f};

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (level instanceof ServerLevel serverLevel) {
            //Prematurely delete the pack item so the card items can go in_enchanting_table.json the same slot
            player.getItemInHand(hand).shrink(1);
            //Roll each card and throw it into a list
            NonNullList<ItemStack> cards = NonNullList.create();
            //Check for luminis ring to determine foil amount
            Optional<ICuriosItemHandler> curios = CuriosApi.getCuriosInventory(player);

            double bonus = player.getAttribute(BuddycardsAttributes.BUDDY_BONUS).getValue();
            double fBonus = player.getAttribute(BuddycardsAttributes.FOIL_BONUS).getValue();
            double gBonus = player.getAttribute(BuddycardsAttributes.GRADING_BONUS).getValue();
            double cardLuck = player.getAttribute(BuddycardsAttributes.BUDDY_LUCK).getValue();
            int cardAmt = CARD_AMT + (int) bonus + (level.getRandom().nextFloat() < bonus - ((int) bonus) ? 1 : 0);
            int foilAmt = FOIL_AMT + (int) fBonus + (level.getRandom().nextFloat() < fBonus - ((int) fBonus) ? 1 : 0);
            int gradeAmt = (int) gBonus + (level.getRandom().nextFloat() < gBonus - (int) gBonus ? 1 : 0);
            for (int i = 0; i < cardAmt; i++) {
                BuddycardItem card = rollCard(level.getRandom(), cardLuck);
                ItemStack item = new ItemStack(card);
                //If its one of the last ones that needs foil, make it foil
                int foil = 0, grade = 0;
                if (i >= cardAmt - foilAmt) {
                    double luck = player.getAttribute(BuddycardsAttributes.FOIL_LUCK).getValue();
                    float rand = level.getRandom().nextFloat();
                    if (luck > 0)
                        while (luck >= 1 || (luck > 0 && level.getRandom().nextFloat() < luck)) {
                            rand = Math.max(rand, level.getRandom().nextFloat());
                            luck--;
                        }
                    else if (luck < 0)
                        while (luck <= -1 || (luck < 0 && -level.getRandom().nextFloat() > luck)) {
                            rand = Math.min(rand, level.getRandom().nextFloat());
                            luck--;
                        }
                    foil = rand >= .95 ? 3 : rand >= .8 ? 2 : 1;
                    BuddycardItem.setShiny(item, foil);
                }
                //If its one of the first ones that needs grade, grade it
                if (i < gradeAmt) {
                    double luck = player.getAttribute(BuddycardsAttributes.GRADING_LUCK).getValue();
                    float rand = level.getRandom().nextFloat();
                    if (luck > 0)
                        while (luck >= 1 || (luck > 0 && level.getRandom().nextFloat() < luck)) {
                            rand = Math.max(rand, level.getRandom().nextFloat());
                            luck--;
                        }
                    else if (luck < 0)
                        while (luck <= -1 || (luck < 0 && -level.getRandom().nextFloat() > luck)) {
                            rand = Math.min(rand, level.getRandom().nextFloat());
                            luck--;
                        }
                    for (grade = 1; grade < 5; grade++) {
                        if (rand < GRADING_ODDS[grade - 1])
                            break;
                        rand -= GRADING_ODDS[grade - 1];
                    }
                    BuddycardItem.setGrade(item, grade);
                }
                cards.add(item);
            }
            //Handle fake players by spawning item entities in_enchanting_table.json front of them instead of adding to inventory
            if (player instanceof FakePlayer) {
                for (ItemStack cardStack : cards) {
                    BlockPos blockPos = player.blockPosition();
                    ItemEntity itemEntity = new ItemEntity(serverLevel,
                            blockPos.getX() + 0.5,
                            blockPos.getY() + 0.5,
                            blockPos.getZ() + 0.5,
                            cardStack);
                    itemEntity.setDeltaMovement(0, 0.2, 0);
                    serverLevel.addFreshEntity(itemEntity);
                }
            } else {
                //Give each card to the player and put their collection data in_enchanting_table.json
                cards.forEach(card -> ItemHandlerHelper.giveItemToPlayer(player, card));
            }
        }
        //Return success every time because we shrink it ourselves instead of using consume
        return InteractionResultHolder.success(player.getItemInHand(hand));
    }

    public BuddycardItem rollCard(RandomSource random) {
        return rollCard(random, 0);
    }

    public BuddycardItem rollCard(RandomSource random, double luck) {
        Optional<Rarity> optional = rarityWeights.getRandomValue(random);
        if (luck > 0)
            while (luck >= 1 || (luck > 0 && luck < random.nextFloat())) {
                Optional<Rarity> reroll = rarityWeights.getRandomValue(random);
                if (reroll.get().ordinal() > optional.get().ordinal())
                    optional = reroll;
                luck--;
            }
        else if (luck < 0)
            while (luck <= -1 || (luck < 0 && -random.nextFloat() > luck)) {
                Optional<Rarity> reroll = rarityWeights.getRandomValue(random);
                if (reroll.get().ordinal() < optional.get().ordinal())
                    optional = reroll;
                luck--;
            }
        return optional
                .map(this::getPossibleCards)
                .map(cards -> cards.get(random.nextInt(cards.size())))
                .orElseThrow(() -> new IllegalStateException("Card pack " + getDescriptionId() + " does not contain cards for rarity"));
    }

    public abstract List<BuddycardItem> getPossibleCards(Rarity rarity);
}
