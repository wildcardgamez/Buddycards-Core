package com.wildcard.buddycards.enchantment;

import com.wildcard.buddycards.Buddycards;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;

public class EnchantmentKeys {
    public static ResourceKey<Enchantment> EXTRA_PAGE = ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.fromNamespaceAndPath(Buddycards.MOD_ID, "extra_page"));
    public static ResourceKey<Enchantment> RAPID_RECOVERY = ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.fromNamespaceAndPath(Buddycards.MOD_ID, "rapid_recovery"));

    public static int getEnchantmentLevel(Level level, ResourceKey<Enchantment> enchantment, ItemStack itemStack) {
        return itemStack.getEnchantmentLevel(level.registryAccess().asGetterLookup().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(enchantment));
    }
}
