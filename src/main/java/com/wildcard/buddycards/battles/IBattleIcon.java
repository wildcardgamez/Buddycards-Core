package com.wildcard.buddycards.battles;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.*;
import com.wildcard.buddycards.Buddycards;
import net.minecraft.resources.ResourceLocation;

public sealed interface IBattleIcon permits TextureBattleIcon, BuddycardBattleIcon {

    Codec<IBattleIcon> ALL_ICON_CODEC = Codec.either(BuddycardBattleIcon.CODEC, TextureBattleIcon.CODEC).xmap(IBattleIcon::unwrap, IBattleIcon::wrap);

    int width();

    static IBattleIcon unwrap(Either<BuddycardBattleIcon, TextureBattleIcon> either) {
        return either.left().map(IBattleIcon.class::cast).orElseGet(() -> either.right().get());
    }
    static Either<BuddycardBattleIcon, TextureBattleIcon> wrap(IBattleIcon icon) {
        /*
        TODO: once switch pattern matching is out of preview and available
        return switch (icon) {
            case BuddycardBattleIcon b -> Either.left(b);
            case TextureBattleIcon t -> Either.right(t);
        };*/
       if (icon instanceof BuddycardBattleIcon b) {
           return Either.left(b);
       }
       if (icon instanceof TextureBattleIcon t) {
           return Either.right(t);
       }
       throw new RuntimeException("icon is " + icon.getClass().getName() + " however, only BuddycardBattleIcon and TextureBattleIcon are permitted by the seal on IBattleIcon");
    }
}
