package com.wildcard.buddycards.battles;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wildcard.buddycards.Buddycards;
import com.wildcard.buddycards.battles.game.BattleStatusEffect;
import com.wildcard.buddycards.screens.PlaymatScreen;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public record TextureBattleIcon(int texturePosX, int texturePosY, ResourceLocation texture, int width, List<BattleInfo> info) implements IBattleIcon {

    public static final Codec<TextureBattleIcon> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("x").forGetter(TextureBattleIcon::texturePosX),
            Codec.INT.fieldOf("y").forGetter(TextureBattleIcon::texturePosY),
            ResourceLocation.CODEC.fieldOf("texture").forGetter(TextureBattleIcon::texture),
            Codec.INT.fieldOf("width").forGetter(TextureBattleIcon::width),
            BattleInfo.CODEC.listOf().fieldOf("info").forGetter(TextureBattleIcon::info)
    ).apply(instance, TextureBattleIcon::new));

    public static final ResourceLocation TEXTURE = new ResourceLocation(Buddycards.MOD_ID, "textures/gui/playmat.png");

    public static IBattleIcon dividerIcon = new TextureBattleIcon(0,232, TEXTURE, 6, List.of());
    public static IBattleIcon spacerIcon = new TextureBattleIcon(0,220, TEXTURE, 41, List.of());
    public static IBattleIcon playIcon = new TextureBattleIcon(0,244, TEXTURE, 12, List.of());
    public static IBattleIcon drawIcon = new TextureBattleIcon(12,244, TEXTURE, 12, List.of());
    public static IBattleIcon deathIcon = new TextureBattleIcon(48,244, TEXTURE, 12, List.of());
    public static IBattleIcon startAttackIcon = new TextureBattleIcon(96,244, TEXTURE, 12, List.of());
    public static IBattleIcon powerIcon = new TextureBattleIcon(108,244, TEXTURE, 12, List.of());
    public static IBattleIcon winIcon = new TextureBattleIcon(120,244, TEXTURE, 12, List.of());
    public static IBattleIcon returnIcon = new TextureBattleIcon(132,244, TEXTURE, 12, List.of());
    public static IBattleIcon xIcon = new TextureBattleIcon(144,244, TEXTURE, 12, List.of());
    public static IBattleIcon doublePowerIcon = new TextureBattleIcon(156,244, TEXTURE, 12, List.of());
    public static IBattleIcon questionIcon = new TextureBattleIcon(168,244, TEXTURE, 12, List.of());

    public static IBattleIcon energyIcon(int amt) {
        return new TextureBattleIcon(24, 244, TEXTURE, 12, List.of(new BattleInfo(String.valueOf(amt), 12, 0, 0xFFffff89, false)));
    }

    public static IBattleIcon damageIcon(int amt) {
        return new TextureBattleIcon(36, 244, TEXTURE, 12, List.of(new BattleInfo(String.valueOf(amt), 12, 0, 0xFFffff89, false)));
    }

    public static IBattleIcon addIcon(int amt) {
        return new TextureBattleIcon(60, 244, TEXTURE, 12, List.of(new BattleInfo(String.valueOf(amt), 12, 0, 0xFFffff89, false)));
    }

    public static IBattleIcon subtractIcon(int amt) {
        return new TextureBattleIcon(72, 244, TEXTURE, 12, List.of(new BattleInfo(String.valueOf(amt), 12, 0, 0xFFffff89, false)));
    }

    public static IBattleIcon healIcon(int amt) {
        return new TextureBattleIcon(84, 244, TEXTURE, 12, List.of(new BattleInfo(String.valueOf(amt), 12, 0, 0xFFffff89, false)));
    }

    public static IBattleIcon statusIcon(BattleStatusEffect status) {
        return new TextureBattleIcon(180, 244, TEXTURE, 12, List.of(new BattleInfo(Character.toString('\u0ED0' + status.ordinal()), 8, 2, status.getColor(), false)));
    }

    public static IBattleIcon equalsIcon(int amt) {
        return new TextureBattleIcon(196, 244, TEXTURE, 12, List.of(new BattleInfo(String.valueOf(amt), 12, 0, 0xFFffff89, false)));
    }

    public record BattleInfo(String display, int x, int y, int color, boolean isLeftAligned) {
        public static final Codec<BattleInfo> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.STRING.fieldOf("display").forGetter(BattleInfo::display),
                Codec.INT.fieldOf("x").forGetter(BattleInfo::x),
                Codec.INT.fieldOf("y").forGetter(BattleInfo::y),
                Codec.INT.fieldOf("color").forGetter(BattleInfo::color),
                Codec.BOOL.fieldOf("leftAligned").forGetter(BattleInfo::isLeftAligned)
        ).apply(instance, BattleInfo::new));
    }
}
