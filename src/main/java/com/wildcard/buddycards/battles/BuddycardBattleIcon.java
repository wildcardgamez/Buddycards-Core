package com.wildcard.buddycards.battles;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wildcard.buddycards.item.BuddycardItem;
import net.minecraft.core.Registry;
import net.minecraft.world.item.Item;

public final class BuddycardBattleIcon implements IBattleIcon {

    public static final Codec<BuddycardBattleIcon> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Registry.ITEM.byNameCodec().fieldOf("card").forGetter(BuddycardBattleIcon::getItem)
    ).apply(instance, BuddycardBattleIcon::new));
    private final Item item;
    private BuddycardBattleIcon(Item item) {
        this.item = item;
    }

    @Override
    public int width() {
        return 8;
    }

    public Item getItem() {
        return item;
    }

    public static IBattleIcon create(BuddycardItem buddycard) {
        return new BuddycardBattleIcon(buddycard);
    }
}
