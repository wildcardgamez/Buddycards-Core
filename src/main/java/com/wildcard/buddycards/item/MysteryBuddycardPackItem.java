package com.wildcard.buddycards.item;

import com.wildcard.buddycards.Buddycards;
import com.wildcard.buddycards.core.BuddycardsAPI;
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

public class MysteryBuddycardPackItem extends BuddycardPackItem {
    public MysteryBuddycardPackItem(BuddycardsItems.BuddycardRequirement shouldLoad, int amount, int foils, SimpleWeightedRandomList<Rarity> rarityWeights, boolean includeUnloaded, Properties properties) {
        super(shouldLoad, amount, foils, rarityWeights, properties);
        INCLUDE_UNLOADED = includeUnloaded;
    }

    protected final boolean INCLUDE_UNLOADED;

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("item." + Buddycards.MOD_ID + ".buddycard.set_mystery").withStyle(ChatFormatting.GRAY));
    }

    @Override
    public List<BuddycardItem> getPossibleCards(Rarity rarity) {
        return BuddycardsAPI.getAllCards()
                .stream()
                .filter(card -> card.getRarity() == rarity && card.shouldLoad())
                .toList();
    }
}
