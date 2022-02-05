package com.wildcard.buddycards.item;

import com.wildcard.buddycards.container.BinderContainer;
import com.wildcard.buddycards.registries.BuddycardsItems;
import com.wildcard.buddycards.registries.BuddycardsMisc;
import com.wildcard.buddycards.inventory.BinderInventory;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;

public class BuddycardBinderItem extends Item {
    public BuddycardBinderItem(BuddycardsItems.BuddycardRequirement shouldLoad, Properties properties) {
        super(properties);
        REQUIREMENT = shouldLoad;
    }

    protected BuddycardsItems.BuddycardRequirement REQUIREMENT;

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
            NetworkHooks.openGui((ServerPlayer) player, new SimpleMenuProvider(
                    (id, playerInventory, entity) -> new BinderContainer(id, player.getInventory(), new BinderInventory(finalSlots, binder))
                    , player.getItemInHand(hand).getHoverName()));
        }
        return InteractionResultHolder.success(player.getItemInHand(hand));
    }

    @Override
    public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items) {
        //Only show in the creative menu when the respective mod is loaded
        if(this.allowdedIn(group) && REQUIREMENT.shouldLoad()) {
            items.add(new ItemStack(this));
        }
    }

    public boolean isEnchantable(ItemStack stack) {
        return stack.getCount() == 1;
    }

    public int getEnchantmentValue() {
        return 1;
    }
}
