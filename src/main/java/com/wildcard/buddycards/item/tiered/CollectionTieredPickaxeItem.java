package com.wildcard.buddycards.item.tiered;

import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.component.ItemAttributeModifiers;

import java.util.function.Supplier;

public class CollectionTieredPickaxeItem extends PickaxeItem implements ICollectionTieredItem {
    public CollectionTieredPickaxeItem(Tier[] tiers, Properties properties, ExtraAttributes attributes) {
        super(tiers[0], properties);
        this.tiers = tiers;
        this.tieredModifiers = new Supplier[4];
        for (int i = 0; i < 4; i++) {
            int finalI = i;
            this.tieredModifiers[i] = () -> {
                ItemAttributeModifiers.Builder builder = ItemAttributeModifiers.builder();
                        builder.add(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_ID, tiers[finalI].getAttackDamageBonus(), AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND);
                        builder.add(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_ID, tiers[finalI].getSpeed(), AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND);
                if (attributes != null)
                    attributes.applyAttributes(builder, finalI, EquipmentSlotGroup.MAINHAND);
                return builder.build();
            };
        }
    }

    final Tier[] tiers;
    private final Supplier<ItemAttributeModifiers>[] tieredModifiers;

    public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair) {
        return this.tiers[getCollectionTier(toRepair)].getRepairIngredient().test(repair);
    }

    @Override
    public ItemAttributeModifiers getDefaultAttributeModifiers(ItemStack stack) {
        return tieredModifiers[getCollectionTier(stack)].get();
    }
}
