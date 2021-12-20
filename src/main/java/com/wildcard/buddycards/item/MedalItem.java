package com.wildcard.buddycards.item;

import com.wildcard.buddycards.util.CuriosIntegration;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

public class MedalItem extends Item implements ICurioItem {
    public MedalItem(MedalTypes type, Item.Properties properties) {
        super(properties);
        this.TYPE = type;
    }
    final MedalTypes TYPE;

    @Override
    public ICapabilityProvider initCapabilities(final ItemStack stack, CompoundTag unused) {
        return CuriosIntegration.initCapabilities(TYPE, stack);
    }
}
