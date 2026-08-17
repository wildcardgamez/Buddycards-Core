package com.wildcard.buddycards.item;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.function.Consumer;

public class AttributeCurioItem extends Item implements ICurioItem {
    public AttributeCurioItem(Properties properties, Consumer<Multimap<Holder<Attribute>, AttributeModifier>> consumer) {
        super(properties);
        this.consumer = consumer;
    }

    private final Consumer<Multimap<Holder<Attribute>, AttributeModifier>> consumer;

    @Override
    public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(SlotContext slotContext, ResourceLocation id, ItemStack stack) {
        Multimap<Holder<Attribute>, AttributeModifier> map = LinkedHashMultimap.create();
        consumer.accept(map);
        return map;
    }
}
