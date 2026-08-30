package com.wildcard.buddycards.item;

import com.wildcard.buddycards.registries.BuddycardsItems;
import com.wildcard.buddycards.registries.BuddycardsMisc;
import net.minecraft.client.gui.Font;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class BuddycardsArmorItem extends ArmorItem {
    public BuddycardsArmorItem(Holder<ArmorMaterial> materialIn, Type type) {
        super(materialIn, type, BuddycardsItems.UNCOMMON_TOOL_PROPERTIES);
    }

    public BuddycardsArmorItem(Holder<ArmorMaterial> materialIn, Type type, Properties properties) {
        super(materialIn, type, properties);
    }


}
