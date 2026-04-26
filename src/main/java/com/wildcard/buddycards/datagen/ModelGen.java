package com.wildcard.buddycards.datagen;

import com.wildcard.buddycards.Buddycards;
import com.wildcard.buddycards.core.BuddycardSet;
import com.wildcard.buddycards.core.BuddycardsAPI;
import com.wildcard.buddycards.item.BuddycardItem;
import com.wildcard.buddycards.registries.BuddycardsItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.armortrim.TrimMaterial;
import net.minecraft.world.item.armortrim.TrimMaterials;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.ModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

import java.util.LinkedHashMap;
import java.util.Map;

public class ModelGen extends ItemModelProvider {
    public ModelGen(PackOutput output, String modid, ExistingFileHelper existingFileHelper) {
        super(output, modid, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        for (BuddycardItem card: BuddycardsAPI.getAllCards())
            genCardModel(card.getSet(), card.getCardNumber());
        for(RegistryObject<Item> item : BuddycardsItems.ITEMS.getEntries())
            if(item.get() instanceof ArmorItem)
                genArmorModel(item);
    }

    /**
     * Makes every model for a card, including all grades for normal and shiny cards
     * @param set set of card to generate models for
     * @param cardNum card number of card to generate models for
     */
    void genCardModel(BuddycardSet set, int cardNum) {
        String setName = set.getName();
        ItemModelBuilder card = getBuilder(ModelProvider.ITEM_FOLDER + "/buddycard_" + setName + cardNum)
                .parent(factory.apply(new ResourceLocation(Buddycards.MOD_ID, ModelProvider.ITEM_FOLDER + "/buddycard")))
                .texture("layer0", new ResourceLocation(Buddycards.MOD_ID, ModelProvider.ITEM_FOLDER + "/" + setName + "_set/" + cardNum));
        for (int i = 0; i <= 5; i++) {
            for (int j = 0; j <= 3; j++)
                if (j + i != 0)
                    card.override().predicate(new ResourceLocation(Buddycards.MOD_ID, "grade"), i).predicate(new ResourceLocation(Buddycards.MOD_ID, "foil"), j).model(genFoiledGradedCardModel(setName, cardNum, i, j));
        }
    }
    ModelFile genFoiledGradedCardModel(String setName, int cardNum, int grade, int foil) {
        ItemModelBuilder card = getBuilder(ModelProvider.ITEM_FOLDER + "/buddycard_" + setName + cardNum + "_g" + grade + "_f" + foil)
                .parent(factory.apply(new ResourceLocation(Buddycards.MOD_ID, ModelProvider.ITEM_FOLDER + "/buddycard")))
                .texture("layer0", new ResourceLocation(Buddycards.MOD_ID, ModelProvider.ITEM_FOLDER + "/" + setName + "_set/" + cardNum));
        if (foil != 0)
                card.texture("layer1", new ResourceLocation(Buddycards.MOD_ID,ModelProvider.ITEM_FOLDER + "/foil" + foil));
        if (grade != 0)
                card.texture(foil == 0 ? "layer1" : "layer2", new ResourceLocation(Buddycards.MOD_ID,ModelProvider.ITEM_FOLDER + "/grade" + grade));
        return card;
    }

    private static final LinkedHashMap<ResourceKey<TrimMaterial>, Float> trimMaterials = new LinkedHashMap<>();

    static {
        trimMaterials.put(TrimMaterials.QUARTZ, 0.1F);
        trimMaterials.put(TrimMaterials.IRON, 0.2F);
        trimMaterials.put(TrimMaterials.NETHERITE, 0.3F);
        trimMaterials.put(TrimMaterials.REDSTONE, 0.4F);
        trimMaterials.put(TrimMaterials.COPPER, 0.5F);
        trimMaterials.put(TrimMaterials.GOLD, 0.6F);
        trimMaterials.put(TrimMaterials.EMERALD, 0.7F);
        trimMaterials.put(TrimMaterials.DIAMOND, 0.8F);
        trimMaterials.put(TrimMaterials.LAPIS, 0.9F);
        trimMaterials.put(TrimMaterials.AMETHYST, 1.0F);
    }

    void genArmorModel(RegistryObject<?> registryItem) {
        if (registryItem.get() instanceof ArmorItem item) {
            for (Map.Entry<ResourceKey<TrimMaterial>, Float> entry : trimMaterials.entrySet()) {
                ResourceKey<TrimMaterial> trimMaterial = entry.getKey();
                float trimValue = entry.getValue();

                String armorType = switch (item.getEquipmentSlot()) {
                    case HEAD -> "helmet";
                    case CHEST -> "chestplate";
                    case LEGS -> "leggings";
                    case FEET -> "boots";
                    default -> "";
                };

                String armorItemPath = "item/" + item;
                String trimPath = "trims/items/" + armorType + "_trim_" + trimMaterial.location().getPath();
                String currentTrimName = armorItemPath + "_" + trimMaterial.location().getPath() + "_trim";
                ResourceLocation armorItemResLoc = new ResourceLocation(Buddycards.MOD_ID, armorItemPath);
                ResourceLocation trimResLoc = new ResourceLocation(trimPath); // minecraft namespace
                ResourceLocation trimNameResLoc = new ResourceLocation(Buddycards.MOD_ID, currentTrimName);

                existingFileHelper.trackGenerated(trimResLoc, PackType.CLIENT_RESOURCES, ".png", "textures");

                getBuilder(currentTrimName)
                        .parent(new ModelFile.UncheckedModelFile("item/generated"))
                        .texture("layer0", armorItemResLoc)
                        .texture("layer1", trimResLoc);

                this.withExistingParent(registryItem.getId().getPath(),
                                mcLoc("item/generated"))
                        .override()
                        .model(new ModelFile.UncheckedModelFile(trimNameResLoc))
                        .predicate(mcLoc("trim_type"), trimValue).end()
                        .texture("layer0",
                                new ResourceLocation(Buddycards.MOD_ID,
                                        "item/" + registryItem.getId().getPath()));
            }
        }
    }
}