package com.wildcard.buddycards.util;

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
import top.theillusivec4.curios.api.SlotTypeMessage;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;
import top.theillusivec4.curios.api.type.capability.ICurio;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CuriosIntegration {
    public static void imc() {
        InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> new SlotTypeMessage.Builder("medal")
                .icon(new ResourceLocation(Buddycards.MOD_ID, "misc/medal")).build());
        InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> new SlotTypeMessage.Builder("ring")
                .build());
    }

    public static ICapabilityProvider initCapabilities(IMedalTypes type, ItemStack itemStack) {
        ICurio curio = new ICurio() {
            @Override
            public boolean canRightClickEquip() {
                return true;
            }

            @Override
            public ItemStack getStack() {
                return itemStack;
            }

            @Override
            public void curioTick(String identifier, int index, LivingEntity livingEntity) {
                if (livingEntity instanceof Player) {
                    int mod = EnchantmentHelper.getItemEnchantmentLevel(BuddycardsMisc.BUDDY_BOOST.get(), itemStack);
                    type.applyEffect((Player) livingEntity, mod);
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

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void LoadTextures(TextureStitchEvent.Pre event) {
        event.addSprite(new ResourceLocation(Buddycards.MOD_ID, "misc/medal"));
    }

    public static void setupRenderers() {
        CuriosRendererRegistry.register(BuddycardsItems.MEDAL_BASE.get(), () -> new MedalRenderer(getDefaultMedalTexture("buddysteel_medal_base")));
        CuriosRendererRegistry.register(BuddycardsItems.MEDAL_NETHER.get(), () -> new MedalRenderer(getDefaultMedalTexture("buddysteel_medal_nether")));
        CuriosRendererRegistry.register(BuddycardsItems.MEDAL_END.get(), () -> new MedalRenderer(getDefaultMedalTexture("buddysteel_medal_end")));
    }

    protected static ResourceLocation getDefaultMedalTexture(String name) {
        return new ResourceLocation(Buddycards.MOD_ID, "textures/models/medal/" + name + ".png");
    }
}
