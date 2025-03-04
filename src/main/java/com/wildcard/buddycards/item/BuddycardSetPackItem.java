package com.wildcard.buddycards.item;

import com.wildcard.buddycards.core.BuddycardSet;
import com.wildcard.buddycards.registries.BuddycardsItems;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BuddycardSetPackItem extends BuddycardPackItem {
    public BuddycardSetPackItem(BuddycardsItems.BuddycardRequirement shouldLoad, BuddycardSet set, int amount, int foils, SimpleWeightedRandomList<Rarity> rarityWeights, Properties properties) {
        super(shouldLoad, amount, foils, rarityWeights, properties);
        SET = set;
    }

    protected final BuddycardSet SET;

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable(SET.getDescriptionId()).withStyle(ChatFormatting.GRAY));
    }

    public List<BuddycardItem> getPossibleCards(Rarity rarity) {
        return SET.getCards()
                .stream()
                .filter(card -> card.getRarity() == rarity)
                .toList();
    }
}
