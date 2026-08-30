package com.wildcard.buddycards.item;

import com.google.common.base.Suppliers;
import com.google.common.collect.Multimap;
import com.wildcard.buddycards.Buddycards;
import com.wildcard.buddycards.core.ICollectionTieredItem;
import com.wildcard.buddycards.registries.BuddycardsComponents;
import com.wildcard.buddycards.registries.BuddycardsItems;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Supplier;

public class BuddysteelArmorItem extends ArmorItem implements ICollectionTieredItem {
    public BuddysteelArmorItem(Holder<ArmorMaterial>[] materialIn, Type type) {
        this(materialIn, type, null);
    }

    public BuddysteelArmorItem(Holder<ArmorMaterial>[] materialIn, Type type, ExtraAttributes attributes) {
        super(materialIn[0], type, new Properties().stacksTo(1).rarity(Rarity.UNCOMMON).component(BuddycardsComponents.COLLECTION_TIER, 0));
        tieredMaterials = materialIn;
        tieredModifiers = new Supplier[4];
        for (int i = 0; i < 4; i++) {
            int finalI = i;
            tieredModifiers[i] = Suppliers.memoize(() -> {
                ItemAttributeModifiers.Builder builder = ItemAttributeModifiers.builder();
                EquipmentSlotGroup equipmentslotgroup = EquipmentSlotGroup.bySlot(type.getSlot());
                ResourceLocation resourcelocation = ResourceLocation.withDefaultNamespace("armor." + type.getName());
                builder.add(Attributes.ARMOR, new AttributeModifier(resourcelocation, materialIn[finalI].value().getDefense(type), AttributeModifier.Operation.ADD_VALUE), equipmentslotgroup);
                builder.add(Attributes.ARMOR_TOUGHNESS, new AttributeModifier(resourcelocation, materialIn[finalI].value().toughness(), AttributeModifier.Operation.ADD_VALUE), equipmentslotgroup);
                float kbr = materialIn[finalI].value().knockbackResistance();
                if (kbr > 0.0F) {
                    builder.add(Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier(resourcelocation, kbr, AttributeModifier.Operation.ADD_VALUE), equipmentslotgroup);
                }
                if (attributes != null)
                    attributes.applyAttributes(builder, finalI, equipmentslotgroup);
                return builder.build();
            });
        }
    }

    @Override
    public Component getName(ItemStack stack) {
        return getTier(stack) == 3 ? Component.translatable(getDescriptionId() + ".perfect") : super.getName(stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(getTierComponent(stack));
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }

    private final Supplier<ItemAttributeModifiers>[] tieredModifiers;
    protected final Holder<ArmorMaterial>[] tieredMaterials;

    @Override
    public ItemAttributeModifiers getDefaultAttributeModifiers(ItemStack stack) {
        return tieredModifiers[getTier(stack)].get();
    }

    public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair) {
        return (this.tieredMaterials[getTier(toRepair)].value()).repairIngredient().get().test(repair) || super.isValidRepairItem(toRepair, repair);
    }

    public interface ExtraAttributes {
        void applyAttributes(ItemAttributeModifiers.Builder builder, int tier, EquipmentSlotGroup slot);
    }

    @Override
    public @Nullable ResourceLocation getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, ArmorMaterial.Layer layer, boolean innerModel) {
        return ResourceLocation.fromNamespaceAndPath(this.getCreatorModId(stack), "textures/models/armor/charged_buddysteel_tier" + getExactTier(stack) + (slot == EquipmentSlot.LEGS ? "_layer2.png" : "_layer1.png"));
    }
}
