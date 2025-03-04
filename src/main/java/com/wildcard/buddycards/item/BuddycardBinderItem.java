package com.wildcard.buddycards.item;

import com.wildcard.buddycards.container.BinderContainer;
import com.wildcard.buddycards.core.BuddycardSet;
import com.wildcard.buddycards.menu.BinderMenu;
import com.wildcard.buddycards.registries.BuddycardsItems;
import com.wildcard.buddycards.registries.BuddycardsMisc;
import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
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
    public BuddycardBinderItem(BuddycardsItems.BuddycardRequirement shouldLoad, Properties properties, BuddycardSet set) {
        super(properties);
        REQUIREMENT = shouldLoad;
        SET = set;
    }

    protected BuddycardsItems.BuddycardRequirement REQUIREMENT;
    protected BuddycardSet SET;

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable(SET.getDescriptionId()).withStyle(ChatFormatting.GRAY));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack binder = player.getItemInHand(hand);
        if(level instanceof ServerLevel) {
            //Find the amount of slots and then open the binder GUI
            int slots = 54;
            switch (EnchantmentHelper.getItemEnchantmentLevel(BuddycardsMisc.EXTRA_PAGE.get(), binder)) {
                case 3:
                    slots += 24;
                case 2:
                    slots += 24;
                case 1:
                    slots += 18;
            }
            int finalSlots = slots;
            NetworkHooks.openScreen((ServerPlayer) player, new SimpleMenuProvider(
                    (id, playerInventory, entity) -> new BinderMenu(id, player.getInventory(), new BinderContainer(finalSlots, binder))
                    , player.getItemInHand(hand).getHoverName()));
        }
        return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand), level.isClientSide());
    }

    @Override
    public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items) {
        //Only show in the creative menu when the respective mod is loaded
        if(this.allowdedIn(group) && REQUIREMENT.shouldLoad()) {
            items.add(new ItemStack(this));
        }
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return stack.getCount() == 1;
    }

    @Override
    public int getEnchantmentValue() {
        return 1;
    }
}
