package com.wildcard.buddycards.item;

import com.wildcard.buddycards.Buddycards;
import com.wildcard.buddycards.core.BuddycardSet;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Supplier;

public class BuddycardBoosterBoxItem extends BlockItem {


    public BuddycardBoosterBoxItem(Block block, Supplier<BuddycardPackItem> packSupplier, Properties properties) {
        super(block, properties);
        this.packSupplier = packSupplier;
    }

    protected final Supplier<BuddycardPackItem> packSupplier;

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        packSupplier.get().appendHoverText(stack, level, tooltip, flag);
    }
}
