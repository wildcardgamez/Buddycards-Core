package com.wildcard.buddycards.integration;

import com.wildcard.buddycards.Buddycards;
import com.wildcard.buddycards.client.renderer.MedalRenderer;
import com.wildcard.buddycards.item.IMedalTypes;
import com.wildcard.buddycards.registries.BuddycardsItems;
import com.wildcard.buddycards.registries.BuddycardsMisc;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import top.theillusivec4.curios.api.CuriosCapability;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.SlotTypeMessage;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;
import top.theillusivec4.curios.api.type.capability.ICurio;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CuriosIntegration {
    public static ICapabilityProvider initCapabilities(IMedalTypes type, ItemStack itemStack) {
        ICurio curio = new ICurio() {
            @Override
            public boolean canEquipFromUse(SlotContext slotContext) {
                return true;
            }

            @Override
            public ItemStack getStack() {
                return itemStack;
            }

            @Override
            public void curioTick(SlotContext slotContext) {
                if (slotContext.entity() instanceof Player player) {
                    int mod = EnchantmentHelper.getTagEnchantmentLevel(BuddycardsMisc.BUDDY_BOOST.get(), itemStack);
                    type.applyEffect(player, mod);
                }
            }
        };

        return new ICapabilityProvider() {
            private final LazyOptional<ICurio> curioOpt = LazyOptional.of(() -> curio);

            @Nonnull
            @Override
            public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
                return CuriosCapability.ITEM.orEmpty(cap, curioOpt);
            }
        };
    }

    public static void setupRenderers() {
        CuriosRendererRegistry.register(BuddycardsItems.MEDAL_BASE.get(), () -> new MedalRenderer(getDefaultMedalTexture("buddysteel_medal_base")));
        CuriosRendererRegistry.register(BuddycardsItems.MEDAL_NETHER.get(), () -> new MedalRenderer(getDefaultMedalTexture("buddysteel_medal_nether")));
        CuriosRendererRegistry.register(BuddycardsItems.MEDAL_END.get(), () -> new MedalRenderer(getDefaultMedalTexture("buddysteel_medal_end")));
        CuriosRendererRegistry.register(BuddycardsItems.LUMINIS_MEDAL_BASE.get(), () -> new MedalRenderer(getDefaultMedalTexture("luminis_medal_base")));
        CuriosRendererRegistry.register(BuddycardsItems.LUMINIS_MEDAL_NETHER.get(), () -> new MedalRenderer(getDefaultMedalTexture("luminis_medal_nether")));
        CuriosRendererRegistry.register(BuddycardsItems.LUMINIS_MEDAL_END.get(), () -> new MedalRenderer(getDefaultMedalTexture("luminis_medal_end")));
        CuriosRendererRegistry.register(BuddycardsItems.ZYLEX_MEDAL_BASE.get(), () -> new MedalRenderer(getDefaultMedalTexture("zylex_medal_base")));
        CuriosRendererRegistry.register(BuddycardsItems.ZYLEX_MEDAL_NETHER.get(), () -> new MedalRenderer(getDefaultMedalTexture("zylex_medal_nether")));
        CuriosRendererRegistry.register(BuddycardsItems.ZYLEX_MEDAL_END.get(), () -> new MedalRenderer(getDefaultMedalTexture("zylex_medal_end")));
    }

    protected static ResourceLocation getDefaultMedalTexture(String name) {
        return new ResourceLocation(Buddycards.MOD_ID, "textures/models/medal/" + name + ".png");
    }
}
