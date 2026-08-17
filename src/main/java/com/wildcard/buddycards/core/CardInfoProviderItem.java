package com.wildcard.buddycards.core;

import net.minecraft.world.item.ItemStack;

import java.util.stream.Stream;

public interface CardInfoProviderItem {
    Stream<CardInfo> getAllCardInfo(ItemStack stack);
}
