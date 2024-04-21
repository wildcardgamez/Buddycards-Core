package com.wildcard.buddycards.registries;

import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ListMultimap;
import com.ibm.icu.impl.Pair;
import com.wildcard.buddycards.Buddycards;
import com.wildcard.buddycards.battles.BattleComponent;
import com.wildcard.buddycards.battles.BuddycardBattleIcon;
import com.wildcard.buddycards.battles.IBattleIcon;
import com.wildcard.buddycards.battles.TextureBattleIcon;
import com.wildcard.buddycards.battles.game.BattleAbility;
import com.wildcard.buddycards.battles.game.BattleEvent;
import com.wildcard.buddycards.battles.game.BattleGame;
import com.wildcard.buddycards.battles.game.BattleStatusEffect;
import com.wildcard.buddycards.container.BattleContainer;
import com.wildcard.buddycards.core.BuddycardSet;
import com.wildcard.buddycards.gear.BuddycardsArmorMaterial;
import com.wildcard.buddycards.gear.BuddycardsToolTier;
import com.wildcard.buddycards.item.*;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.*;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class BuddycardsItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Buddycards.MOD_ID);

    public static void registerItems() {
        //Register base set
        /*SAPS       */ registerCard(BASE_SET,  1, Rarity.COMMON,   2, 1, new BattleAbility.Builder().add(BattleEvent.TURN.ability("growing_up", (game, slot, target, source) -> {
            game.turnPower[slot]++;
            game.container.addLog(new BattleComponent(new TranslatableComponent("battles.ability.buddycards.growing_up.log"), List.of(BuddycardBattleIcon.create(game.getCard(slot)), TextureBattleIcon.dividerIcon, BuddycardBattleIcon.create(game.getCard(slot)), TextureBattleIcon.addIcon(1))));
            game.updatePower(slot);
            return true;
        })).build());
        /*ROK        */ registerCard(BASE_SET,  2, Rarity.COMMON,   2, 1, new BattleAbility.Builder().add(BattleEvent.PLAYED.ability("stone_toss", (game, slot, target, source) -> {
            int opp = BattleGame.opposite(slot);
            game.directAttack(opp, slot, 1);
            if(game.getCard(opp) != null) {
                game.container.addLog(new BattleComponent(new TranslatableComponent("battles.ability.buddycards.stone_toss.log1").append(new TranslatableComponent(game.getCard(opp).getDescriptionId())).append(new TranslatableComponent("battles.ability.buddycards.stone_toss.log2")), List.of(BuddycardBattleIcon.create(game.getCard(slot)), TextureBattleIcon.dividerIcon, BuddycardBattleIcon.create(game.getCard(slot)), TextureBattleIcon.damageIcon(1), BuddycardBattleIcon.create(game.getCard(opp)))));
                game.updatePower(opp);
            } else
                game.container.addLog(new BattleComponent(new TranslatableComponent("battles.ability.buddycards.stone_toss.log1").append(BattleGame.getOwner(slot) ? game.container.name2 : game.container.name1).append(new TranslatableComponent("battles.ability.buddycards.stone_toss.log2")), List.of(BuddycardBattleIcon.create(game.getCard(slot)), TextureBattleIcon.dividerIcon, BuddycardBattleIcon.create(game.getCard(slot)), TextureBattleIcon.damageIcon(1))));
            return true;
        })).build());
        /*OINKE      */ registerCard(BASE_SET,  3, Rarity.COMMON,   3, 2, new BattleAbility.Builder().add(BattleEvent.DEATH.ability("tasty_bacon", (game, slot, target, source) -> {
            if(BattleGame.getOwner(slot))
                game.container.health1+=2;
            else
                game.container.health2+=2;
            game.container.addLog(new BattleComponent(new TextComponent("").append(BattleGame.getOwner(slot) ? game.container.name1 : game.container.name2).append(new TranslatableComponent("battles.ability.buddycards.tasty_bacon.log")), List.of(BuddycardBattleIcon.create(game.getCard(slot)), TextureBattleIcon.dividerIcon, TextureBattleIcon.healIcon(2))));
            return true;
        })).build());
        /*COLE       */ registerCard(BASE_SET,  4, Rarity.COMMON,   3, 2, new BattleAbility.Builder().add(BattleEvent.DEATH.ability("stoke_flames", (game, slot, target, source) -> {
            List<IBattleIcon> icons = new ArrayList<>(List.of(BuddycardBattleIcon.create(game.getCard(slot)), TextureBattleIcon.dividerIcon));
            for (int i: BattleEvent.Distribution.ROW.apply(slot, game)) {
                if(game.container.getItem(BattleGame.translateFrom(i)).m_204117_(BuddycardsMisc.BCB_FIRE)) {
                    game.turnPower[i]++;
                    icons.add(BuddycardBattleIcon.create(game.getCard(i)));
                    icons.add(TextureBattleIcon.addIcon(1));
                }
            }
            if(icons.size() > 2) {
                game.container.addLog(new BattleComponent(new TranslatableComponent("battles.ability.buddycards.stoke_flames.log"), icons));
                game.updatePower();
            }
            return true;
        })).build());
        /*METALICO   */ registerCard(BASE_SET,  5, Rarity.COMMON,   3, 2, new BattleAbility.Builder().add(BattleEvent.KILL.ability("iron_temper", (game, slot, target, source) -> {
            game.turnPower[slot]++;
            game.container.addLog(new BattleComponent(new TranslatableComponent("battles.ability.buddycards.iron_temper.log"), List.of(BuddycardBattleIcon.create(game.getCard(slot)), TextureBattleIcon.dividerIcon, BuddycardBattleIcon.create(game.getCard(slot)), TextureBattleIcon.addIcon(1))));
            game.updatePower(slot);
            return true;
        })).build());
        /*DR_LAZULI  */ registerCard(BASE_SET,  6, Rarity.COMMON,   4, 2, new BattleAbility.Builder().add(BattleEvent.PLAYED.ability("simple_enchant", (game, slot, target, source) -> {
            List<IBattleIcon> icons = new ArrayList<>(List.of(BuddycardBattleIcon.create(game.getCard(slot)), TextureBattleIcon.dividerIcon));
            for (int i: BattleEvent.Distribution.ROW.apply(slot, game)) {
                if(game.container.getItem(BattleGame.translateFrom(i)).m_204117_(BuddycardsMisc.BCB_ENCHANTABLE)) {
                    game.turnPower[i]++;
                    icons.add(BuddycardBattleIcon.create(game.getCard(i)));
                    icons.add(TextureBattleIcon.addIcon(1));
                }
            }
            if(icons.size() > 2) {
                game.container.addLog(new BattleComponent(new TranslatableComponent("battles.ability.buddycards.simple_enchant.log"), icons));
                game.updatePower();
            }
            return true;
        })).build());
        /*BETSY      */ registerCard(BASE_SET,  7, Rarity.COMMON,   5, 2, new BattleAbility.Builder().add(BattleEvent.TURN.ability("milk_maid", (game, slot, target, source) -> {
            if(BattleGame.getOwner(slot))
                game.container.health1++;
            else
                game.container.health2++;
            game.container.addLog(new BattleComponent(new TextComponent("").append(BattleGame.getOwner(slot) ? game.container.name1 : game.container.name2).append(new TranslatableComponent("battles.ability.buddycards.milk_maid.log")), List.of(BuddycardBattleIcon.create(game.getCard(slot)), TextureBattleIcon.dividerIcon, TextureBattleIcon.healIcon(1))));
            return true;
        })).build());
        /*SNOWBELLE  */ registerCard(BASE_SET,  8, Rarity.COMMON,   1, 1, new BattleAbility.Builder().add(BattleEvent.PLAYED.ability("snowball_fight", (game, slot, target, source) -> {
            if(BattleGame.getOwner(slot))
                game.container.health2--;
            else
                game.container.health1--;
            game.container.addLog(new BattleComponent(new TextComponent("").append(BattleGame.getOwner(slot) ? game.container.name2 : game.container.name1).append(new TranslatableComponent("battles.ability.buddycards.snowball_fight.log")), List.of(BuddycardBattleIcon.create(game.getCard(slot)), TextureBattleIcon.dividerIcon, BuddycardBattleIcon.create(game.getCard(slot)), TextureBattleIcon.damageIcon(1))));
            return true;
        })).build());
        /*AQUA       */ registerCard(BASE_SET,  9, Rarity.COMMON,   2, 0, new BattleAbility.Builder().add(BattleEvent.DEATH.ability("wash_out", (game, slot, target, source) -> {
            List<IBattleIcon> icons = new ArrayList<>(List.of(BuddycardBattleIcon.create(game.getCard(slot)), TextureBattleIcon.dividerIcon));
            List<Integer> changedCards = new ArrayList<>();
            for (int i: BattleEvent.Distribution.ALL.apply(slot, game)) {
                boolean a = game.state[i].status == (BattleStatusEffect.FIRE), b = game.container.getItem(BattleGame.translateFrom(i)).m_204117_(BuddycardsMisc.BCB_FIRE);
                if(a || b)
                    icons.add(BuddycardBattleIcon.create(game.getCard(i)));
                if(a) {
                    game.state[i].status = BattleStatusEffect.EMPTY;
                    icons.add(TextureBattleIcon.statusIcon(BattleStatusEffect.EMPTY));
                }
                if(b) {
                    game.directAttack(BattleGame.opposite(slot), slot, 1, false, false);
                    icons.add(TextureBattleIcon.subtractIcon(1));
                    changedCards.add(i);
                }
            }
            if(icons.size() > 2) {
                game.container.addLog(new BattleComponent(new TranslatableComponent("battles.ability.buddycards.wash_out.log"), icons));
                for(int i : changedCards)
                    game.updatePower(i);
            }
            return true;
        })).build());
        /*FORTUNA    */ registerCard(BASE_SET, 10, Rarity.COMMON,   1, 1, new BattleAbility.Builder().add(BattleEvent.DEATH.ability("tasty_treat", (game, slot, target, source) -> {
            if(BattleGame.getOwner(slot))
                game.container.health1++;
            else
                game.container.health2++;
            game.container.addLog(new BattleComponent(new TextComponent("").append(BattleGame.getOwner(slot) ? game.container.name1 : game.container.name2).append(new TranslatableComponent("battles.ability.buddycards.tasty_treat.log")), List.of(BuddycardBattleIcon.create(game.getCard(slot)), TextureBattleIcon.dividerIcon, TextureBattleIcon.healIcon(1))));
            return true;
        })).build());
        /*WEET       */ registerCard(BASE_SET, 11, Rarity.COMMON,   3, 0, new BattleAbility.Builder().add(BattleEvent.DEATH.ability("fresh_harvest", (game, slot, target, source) -> {
            List<IBattleIcon> icons = new ArrayList<>(List.of(BuddycardBattleIcon.create(game.getCard(slot)), TextureBattleIcon.dividerIcon));
            for (int i: BattleEvent.Distribution.ROW.apply(slot, game)) {
                if(game.container.getItem(BattleGame.translateFrom(i)).m_204117_(BuddycardsMisc.BCB_ANIMAL)) {
                    game.turnPower[i] += 2;
                    icons.add(BuddycardBattleIcon.create(game.getCard(i)));
                    icons.add(TextureBattleIcon.addIcon(2));
                }
            }
            if(icons.size() > 2) {
                game.container.addLog(new BattleComponent(new TranslatableComponent("battles.ability.buddycards.fresh_harvest.log"), icons));
            }
            return true;
        })).build());
        /*GRASSLING  */ registerCard(BASE_SET, 12, Rarity.COMMON,   3, 1, new BattleAbility.Builder().add(BattleEvent.PLAYED.ability("feeding_time", (game, slot, target, source) -> {
            List<IBattleIcon> icons = new ArrayList<>(List.of(BuddycardBattleIcon.create(game.getCard(slot)), TextureBattleIcon.dividerIcon));
            for (int i: BattleEvent.Distribution.ROW.apply(slot, game)) {
                if(game.container.getItem(BattleGame.translateFrom(i)).m_204117_(BuddycardsMisc.BCB_ANIMAL)) {
                    game.turnPower[i]++;
                    icons.add(BuddycardBattleIcon.create(game.getCard(i)));
                    icons.add(TextureBattleIcon.addIcon(1));
                }
            }
            if(icons.size() > 2) {
                game.container.addLog(new BattleComponent(new TranslatableComponent("battles.ability.buddycards.feeding_time.log"), icons));
                game.updatePower();
            }
            return true;
        })).build());
        /*YANG       */ registerCard(BASE_SET, 13, Rarity.UNCOMMON, 4, 3, new BattleAbility.Builder().add(BattleEvent.KILL.ability("play_fetch", (game, slot, target, source) -> {
            if(game.container.tryDrawCard(BattleGame.getOwner(slot)))
                game.container.addLog(new BattleComponent(new TextComponent("").append(BattleGame.getOwner(slot) ? game.container.name1 : game.container.name2).append(new TranslatableComponent("battles.ability.buddycards.play_fetch.log")), List.of(BuddycardBattleIcon.create(game.getCard(slot)), TextureBattleIcon.dividerIcon, TextureBattleIcon.drawIcon)));
            return true;
        })).build());
        /*YIN        */ registerCard(BASE_SET, 14, Rarity.UNCOMMON, 4, 2, new BattleAbility.Builder().add(BattleEvent.FIGHT.ability("lazy_cat", (game, slot, target, source) -> {
            if(game.turnPower[slot] < 2 && game.isP1() == BattleGame.getOwner(slot)) {
                game.turnPower[slot] += 2;
                game.container.addLog(new BattleComponent(new TranslatableComponent("battles.ability.buddycards.lazy_cat.log"), List.of(BuddycardBattleIcon.create(game.getCard(slot)), TextureBattleIcon.dividerIcon, BuddycardBattleIcon.create(game.getCard(slot)), TextureBattleIcon.addIcon(2))));
                game.updatePower(slot);
                return false;
            }
            return true;
        })).build());
        /*BLING      */ registerCard(BASE_SET, 15, Rarity.UNCOMMON, 3, 1, new BattleAbility.Builder().add(BattleEvent.PLAYED.ability("gilded_gift", (game, slot, target, source) -> {
            List<IBattleIcon> icons = new ArrayList<>(List.of(BuddycardBattleIcon.create(game.getCard(slot)), TextureBattleIcon.dividerIcon));
            for (int i: BattleEvent.Distribution.ROW.apply(slot, game)) {
                if(game.container.getItem(BattleGame.translateFrom(i)).m_204117_(BuddycardsMisc.BCB_FOOD)) {
                    game.turnPower[i]++;
                    icons.add(BuddycardBattleIcon.create(game.getCard(i)));
                    icons.add(TextureBattleIcon.addIcon(1));
                }
            }
            if(icons.size() > 2) {
                game.container.addLog(new BattleComponent(new TranslatableComponent("battles.ability.buddycards.gilded_gift.log"), icons));
                game.updatePower();
            }
            return true;
        })).build());
        /*EMERELDA   */ registerCard(BASE_SET, 16, Rarity.UNCOMMON, 3, 1, new BattleAbility.Builder().add(BattleEvent.PLAYED.ability("rich_trade", (game, slot, target, source) -> {
            List<IBattleIcon> icons = new ArrayList<>(List.of(BuddycardBattleIcon.create(game.getCard(slot)), TextureBattleIcon.dividerIcon));
            while (game.container.tryDrawCard(BattleGame.getOwner(slot)))
                icons.add(TextureBattleIcon.drawIcon);
            if(icons.size() > 2)
                game.container.addLog(new BattleComponent(new TextComponent("").append(BattleGame.getOwner(slot) ? game.container.name1 : game.container.name2).append(new TranslatableComponent("battles.ability.buddycards.rich_trade.log")), icons));
            return true;
        })).build());
        /*DIO        */ registerCard(BASE_SET, 17, Rarity.UNCOMMON, 7, 3, new BattleAbility.Builder().add(BattleEvent.DAMAGED.ability("unbreakable", (game, slot, target, source) -> {
            game.state[slot].power++;
            game.container.addLog(new BattleComponent(new TranslatableComponent("battles.ability.buddycards.unbreakable.log"), List.of(BuddycardBattleIcon.create(game.getCard(slot)), TextureBattleIcon.dividerIcon, BuddycardBattleIcon.create(game.getCard(slot)), TextureBattleIcon.addIcon(1))));
            game.updatePower(slot);
            return true;
        })).build());
        /*TREATSY    */ registerCard(BASE_SET, 18, Rarity.UNCOMMON, 4, 2, new BattleAbility.Builder().add(BattleEvent.DEATH.ability("delicious_desert", (game, slot, target, source) -> {
            if(BattleGame.getOwner(slot))
                game.container.health1+=3;
            else
                game.container.health2+=3;
            game.container.addLog(new BattleComponent(new TextComponent("").append(BattleGame.getOwner(slot) ? game.container.name1 : game.container.name2).append(new TranslatableComponent("battles.ability.buddycards.delicious_desert.log")), List.of(BuddycardBattleIcon.create(game.getCard(slot)), TextureBattleIcon.dividerIcon, TextureBattleIcon.healIcon(3))));
            return true;
        })).build());
        /*RED        */ registerCard(BASE_SET, 19, Rarity.UNCOMMON, 4, 2, new BattleAbility.Builder().add(BattleEvent.TURN.ability("energize", (game, slot, target, source) -> {
            List<IBattleIcon> icons = new ArrayList<>(List.of(BuddycardBattleIcon.create(game.getCard(slot)), TextureBattleIcon.dividerIcon));
            List<Integer> cards = new ArrayList<>();
            for (int i: BattleEvent.Distribution.ADJACENT.apply(slot, game)) {
                if(game.container.getItem(BattleGame.translateFrom(i)).m_204117_(BuddycardsMisc.BCB_REDSTONE)) {
                    cards.add(i);
                    icons.add(BuddycardBattleIcon.create(game.getCard(i)));
                    icons.add(TextureBattleIcon.powerIcon);
                }
            }
            if(cards.size() > 0) {
                game.container.addLog(new BattleComponent(new TranslatableComponent("battles.ability.buddycards.energize.log"), icons));
                for(int i : cards)
                    game.trigger(BattleEvent.POWERED, i, i, slot);
            }
            return true;
        })).build());
        /*MELONY     */ registerCard(BASE_SET, 20, Rarity.UNCOMMON, 2, 1, new BattleAbility.Builder().add(BattleEvent.DEATH.ability("sweet_treat", (game, slot, target, source) -> {
            if(BattleGame.getOwner(slot))
                game.container.health1+=2;
            else
                game.container.health2+=2;
            game.container.addLog(new BattleComponent(new TextComponent("").append(BattleGame.getOwner(slot) ? game.container.name1 : game.container.name2).append(new TranslatableComponent("battles.ability.buddycards.sweet_treat.log")), List.of(BuddycardBattleIcon.create(game.getCard(slot)), TextureBattleIcon.dividerIcon, TextureBattleIcon.healIcon(2))));
            return true;
        })).build());
        /*SPOOPY     */ registerCard(BASE_SET, 21, Rarity.UNCOMMON, 4, 1, new BattleAbility.Builder().add(BattleEvent.FIGHT.ability("spook_em", (game, slot, target, source) -> {
            if(target != slot && game.getCard(target) != null && game.isP1() == BattleGame.getOwner(slot)) {
                game.directAttack(target, slot, 1, false, false);
                game.container.addLog(new BattleComponent(new TranslatableComponent(game.getCard(target).getDescriptionId()).append(new TranslatableComponent("battles.ability.buddycards.spook_em.log")), List.of(BuddycardBattleIcon.create(game.getCard(slot)), TextureBattleIcon.dividerIcon, BuddycardBattleIcon.create(game.getCard(target)), TextureBattleIcon.subtractIcon(1))));
                game.updatePower(target);
            }
            return true;
        })).build());
        /*KNALL_EDGY */ registerCard(BASE_SET, 22, Rarity.RARE,     8, 3, new BattleAbility.Builder().add(BattleEvent.PLAYED.ability("premium_enchant", (game, slot, target, source) -> {
            List<IBattleIcon> icons = new ArrayList<>(List.of(BuddycardBattleIcon.create(game.getCard(slot)), TextureBattleIcon.dividerIcon));
            for (int i: BattleEvent.Distribution.ROW.apply(slot, game)) {
                if(game.container.getItem(BattleGame.translateFrom(i)).m_204117_(BuddycardsMisc.BCB_ENCHANTABLE)) {
                    game.turnPower[i] += 2;
                    icons.add(BuddycardBattleIcon.create(game.getCard(i)));
                    icons.add(TextureBattleIcon.addIcon(2));
                }
            }
            if(icons.size() > 2) {
                game.container.addLog(new BattleComponent(new TranslatableComponent("battles.ability.buddycards.premium_enchant.log"), icons));
                game.updatePower();
            }
            return true;
        })).add(BattleEvent.KILL.ability("xp_farm", (game, slot, target, source) -> {
            List<IBattleIcon> icons = new ArrayList<>(List.of(BuddycardBattleIcon.create(game.getCard(slot)), TextureBattleIcon.dividerIcon));
            for (int i: BattleEvent.Distribution.ROW.apply(slot, game)) {
                if(game.container.getItem(BattleGame.translateFrom(i)).m_204117_(BuddycardsMisc.BCB_ENCHANTABLE)) {
                    game.turnPower[i]++;
                    icons.add(BuddycardBattleIcon.create(game.getCard(i)));
                    icons.add(TextureBattleIcon.addIcon(1));
                }
            }
            if(icons.size() > 2) {
                game.container.addLog(new BattleComponent(new TranslatableComponent("battles.ability.buddycards.premium_enchant.log"), icons));
                game.updatePower();
            }
            return true;
        })).build());
        /*CASTER     */ registerCard(BASE_SET, 23, Rarity.RARE,     9, 4, new BattleAbility.Builder().add(BattleEvent.PLAYED.ability("anvil_drop", (game, slot, target, source) -> {
            int opp = BattleGame.opposite(slot);
            if(game.getCard(opp) != null) {
                game.directAttack(opp, slot, 4);
                game.container.addLog(new BattleComponent(new TranslatableComponent("battles.ability.buddycards.anvil_drop.log1").append(new TranslatableComponent(game.getCard(opp).getDescriptionId())).append(new TranslatableComponent("battles.ability.buddycards.anvil_drop.log2")), List.of(BuddycardBattleIcon.create(game.getCard(slot)), TextureBattleIcon.dividerIcon, BuddycardBattleIcon.create(game.getCard(slot)), TextureBattleIcon.damageIcon(4), BuddycardBattleIcon.create(game.getCard(opp)))));
                game.updatePower(opp);
            }
            return true;
        })).add(BattleEvent.TURN.ability("reforge", (game, slot, target, source) -> {
            List<IBattleIcon> icons = new ArrayList<>(List.of(BuddycardBattleIcon.create(game.getCard(slot)), TextureBattleIcon.dividerIcon));
            for (int i: BattleEvent.Distribution.ADJACENT.apply(slot, game)) {
                if(game.container.getItem(BattleGame.translateFrom(i)).m_204117_(BuddycardsMisc.BCB_METAL)) {
                    game.turnPower[i]++;
                    icons.add(BuddycardBattleIcon.create(game.getCard(i)));
                    icons.add(TextureBattleIcon.addIcon(1));
                }
            }
            if(icons.size() > 2) {
                game.container.addLog(new BattleComponent(new TranslatableComponent("battles.ability.buddycards.reforge.log"), icons));
                game.updatePower();
            }
            return true;
        })).build());
        /*PISTY      */ registerCard(BASE_SET, 24, Rarity.RARE,     6, 2, new BattleAbility.Builder().add(BattleEvent.PLAYED.ability("push_back", (game, slot, target, source) -> {
            int opp = BattleGame.opposite(slot);
            BuddycardItem returnedCard = game.returnCard(opp);
            if (returnedCard != null) {
                game.container.addLog(new BattleComponent(new TranslatableComponent(returnedCard.getDescriptionId()).append(new TranslatableComponent("battles.ability.buddycards.push_back.log")), List.of(BuddycardBattleIcon.create(game.getCard(slot)), TextureBattleIcon.dividerIcon, BuddycardBattleIcon.create(returnedCard), TextureBattleIcon.returnIcon)));
            }
            return true;
        })).add(BattleEvent.POWERED.ability("empowered_push", (game, slot, target, source) -> {
            int opp = BattleGame.opposite(slot);
            BuddycardItem returnedCard = game.returnCard(opp);
            if (returnedCard != null) {
                game.container.addLog(new BattleComponent(new TranslatableComponent(returnedCard.getDescriptionId()).append(new TranslatableComponent("battles.ability.buddycards.empowered_push.log")), List.of(BuddycardBattleIcon.create(game.getCard(slot)), TextureBattleIcon.dividerIcon, BuddycardBattleIcon.create(returnedCard), TextureBattleIcon.returnIcon)));
            }
            return true;
        })).build());
        /*KNIGHT     */ registerCard(BASE_SET, 25, Rarity.RARE,     7, 4, new BattleAbility.Builder().add(BattleEvent.DAMAGED.ability("prismarine_spines", (game, slot, target, source) -> {
            if(game.getCard(source) != null) {
                game.directAttack(source, slot, 1, false, false);
                game.container.addLog(new BattleComponent(new TranslatableComponent(game.getCard(source).getDescriptionId()).append(new TranslatableComponent("battles.ability.buddycards.prismarine_spines.log")), List.of(BuddycardBattleIcon.create(game.getCard(slot)), TextureBattleIcon.dividerIcon, BuddycardBattleIcon.create(game.getCard(source)), TextureBattleIcon.subtractIcon(1))));
                game.updatePower(source);
            }
            return true;
        })).add(BattleEvent.TURN.ability("laser_gaze", (game, slot, target, source) -> {
            int opp = BattleGame.opposite(slot);
            if(game.getCard(opp) != null && (game.container.turn / 2) % 2 == 1) {
                game.directAttack(opp, slot, 2);
                game.container.addLog(new BattleComponent(new TranslatableComponent("battles.ability.buddycards.laser_gaze.log1").append(new TranslatableComponent(game.getCard(opp).getDescriptionId())).append(new TranslatableComponent("battles.ability.buddycards.laser_gaze.log2")), List.of(BuddycardBattleIcon.create(game.getCard(slot)), TextureBattleIcon.dividerIcon, BuddycardBattleIcon.create(game.getCard(slot)), TextureBattleIcon.damageIcon(2), BuddycardBattleIcon.create(game.getCard(opp)))));
                game.updatePower(opp);
            }
            return true;
        })).build());
        /*CREATOR    */ registerCard(BASE_SET, 26, Rarity.EPIC,     8, 3, new BattleAbility.Builder().add(BattleEvent.PLAYED.ability("grand_creation", (game, slot, target, source) -> {
            List<IBattleIcon> icons = new ArrayList<>(List.of(BuddycardBattleIcon.create(game.getCard(slot)), TextureBattleIcon.dividerIcon));
            List<Pair<Integer, ItemStack>> cards = new ArrayList<>();
            for (int i: BattleEvent.Distribution.ROW.apply(slot, game)) {
                if(game.getCard(i) == null) {
                    ItemStack card = BattleGame.getOwner(slot) ? game.container.deck1.removeItem(BattleContainer.random.nextInt(16), 1) : game.container.deck2.removeItem(BattleContainer.random.nextInt(16), 1);
                    while(card.isEmpty())
                        card = BattleGame.getOwner(slot) ? game.container.deck1.removeItem(BattleContainer.random.nextInt(16), 1) : game.container.deck2.removeItem(BattleContainer.random.nextInt(16), 1);
                    cards.add(Pair.of(i, card));
                    icons.add(BuddycardBattleIcon.create((BuddycardItem) card.getItem()));
                    icons.add(TextureBattleIcon.playIcon);
                }
            }
            if(cards.size() > 0) {
                game.updatePower();
                game.container.addLog(new BattleComponent(new TranslatableComponent("battles.ability.buddycards.grand_creation.log"), icons));
                for (Pair<Integer, ItemStack> p : cards) {
                    game.addCard(p.first, p.second, (BuddycardItem) p.second.getItem());
                }
            }
            return true;
        })).add(BattleEvent.DEATH.ability("craft_together", (game, slot, target, source) -> {
            List<IBattleIcon> icons = new ArrayList<>(List.of(BuddycardBattleIcon.create(game.getCard(slot)), TextureBattleIcon.dividerIcon));
            int power = 0;
            for (int i: BattleEvent.Distribution.ADJACENT.apply(slot, game)) {
                if(game.getCard(i) != null) {
                    power += game.turnPower[i];
                    icons.add(BuddycardBattleIcon.create(game.getCard(i)));
                    icons.add(TextureBattleIcon.xIcon);
                    game.removeCard(i);
                }
            }
            if(power > 0) {
                game.turnPower[slot] += power;
                icons.add(BuddycardBattleIcon.create(game.getCard(slot)));
                icons.add(TextureBattleIcon.addIcon(power));
                game.container.addLog(new BattleComponent(new TranslatableComponent("battles.ability.buddycards.craft_together.log1").append("" + power).append(new TranslatableComponent("battles.ability.buddycards.craft_together.log2")), icons));
                game.updatePower();
                return false;
            }
            return true;
        })).build());
        /*MELTOR     */ registerCard(BASE_SET, 27, Rarity.EPIC,     6, 3, new BattleAbility.Builder().add(BattleEvent.PLAYED.ability("smeltdown", (game, slot, target, source) -> {
            List<IBattleIcon> icons = new ArrayList<>(List.of(BuddycardBattleIcon.create(game.getCard(slot)), TextureBattleIcon.dividerIcon));
            List<Integer> cards = new ArrayList<>();
            int power = 0;
            for (int i: BattleEvent.Distribution.ROW.apply(slot, game)) {
                if(slot != i && game.getCard(i) != null && !game.container.getItem(BattleGame.translateFrom(i)).m_204117_(BuddycardsMisc.BCB_METAL)) {
                    cards.add(i);
                    power+=2;
                    icons.add(BuddycardBattleIcon.create(game.getCard(i)));
                    icons.add(TextureBattleIcon.deathIcon);
                }
            }
            if(cards.size() > 0) {
                icons.add(BuddycardBattleIcon.create(game.getCard(slot)));
                icons.add(TextureBattleIcon.addIcon(power));
                game.turnPower[slot] += power;
                game.container.addLog(new BattleComponent(new TranslatableComponent("battles.ability.buddycards.smeltdown.log1").append("" + power).append(new TranslatableComponent("battles.ability.buddycards.smeltdown.log2")), icons));
                game.updatePower();
                for (int i : cards) {
                    if(game.trigger(BattleEvent.DEATH, i))
                        game.removeCard(i);
                }
            }
            return true;
        })).add(BattleEvent.OBSERVE_DEATH.ability("flaming_forge", (game, slot, target, source) -> {
            if(!game.container.getItem(BattleGame.translateFrom(target)).m_204117_(BuddycardsMisc.BCB_FIRE))
                return true;
            List<IBattleIcon> icons = new ArrayList<>(List.of(BuddycardBattleIcon.create(game.getCard(slot)), TextureBattleIcon.dividerIcon));
            for (int i: BattleEvent.Distribution.ROW.apply(slot, game)) {
                if(i == slot || game.container.getItem(BattleGame.translateFrom(i)).m_204117_(BuddycardsMisc.BCB_METAL)) {
                    game.turnPower[i]+= 2;
                    icons.add(BuddycardBattleIcon.create(game.getCard(i)));
                    icons.add(TextureBattleIcon.addIcon(2));
                    game.updatePower(i);
                }
            }
            if(icons.size() > 2) {
                game.container.addLog(new BattleComponent(new TranslatableComponent("battles.ability.buddycards.flaming_forge.log"), icons));
                game.updatePower(slot);
            }
            return true;
        })).build());
        /*ANGSTY_SAPS*/ registerCard(BASE_SET, 28, Rarity.COMMON,   3, 2, new BattleAbility.Builder().add(BattleEvent.TURN.ability("thick_thicket", (game, slot, target, source) -> {
            for (int i: BattleEvent.Distribution.ADJACENT.apply(slot, game))
                if(game.getCard(i) == null)
                    return true;
            game.turnPower[slot]+=2;
            game.container.addLog(new BattleComponent(new TranslatableComponent("battles.ability.buddycards.thick_thicket.log"), List.of(BuddycardBattleIcon.create(game.getCard(slot)), TextureBattleIcon.dividerIcon, BuddycardBattleIcon.create(game.getCard(slot)), TextureBattleIcon.addIcon(2))));
            game.updatePower(slot);
            return true;
        })).build());
        /*COPPOR     */ registerCard(BASE_SET, 29, Rarity.COMMON,   2, 2, new BattleAbility.Builder().add(BattleEvent.POWERED.ability("conduction", (game, slot, target, source) -> {
            game.turnPower[slot]++;
            game.container.addLog(new BattleComponent(new TranslatableComponent("battles.ability.buddycards.conduction.log"), List.of(BuddycardBattleIcon.create(game.getCard(slot)), TextureBattleIcon.dividerIcon, BuddycardBattleIcon.create(game.getCard(slot)), TextureBattleIcon.addIcon(1))));
            game.updatePower(slot);
            return true;
        })).build());
        /*AIMY       */ registerCard(BASE_SET, 30, Rarity.COMMON,   2, 0, new BattleAbility.Builder().add(BattleEvent.DEATH.ability("courage_chime", (game, slot, target, source) -> {
            List<IBattleIcon> icons = new ArrayList<>(List.of(BuddycardBattleIcon.create(game.getCard(slot)), TextureBattleIcon.dividerIcon));
            List<Integer> cards = new ArrayList<>();
            for (int i: BattleEvent.Distribution.ROW.apply(slot, game)) {
                if(i != slot && game.getCard(i) != null) {
                    game.turnPower[i]++;
                    cards.add(i);
                    icons.add(BuddycardBattleIcon.create(game.getCard(i)));
                    icons.add(TextureBattleIcon.addIcon(1));
                }
            }
            if(icons.size() > 2) {
                game.container.addLog(new BattleComponent(new TranslatableComponent("battles.ability.buddycards.courage_chime.log"), icons));
                for(int i : cards) game.updatePower(i);
            }
            return true;
        })).build());
        /*DROPPER    */ registerCard(BASE_SET, 31, Rarity.COMMON,   1, 2, new BattleAbility.Builder().add(BattleEvent.TURN.ability("drip_drop", (game, slot, target, source) -> {
            BuddycardItem returnedCard = game.returnCard(slot);
            if (returnedCard != null) {
                game.container.addLog(new BattleComponent(new TranslatableComponent("battles.ability.buddycards.drip_drop.log"), List.of(BuddycardBattleIcon.create(returnedCard), TextureBattleIcon.dividerIcon, BuddycardBattleIcon.create(returnedCard), TextureBattleIcon.returnIcon)));
            }
            return true;
        })).build());
        /*TICK       */ registerCard(BASE_SET, 32, Rarity.UNCOMMON, 3, 1, new BattleAbility.Builder().add(BattleEvent.POWERED.ability("pulse_extender", (game, slot, target, source) -> {
            List<IBattleIcon> icons = new ArrayList<>(List.of(BuddycardBattleIcon.create(game.getCard(slot)), TextureBattleIcon.dividerIcon));
            List<Integer> cards = new ArrayList<>();
            if (game.turnPower[slot] < 8) {
                for (int i : BattleEvent.Distribution.ADJACENT.apply(slot, game)) {
                    if (game.container.getItem(BattleGame.translateFrom(i)).m_204117_(BuddycardsMisc.BCB_REDSTONE)) {
                        cards.add(i);
                        icons.add(BuddycardBattleIcon.create(game.getCard(i)));
                        icons.add(TextureBattleIcon.doublePowerIcon);
                    }
                }
                icons.add(BuddycardBattleIcon.create(game.getCard(slot)));
                icons.add(TextureBattleIcon.addIcon(1));
                game.turnPower[slot]++;
                game.updatePower(slot);
                game.container.addLog(new BattleComponent(new TranslatableComponent("battles.ability.buddycards.pulse_extender.log"), icons));
                for (int i : cards) {
                    game.trigger(BattleEvent.POWERED, i, i, slot);
                }
                return true;
            }
            icons.add(BuddycardBattleIcon.create(game.getCard(slot)));
            icons.add(TextureBattleIcon.deathIcon);
            game.container.addLog(new BattleComponent(new TranslatableComponent("battles.ability.buddycards.pulse_extender.log_2"), icons));
            game.trigger(BattleEvent.DEATH, slot, slot, slot);
            game.trigger(BattleEvent.OBSERVE_DEATH, slot, slot, slot, BattleEvent.Distribution.ALL);
            game.removeCard(slot);
            return true;
        })).build());
        /*TOCK       */ registerCard(BASE_SET, 33, Rarity.UNCOMMON, 4, 0, new BattleAbility.Builder().add(BattleEvent.PLAYED.ability("waiting_game", (game, slot, target, source) -> {
            int power = game.container.turn / 2;
            if(power > 0) {
                game.turnPower[slot] += power;
                game.container.addLog(new BattleComponent(new TranslatableComponent("battles.ability.buddycards.waiting_game.log1").append("" + power).append(new TranslatableComponent("battles.ability.buddycards.waiting_game.log2")), List.of(BuddycardBattleIcon.create(game.getCard(slot)), TextureBattleIcon.dividerIcon, BuddycardBattleIcon.create(game.getCard(slot)), TextureBattleIcon.addIcon(power))));
                game.updatePower();
            }
            return true;
        })).build());
        /*WHARE      */ registerCard(BASE_SET, 34, Rarity.UNCOMMON, 4, 0, new BattleAbility.Builder().add(BattleEvent.PLAYED.ability("travel_game", (game, slot, target, source) -> {
            int power = 0;
            for (int i: BattleEvent.Distribution.ALL.apply(slot, game))
                if(game.getCard(i) != null)
                    power++;
            if(power > 0) {
                game.turnPower[slot] += power;
                game.container.addLog(new BattleComponent(new TranslatableComponent("battles.ability.buddycards.travel_game.log1").append("" + power).append(new TranslatableComponent("battles.ability.buddycards.travel_game.log2")), List.of(BuddycardBattleIcon.create(game.getCard(slot)), TextureBattleIcon.dividerIcon, BuddycardBattleIcon.create(game.getCard(slot)), TextureBattleIcon.addIcon(power))));
                game.updatePower();
            }
            return true;
        })).build());
        /*AMBYSTOMA  */ registerCard(BASE_SET, 35, Rarity.RARE,     4, 3, new BattleAbility.Builder().add(BattleEvent.KILL.ability("axo-regeneration", (game, slot, target, source) -> {
            int power = 3 - game.turnPower[slot];
            if(power > 0) {
                game.turnPower[slot] += power;
                game.container.addLog(new BattleComponent(new TranslatableComponent("battles.ability.buddycards.axo-regeneration.log"), List.of(BuddycardBattleIcon.create(game.getCard(slot)), TextureBattleIcon.dividerIcon, BuddycardBattleIcon.create(game.getCard(slot)), TextureBattleIcon.addIcon(power))));
                game.updatePower();
            }
            return true;
        })).add(BattleEvent.PLAYED.ability("rare_blue", (game, slot, target, source) -> {
            if(BattleContainer.random.nextInt(10) == 0) {
                game.turnPower[slot] += 6;
                game.container.addLog(new BattleComponent(new TranslatableComponent("battles.ability.buddycards.rare_blue.log"), List.of(BuddycardBattleIcon.create(game.getCard(slot)), TextureBattleIcon.dividerIcon, BuddycardBattleIcon.create(game.getCard(slot)), TextureBattleIcon.addIcon(6))));
                game.updatePower();
            }
            return true;
        })).build());
        /*KART       */ registerCard(BASE_SET, 36, Rarity.RARE,     5, 3, new BattleAbility.Builder().add(BattleEvent.TURN.ability("railway", (game, slot, target, source) -> {
            for (int i: BattleEvent.Distribution.ADJACENT.apply(slot, game)) {
                if (game.getCard(i) == null) {
                    BuddycardItem b = game.moveCard(slot, i);
                    game.turnPower[i] += 2;
                    game.container.addLog(new BattleComponent(new TranslatableComponent("battles.ability.buddycards.railway.log"), List.of(BuddycardBattleIcon.create(b), TextureBattleIcon.dividerIcon, BuddycardBattleIcon.create(b), TextureBattleIcon.returnIcon, TextureBattleIcon.addIcon((2)))));
                    game.updatePower(i);
                    break;
                }
            }
            return true;
        })).add(BattleEvent.POWERED.ability("mine_crash", (game, slot, target, source) -> {
            if(game.turnPower[slot] >= 10)
                game.container.addLog(new BattleComponent(new TranslatableComponent("battles.ability.buddycards.mine_crash.log1").append(BattleGame.getOwner(slot) ? game.container.name2 : game.container.name1).append(new TranslatableComponent("battles.ability.buddycards.mine_crash.log2")), List.of(BuddycardBattleIcon.create(game.getCard(slot)), TextureBattleIcon.dividerIcon, BuddycardBattleIcon.create(game.getCard(slot)), TextureBattleIcon.damageIcon(10))));
            return true;
        })).build());

        //Register nether set
        /*BRIK     */ registerCard(NETHER_SET,  1, Rarity.COMMON,   4, 2, new BattleAbility.Builder().add(BattleEvent.PLAYED.ability("brick_toss", (game, slot, target, source) -> {
            int opp = BattleGame.opposite(slot);
            game.directAttack(opp, slot, 2);
            if (game.getCard(opp) != null) {
                game.container.addLog(new BattleComponent(new TranslatableComponent("battles.ability.buddycards.brick_toss.log1").append(new TranslatableComponent(game.getCard(opp).getDescriptionId())).append(new TranslatableComponent("battles.ability.buddycards.brick_toss.log2")), List.of(BuddycardBattleIcon.create(game.getCard(slot)), TextureBattleIcon.dividerIcon, BuddycardBattleIcon.create(game.getCard(slot)), TextureBattleIcon.damageIcon(2), BuddycardBattleIcon.create(game.getCard(opp)))));
                game.updatePower(opp);
            } else
                game.container.addLog(new BattleComponent(new TranslatableComponent("battles.ability.buddycards.brick_toss.log1").append(BattleGame.getOwner(slot) ? game.container.name2 : game.container.name1).append(new TranslatableComponent("battles.ability.buddycards.brick_toss.log2")), List.of(BuddycardBattleIcon.create(game.getCard(slot)), TextureBattleIcon.dividerIcon, BuddycardBattleIcon.create(game.getCard(slot)), TextureBattleIcon.damageIcon(2))));

            return true;
        })).build());
        /*OBBY     */ registerCard(NETHER_SET,  2, Rarity.COMMON,   6, 3, new BattleAbility.Builder().add(BattleEvent.TURN.ability("tough_stuff", (game, slot, target, source) -> {
            int power = 3 - game.turnPower[slot];
            if(power > 0) {
                game.turnPower[slot] += power;
                game.container.addLog(new BattleComponent(new TranslatableComponent("battles.ability.buddycards.tough_stuff.log"), List.of(BuddycardBattleIcon.create(game.getCard(slot)), TextureBattleIcon.dividerIcon, BuddycardBattleIcon.create(game.getCard(slot)), TextureBattleIcon.addIcon(power))));
                game.updatePower();
            }
            return true;
        })).build());
        /*BRITE    */ registerCard(NETHER_SET,  3, Rarity.COMMON,   4, 2, new BattleAbility.Builder().add(BattleEvent.DEATH.ability("radiant_glow", (game, slot, target, source) -> {
            List<IBattleIcon> icons = new ArrayList<>(List.of(BuddycardBattleIcon.create(game.getCard(slot)), TextureBattleIcon.dividerIcon));
            for (int i: BattleEvent.Distribution.ALL_ENEMY.apply(slot, game)) {
                if(game.container.getItem(BattleGame.translateFrom(i)).m_204117_(BuddycardsMisc.BCB_MONSTER)) {
                    game.directAttack(BattleGame.opposite(slot), slot, 1, false, false);
                    icons.add(BuddycardBattleIcon.create(game.getCard(i)));
                    icons.add(TextureBattleIcon.subtractIcon(1));
                }
            }
            if(icons.size() > 2) {
                game.container.addLog(new BattleComponent(new TranslatableComponent("battles.ability.buddycards.radiant_glow.log"), icons));
                game.updatePower();
            }
            return true;
        })).build());
        /*SLINKY   */ registerCard(NETHER_SET,  4, Rarity.COMMON,   4, 2, new BattleAbility.Builder().add(BattleEvent.DEATH.ability("bounce_back", (game, slot, target, source) -> {
            BuddycardItem returnedCard = game.returnCard(slot);
            if (returnedCard != null) {
                game.container.addLog(new BattleComponent(new TranslatableComponent("battles.ability.buddycards.bounce_back"), List.of(BuddycardBattleIcon.create(returnedCard), TextureBattleIcon.dividerIcon, BuddycardBattleIcon.create(returnedCard), TextureBattleIcon.returnIcon)));
                return false;
            }
            return true;
        })).build());
        /*BIG_PIG  */ registerCard(NETHER_SET,  5, Rarity.COMMON,   5, 3, new BattleAbility.Builder().add(BattleEvent.DEATH.ability("hearty_bacon", (game, slot, target, source) -> {
            if(BattleGame.getOwner(slot))
                game.container.health1+=2;
            else
                game.container.health2+=2;
            List<IBattleIcon> icons = new ArrayList<>(List.of(BuddycardBattleIcon.create(game.getCard(slot)), TextureBattleIcon.dividerIcon, TextureBattleIcon.healIcon(2)));
            for (int i : BattleEvent.Distribution.ROW.apply(slot, game)) {
                game.state[i].status = BattleStatusEffect.STRENGTH;
                icons.add(BuddycardBattleIcon.create(game.getCard(i)));
            }
            icons.add(TextureBattleIcon.statusIcon(BattleStatusEffect.STRENGTH));
            game.container.addLog(new BattleComponent(new TextComponent("").append(BattleGame.getOwner(slot) ? game.container.name1 : game.container.name2).append(new TranslatableComponent("battles.ability.buddycards.hearty_bacon.log")), icons));
            return true;
        })).build());
        /*SOL      */ registerCard(NETHER_SET,  6, Rarity.COMMON,   3, 2, new BattleAbility.Builder().add(BattleEvent.KILL.ability("soul_steal", (game, slot, target, source) -> {
            if(BattleGame.getOwner(slot))
                game.container.health1++;
            else
                game.container.health2++;
            game.container.addLog(new BattleComponent(new TextComponent("").append(BattleGame.getOwner(slot) ? game.container.name1 : game.container.name2).append(new TranslatableComponent("battles.ability.buddycards.soul_steal.log")), List.of(BuddycardBattleIcon.create(game.getCard(slot)), TextureBattleIcon.dividerIcon, TextureBattleIcon.healIcon(1))));
            return true;
        })).build());
        /*BASISAL  */ registerCard(NETHER_SET,  7, Rarity.COMMON,   4, 2, DEFAULT_NO_ABILITIES);
        /*VINNIE   */ registerCard(NETHER_SET,  8, Rarity.COMMON,   4, 3, DEFAULT_NO_ABILITIES);
        /*SHROOM   */ registerCard(NETHER_SET,  9, Rarity.COMMON,   3, 2, DEFAULT_NO_ABILITIES);
        /*WARP_NYE */ registerCard(NETHER_SET, 10, Rarity.COMMON,   2, 1, DEFAULT_NO_ABILITIES);
        /*FYA      */ registerCard(NETHER_SET, 11, Rarity.COMMON,   2, 1, new BattleAbility.Builder().add(BattleEvent.DAMAGED.ability("spicy_flames", (game, slot, target, source) -> {
            if(source != slot && game.getCard(source) != null) {
                game.state[source].status = BattleStatusEffect.FIRE;
                game.container.addLog(new BattleComponent(new TranslatableComponent(game.getCard(source).getDescriptionId()).append(new TranslatableComponent("battles.ability.buddycards.spicy_flames.log")), List.of(BuddycardBattleIcon.create(game.getCard(slot)), TextureBattleIcon.dividerIcon, BuddycardBattleIcon.create(game.getCard(source)), TextureBattleIcon.statusIcon(BattleStatusEffect.FIRE))));
            }
            return true;
        })).build());
        /*MAGMA    */ registerCard(NETHER_SET, 12, Rarity.COMMON,   6, 1, new BattleAbility.Builder().add(BattleEvent.PLAYED.ability("meltdown", (game, slot, target, source) -> {
            List<IBattleIcon> icons = new ArrayList<>(List.of(BuddycardBattleIcon.create(game.getCard(slot)), TextureBattleIcon.dividerIcon));
            for (int i : BattleEvent.Distribution.ALL.apply(slot, game)) {
                if (!game.container.getItem(BattleGame.translateFrom(i)).m_204117_(BuddycardsMisc.BCB_FIRE)) {
                    game.state[i].status = BattleStatusEffect.FIRE;
                    icons.add(BuddycardBattleIcon.create(game.getCard(i)));
                }
            }
            icons.add(TextureBattleIcon.statusIcon(BattleStatusEffect.FIRE));
            if (icons.size() > 2) {
                game.container.addLog(new BattleComponent(new TranslatableComponent("battles.ability.buddycards.meltdown.log"), icons));
                game.updatePower();
            }
            return true;
        })).build());
        /*SKULLY   */ registerCard(NETHER_SET, 13, Rarity.UNCOMMON, 5, 2, DEFAULT_NO_ABILITIES);
        /*BLAZT    */ registerCard(NETHER_SET, 14, Rarity.UNCOMMON, 6, 2, new BattleAbility.Builder().add(BattleEvent.ACTIVATED.ability("flaming_shot", (game, slot, target, source) -> {
            int opp = BattleGame.opposite(slot);
            if(game.getCard(opp) != null) {
                game.state[opp].status = BattleStatusEffect.FIRE;
                game.directAttack(opp, slot, 1, false, false);
                game.container.addLog(new BattleComponent(new TranslatableComponent(game.getCard(source).getDescriptionId()).append(new TranslatableComponent("battles.ability.buddycards.flaming_shot.log")), List.of(BuddycardBattleIcon.create(game.getCard(slot)), TextureBattleIcon.dividerIcon, BuddycardBattleIcon.create(game.getCard(opp)), TextureBattleIcon.statusIcon(BattleStatusEffect.FIRE), TextureBattleIcon.subtractIcon(1))));
                game.updatePower(opp);
            }
            return true;
        })).build());
        /*QUAZI    */ registerCard(NETHER_SET, 15, Rarity.UNCOMMON, 4, 2, new BattleAbility.Builder().add(BattleEvent.POWERED.ability("subtraction", (game, slot, target, source) -> {
            if(target != slot && game.getCard(target) != null && game.isP1() == BattleGame.getOwner(slot)) {
                game.directAttack(target, slot, 1, false, false);
                game.container.addLog(new BattleComponent(new TranslatableComponent(game.getCard(target).getDescriptionId()).append(new TranslatableComponent("battles.ability.buddycards.subtraction.log")), List.of(BuddycardBattleIcon.create(game.getCard(slot)), TextureBattleIcon.dividerIcon, BuddycardBattleIcon.create(game.getCard(target)), TextureBattleIcon.subtractIcon(1))));
                game.updatePower(target);
            }
            return true;
        })).build());
        /*HOT_POT  */ registerCard(NETHER_SET, 16, Rarity.UNCOMMON, 3, 2, new BattleAbility.Builder().add(BattleEvent.DEATH.ability("fire_resistance", (game, slot, target, source) -> {
            List<IBattleIcon> icons = new ArrayList<>(List.of(BuddycardBattleIcon.create(game.getCard(slot)), TextureBattleIcon.dividerIcon));
            List<Integer> cards = new ArrayList<>();
            for (int i: BattleEvent.Distribution.ADJACENT.apply(slot, game)) {
                if(game.getCard(i) != null && game.state[i].status.equals(BattleStatusEffect.FIRE)) {
                    game.state[i].status = BattleStatusEffect.EMPTY;
                    game.turnPower[i]+=2;
                    icons.add(TextureBattleIcon.statusIcon(BattleStatusEffect.EMPTY));
                    icons.add(TextureBattleIcon.addIcon(2));
                    cards.add(i);
                }
            }
            if(icons.size() > 2) {
                game.container.addLog(new BattleComponent(new TranslatableComponent("battles.ability.buddycards.fire_resistance.log"), icons));
                for(int i : cards)
                    game.updatePower(i);
            }
            return true;
        })).build());
        /*CRYBABY  */ registerCard(NETHER_SET, 17, Rarity.UNCOMMON, 6, 4, DEFAULT_NO_ABILITIES);
        /*FYO      */ registerCard(NETHER_SET, 18, Rarity.UNCOMMON, 4, 1, new BattleAbility.Builder().add(BattleEvent.DAMAGED.ability("spirit_flames", (game, slot, target, source) -> {
            if(source != slot && game.getCard(source) != null) {
                game.state[source].status = BattleStatusEffect.FIRE;
                game.turnPower[source] -= 1;
                game.container.addLog(new BattleComponent(new TranslatableComponent(game.getCard(source).getDescriptionId()).append(new TranslatableComponent("battles.ability.buddycards.spirit_flames.log")), List.of(BuddycardBattleIcon.create(game.getCard(slot)), TextureBattleIcon.dividerIcon, BuddycardBattleIcon.create(game.getCard(source)), TextureBattleIcon.statusIcon(BattleStatusEffect.FIRE), TextureBattleIcon.subtractIcon(1))));
            }
            return true;
        })).build());
        /*LAMP     */ registerCard(NETHER_SET, 19, Rarity.UNCOMMON, 5, 2, new BattleAbility.Builder().add(BattleEvent.PLAYED.ability("soul_light", (game, slot, target, source) -> {
            List<IBattleIcon> icons = new ArrayList<>(List.of(BuddycardBattleIcon.create(game.getCard(slot)), TextureBattleIcon.dividerIcon));
            for (int i: BattleEvent.Distribution.ALL_ENEMY.apply(slot, game)) {
                if(game.container.getItem(BattleGame.translateFrom(i)).m_204117_(BuddycardsMisc.BCB_MONSTER)) {
                    game.directAttack(BattleGame.opposite(slot), slot, 2, false, false);
                    icons.add(BuddycardBattleIcon.create(game.getCard(i)));
                    icons.add(TextureBattleIcon.subtractIcon(2));
                }
            }
            if(icons.size() > 2) {
                game.container.addLog(new BattleComponent(new TranslatableComponent("battles.ability.buddycards.soul_light.log"), icons));
                game.updatePower();
            }
            return true;
        })).build());
        /*GHOST    */ registerCard(NETHER_SET, 20, Rarity.UNCOMMON, 8, 2, DEFAULT_NO_ABILITIES);
        /*STRIDE   */ registerCard(NETHER_SET, 21, Rarity.UNCOMMON, 5, 3, DEFAULT_NO_ABILITIES);
        /*BARTENDER*/ registerCard(NETHER_SET, 22, Rarity.RARE,     6, 3, DEFAULT_NO_ABILITIES);
        /*PORT     */ registerCard(NETHER_SET, 23, Rarity.RARE,     10,6, DEFAULT_NO_ABILITIES);
        /*TRIPLE   */ registerCard(NETHER_SET, 24, Rarity.RARE,     10,4, DEFAULT_NO_ABILITIES);
        /*ANKER    */ registerCard(NETHER_SET, 25, Rarity.RARE,     7, 4, DEFAULT_NO_ABILITIES);
        /*BEAKER   */ registerCard(NETHER_SET, 26, Rarity.EPIC,     10,5, DEFAULT_NO_ABILITIES);
        /*N_RITE   */ registerCard(NETHER_SET, 27, Rarity.EPIC,     9, 4, DEFAULT_NO_ABILITIES);

        //Register end set
        /*ENROK    */ registerCard(END_SET,  1, Rarity.COMMON,   2, 1, new BattleAbility.Builder().add(BattleEvent.PLAYED.ability("cheese_toss", (game, slot, target, source) -> {
            if(BattleContainer.random.nextBoolean()) {
                int opp = BattleGame.opposite(slot);
                game.directAttack(opp, slot, 1);
                if (game.getCard(opp) != null) {
                    game.container.addLog(new BattleComponent(new TranslatableComponent("battles.ability.buddycards.cheese_toss.log1").append(new TranslatableComponent(game.getCard(opp).getDescriptionId())).append(new TranslatableComponent("battles.ability.buddycards.cheese_toss.log2")), List.of(BuddycardBattleIcon.create(game.getCard(slot)), TextureBattleIcon.dividerIcon, BuddycardBattleIcon.create(game.getCard(slot)), TextureBattleIcon.damageIcon(2), BuddycardBattleIcon.create(game.getCard(opp)))));
                    game.updatePower(opp);
                } else
                    game.container.addLog(new BattleComponent(new TranslatableComponent("battles.ability.buddycards.cheese_toss.log1").append(BattleGame.getOwner(slot) ? game.container.name2 : game.container.name1).append(new TranslatableComponent("battles.ability.buddycards.cheese_toss.log2")), List.of(BuddycardBattleIcon.create(game.getCard(slot)), TextureBattleIcon.dividerIcon, BuddycardBattleIcon.create(game.getCard(slot)), TextureBattleIcon.damageIcon(2))));
            }
            return true;
        })).build());
        /*CHESTER  */ registerCard(END_SET,  2, Rarity.COMMON,   2, 1, DEFAULT_NO_ABILITIES);
        /*SCALES   */ registerCard(END_SET,  3, Rarity.COMMON,   2, 1, DEFAULT_NO_ABILITIES);
        /*VWOOP    */ registerCard(END_SET,  4, Rarity.COMMON,   2, 1, DEFAULT_NO_ABILITIES);
        /*SILVER   */ registerCard(END_SET,  5, Rarity.COMMON,   2, 1, DEFAULT_NO_ABILITIES);
        /*LIBRARIA */ registerCard(END_SET,  6, Rarity.COMMON,   2, 1, DEFAULT_NO_ABILITIES);
        /*AIROW    */ registerCard(END_SET,  7, Rarity.COMMON,   2, 1, DEFAULT_NO_ABILITIES);
        /*WEBSTER  */ registerCard(END_SET,  8, Rarity.COMMON,   2, 1, DEFAULT_NO_ABILITIES);
        /*PERL     */ registerCard(END_SET,  9, Rarity.COMMON,   2, 1, DEFAULT_NO_ABILITIES);
        /*FROOTY   */ registerCard(END_SET, 10, Rarity.COMMON,   2, 1, DEFAULT_NO_ABILITIES);
        /*ROD      */ registerCard(END_SET, 11, Rarity.COMMON,   2, 1, DEFAULT_NO_ABILITIES);
        /*ROKIT    */ registerCard(END_SET, 12, Rarity.COMMON,   2, 1, DEFAULT_NO_ABILITIES);
        /*WALLY    */ registerCard(END_SET, 13, Rarity.UNCOMMON, 2, 1, DEFAULT_NO_ABILITIES);
        /*FLOUER   */ registerCard(END_SET, 14, Rarity.UNCOMMON, 2, 1, DEFAULT_NO_ABILITIES);
        /*LINGERER */ registerCard(END_SET, 15, Rarity.UNCOMMON, 2, 1, DEFAULT_NO_ABILITIES);
        /*CHORUS   */ registerCard(END_SET, 16, Rarity.UNCOMMON, 2, 1, DEFAULT_NO_ABILITIES);
        /*SHULK    */ registerCard(END_SET, 17, Rarity.UNCOMMON, 2, 1, DEFAULT_NO_ABILITIES);
        /*MENACE   */ registerCard(END_SET, 18, Rarity.UNCOMMON, 2, 1, DEFAULT_NO_ABILITIES);
        /*VISIONARY*/ registerCard(END_SET, 19, Rarity.UNCOMMON, 2, 1, DEFAULT_NO_ABILITIES);
        /*SLEEPER  */ registerCard(END_SET, 20, Rarity.UNCOMMON, 2, 1, DEFAULT_NO_ABILITIES);
        /*SPECTRE  */ registerCard(END_SET, 21, Rarity.UNCOMMON, 2, 1, DEFAULT_NO_ABILITIES);
        /*PACKAGE  */ registerCard(END_SET, 22, Rarity.RARE,     2, 1, DEFAULT_NO_ABILITIES);
        /*BROK     */ registerCard(END_SET, 23, Rarity.RARE,     2, 1, DEFAULT_NO_ABILITIES);
        /*PORTZ    */ registerCard(END_SET, 24, Rarity.RARE,     2, 1, DEFAULT_NO_ABILITIES);
        /*CRYSTAL  */ registerCard(END_SET, 25, Rarity.RARE,     2, 1, DEFAULT_NO_ABILITIES);
        /*GLIDER   */ registerCard(END_SET, 26, Rarity.EPIC,     2, 1, DEFAULT_NO_ABILITIES);
        /*ENDER    */ registerCard(END_SET, 27, Rarity.EPIC,     2, 1, DEFAULT_NO_ABILITIES);

        //Register seasonal set
        /*HALLOWEEN_YIN    */ registerReprintCard(HOLIDAY_SET, 1, Rarity.UNCOMMON, HALLOWEEN_BUDDYCARD_REQUIREMENT,4, 2, new BattleAbility.Builder().add(BattleEvent.FIGHT.ability("lazy_cat", (game, slot, target, source) -> {
            if(game.turnPower[slot] < 2 && game.isP1() == BattleGame.getOwner(slot)) {
                game.turnPower[slot] += 2;
                game.container.addLog(new BattleComponent(new TranslatableComponent("battles.ability.buddycards.lazy_cat.log"), List.of(BuddycardBattleIcon.create(game.getCard(slot)), TextureBattleIcon.dividerIcon, BuddycardBattleIcon.create(game.getCard(slot)), TextureBattleIcon.addIcon(2))));
                game.updatePower(slot);
                return false;
            }
            return true;
        })).build(), BASE_SET, 14);
        /*HALLOWEEN_SPOOPY */ registerReprintCard(HOLIDAY_SET, 2, Rarity.UNCOMMON, HALLOWEEN_BUDDYCARD_REQUIREMENT,4, 1, new BattleAbility.Builder().add(BattleEvent.FIGHT.ability("spook_em", (game, slot, target, source) -> {
            if(target != slot && game.getCard(target) != null && game.isP1() == BattleGame.getOwner(slot)) {
                game.directAttack(target, slot, 1, false, false);
                game.container.addLog(new BattleComponent(new TranslatableComponent(game.getCard(target).getDescriptionId()).append(new TranslatableComponent("battles.ability.buddycards.spook_em.log")), List.of(BuddycardBattleIcon.create(game.getCard(slot)), TextureBattleIcon.dividerIcon, BuddycardBattleIcon.create(game.getCard(target)), TextureBattleIcon.subtractIcon(1))));
                game.updatePower(target);
            }
            return true;
        })).build(), BASE_SET, 21);
        /*HALLOWEEN_VWOOP  */ registerReprintCard(HOLIDAY_SET, 3, Rarity.COMMON, HALLOWEEN_BUDDYCARD_REQUIREMENT, 2, 1, DEFAULT_NO_ABILITIES, END_SET, 4);
        /*CHRISTMAS_COLE   */ registerReprintCard(HOLIDAY_SET, 4, Rarity.COMMON, CHRISTMAS_BUDDYCARD_REQUIREMENT, 3, 2, new BattleAbility.Builder().add(BattleEvent.DEATH.ability("stoke_flames", (game, slot, target, source) -> {
            List<IBattleIcon> icons = new ArrayList<>(List.of(BuddycardBattleIcon.create(game.getCard(slot)), TextureBattleIcon.dividerIcon));
            for (int i: BattleEvent.Distribution.ROW.apply(slot, game)) {
                if(game.container.getItem(BattleGame.translateFrom(i)).m_204117_(BuddycardsMisc.BCB_FIRE)) {
                    game.turnPower[i]++;
                    icons.add(BuddycardBattleIcon.create(game.getCard(i)));
                    icons.add(TextureBattleIcon.addIcon(1));
                }
            }
            if(icons.size() > 2) {
                game.container.addLog(new BattleComponent(new TranslatableComponent("battles.ability.buddycards.stoke_flames.log"), icons));
                game.updatePower();
            }
            return true;
        })).build(), BASE_SET, 4);
        /*CHRISTMAS_CHESTER*/ registerReprintCard(HOLIDAY_SET, 5, Rarity.COMMON, CHRISTMAS_BUDDYCARD_REQUIREMENT, 2, 1, DEFAULT_NO_ABILITIES, END_SET, 2);
        /*CHRISTMAS_CHOKO  */ registerCard(HOLIDAY_SET, 6, Rarity.UNCOMMON, CHRISTMAS_BUDDYCARD_REQUIREMENT, 2, 1, DEFAULT_NO_ABILITIES);

        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static final BuddycardSet BASE_SET = new BuddycardSet("base");
    public static final BuddycardSet NETHER_SET = new BuddycardSet("nether");
    public static final BuddycardSet END_SET = new BuddycardSet("end");
    public static final BuddycardSet HOLIDAY_SET = new BuddycardSet("holiday");

    //Default parameters
    public static final Item.Properties DEFAULT_PROPERTIES = new Item.Properties().tab(Buddycards.TAB);
    public static final Item.Properties DEFAULT_UNCOMMON_PROPERTIES = new Item.Properties().tab(Buddycards.TAB).rarity(Rarity.UNCOMMON);
    public static final Item.Properties UNCOMMON_TOOL_PROPERTIES = new Item.Properties().tab(Buddycards.TAB).rarity(Rarity.UNCOMMON).stacksTo(1);
    public static final Item.Properties DEFAULT_RARE_PROPERTIES = new Item.Properties().tab(Buddycards.TAB).rarity(Rarity.RARE);
    public static final Item.Properties DEFAULT_EPIC_PROPERTIES = new Item.Properties().tab(Buddycards.TAB).rarity(Rarity.EPIC);
    public static final Item.Properties DEFAULT_CARD_PROPERTIES = new Item.Properties().tab(Buddycards.CARDS_TAB);
    public static final Item.Properties DEFAULT_PACK_PROPERTIES = new Item.Properties().tab(Buddycards.TAB).stacksTo(16).rarity(Rarity.UNCOMMON);
    public static final Item.Properties RARE_PACK_PROPERTIES = new Item.Properties().tab(Buddycards.TAB).stacksTo(16).rarity(Rarity.RARE);
    public static final Item.Properties DEFAULT_BINDER_PROPERTIES = new Item.Properties().tab(Buddycards.TAB).stacksTo(1).rarity(Rarity.UNCOMMON);
    public static final Item.Properties DEFAULT_CURIO_PROPERTIES = new Item.Properties().tab(Buddycards.TAB).stacksTo(1).rarity(Rarity.UNCOMMON);
    public static final Item.Properties BUDDYSTEEL_MEDAL_PROPERTIES = new Item.Properties().tab(Buddycards.TAB).stacksTo(1).rarity(Rarity.COMMON);

    public static final BuddycardRequirement DEFAULT_BUDDYCARD_REQUIREMENT = () -> true;
    public static final BuddycardRequirement HALLOWEEN_BUDDYCARD_REQUIREMENT = () -> Calendar.getInstance().get(Calendar.MONTH) == Calendar.OCTOBER && Calendar.getInstance().get(Calendar.DATE) >= 29;
    public static final BuddycardRequirement CHRISTMAS_BUDDYCARD_REQUIREMENT = () -> Calendar.getInstance().get(Calendar.MONTH) == Calendar.DECEMBER && Calendar.getInstance().get(Calendar.DATE) >= 24 && Calendar.getInstance().get(Calendar.DATE) <= 26;

    public static final SimpleWeightedRandomList<Rarity> DEFAULT_RARITY_WEIGHTS = SimpleWeightedRandomList.<Rarity>builder()
            .add(Rarity.COMMON, 24)
            .add(Rarity.UNCOMMON, 12)
            .add(Rarity.RARE, 3)
            .add(Rarity.EPIC, 1)
            .build();

    //Lack of abilities
    public static final ImmutableListMultimap<BattleEvent, BattleAbility> DEFAULT_NO_ABILITIES = ImmutableListMultimap.of();

    //Packs
    public static final RegistryObject<BuddycardPackItem> PACK_BASE = ITEMS.register("buddycard_pack_base", () -> new BuddycardSetPackItem(DEFAULT_BUDDYCARD_REQUIREMENT, BASE_SET, 4, 1, DEFAULT_RARITY_WEIGHTS, DEFAULT_PACK_PROPERTIES));
    public static final RegistryObject<BuddycardPackItem> PACK_NETHER = ITEMS.register("buddycard_pack_nether", () -> new BuddycardSetPackItem(DEFAULT_BUDDYCARD_REQUIREMENT, NETHER_SET, 4, 1, DEFAULT_RARITY_WEIGHTS, DEFAULT_PACK_PROPERTIES));
    public static final RegistryObject<BuddycardPackItem> PACK_END = ITEMS.register("buddycard_pack_end", () -> new BuddycardSetPackItem(DEFAULT_BUDDYCARD_REQUIREMENT, END_SET, 4, 1, DEFAULT_RARITY_WEIGHTS, DEFAULT_PACK_PROPERTIES));
    public static final RegistryObject<BuddycardPackItem> MYSTERY_PACK = ITEMS.register("buddycard_pack_mystery", () -> new MysteryBuddycardPackItem(DEFAULT_BUDDYCARD_REQUIREMENT, 4, 1, DEFAULT_RARITY_WEIGHTS, false, RARE_PACK_PROPERTIES));
    //Binders
    public static final RegistryObject<BuddycardBinderItem> BINDER_BASE = ITEMS.register("buddycard_binder_base", () -> new BuddycardBinderItem(DEFAULT_BUDDYCARD_REQUIREMENT, DEFAULT_BINDER_PROPERTIES, BASE_SET));
    public static final RegistryObject<BuddycardBinderItem> BINDER_NETHER = ITEMS.register("buddycard_binder_nether", () -> new BuddycardBinderItem(DEFAULT_BUDDYCARD_REQUIREMENT, DEFAULT_BINDER_PROPERTIES, NETHER_SET));
    public static final RegistryObject<BuddycardBinderItem> BINDER_END = ITEMS.register("buddycard_binder_end", () -> new BuddycardBinderItem(DEFAULT_BUDDYCARD_REQUIREMENT, DEFAULT_BINDER_PROPERTIES, END_SET));
    public static final RegistryObject<EnderBinderItem> ENDER_BINDER = ITEMS.register("ender_buddycard_binder", () -> new EnderBinderItem(DEFAULT_BINDER_PROPERTIES));
    //Deckboxes
    public static final RegistryObject<DeckboxItem> BUDDYSTEEL_DECKBOX = ITEMS.register("buddysteel_deckbox", () -> new DeckboxItem(DEFAULT_BINDER_PROPERTIES));
    public static final RegistryObject<DeckboxItem> LUMINIS_DECKBOX = ITEMS.register("luminis_deckbox", () -> new DeckboxItem(DEFAULT_BINDER_PROPERTIES));
    public static final RegistryObject<DeckboxItem> ZYLEX_DECKBOX = ITEMS.register("zylex_deckbox", () -> new DeckboxItem(DEFAULT_BINDER_PROPERTIES));
    //Playmats
    public static final RegistryObject<BlockItem> PLAYMAT_BASE = ITEMS.register("playmat_base", () -> new SetBasedBlockItem(BuddycardsBlocks.PLAYMAT_BASE.get(), DEFAULT_PROPERTIES, BASE_SET));
    public static final RegistryObject<BlockItem> PLAYMAT_NETHER = ITEMS.register("playmat_nether", () -> new SetBasedBlockItem(BuddycardsBlocks.PLAYMAT_NETHER.get(), DEFAULT_PROPERTIES, NETHER_SET));
    public static final RegistryObject<BlockItem> PLAYMAT_END = ITEMS.register("playmat_end", () -> new SetBasedBlockItem(BuddycardsBlocks.PLAYMAT_END.get(), DEFAULT_PROPERTIES, END_SET));
    //Buddysteel Items
    public static final RegistryObject<Item> BUDDYSTEEL_BLEND = ITEMS.register("buddysteel_blend", () -> new Item(DEFAULT_PROPERTIES));
    public static final RegistryObject<Item> BUDDYSTEEL_INGOT = ITEMS.register("buddysteel_ingot", () -> new Item(DEFAULT_PROPERTIES));
    public static final RegistryObject<Item> BUDDYSTEEL_NUGGET = ITEMS.register("buddysteel_nugget", () -> new Item(DEFAULT_PROPERTIES));
    public static final RegistryObject<Item> BUDDYSTEEL_BLOCK = ITEMS.register("buddysteel_block", () -> new BlockItem(BuddycardsBlocks.BUDDYSTEEL_BLOCK.get(), DEFAULT_PROPERTIES));
    public static final RegistryObject<BuddysteelPowerMeterItem> BUDDYSTEEL_POWER_METER = ITEMS.register("buddysteel_power_meter", () -> new BuddysteelPowerMeterItem(DEFAULT_UNCOMMON_PROPERTIES));
    public static final RegistryObject<Item> BUDDYSTEEL_HELMET = ITEMS.register("buddysteel_helmet", () -> new BuddycardsArmorItem(BuddycardsArmorMaterial.BUDDYSTEEL, EquipmentSlot.HEAD));
    public static final RegistryObject<Item> BUDDYSTEEL_CHESTPLATE = ITEMS.register("buddysteel_chestplate", () -> new BuddycardsArmorItem(BuddycardsArmorMaterial.BUDDYSTEEL, EquipmentSlot.CHEST));
    public static final RegistryObject<Item> BUDDYSTEEL_LEGGINGS = ITEMS.register("buddysteel_leggings", () -> new BuddycardsArmorItem(BuddycardsArmorMaterial.BUDDYSTEEL, EquipmentSlot.LEGS));
    public static final RegistryObject<Item> BUDDYSTEEL_BOOTS = ITEMS.register("buddysteel_boots", () -> new BuddycardsArmorItem(BuddycardsArmorMaterial.BUDDYSTEEL, EquipmentSlot.FEET));
    public static final RegistryObject<Item> BUDDYSTEEL_SWORD = ITEMS.register("buddysteel_sword", () -> new SwordItem(BuddycardsToolTier.BUDDYSTEEL,3, -2.4F, UNCOMMON_TOOL_PROPERTIES));
    public static final RegistryObject<Item> BUDDYSTEEL_SHOVEL = ITEMS.register("buddysteel_shovel", () -> new ShovelItem(BuddycardsToolTier.BUDDYSTEEL,1.5F, -3F, UNCOMMON_TOOL_PROPERTIES));
    public static final RegistryObject<Item> BUDDYSTEEL_PICKAXE = ITEMS.register("buddysteel_pickaxe", () -> new PickaxeItem(BuddycardsToolTier.BUDDYSTEEL,1, -2.8F, UNCOMMON_TOOL_PROPERTIES));
    public static final RegistryObject<Item> BUDDYSTEEL_AXE = ITEMS.register("buddysteel_axe", () -> new AxeItem(BuddycardsToolTier.BUDDYSTEEL,6, -3.1F, UNCOMMON_TOOL_PROPERTIES));
    public static final RegistryObject<Item> BUDDYSTEEL_HOE = ITEMS.register("buddysteel_hoe", () -> new HoeItem(BuddycardsToolTier.BUDDYSTEEL,-2, -1F, UNCOMMON_TOOL_PROPERTIES));
    //Luminis Items
    public static final RegistryObject<BlockItem> LUMINIS_ORE = ITEMS.register("luminis_ore", () -> new BlockItem(BuddycardsBlocks.LUMINIS_ORE.get(), DEFAULT_PROPERTIES));
    public static final RegistryObject<BlockItem> DEEPSLATE_LUMINIS_ORE = ITEMS.register("deepslate_luminis_ore", () -> new BlockItem(BuddycardsBlocks.DEEPSLATE_LUMINIS_ORE.get(), DEFAULT_PROPERTIES));
    public static final RegistryObject<Item> LUMINIS_CRYSTAL = ITEMS.register("luminis_crystal", () -> new Item(DEFAULT_PROPERTIES));
    public static final RegistryObject<BlockItem> LUMINIS_CRYSTAL_BLOCK = ITEMS.register("luminis_crystal_block", () -> new BlockItem(BuddycardsBlocks.LUMINIS_CRYSTAL_BLOCK.get(), DEFAULT_PROPERTIES));
    public static final RegistryObject<Item> LUMINIS = ITEMS.register("luminis", () -> new Item(DEFAULT_PROPERTIES));
    public static final RegistryObject<BlockItem> LUMINIS_BLOCK = ITEMS.register("luminis_block", () -> new BlockItem(BuddycardsBlocks.LUMINIS_BLOCK.get(), DEFAULT_PROPERTIES));
    public static final RegistryObject<Item> LUMINIS_PANEL = ITEMS.register("luminis_panel", () -> new Item(DEFAULT_PROPERTIES));
    public static final RegistryObject<BlockItem> LUMINIS_PANELS = ITEMS.register("luminis_panels", () -> new BlockItem(BuddycardsBlocks.LUMINIS_PANELS.get(), DEFAULT_PROPERTIES));
    public static final RegistryObject<Item> CRIMSON_LUMINIS = ITEMS.register("crimson_luminis", () -> new Item(DEFAULT_PROPERTIES));
    public static final RegistryObject<BlockItem> CRIMSON_LUMINIS_BLOCK = ITEMS.register("crimson_luminis_block", () -> new BlockItem(BuddycardsBlocks.CRIMSON_LUMINIS_BLOCK.get(), DEFAULT_PROPERTIES));
    public static final RegistryObject<BlockItem> KINETIC_CHAMBER = ITEMS.register("kinetic_chamber", () -> new DescriptionBlockItem(BuddycardsBlocks.KINETIC_CHAMBER.get(), DEFAULT_PROPERTIES));
    public static final RegistryObject<Item> LUMINIS_HELMET = ITEMS.register("luminis_helmet", () -> new BuddycardsArmorItem(BuddycardsArmorMaterial.LUMINIS, EquipmentSlot.HEAD));
    public static final RegistryObject<Item> LUMINIS_PICKAXE = ITEMS.register("luminis_pickaxe", () -> new PickaxeItem(BuddycardsToolTier.LUMINIS,1, -2.8F, UNCOMMON_TOOL_PROPERTIES));
    public static final RegistryObject<Item> LUMINIS_RING = ITEMS.register("luminis_ring", () -> new DescriptionItem(DEFAULT_CURIO_PROPERTIES));
    //Zylex Items
    public static final RegistryObject<Item> ZYLEX = ITEMS.register("zylex", () -> new Item(DEFAULT_PROPERTIES));
    public static final RegistryObject<Item> ZYLEX_NUGGET = ITEMS.register("zylex_nugget", () -> new Item(DEFAULT_PROPERTIES));
    public static final RegistryObject<BlockItem> ZYLEX_BLOCK = ITEMS.register("zylex_block", () -> new BlockItem(BuddycardsBlocks.ZYLEX_BLOCK.get(), DEFAULT_PROPERTIES));
    public static final RegistryObject<Item> VOID_ZYLEX = ITEMS.register("void_zylex", () -> new Item(DEFAULT_PROPERTIES));
    public static final RegistryObject<BlockItem> VOID_ZYLEX_BLOCK = ITEMS.register("void_zylex_block", () -> new BlockItem(BuddycardsBlocks.VOID_ZYLEX_BLOCK.get(), DEFAULT_PROPERTIES));
    public static final RegistryObject<Item> ZYLEX_BOOTS = ITEMS.register("zylex_boots", () -> new BuddycardsArmorItem(BuddycardsArmorMaterial.ZYLEX, EquipmentSlot.FEET));
    public static final RegistryObject<Item> ZYLEX_HOE = ITEMS.register("zylex_hoe", () -> new HoeItem(BuddycardsToolTier.ZYLEX,-2, -1F, UNCOMMON_TOOL_PROPERTIES));
    public static final RegistryObject<Item> ZYLEX_RING = ITEMS.register("zylex_ring", () -> new DescriptionItem(DEFAULT_CURIO_PROPERTIES));
    public static final RegistryObject<ZylexPowerMeterItem> ZYLEX_POWER_METER = ITEMS.register("zylex_power_meter", () -> new ZylexPowerMeterItem(DEFAULT_RARE_PROPERTIES));
    //Medals
    public static final RegistryObject<BlankBuddysteelMedalItem> BLANK_BUDDYSTEEL_MEDAL = ITEMS.register("blank_buddysteel_medal", () -> new BlankBuddysteelMedalItem(BUDDYSTEEL_MEDAL_PROPERTIES));
    public static final RegistryObject<BuddysteelSetMedalItem> MEDAL_BASE = ITEMS.register("buddysteel_medal_base", () -> new BuddysteelSetMedalItem(DEFAULT_BUDDYCARD_REQUIREMENT, MedalTypes.BASE_SET, BASE_SET, DEFAULT_CURIO_PROPERTIES));
    public static final RegistryObject<BuddysteelSetMedalItem> MEDAL_NETHER = ITEMS.register("buddysteel_medal_nether", () -> new BuddysteelSetMedalItem(DEFAULT_BUDDYCARD_REQUIREMENT, MedalTypes.NETHER_SET, NETHER_SET, DEFAULT_CURIO_PROPERTIES));
    public static final RegistryObject<BuddysteelSetMedalItem> MEDAL_END = ITEMS.register("buddysteel_medal_end", () -> new BuddysteelSetMedalItem(DEFAULT_BUDDYCARD_REQUIREMENT, MedalTypes.END_SET, END_SET, DEFAULT_CURIO_PROPERTIES));
    //Grading Sleeves
    public static final RegistryObject<GradingSleeveItem> GRADING_SLEEVE = ITEMS.register("grading_sleeve", () -> new GradingSleeveItem(DEFAULT_PROPERTIES, new float[]{0.4f, 0.3f, 0.225f, 0.073f}));
    public static final RegistryObject<GradingSleeveItem> GOLDEN_GRADING_SLEEVE = ITEMS.register("golden_grading_sleeve", () -> new GradingSleeveItem(DEFAULT_UNCOMMON_PROPERTIES, new float[]{0.1f, 0.4f, 0.3f, 0.195f}));
    public static final RegistryObject<GradingSleeveItem> CREATIVE_GRADING_SLEEVE = ITEMS.register("creative_grading_sleeve", () -> new CreativeGradingSleeveItem(DEFAULT_EPIC_PROPERTIES, new float[]{0.1f, 0.4f, 0.3f, 0.19f}));
    public static final RegistryObject<LuminisSleeveItem> LUMINIS_SLEEVE = ITEMS.register("luminis_sleeve", () -> new LuminisSleeveItem(DEFAULT_RARE_PROPERTIES));
    public static final RegistryObject<GradingSleeveItem> ZYLEX_GRADING_SLEEVE = ITEMS.register("zylex_grading_sleeve", () -> new GradingSleeveItem(DEFAULT_RARE_PROPERTIES, new float[]{0, 0, 0.25f, 0.6f}));
    public static final RegistryObject<BattleSleeveItem> BATTLE_SLEEVE = ITEMS.register("battle_sleeve", () -> new BattleSleeveItem(DEFAULT_PROPERTIES));
    //Card Display Items
    public static final RegistryObject<BlockItem> OAK_CARD_DISPLAY_ITEM = ITEMS.register("oak_card_display", () -> new BlockItem(BuddycardsBlocks.OAK_CARD_DISPLAY.get(), DEFAULT_PROPERTIES));
    public static final RegistryObject<BlockItem> SPRUCE_CARD_DISPLAY_ITEM = ITEMS.register("spruce_card_display", () -> new BlockItem(BuddycardsBlocks.SPRUCE_CARD_DISPLAY.get(), DEFAULT_PROPERTIES));
    public static final RegistryObject<BlockItem> BIRCH_CARD_DISPLAY_ITEM = ITEMS.register("birch_card_display", () -> new BlockItem(BuddycardsBlocks.BIRCH_CARD_DISPLAY.get(), DEFAULT_PROPERTIES));
    public static final RegistryObject<BlockItem> JUNGLE_CARD_DISPLAY_ITEM = ITEMS.register("jungle_card_display", () -> new BlockItem(BuddycardsBlocks.JUNGLE_CARD_DISPLAY.get(), DEFAULT_PROPERTIES));
    public static final RegistryObject<BlockItem> ACACIA_CARD_DISPLAY_ITEM = ITEMS.register("acacia_card_display", () -> new BlockItem(BuddycardsBlocks.ACACIA_CARD_DISPLAY.get(), DEFAULT_PROPERTIES));
    public static final RegistryObject<BlockItem> DARK_OAK_CARD_DISPLAY_ITEM = ITEMS.register("dark_oak_card_display", () -> new BlockItem(BuddycardsBlocks.DARK_OAK_CARD_DISPLAY.get(), DEFAULT_PROPERTIES));
    public static final RegistryObject<BlockItem> CRIMSON_CARD_DISPLAY_ITEM = ITEMS.register("crimson_card_display", () -> new BlockItem(BuddycardsBlocks.CRIMSON_CARD_DISPLAY.get(), DEFAULT_PROPERTIES));
    public static final RegistryObject<BlockItem> WARPED_CARD_DISPLAY_ITEM = ITEMS.register("warped_card_display", () -> new BlockItem(BuddycardsBlocks.WARPED_CARD_DISPLAY.get(), DEFAULT_PROPERTIES));
    //Booster Box Items
    public static final RegistryObject<BuddycardBoosterBoxItem> BOOSTER_BOX_BASE = ITEMS.register("buddycard_booster_box_base", () -> new BuddycardBoosterBoxItem(BuddycardsBlocks.BOOSTER_BOX_BASE.get(), PACK_BASE, DEFAULT_UNCOMMON_PROPERTIES));
    public static final RegistryObject<BuddycardBoosterBoxItem> BOOSTER_BOX_NETHER = ITEMS.register("buddycard_booster_box_nether", () -> new BuddycardBoosterBoxItem(BuddycardsBlocks.BOOSTER_BOX_NETHER.get(), PACK_NETHER, DEFAULT_UNCOMMON_PROPERTIES));
    public static final RegistryObject<BuddycardBoosterBoxItem> BOOSTER_BOX_END = ITEMS.register("buddycard_booster_box_end", () -> new BuddycardBoosterBoxItem(BuddycardsBlocks.BOOSTER_BOX_END.get(), PACK_END, DEFAULT_UNCOMMON_PROPERTIES));
    public static final RegistryObject<BuddycardBoosterBoxItem> BOOSTER_BOX_MYSTERY = ITEMS.register("buddycard_booster_box_mystery", () -> new BuddycardBoosterBoxItem(BuddycardsBlocks.BOOSTER_BOX_MYSTERY.get(), MYSTERY_PACK, DEFAULT_EPIC_PROPERTIES));

    public static final  RegistryObject<ForgeSpawnEggItem> ENDERLING_SPAWN_EGG = ITEMS.register("spawn_egg_enderling", () -> new ForgeSpawnEggItem(BuddycardsEntities.ENDERLING, 0x2E2744, 0x9A72CC, DEFAULT_PROPERTIES));

    public static RegistryObject<BuddycardItem> registerCard(BuddycardSet set, int cardNumber, Rarity rarity, Item.Properties properties, BuddycardRequirement requirement, int cost, int power, ListMultimap<BattleEvent, BattleAbility> abilities) {
        Objects.requireNonNull(set);
        return ITEMS.register("buddycard_" + set.getName() + cardNumber, () -> new BuddycardItem(requirement, set, cardNumber, rarity, properties, cost, power, abilities));
    }
    public static RegistryObject<BuddycardItem> registerCard(BuddycardSet set, int cardNumber, Rarity rarity, BuddycardRequirement requirement, int cost, int power, ListMultimap<BattleEvent, BattleAbility> abilities) {
        return registerCard(set, cardNumber, rarity, DEFAULT_CARD_PROPERTIES, requirement, cost, power, abilities);
    }
    public static RegistryObject<BuddycardItem> registerCard(BuddycardSet set, int cardNumber, Rarity rarity, int cost, int power, ListMultimap<BattleEvent, BattleAbility> abilities) {
        return registerCard(set, cardNumber, rarity, DEFAULT_CARD_PROPERTIES, DEFAULT_BUDDYCARD_REQUIREMENT, cost, power, abilities);
    }

    public static RegistryObject<BuddycardItem> registerReprintCard(BuddycardSet set, int cardNumber, Rarity rarity, Item.Properties properties, BuddycardRequirement requirement, int cost, int power, ListMultimap<BattleEvent, BattleAbility> abilities, BuddycardSet originalSet, int originalCardNum) {
        Objects.requireNonNull(set);
        return ITEMS.register("buddycard_" + set.getName() + cardNumber, () -> new BuddycardReprintItem(requirement, set, cardNumber, rarity, properties, cost, power, abilities, originalSet, originalCardNum));
    }
    public static RegistryObject<BuddycardItem> registerReprintCard(BuddycardSet set, int cardNumber, Rarity rarity, BuddycardRequirement requirement, int cost, int power, ListMultimap<BattleEvent, BattleAbility> abilities, BuddycardSet originalSet, int originalCardNum) {
        return registerReprintCard(set, cardNumber, rarity, DEFAULT_CARD_PROPERTIES, requirement, cost, power, abilities, originalSet, originalCardNum);
    }

    public interface BuddycardRequirement {
        boolean shouldLoad();
    }
}
