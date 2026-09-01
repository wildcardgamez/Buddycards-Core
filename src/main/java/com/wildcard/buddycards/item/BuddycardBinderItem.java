package com.wildcard.buddycards.item;

import com.wildcard.buddycards.container.BinderItemHandler;
import com.wildcard.buddycards.core.BuddycardSet;
import com.wildcard.buddycards.core.CardInfo;
import com.wildcard.buddycards.enchantment.EnchantmentKeys;
import com.wildcard.buddycards.menu.BinderMenu;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.stream.Stream;

public class BuddycardBinderItem extends Item implements ICardInfoProviderItem {
    public BuddycardBinderItem(Properties properties, BuddycardSet set, ResourceLocation texture, boolean large) {
        super(properties);
        SET = set;
        TEXTURE = texture;
        LARGE = large;
    }

    @Deprecated
    public BuddycardBinderItem(Properties properties, BuddycardSet set, ResourceLocation texture) {
        super(properties);
        SET = set;
        TEXTURE = texture;
        LARGE = false;
    }

    protected final BuddycardSet SET;
    protected final ResourceLocation TEXTURE;
    protected final boolean LARGE;

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable(SET.getDescriptionId()).withStyle(ChatFormatting.GRAY));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if(hand.equals(InteractionHand.OFF_HAND))
            return InteractionResultHolder.pass(player.getItemInHand(hand));
        ItemStack binder = player.getItemInHand(hand);
        if(level instanceof ServerLevel && player instanceof ServerPlayer serverPlayer && binder.getItem() instanceof BuddycardBinderItem) {
            int pages = 3 + EnchantmentKeys.getEnchantmentLevel(level, EnchantmentKeys.EXTRA_PAGE, binder);
            serverPlayer.openMenu(new SimpleMenuProvider(
                    (id, playerInventory, entity) -> new BinderMenu(id, player.getInventory(), new BinderItemHandler(binder, pages, LARGE, level.registryAccess()))
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

    public boolean isLarge() {
        return LARGE;
    }

    public Stream<CardInfo> getAllCardInfo(ItemStack stack, Player player) {
        return stack.get(DataComponents.CONTAINER).stream().filter(i -> i.getItem() instanceof BuddycardItem).map(BuddycardItem::getCardInfo).distinct();
    }
}
