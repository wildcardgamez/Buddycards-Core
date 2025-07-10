package com.wildcard.buddycards.item;

import com.wildcard.buddycards.container.BinderContainer;
import com.wildcard.buddycards.core.BuddycardSet;
import com.wildcard.buddycards.menu.BinderMenu;
import com.wildcard.buddycards.registries.BuddycardsItems;
import com.wildcard.buddycards.registries.BuddycardsMisc;
import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BuddycardBinderItem extends Item {
    public BuddycardBinderItem(Properties properties, BuddycardSet set, ResourceLocation texture) {
        super(properties);
        SET = set;
        TEXTURE = texture;
    }

    protected final BuddycardSet SET;
    protected final ResourceLocation TEXTURE;

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable(SET.getDescriptionId()).withStyle(ChatFormatting.GRAY));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack binder = player.getItemInHand(hand);
        if(level instanceof ServerLevel) {
            int pages = 3 + EnchantmentHelper.getItemEnchantmentLevel(BuddycardsMisc.EXTRA_PAGE.get(), binder);
            int stacks = EnchantmentHelper.getItemEnchantmentLevel(BuddycardsMisc.THICK_POCKETS.get(), binder);
            NetworkHooks.openScreen((ServerPlayer) player, new SimpleMenuProvider(
                    (id, playerInventory, entity) -> new BinderMenu(id, player.getInventory(), new BinderContainer(binder, pages, stacks))
                    , player.getItemInHand(hand).getHoverName()));
        }
        return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand), level.isClientSide());
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return stack.getCount() == 1;
    }

    @Override
    public int getEnchantmentValue() {
        return 1;
    }

    public ResourceLocation getBinderTexture() {
        return TEXTURE;
    }
}
