package com.wildcard.buddycards.core;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.stream.Stream;

public interface ICardInfoProviderItem {
    Stream<CardInfo> getAllCardInfo(ItemStack stack, @Nullable Player player);
}
