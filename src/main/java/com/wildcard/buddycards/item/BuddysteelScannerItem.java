package com.wildcard.buddycards.item;

import com.wildcard.buddycards.core.*;
import com.wildcard.buddycards.menu.ScannerMenu;
import com.wildcard.buddycards.registries.BuddycardsComponents;
import com.wildcard.buddycards.registries.BuddycardsItems;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.stream.Stream;

public class BuddysteelScannerItem extends Item implements CardInfoProviderItem {
    public BuddysteelScannerItem(boolean creative) {
        super(new Properties().rarity(Rarity.UNCOMMON).stacksTo(1).component(BuddycardsComponents.SCANNER_COLLECTION, new CompoundTag()).component(BuddycardsComponents.COLLECTION_TIER, creative ? 4 : 0));
        CREATIVE = creative;
    }

    public BuddysteelScannerItem() {
        super(new Properties().rarity(Rarity.UNCOMMON).stacksTo(1).component(BuddycardsComponents.SCANNER_COLLECTION, new CompoundTag()).component(BuddycardsComponents.COLLECTION_TIER, 0));
        CREATIVE = false;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        BlockPos pos = context.getClickedPos();
        if (context.getLevel() instanceof ServerLevel level) {
            if (level.getBlockState(pos).getBlock() instanceof CardInfoProviderBlock block) {
                addCardInfo(context.getItemInHand(), block.getAllCardInfo(level.getBlockState(pos), level, pos, context.getPlayer()));
                return InteractionResult.SUCCESS;
            }
        }
        return super.useOn(context);
    }

    private final boolean CREATIVE;

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable(getDescriptionId() + ".desc" + stack.get(BuddycardsComponents.COLLECTION_TIER)).withStyle(ChatFormatting.GRAY));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        if (level instanceof ServerLevel) {
            ItemStack scanner = player.getItemInHand(usedHand);
            ItemStack offhandItem = player.getItemInHand(usedHand == InteractionHand.MAIN_HAND ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND);
            if (player.isCrouching() && usedHand.equals(InteractionHand.MAIN_HAND) && scanner.getItem() instanceof BuddysteelScannerItem) {
                Stream<CardInfo> stream = Stream.empty();
                for(ItemStack stack : player.getAllSlots()) {
                    if (stack.getItem() instanceof CardInfoProviderItem item)
                        stream = Stream.concat(stream, item.getAllCardInfo(stack, player)).distinct();
                }
                System.out.println();
                addCardInfo(scanner, stream);
                return InteractionResultHolder.success(scanner);
            }
            else if (offhandItem.getItem() instanceof CardInfoProviderItem item) {
                addCardInfo(scanner, item.getAllCardInfo(offhandItem, player));
                return InteractionResultHolder.success(scanner);
            } else if (player instanceof ServerPlayer serverPlayer && scanner.getItem() instanceof BuddysteelScannerItem) {
                int type = scanner.get(BuddycardsComponents.COLLECTION_TIER);
                if (type == 0)
                    if (offhandItem.getItem().equals(BuddycardsItems.LUMINIS_SCANNER_CHIP.get())) {
                        scanner.set(BuddycardsComponents.COLLECTION_TIER, 1);
                        offhandItem.shrink(1);
                        return InteractionResultHolder.success(scanner);
                    }
                    else if (offhandItem.getItem().equals(BuddycardsItems.ZYLEX_SCANNER_CHIP.get())) {
                        scanner.set(BuddycardsComponents.COLLECTION_TIER, 2);
                        offhandItem.shrink(1);
                        return InteractionResultHolder.success(scanner);
                    }
                if (type == 1 && offhandItem.getItem().equals(BuddycardsItems.ZYLEX_SCANNER_CHIP.get())) {
                    scanner.set(BuddycardsComponents.COLLECTION_TIER, 3);
                    offhandItem.shrink(1);
                    return InteractionResultHolder.success(scanner);
                }
                if (type == 2 && offhandItem.getItem().equals(BuddycardsItems.LUMINIS_SCANNER_CHIP.get())) {
                    scanner.set(BuddycardsComponents.COLLECTION_TIER, 3);
                    offhandItem.shrink(1);
                    return InteractionResultHolder.success(scanner);
                }
                if (type != 4 && offhandItem.getItem().equals(BuddycardsItems.PERFECT_SCANNER_CHIP.get())) {
                    scanner.set(BuddycardsComponents.COLLECTION_TIER, 4);
                    offhandItem.shrink(1);
                    return InteractionResultHolder.success(scanner);
                }
                if (usedHand.equals(InteractionHand.MAIN_HAND)) {
                    serverPlayer.openMenu(new SimpleMenuProvider((id, playerInventory, entity) -> new ScannerMenu(id, getAllCardInfo(scanner, player), scanner.get(BuddycardsComponents.COLLECTION_TIER)), Component.empty()));
                }
            }
        }
        return super.use(level, player, usedHand);
    }

    public void addCardInfo(ItemStack stack, Stream<CardInfo> infoStream) {
        CustomData.update(DataComponents.CUSTOM_DATA, stack, (tag) -> {
            List<CardInfo> list = infoStream.toList();
            for (CardInfo card : list) {
                CompoundTag setTag = tag.contains(card.set()) ? tag.getCompound(card.set()) : new CompoundTag();
                CompoundTag cardTag = setTag.contains(String.valueOf(card.number())) ? setTag.getCompound(String.valueOf(card.number())) : new CompoundTag();
                CompoundTag foilTag = cardTag.contains(String.valueOf(card.foil())) ? cardTag.getCompound(String.valueOf(card.foil())) : new CompoundTag();
                foilTag.putBoolean(String.valueOf(card.grade()), true);
                cardTag.put(String.valueOf(card.foil()), foilTag);
                setTag.put(String.valueOf(card.number()), cardTag);
                tag.put(card.set(), setTag);
            }
            stack.set(BuddycardsComponents.SCANNER_COLLECTION, tag);
        });
    }

    @Override
    public Stream<CardInfo> getAllCardInfo(ItemStack stack, Player player) {
        NonNullList<CardInfo> list = NonNullList.create();
        if (!CREATIVE) {
            CompoundTag tag = stack.has(BuddycardsComponents.SCANNER_COLLECTION) ? stack.get(BuddycardsComponents.SCANNER_COLLECTION) : new CompoundTag();
            if (tag != null && !tag.isEmpty()) {
                for (String set : tag.getAllKeys()) {
                    CompoundTag setTag = tag.getCompound(set);
                    for (String number : setTag.getAllKeys()) {
                        CompoundTag cardTag = setTag.getCompound(number);
                        for (String foil : cardTag.getAllKeys()) {
                            CompoundTag foilTag = cardTag.getCompound(foil);
                            for (String grade : foilTag.getAllKeys()) {
                                list.add(new CardInfo(set, Integer.parseInt(number), Integer.parseInt(foil), Integer.parseInt(grade)));
                            }
                        }
                    }
                }
            }
        } else {
            for (BuddycardSet set : BuddycardsAPI.getAllSets()) {
                for (BuddycardItem card : set.getCards()) {
                    for (int i = 0; i < 4; i++) {
                        for (int j = 0; j < 6; j++) {
                            list.add(new CardInfo(set.getName(), card.CARD_NUMBER, i, j));
                        }
                    }
                }
            }
        }
        return list.stream();
    }

    public float getCompletionPercentageForSet(ItemStack stack, int tier, String set) {
        if (!stack.has(BuddycardsComponents.SCANNER_COLLECTION))
            return 0;
        List<CardInfo> info = getAllCardInfo(stack, null).toList();
        int top = 0, bottom = 0;
        if (set == "all") {
            for (BuddycardSet buddycardSet : BuddycardsAPI.getAllSets()) {
                if (!buddycardSet.isPromo()) {
                    if (tier == 0)
                        for (BuddycardItem card : buddycardSet.getCards()) {
                            bottom++;
                            if (info.stream().anyMatch(i -> i.number() == card.CARD_NUMBER))
                                top++;
                        }
                    else if (tier == 1)
                        for (BuddycardItem card : buddycardSet.getCards()) {
                            for (int j = 0; j < 4; j++) {
                                bottom++;
                                int finalJ = j;
                                if (info.stream().anyMatch(i -> i.number() == card.CARD_NUMBER && i.foil() == finalJ))
                                    top++;
                            }
                        }
                    else if (tier == 2)
                        for (BuddycardItem card : buddycardSet.getCards()) {
                            for (int j = 0; j < 6; j++) {
                                bottom++;
                                int finalJ = j;
                                if (info.stream().anyMatch(i -> i.number() == card.CARD_NUMBER && i.grade() == finalJ))
                                    top++;
                            }
                        }
                    else if (tier == 3)
                        for (BuddycardItem card : buddycardSet.getCards()) {
                            for (int j = 0; j < 4; j++) {
                                bottom++;
                                int finalJ = j;
                                if (info.stream().anyMatch(i -> i.number() == card.CARD_NUMBER && i.foil() == finalJ))
                                    top++;
                            }
                            for (int j = 1; j < 6; j++) {
                                bottom++;
                                int finalJ = j;
                                if (info.stream().anyMatch(i -> i.number() == card.CARD_NUMBER && i.grade() == finalJ))
                                    top++;
                            }
                        }
                    else if (tier == 4)
                        for (BuddycardItem card : buddycardSet.getCards()) {
                            for (int j = 0; j < 4; j++) {
                                int finalJ = j;
                                for (int k = 0; k < 6; k++) {
                                    bottom++;
                                    int finalK = k;
                                    if (info.stream().anyMatch(i -> i.number() == card.CARD_NUMBER && i.foil() == finalJ && i.grade() == finalK))
                                        top++;
                                }
                            }
                        }
                }
            }
        } else {
            info = info.stream().filter(i -> i.set().equals(set)).toList();
            BuddycardSet buddycardSet = BuddycardsAPI.findSet(set);
            if (tier == 0)
                for (BuddycardItem card : buddycardSet.getCards()) {
                    bottom++;
                    if (info.stream().anyMatch(i -> i.number() == card.CARD_NUMBER))
                        top++;
                }
            else if (tier == 1)
                for (BuddycardItem card : buddycardSet.getCards()) {
                    for (int j = 0; j < 4; j++) {
                        bottom++;
                        int finalJ = j;
                        if (info.stream().anyMatch(i -> i.number() == card.CARD_NUMBER && i.foil() == finalJ))
                            top++;
                    }
                }
            else if (tier == 2)
                for (BuddycardItem card : buddycardSet.getCards()) {
                    for (int j = 0; j < 6; j++) {
                        bottom++;
                        int finalJ = j;
                        if (info.stream().anyMatch(i -> i.number() == card.CARD_NUMBER && i.grade() == finalJ))
                            top++;
                    }
                }
            else if (tier == 3)
                for (BuddycardItem card : buddycardSet.getCards()) {
                    for (int j = 0; j < 4; j++) {
                        bottom++;
                        int finalJ = j;
                        if (info.stream().anyMatch(i -> i.number() == card.CARD_NUMBER && i.foil() == finalJ))
                            top++;
                    }
                    for (int j = 1; j < 6; j++) {
                        bottom++;
                        int finalJ = j;
                        if (info.stream().anyMatch(i -> i.number() == card.CARD_NUMBER && i.grade() == finalJ))
                            top++;
                    }
                }
            else if (tier == 4)
                for (BuddycardItem card : buddycardSet.getCards()) {
                    for (int j = 0; j < 4; j++) {
                        int finalJ = j;
                        for (int k = 0; k < 6; k++) {
                            bottom++;
                            int finalK = k;
                            if (info.stream().anyMatch(i -> i.number() == card.CARD_NUMBER && i.foil() == finalJ && i.grade() == finalK))
                                top++;
                        }
                    }
                }
        }
        if (bottom == 0)
            return 0;
        return ((float) top)/ bottom;
    }
}
