package com.wildcard.buddycards.item;

import com.wildcard.buddycards.registries.BuddycardsAttributes;
import com.wildcard.buddycards.registries.BuddycardsComponents;
import com.wildcard.buddycards.registries.BuddycardsItems;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

public class GradingSleeveItem extends SleeveItem {
    public GradingSleeveItem(Properties properties, float[] odds) {
        super(properties);
        ODDS = odds;
        CONSUME = true;
    }

    public GradingSleeveItem(Properties properties, float[] odds, boolean creative) {
        super(properties);
        ODDS = odds;
        CONSUME = !creative;
    }

    public final float[] ODDS;
    public final boolean CONSUME;

    @Override
    public boolean trySleeve(ItemStack card, ItemStack sleeves, Player player, Level level) {
        if (level instanceof ServerLevel && card.getItem() instanceof BuddycardItem && BuddycardItem.getGrade(card) == 0) {
            int grade;
            float rand = level.getRandom().nextFloat();
            double luck = player.getAttribute(BuddycardsAttributes.GRADING_LUCK).getValue();
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
                if (rand < ODDS[grade - 1])
                    break;
                rand -= ODDS[grade - 1];
            }
            ItemStack newCard = card.split(1);
            newCard.set(BuddycardsComponents.BUDDYCARD_GRADE, grade);
            if(CONSUME)
                sleeves.shrink(1);
            ItemHandlerHelper.giveItemToPlayer(player, newCard);
            if(grade == 5)
                player.playSound(SoundEvents.UI_TOAST_CHALLENGE_COMPLETE, 1, 1);
            return true;
        }
        return false;
    }
}
