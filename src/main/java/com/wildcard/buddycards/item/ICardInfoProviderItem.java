package com.wildcard.buddycards.item;

import com.wildcard.buddycards.core.CardInfo;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.stream.Stream;

public interface ICardInfoProviderItem {
    Stream<CardInfo> getAllCardInfo(ItemStack stack, @Nullable Player player);
}
