package com.wildcard.buddycards.item;

import com.wildcard.buddycards.Buddycards;
import com.wildcard.buddycards.client.BuddycardsLayers;
import com.wildcard.buddycards.gear.BuddycardsArmorMaterial;
import net.minecraft.ChatFormatting;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.IItemRenderProperties;

import java.util.List;
import java.util.function.Consumer;

public class BuddycardsArmorItem extends ArmorItem {
    public BuddycardsArmorItem(ArmorMaterial materialIn, EquipmentSlot slot) {
        super(materialIn, slot, new Item.Properties().tab(Buddycards.TAB));
    }

    @Override
    public Rarity getRarity(ItemStack stack) {
        return material.equals(BuddycardsArmorMaterial.BUDDYSTEEL) ? Rarity.UNCOMMON : Rarity.EPIC;
    }

    @Override
    public void initializeClient(Consumer<IItemRenderProperties> consumer) {
        consumer.accept(new Render());
    }

    private static final class Render implements IItemRenderProperties {
        @Override
        @OnlyIn(Dist.CLIENT)
        public HumanoidModel<?> getArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlot armorSlot, HumanoidModel<?> _default) {
            if (!((ArmorItem)itemStack.getItem()).getMaterial().equals(BuddycardsArmorMaterial.BUDDYSTEEL))
                return BuddycardsLayers.getArmor(armorSlot);
            return IItemRenderProperties.super.getArmorModel(entityLiving, itemStack, armorSlot, _default);
        }
    }
}
