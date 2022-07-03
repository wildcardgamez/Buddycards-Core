package com.wildcard.buddycards.util;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;

import java.io.File;

@Mod.EventBusSubscriber
public class ConfigManager {
    private static final ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec config;

    static {
        init();
        config = builder.build();
    }

    public static void loadConfig(String path) {
        final CommentedFileConfig file = CommentedFileConfig.builder(new File(path)).sync().autosave().writingMode(WritingMode.REPLACE).build();
        file.load();
        config.setConfig(file);
    }

    public static void init() {
        builder.comment("Buddycards Core config");
        zombieChance = builder.comment("\nOdds of baby zombie dropping base set packs, 0 for 0%, 1 for 100%, default is 5%")
                .defineInRange("mobDrops.zombieChance", .05, 0, 1);
        villagerChance = builder.comment("\nOdds of baby villager dropping base set packs, 0 for 0%, 1 for 100%, default is 5%")
                .defineInRange("mobDrops.villagerChance", .05, 0, 1);
        zombieVillagerChance = builder.comment("\nOdds of baby zombie villager dropping base set packs, 0 for 0%, 1 for 100%, default is 10%")
                .defineInRange("mobDrops.zombieVillagerChance", .1, 0, 1);
        piglinChance = builder.comment("\nOdds of baby piglin dropping nether set packs, 0 for 0%, 1 for 100%, default is 5%")
                .defineInRange("mobDrops.piglinChance", .05, 0, 1);
        zombiePiglinChance = builder.comment("\nOdds of baby zombie piglin dropping nether set , 0 for 0%, 1 for 100%, default is 5%")
                .defineInRange("mobDrops.zombiePiglinChance", .05, 0, 1);
        shulkerChance = builder.comment("\nOdds of shulkers dropping end set packs, 0 for 0%, 1 for 100%, default is 5%")
                .defineInRange("mobDrops.shulkerChance", .05, 0, 1);
        dragonChance = builder.comment("\nOdds of ender dragons dropping end set packs, 0 for 0%, 1 for 100%, default is 100%")
                .defineInRange("mobDrops.dragonChance", 1f, 0, 1);
        dragonMaxPacks = builder.comment("\nMaximum amount of packs dropped when a dragon drops packs, default is 4")
                .defineInRange("mobDrops.dragonMaxPacks", 4, 1, 16);
        witherChance = builder.comment("\nOdds of withers dropping nether set packs, 0 for 0%, 1 for 100%, default is 50%")
                .defineInRange("mobDrops.witherChance", .5f, 0, 1);
        witherMaxPacks = builder.comment("\nMaximum amount of packs dropped when a wither drops packs, default is 3")
                .defineInRange("mobDrops.witherMaxPacks", 3, 1, 16);

        luminisVeins = builder.comment("\nEnable Luminis vein generation, default is true")
                .define("luminis.enableVeins", true);
        luminisChunks = builder.comment("\nDistance between chunks that can spawn Luminis veins, under right conditions, default is 5")
                .defineInRange("luminis.veinChunkDistance", 5, 1, 16);
        luminisBranchMin = builder.comment("\nMinimum amount of branches in a Luminis vein, default is 2")
                .defineInRange("luminis.veinBranchAmtMin", 2, 1, 16);
        luminisBranchMax = builder.comment("\nMaximum amount of branches in a Luminis vein, default is 4")
                .defineInRange("luminis.veinBranchAmtMax", 4, 1, 32);
        luminisBranchLengthMin = builder.comment("\nMinimum length of branches in a Luminis vein, default is 8")
                .defineInRange("luminis.veinBranchLengthMin", 8, 1, 16);
        luminisBranchLengthMax = builder.comment("\nMaximum length of branches in a Luminis vein, default is 24")
                .defineInRange("luminis.veinBranchLengthMax", 24, 1, 32);
        luminisToCrimsonAvg = builder.comment("\nAmount of Luminis blocks, on average, needed to make 1 Crimson Luminis in an explosion, default is 2")
                .defineInRange("luminis.explodeCrimsonAvg", 2, 1, 16);
        kineticSuccessRate = builder.comment("\nOdds of an explosion triggering a Kinetic Chamber to activate, default is 50%")
                .defineInRange("luminis.kineticSuccessOdds", .5f, 0, 1);
        luminisKineticCrimsonOdds = builder.comment("\nOdds of a kinetic chamber properly converting Luminis blocks to Crimson Luminis, default is 75% \nWARNING: FAILURE WILL CONSUME")
                .defineInRange("luminis.kineticCrimsonOdds", .75f, 0, 1);
        luminisKineticSpecialtyOdds = builder.comment("\nOdds of a kinetic chamber properly converting Crimson Luminis blocks to specialty items, default is 85% \nWARNING: FAILURE WILL CONSUME")
                .defineInRange("luminis.kineticSpecialtyOdds", .85f, 0, 1);
    }

    public static ForgeConfigSpec.DoubleValue zombieChance;
    public static ForgeConfigSpec.DoubleValue villagerChance;
    public static ForgeConfigSpec.DoubleValue zombieVillagerChance;
    public static ForgeConfigSpec.DoubleValue piglinChance;
    public static ForgeConfigSpec.DoubleValue zombiePiglinChance;
    public static ForgeConfigSpec.DoubleValue shulkerChance;
    public static ForgeConfigSpec.DoubleValue dragonChance;
    public static ForgeConfigSpec.IntValue dragonMaxPacks;
    public static ForgeConfigSpec.DoubleValue witherChance;
    public static ForgeConfigSpec.IntValue witherMaxPacks;

    public static ForgeConfigSpec.BooleanValue luminisVeins;
    public static ForgeConfigSpec.IntValue luminisChunks;
    public static ForgeConfigSpec.IntValue luminisBranchMin;
    public static ForgeConfigSpec.IntValue luminisBranchMax;
    public static ForgeConfigSpec.IntValue luminisBranchLengthMin;
    public static ForgeConfigSpec.IntValue luminisBranchLengthMax;
    public static ForgeConfigSpec.IntValue luminisToCrimsonAvg;
    public static ForgeConfigSpec.DoubleValue kineticSuccessRate;
    public static ForgeConfigSpec.DoubleValue luminisKineticCrimsonOdds;
    public static ForgeConfigSpec.DoubleValue luminisKineticSpecialtyOdds;
}
