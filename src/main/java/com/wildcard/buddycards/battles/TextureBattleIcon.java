package com.wildcard.buddycards.battles;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wildcard.buddycards.screens.PlaymatScreen;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public record TextureBattleIcon(int texturePosX, int texturePosY, ResourceLocation texture, int width, List<BattleInfo> info) implements IBattleIcon {

    public static final Codec<TextureBattleIcon> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("x").forGetter(TextureBattleIcon::texturePosX),
            Codec.INT.fieldOf("y").forGetter(TextureBattleIcon::texturePosX),
            ResourceLocation.CODEC.fieldOf("texture").forGetter(TextureBattleIcon::texture),
            Codec.INT.fieldOf("width").forGetter(TextureBattleIcon::width),
            BattleInfo.CODEC.listOf().fieldOf("info").forGetter(TextureBattleIcon::info)
    ).apply(instance, TextureBattleIcon::new));

    public static IBattleIcon createDeath() {
        return new TextureBattleIcon(43,244, PlaymatScreen.TEXTURE1, 12, List.of());
    }

    public static IBattleIcon createDamage(int damage) {
        return new TextureBattleIcon(30, 244, PlaymatScreen.TEXTURE1, 12, List.of(new BattleInfo(damage, 12, 0, 0xFFFF8080, false)));
    }

    public record BattleInfo(int display, int x, int y, int color, boolean isLeftAligned) {
        public static final Codec<BattleInfo> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.INT.fieldOf("display").forGetter(BattleInfo::display),
                Codec.INT.fieldOf("x").forGetter(BattleInfo::x),
                Codec.INT.fieldOf("y").forGetter(BattleInfo::y),
                Codec.INT.fieldOf("color").forGetter(BattleInfo::color),
                Codec.BOOL.fieldOf("leftAligned").forGetter(BattleInfo::isLeftAligned)
        ).apply(instance, BattleInfo::new));
    }
}
