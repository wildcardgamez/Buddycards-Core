package com.wildcard.buddycards.item;

import com.wildcard.buddycards.registries.BuddycardsItems;
import net.minecraft.core.Holder;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;

public class BuddycardsArmorItem extends ArmorItem {
    public BuddycardsArmorItem(Holder<ArmorMaterial> materialIn, Type type) {
        super(materialIn, type, BuddycardsItems.UNCOMMON_TOOL_PROPERTIES);
    }

    public BuddycardsArmorItem(Holder<ArmorMaterial> materialIn, Type type, Properties properties) {
        super(materialIn, type, properties);
    }
}
