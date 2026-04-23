package com.wildcard.buddycards.item;

import com.wildcard.buddycards.core.BuddycardSet;
import com.wildcard.buddycards.util.ConfigManager;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PlaymatBlockItem extends SetBasedBlockItem{
    public PlaymatBlockItem(Block block, Properties properties, BuddycardSet set) {
        super(block, properties, set);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable(SET.getDescriptionId()).withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable(ConfigManager.enableBattles.get() ? "block.buddycards.playmat.desc" : "block.buddycards.playmat.desc.disabled").withStyle(ChatFormatting.GRAY));
    }
}
