package com.wildcard.buddycards.util;

import net.neoforged.neoforge.common.ModConfigSpec;

public class ConfigManager {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec SPEC;

    static {
        init();
        SPEC = BUILDER.build();
    }

    public static void init() {
        BUILDER.comment("Buddycards Core config");
        zombieChance = BUILDER.comment("\nOdds of baby zombie dropping base set packs, 0 for 0%, 1 for 100%, default is 5%")
                .defineInRange("mobDrops.zombieChance", .05, 0, 1);
        villagerChance = BUILDER.comment("\nOdds of baby villager dropping base set packs, 0 for 0%, 1 for 100%, default is 5%")
                .defineInRange("mobDrops.villagerChance", .05, 0, 1);
        zombieVillagerChance = BUILDER.comment("\nOdds of baby zombie villager dropping base set packs, 0 for 0%, 1 for 100%, default is 10%")
                .defineInRange("mobDrops.zombieVillagerChance", .1, 0, 1);
        piglinChance = BUILDER.comment("\nOdds of baby piglin dropping nether set packs, 0 for 0%, 1 for 100%, default is 5%")
                .defineInRange("mobDrops.piglinChance", .05, 0, 1);
        zombiePiglinChance = BUILDER.comment("\nOdds of baby zombie piglin dropping nether set , 0 for 0%, 1 for 100%, default is 5%")
                .defineInRange("mobDrops.zombiePiglinChance", .05, 0, 1);
        shulkerChance = BUILDER.comment("\nOdds of shulkers dropping end set packs, 0 for 0%, 1 for 100%, default is 5%")
                .defineInRange("mobDrops.shulkerChance", .05, 0, 1);
        dragonChance = BUILDER.comment("\nOdds of ender dragons dropping end set packs, 0 for 0%, 1 for 100%, default is 100%")
                .defineInRange("mobDrops.dragonChance", 1f, 0, 1);
        dragonMaxPacks = BUILDER.comment("\nMaximum amount of packs dropped when a dragon drops packs, default is 4")
                .defineInRange("mobDrops.dragonMaxPacks", 4, 1, 16);
        witherChance = BUILDER.comment("\nOdds of withers dropping nether set packs, 0 for 0%, 1 for 100%, default is 50%")
                .defineInRange("mobDrops.witherChance", .5f, 0, 1);
        witherMaxPacks = BUILDER.comment("\nMaximum amount of packs dropped when a wither drops packs, default is 3")
                .defineInRange("mobDrops.witherMaxPacks", 3, 1, 16);
        wardenChance = BUILDER.comment("\nOdds of wardens dropping cave set packs, 0 for 0%, 1 for 100%, default is 75%")
                .defineInRange("mobDrops.wardenChance", .75f, 0, 1);
        wardenMaxPacks = BUILDER.comment("\nMaximum amount of packs dropped when a warden drops packs, default is 1")
                .defineInRange("mobDrops.wardenMaxPacks", 1, 1, 16);

        luminisToCrimsonAvg = BUILDER.comment("\nAmount of Luminis blocks, on average, needed to make 1 Crimson Luminis in_enchanting_table.json an explosion, default is 2")
                .defineInRange("luminis.explodeCrimsonAvg", 2, 1, 16);
        kineticSuccessRate = BUILDER.comment("\nOdds of an explosion triggering a Kinetic Chamber to activate, default is 100%")
                .defineInRange("luminis.kineticSuccessOdds", 1f, 0, 1);
        luminisKineticCrimsonOdds = BUILDER.comment("\nOdds of a kinetic chamber properly converting Luminis blocks to Crimson Luminis, default is 80% \nWARNING: FAILURE WILL CONSUME")
                .defineInRange("luminis.kineticCrimsonOdds", .8f, 0, 1);
        luminisKineticSpecialtyOdds = BUILDER.comment("\nOdds of a kinetic chamber properly converting Crimson Luminis blocks to specialty items, default is 90% \nWARNING: FAILURE WILL CONSUME")
                .defineInRange("luminis.kineticSpecialtyOdds", .9f, 0, 1);

        enderlingChanceEnd = BUILDER.comment("\nOdds for an Enderling to spawn with an Enderman in_enchanting_table.json the End, 0-1, default is 0.5%")
                .defineInRange("enderling.endOdds", .005, 0, 1);
        enderlingChanceNether = BUILDER.comment("\nOdds for an Enderling to spawn with an Enderman in_enchanting_table.json the Nether, 0-1, default is 0.5%")
                .defineInRange("enderling.netherOdds", .005, 0, 1);
        enderlingChanceOverworld = BUILDER.comment("\nOdds for an Enderling to spawn with an Enderman in_enchanting_table.json the Overworld, 0-1, default is 1.5%")
                .defineInRange("enderling.overOdds", .015, 0, 1);

        deckLimitCommon = BUILDER.comment("\nMax duplicates of a common card for a Buddycards deck, 0-16, default is 4")
                .defineInRange("deckBuilding.commonDupeLimit", 4, 1, 16);
        deckLimitUncommon = BUILDER.comment("\nMax duplicates of an uncommon card for a Buddycards deck, 0-16, default is 3")
                .defineInRange("deckBuilding.uncommonDupeLimit", 3, 1, 16);
        deckLimitRare = BUILDER.comment("\nMax duplicates of a rare card for a Buddycards deck, 0-16, default is 2")
                .defineInRange("deckBuilding.rareDupeLimit", 2, 1, 16);
        deckLimitEpic = BUILDER.comment("\nMax duplicates of an epic card for a Buddycards deck, 0-16, default is 1")
                .defineInRange("deckBuilding.epicDupeLimit", 1, 1, 16);

        enableBattles = BUILDER.comment("\nEnables Buddycards Battles Beta, default is false. Currently unfinished and buggy. Some bugged cards may cause crashes or other issues.")
                .define("battles.enabled", false);

        buddysteelRingChance = BUILDER.comment("\nChance to pull an extra card in_enchanting_table.json a pack when using a Buddysteel Ring, 0-1, default is 25%")
                .defineInRange("misc.buddysteelRingChance", 0.25, 0, 1);
        chargedRingFoilChance = BUILDER.comment("\nChance for an additional foiled card when using a Charged Buddysteel Ring, 0-1, default is 25%")
                .defineInRange("misc.buddysteelRingChance", 0.25, 0, 1);
        chargedRingGradeChance = BUILDER.comment("\nChance to pull an extra card in_enchanting_table.json a pack when using a Charged Buddysteel Ring, 0-1, default is 50%")
                .defineInRange("misc.buddysteelRingChance", 0.5, 0, 1);
    }

    public static ModConfigSpec.DoubleValue zombieChance;
    public static ModConfigSpec.DoubleValue villagerChance;
    public static ModConfigSpec.DoubleValue zombieVillagerChance;
    public static ModConfigSpec.DoubleValue piglinChance;
    public static ModConfigSpec.DoubleValue zombiePiglinChance;
    public static ModConfigSpec.DoubleValue shulkerChance;
    public static ModConfigSpec.DoubleValue dragonChance;
    public static ModConfigSpec.IntValue dragonMaxPacks;
    public static ModConfigSpec.DoubleValue witherChance;
    public static ModConfigSpec.IntValue witherMaxPacks;
    public static ModConfigSpec.DoubleValue wardenChance;
    public static ModConfigSpec.IntValue wardenMaxPacks;

    public static ModConfigSpec.IntValue luminisToCrimsonAvg;
    public static ModConfigSpec.DoubleValue kineticSuccessRate;
    public static ModConfigSpec.DoubleValue luminisKineticCrimsonOdds;
    public static ModConfigSpec.DoubleValue luminisKineticSpecialtyOdds;

    public static ModConfigSpec.DoubleValue enderlingChanceOverworld;
    public static ModConfigSpec.DoubleValue enderlingChanceNether;
    public static ModConfigSpec.DoubleValue enderlingChanceEnd;

    public static ModConfigSpec.IntValue deckLimitCommon;
    public static ModConfigSpec.IntValue deckLimitUncommon;
    public static ModConfigSpec.IntValue deckLimitRare;
    public static ModConfigSpec.IntValue deckLimitEpic;

    public static ModConfigSpec.BooleanValue enableBattles;

    public static ModConfigSpec.DoubleValue buddysteelRingChance;
    public static ModConfigSpec.DoubleValue chargedRingFoilChance;
    public static ModConfigSpec.DoubleValue chargedRingGradeChance;
}
