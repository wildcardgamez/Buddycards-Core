package com.wildcard.buddycards.item.tiered;

import com.google.common.base.Suppliers;
import com.wildcard.buddycards.registries.BuddycardsComponents;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Supplier;

public class CollectionTieredArmorItem extends ArmorItem implements ICollectionTieredItem {
    public CollectionTieredArmorItem(Holder<ArmorMaterial>[] materialIn, Type type) {
        this(materialIn, type, null);
    }

    public CollectionTieredArmorItem(Holder<ArmorMaterial>[] materialIn, Type type, ExtraAttributes attributes) {
        super(materialIn[0], type, new Properties().stacksTo(1).rarity(Rarity.UNCOMMON).component(BuddycardsComponents.COLLECTION_TIER, 0).durability(type.getDurability(32)));
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
        return getCollectionTier(stack) == 3 ? Component.translatable(getDescriptionId() + ".perfect") : super.getName(stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(getCollectionTierComponent(stack));
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }

    protected final Supplier<ItemAttributeModifiers>[] tieredModifiers;
    protected final Holder<ArmorMaterial>[] tieredMaterials;

    @Override
    public ItemAttributeModifiers getDefaultAttributeModifiers(ItemStack stack) {
        return tieredModifiers[getCollectionTier(stack)].get();
    }

    public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair) {
        return this.tieredMaterials[getCollectionTier(toRepair)].value().repairIngredient().get().test(repair);
    }

    @Override
    public @Nullable ResourceLocation getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, ArmorMaterial.Layer layer, boolean innerModel) {
        return ResourceLocation.fromNamespaceAndPath(this.getCreatorModId(stack), "textures/models/armor/charged_buddysteel_tier" + getExactCollectionTier(stack) + (slot == EquipmentSlot.LEGS ? "_layer2.png" : "_layer1.png"));
    }
}
