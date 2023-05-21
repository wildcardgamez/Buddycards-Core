package com.wildcard.buddycards.registries;

import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ListMultimap;
import com.wildcard.buddycards.Buddycards;
import com.wildcard.buddycards.battles.game.BattleAbility;
import com.wildcard.buddycards.battles.game.BattleEvent;
import com.wildcard.buddycards.battles.game.BattleGame;
import com.wildcard.buddycards.core.BuddycardSet;
import com.wildcard.buddycards.gear.BuddycardsArmorMaterial;
import com.wildcard.buddycards.gear.BuddycardsToolTier;
import com.wildcard.buddycards.item.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.*;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Calendar;
import java.util.Objects;

public class BuddycardsItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Buddycards.MOD_ID);

    public static void registerItems() {
        //Register base set
        /*SAPS       */ registerCard(BASE_SET,  1, Rarity.COMMON,   2, 1, new BattleAbility.Builder().add(BattleEvent.TURN.ability("growing_up", (game, slot, target, source) -> {
            game.state[slot].power++;
            return true;
        })).build());
        /*ROK        */ registerCard(BASE_SET,  2, Rarity.COMMON,   2, 1, new BattleAbility.Builder().add(BattleEvent.PLAYED.ability("stone_toss", (game, slot, target, source) -> {
            game.directAttack(BattleGame.opposite(slot), slot, 1);
            return true;
        })).build());
        /*OINKE      */ registerCard(BASE_SET,  3, Rarity.COMMON,   3, 2, new BattleAbility.Builder().add(BattleEvent.DEATH.ability("tasty_bacon", (game, slot, target, source) -> {
            if(BattleGame.getOwner(slot))
                game.container.health1+=2;
            else
                game.container.health2+=2;
            return true;
        })).build());
        /*COLE       */ registerCard(BASE_SET,  4, Rarity.COMMON,   3, 2, new BattleAbility.Builder().add(BattleEvent.DEATH.ability("stoke_flames", (game, slot, target, source) -> {
            for (int i: BattleEvent.Distribution.ROW.apply(slot, game)) {
                if(game.container.getItem(BattleGame.translateFrom(i)).m_204117_(BuddycardsMisc.BCB_FIRE)) {
                    game.turnPower[i]++;
                }
            }
            return true;
        })).build());
        /*METALICO   */ registerCard(BASE_SET,  5, Rarity.COMMON,   3, 2, new BattleAbility.Builder().add(BattleEvent.KILL.ability("iron_temper", (game, slot, target, source) -> {
            game.turnPower[slot]++;
            return true;
        })).build());
        /*DR_LAZULI  */ registerCard(BASE_SET,  6, Rarity.COMMON,   4, 2, new BattleAbility.Builder().add(BattleEvent.PLAYED.ability("simple_enchant", (game, slot, target, source) -> {
            for (int i: BattleEvent.Distribution.ROW.apply(slot, game)) {
                if(game.container.getItem(BattleGame.translateFrom(i)).m_204117_(BuddycardsMisc.BCB_ENCHANTABLE)) {
                    game.turnPower[i]++;
                }
            }
            return true;
        })).build());
        /*BETSY      */ registerCard(BASE_SET,  7, Rarity.COMMON,   5, 2, new BattleAbility.Builder().add(BattleEvent.TURN.ability("milk_maid", (game, slot, target, source) -> {
            if(BattleGame.getOwner(slot))
                game.container.health1++;
            else
                game.container.health2++;
            return true;
        })).build());
        /*SNOWBELLE  */ registerCard(BASE_SET,  8, Rarity.COMMON,   1, 1, new BattleAbility.Builder().add(BattleEvent.TURN.ability("snowball_fight", (game, slot, target, source) -> {
            if(!BattleGame.getOwner(slot))
                game.container.health1--;
            else
                game.container.health2--;
            return true;
        })).build());
        /*AQUA       */ registerCard(BASE_SET,  9, Rarity.COMMON,   2, 0, new BattleAbility.Builder().add(BattleEvent.DEATH.ability("wash_out", (game, slot, target, source) -> {
            for (int i: BattleEvent.Distribution.ALL_ENEMY.apply(slot, game)) {
                if(game.container.getItem(BattleGame.translateFrom(i)).m_204117_(BuddycardsMisc.BCB_FIRE)) {
                    game.directAttack(BattleGame.opposite(slot), slot, 5);
                }
            }
            return true;
        })).build());
        /*FORTUNA    */ registerCard(BASE_SET, 10, Rarity.COMMON,   1, 1, new BattleAbility.Builder().add(BattleEvent.DEATH.ability("tasty_treat", (game, slot, target, source) -> {
            if(BattleGame.getOwner(slot))
                game.container.health1++;
            else
                game.container.health2++;
            return true;
        })).build());
        /*WEET       */ registerCard(BASE_SET, 11, Rarity.COMMON,   3, 0, new BattleAbility.Builder().add(BattleEvent.DEATH.ability("fresh_harvest", (game, slot, target, source) -> {
            for (int i: BattleEvent.Distribution.ROW.apply(slot, game)) {
                if(game.container.getItem(BattleGame.translateFrom(i)).m_204117_(BuddycardsMisc.BCB_ANIMAL)) {
                    game.turnPower[i]+=2;
                }
            }
            return true;
        })).build());
        /*GRASSLING  */ registerCard(BASE_SET, 12, Rarity.COMMON,   3, 1, new BattleAbility.Builder().add(BattleEvent.PLAYED.ability("feeding_time", (game, slot, target, source) -> {
            for (int i: BattleEvent.Distribution.ROW.apply(slot, game)) {
                if(game.container.getItem(BattleGame.translateFrom(i)).m_204117_(BuddycardsMisc.BCB_ANIMAL)) {
                    game.turnPower[i]++;
                }
            }
            return true;
        })).build());
        /*YANG       */ registerCard(BASE_SET, 13, Rarity.UNCOMMON, 4, 3, new BattleAbility.Builder().add(BattleEvent.KILL.ability("play_fetch", (game, slot, target, source) -> {
            game.container.tryDrawCard(BattleGame.getOwner(slot));
            return true;
        })).build());
        /*YIN        */ registerCard(BASE_SET, 14, Rarity.UNCOMMON, 4, 2, new BattleAbility.Builder().add(BattleEvent.FIGHT.ability("lazy_cat", (game, slot, target, source) -> {
            if(game.turnPower[slot] < 2) {
                game.turnPower[slot] += 2;
                return false;
            }
            return true;
        })).build());
        /*BLING      */ registerCard(BASE_SET, 15, Rarity.UNCOMMON, 3, 1, new BattleAbility.Builder().add(BattleEvent.PLAYED.ability("gilded_gift", (game, slot, target, source) -> {
            for (int i: BattleEvent.Distribution.ROW.apply(slot, game)) {
                if(game.container.getItem(BattleGame.translateFrom(i)).m_204117_(BuddycardsMisc.BCB_FOOD)) {
                    game.turnPower[i]++;
                }
            }
            return true;
        })).build());
        /*EMERELDA   */ registerCard(BASE_SET, 16, Rarity.UNCOMMON, 3, 1, new BattleAbility.Builder().add(BattleEvent.PLAYED.ability("rich_trade", (game, slot, target, source) -> {
            while (game.container.tryDrawCard(BattleGame.getOwner(slot)));
            return true;
        })).build());
        /*DIO        */ registerCard(BASE_SET, 17, Rarity.UNCOMMON, 7, 3, new BattleAbility.Builder().add(BattleEvent.DAMAGED.ability("unbreakable", (game, slot, target, source) -> {
            game.state[slot].power++;
            return true;
        })).build());
        /*TREATSY    */ registerCard(BASE_SET, 18, Rarity.UNCOMMON, 4, 2, new BattleAbility.Builder().add(BattleEvent.DEATH.ability("delicious_desert", (game, slot, target, source) -> {
            if(BattleGame.getOwner(slot))
                game.container.health1+=3;
            else
                game.container.health2+=3;
            return true;
        })).build());
        /*RED        */ registerCard(BASE_SET, 19, Rarity.UNCOMMON, 4, 2, new BattleAbility.Builder().add(BattleEvent.TURN.ability("energize", (game, slot, target, source) -> {
            for (int i: BattleEvent.Distribution.ADJACENT.apply(slot, game)) {
                if(game.container.getItem(BattleGame.translateFrom(i)).m_204117_(BuddycardsMisc.BCB_REDSTONE)) {
                    game.trigger(BattleEvent.POWERED, i, i, slot);
                }
            }
            return true;
        })).build());
        /*MELONY     */ registerCard(BASE_SET, 20, Rarity.UNCOMMON, 2, 1, new BattleAbility.Builder().add(BattleEvent.DEATH.ability("sweet_treat", (game, slot, target, source) -> {
            if(BattleGame.getOwner(slot))
                game.container.health1+=2;
            else
                game.container.health2+=2;
            return true;
        })).build());
        /*SPOOPY     */ registerCard(BASE_SET, 21, Rarity.UNCOMMON, 4, 1, new BattleAbility.Builder().add(BattleEvent.FIGHT.ability("spook_em", (game, slot, target, source) -> {
            if(game.getCard(target) != null)
                game.directAttack(target, slot, 1, false, true);
            return true;
        })).build());
        /*KNALL_EDGY */ registerCard(BASE_SET, 22, Rarity.RARE,     8, 3, new BattleAbility.Builder().add(BattleEvent.PLAYED.ability("premium_enchant", (game, slot, target, source) -> {
            for (int i: BattleEvent.Distribution.ROW.apply(slot, game)) {
                if(game.container.getItem(BattleGame.translateFrom(i)).m_204117_(BuddycardsMisc.BCB_ENCHANTABLE)) {
                    game.turnPower[i]+=2;
                }
            }
            return true;
        })).add(BattleEvent.KILL.ability("xp_farm", (game, slot, target, source) -> {
            for (int i: BattleEvent.Distribution.ROW.apply(slot, game)) {
                if(game.container.getItem(BattleGame.translateFrom(i)).m_204117_(BuddycardsMisc.BCB_ENCHANTABLE)) {
                    game.turnPower[i]++;
                }
            }
            return true;
        })).build());
        /*CASTER     */ registerCard(BASE_SET, 23, Rarity.RARE,     9, 4, new BattleAbility.Builder().add(BattleEvent.PLAYED.ability("anvil_drop", (game, slot, target, source) -> {
            if(game.getCard(target) != null)
                game.directAttack(BattleGame.opposite(slot), slot, 2);
            return true;
        })).add(BattleEvent.TURN.ability("reforge", (game, slot, target, source) -> {
            for (int i: BattleEvent.Distribution.ADJACENT.apply(slot, game)) {
                if(game.container.getItem(BattleGame.translateFrom(i)).m_204117_(BuddycardsMisc.BCB_METAL)) {
                    game.turnPower[i]++;
                }
            }
            return true;
        })).build());
        /*PISTY      */ registerCard(BASE_SET, 24, Rarity.RARE,     6, 2, new BattleAbility.Builder().add(BattleEvent.PLAYED.ability("push_back", (game, slot, target, source) -> {
            if(game.getCard(target) != null) {
                if(game.container.tryPutInHand(!BattleGame.getOwner(slot), game.container.getItem(BattleGame.translateFrom(BattleGame.opposite(slot)))))
                    game.removeCard(BattleGame.opposite(slot));
            }
            return true;
        })).add(BattleEvent.POWERED.ability("empowered_push", (game, slot, target, source) -> {
            if(game.getCard(target) != null) {
                if(game.container.tryPutInHand(!BattleGame.getOwner(slot), game.container.getItem(BattleGame.translateFrom(BattleGame.opposite(slot)))))
                    game.removeCard(BattleGame.opposite(slot));
            }
            return true;
        })).build());
        /*KNIGHT     */ registerCard(BASE_SET, 25, Rarity.RARE,     7, 4, DEFAULT_NO_ABILITIES);
        /*CREATOR    */ registerCard(BASE_SET, 26, Rarity.EPIC,     8, 3, DEFAULT_NO_ABILITIES);
        /*MELTOR     */ registerCard(BASE_SET, 27, Rarity.EPIC,     6, 3, DEFAULT_NO_ABILITIES);
        /*ANGSTY_SAPS*/ registerCard(BASE_SET, 28, Rarity.COMMON,   3, 2, DEFAULT_NO_ABILITIES);
        /*COPPOR     */ registerCard(BASE_SET, 29, Rarity.COMMON,   2, 2, DEFAULT_NO_ABILITIES);
        /*AIMY       */ registerCard(BASE_SET, 30, Rarity.COMMON,   2, 0, DEFAULT_NO_ABILITIES);
        /*DROPPER    */ registerCard(BASE_SET, 31, Rarity.COMMON,   1, 2, DEFAULT_NO_ABILITIES);
        /*TICK       */ registerCard(BASE_SET, 32, Rarity.UNCOMMON, 3, 1, DEFAULT_NO_ABILITIES);
        /*TOCK       */ registerCard(BASE_SET, 33, Rarity.UNCOMMON, 3, 0, DEFAULT_NO_ABILITIES);
        /*WHARE      */ registerCard(BASE_SET, 34, Rarity.UNCOMMON, 3, 0, DEFAULT_NO_ABILITIES);
        /*AMBYSTOMA  */ registerCard(BASE_SET, 35, Rarity.RARE,     4, 3, DEFAULT_NO_ABILITIES);
        /*KART       */ registerCard(BASE_SET, 36, Rarity.RARE,     5, 3, DEFAULT_NO_ABILITIES);

        //Register nether set
        /*BRIK     */ registerCard(NETHER_SET,  1, Rarity.COMMON,   4, 2, new BattleAbility.Builder().add(BattleEvent.PLAYED.ability("brick_toss", (game, slot, target, source) -> {
            game.directAttack(BattleGame.opposite(slot), slot, 2);
            return true;
        })).build());
        /*OBBY     */ registerCard(NETHER_SET,  2, Rarity.COMMON,   6, 3, DEFAULT_NO_ABILITIES);
        /*BRITE    */ registerCard(NETHER_SET,  3, Rarity.COMMON,   4, 2, DEFAULT_NO_ABILITIES);
        /*SLINKY   */ registerCard(NETHER_SET,  4, Rarity.COMMON,   4, 2, DEFAULT_NO_ABILITIES);
        /*BIG_PIG  */ registerCard(NETHER_SET,  5, Rarity.COMMON,   5, 3, DEFAULT_NO_ABILITIES);
        /*SOL      */ registerCard(NETHER_SET,  6, Rarity.COMMON,   3, 2, DEFAULT_NO_ABILITIES);
        /*BASISAL  */ registerCard(NETHER_SET,  7, Rarity.COMMON,   4, 2, DEFAULT_NO_ABILITIES);
        /*VINNIE   */ registerCard(NETHER_SET,  8, Rarity.COMMON,   4, 3, DEFAULT_NO_ABILITIES);
        /*SHROOM   */ registerCard(NETHER_SET,  9, Rarity.COMMON,   3, 2, DEFAULT_NO_ABILITIES);
        /*WARP_NYE */ registerCard(NETHER_SET, 10, Rarity.COMMON,   2, 1, DEFAULT_NO_ABILITIES);
        /*FYA      */ registerCard(NETHER_SET, 11, Rarity.COMMON,   2, 1, DEFAULT_NO_ABILITIES);
        /*MAGMA    */ registerCard(NETHER_SET, 12, Rarity.COMMON,   6, 1, DEFAULT_NO_ABILITIES);
        /*SKULLY   */ registerCard(NETHER_SET, 13, Rarity.UNCOMMON, 5, 2, DEFAULT_NO_ABILITIES);
        /*BLAZT    */ registerCard(NETHER_SET, 14, Rarity.UNCOMMON, 6, 2, DEFAULT_NO_ABILITIES);
        /*QUAZI    */ registerCard(NETHER_SET, 15, Rarity.UNCOMMON, 4, 2, DEFAULT_NO_ABILITIES);
        /*HOT_POT  */ registerCard(NETHER_SET, 16, Rarity.UNCOMMON, 3, 2, DEFAULT_NO_ABILITIES);
        /*CRYBABY  */ registerCard(NETHER_SET, 17, Rarity.UNCOMMON, 6, 4, DEFAULT_NO_ABILITIES);
        /*FYO      */ registerCard(NETHER_SET, 18, Rarity.UNCOMMON, 4, 1, DEFAULT_NO_ABILITIES);
        /*LAMP     */ registerCard(NETHER_SET, 19, Rarity.UNCOMMON, 5, 2, DEFAULT_NO_ABILITIES);
        /*GHOST    */ registerCard(NETHER_SET, 20, Rarity.UNCOMMON, 8, 2, DEFAULT_NO_ABILITIES);
        /*STRIDE   */ registerCard(NETHER_SET, 21, Rarity.UNCOMMON, 5, 3, DEFAULT_NO_ABILITIES);
        /*BARTENDER*/ registerCard(NETHER_SET, 22, Rarity.RARE,     6, 3, DEFAULT_NO_ABILITIES);
        /*PORT     */ registerCard(NETHER_SET, 23, Rarity.RARE,     10,6, DEFAULT_NO_ABILITIES);
        /*TRIPLE   */ registerCard(NETHER_SET, 24, Rarity.RARE,     10,4, DEFAULT_NO_ABILITIES);
        /*ANKER    */ registerCard(NETHER_SET, 25, Rarity.RARE,     7, 4, DEFAULT_NO_ABILITIES);
        /*BEAKER   */ registerCard(NETHER_SET, 26, Rarity.EPIC,     10,5, DEFAULT_NO_ABILITIES);
        /*N_RITE   */ registerCard(NETHER_SET, 27, Rarity.EPIC,     9, 4, DEFAULT_NO_ABILITIES);

        //Register end set
        /*ENROK    */ registerCard(END_SET,  1, Rarity.COMMON,   2, 1, DEFAULT_NO_ABILITIES);
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
        /*HALLOWEEN_YIN    */ registerCard(HOLIDAY_SET, 1, Rarity.RARE, HALLOWEEN_BUDDYCARD_REQUIREMENT, 2, 1, DEFAULT_NO_ABILITIES);
        /*HALLOWEEN_SPOOPY */ registerCard(HOLIDAY_SET, 2, Rarity.RARE, HALLOWEEN_BUDDYCARD_REQUIREMENT, 2, 1, DEFAULT_NO_ABILITIES);
        /*HALLOWEEN_VWOOP  */ registerCard(HOLIDAY_SET, 3, Rarity.RARE, HALLOWEEN_BUDDYCARD_REQUIREMENT, 2, 1, DEFAULT_NO_ABILITIES);
        /*CHRISTMAS_COLE   */ registerCard(HOLIDAY_SET, 4, Rarity.RARE, CHRISTMAS_BUDDYCARD_REQUIREMENT, 2, 1, DEFAULT_NO_ABILITIES);
        /*CHRISTMAS_CHESTER*/ registerCard(HOLIDAY_SET, 5, Rarity.RARE, CHRISTMAS_BUDDYCARD_REQUIREMENT, 2, 1, DEFAULT_NO_ABILITIES);
        /*CHRISTMAS_CHOCO  */ registerCard(HOLIDAY_SET, 6, Rarity.RARE, CHRISTMAS_BUDDYCARD_REQUIREMENT, 2, 1, DEFAULT_NO_ABILITIES);

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
    public static final RegistryObject<BuddycardBinderItem> BINDER_BASE = ITEMS.register("buddycard_binder_base", () -> new BuddycardBinderItem(DEFAULT_BUDDYCARD_REQUIREMENT, DEFAULT_BINDER_PROPERTIES));
    public static final RegistryObject<BuddycardBinderItem> BINDER_NETHER = ITEMS.register("buddycard_binder_nether", () -> new BuddycardBinderItem(DEFAULT_BUDDYCARD_REQUIREMENT, DEFAULT_BINDER_PROPERTIES));
    public static final RegistryObject<BuddycardBinderItem> BINDER_END = ITEMS.register("buddycard_binder_end", () -> new BuddycardBinderItem(DEFAULT_BUDDYCARD_REQUIREMENT, DEFAULT_BINDER_PROPERTIES));
    public static final RegistryObject<EnderBinderItem> ENDER_BINDER = ITEMS.register("ender_buddycard_binder", () -> new EnderBinderItem(DEFAULT_BINDER_PROPERTIES));
    //Deckboxes
    public static final RegistryObject<DeckboxItem> DECKBOX = ITEMS.register("deckbox", () -> new DeckboxItem(DEFAULT_BINDER_PROPERTIES));
    //Playmats
    public static final RegistryObject<BlockItem> PLAYMAT_BASE = ITEMS.register("playmat_base", () -> new BlockItem(BuddycardsBlocks.PLAYMAT_BASE.get(), DEFAULT_PROPERTIES));
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

    public interface BuddycardRequirement {
        boolean shouldLoad();
    }
}
