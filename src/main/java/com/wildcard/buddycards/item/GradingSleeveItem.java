package com.wildcard.buddycards.item;

import com.wildcard.buddycards.registries.BuddycardsAttributes;
import com.wildcard.buddycards.registries.BuddycardsComponents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class GradingSleeveItem extends SleeveItem {
    public GradingSleeveItem(Properties properties, float[] odds) {
        super(properties);
        ODDS = odds;
    }

    public GradingSleeveItem(Properties properties, float[] odds, boolean creative) {
        super(properties, !creative);
        ODDS = odds;
    }

    public final float[] ODDS;

    @Override
    public boolean canSleeve(ItemStack card, ItemStack sleeves) {
        return card.getItem() instanceof BuddycardItem && BuddycardItem.getGrade(card) == 0;
    }

    @Override
    public ItemStack sleeveResult(ItemStack card, ItemStack sleeves, Player player, Level level) {
        int grade;
        float rand = level.getRandom().nextFloat();
        double luck = 0;
        if (player != null)
            luck = player.getAttribute(BuddycardsAttributes.GRADING_LUCK).getValue();
        if (luck > 0)
            while (luck >= 1 || (luck > 0 && level.getRandom().nextFloat() < luck)) {
                rand = Math.max(rand, level.getRandom().nextFloat());
                luck--;
            }
        else if (luck < 0)
            while (luck <= -1 || (luck < 0 && -level.getRandom().nextFloat() > luck)) {
                rand = Math.min(rand, level.getRandom().nextFloat());
                luck++;
            }
        for (grade = 1; grade < 5; grade++) {
            if (rand < ODDS[grade - 1])
                break;
            rand -= ODDS[grade - 1];
        }
        ItemStack newCard = card.copyWithCount(1);
        newCard.set(BuddycardsComponents.BUDDYCARD_GRADE, grade);
        return newCard;
    }
}
