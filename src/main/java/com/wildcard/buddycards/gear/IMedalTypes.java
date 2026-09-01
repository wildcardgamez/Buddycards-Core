package com.wildcard.buddycards.gear;

import com.google.common.collect.Multimap;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;

public interface IMedalTypes {
    void effectTick(LivingEntity player, int mod);

    void applyAttributes(Multimap<Holder<Attribute>, AttributeModifier> map, int mod);
}
