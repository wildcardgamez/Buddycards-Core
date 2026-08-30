package com.wildcard.buddycards.datagen;

import com.wildcard.buddycards.Buddycards;
import com.wildcard.buddycards.core.BuddycardSet;
import com.wildcard.buddycards.core.BuddycardsAPI;
import com.wildcard.buddycards.item.BuddycardItem;
import com.wildcard.buddycards.item.BuddysteelSetMedalItem;
import com.wildcard.buddycards.registries.BuddycardsItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.armortrim.TrimMaterial;
import net.minecraft.world.item.armortrim.TrimMaterials;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.client.model.generators.ModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.LinkedHashMap;
import java.util.Map;

public class ModelGen extends ItemModelProvider {
    public ModelGen(PackOutput output, String modid, ExistingFileHelper existingFileHelper) {
        super(output, modid, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        for (DeferredHolder<Item, ? extends Item> i : BuddycardsItems.ITEMS.getEntries()) {
            if (i.get() instanceof ArmorItem) {
                getBuilder(ModelProvider.ITEM_FOLDER + "/" + i.getId().getPath())
                        .parent(factory.apply(ResourceLocation.withDefaultNamespace("item/generated")))
                        .texture("layer0", (ModelProvider.ITEM_FOLDER + "/" + i.getId().getPath()));
            }
        }
        for (BuddycardItem card: BuddycardsAPI.getAllCards())
            genCardModel(card.getSet(), card.getCardNumber());
        for (BuddycardSet set : BuddycardsAPI.getAllSets())
            if (set.getMedal() != null)
                genMedalModel(set);
    }

    /**
     * Makes every model for a card, including all grades for normal and shiny cards
     * @param set set of card to generate models for
     * @param cardNum card number of card to generate models for
     */
    void genCardModel(BuddycardSet set, int cardNum) {
        String setName = set.getName();
        ItemModelBuilder card = getBuilder(ModelProvider.ITEM_FOLDER + "/buddycard_" + setName + cardNum)
                .parent(factory.apply(Buddycards.buddycardsLocation(ModelProvider.ITEM_FOLDER + "/buddycard")))
                .texture("layer0", Buddycards.buddycardsLocation(ModelProvider.ITEM_FOLDER + "/" + setName + "_set/" + cardNum));
        for (int i = 0; i <= 5; i++) {
            for (int j = 0; j <= 3; j++)
                if (j + i != 0)
                    card.override().predicate(Buddycards.buddycardsLocation("grade"), i).predicate(Buddycards.buddycardsLocation("foil"), j).model(genFoiledGradedCardModel(setName, cardNum, i, j));
        }
    }
    ModelFile genFoiledGradedCardModel(String setName, int cardNum, int grade, int foil) {
        ItemModelBuilder card = getBuilder(ModelProvider.ITEM_FOLDER + "/buddycard_" + setName + cardNum + "_g" + grade + "_f" + foil)
                .parent(factory.apply(Buddycards.buddycardsLocation(ModelProvider.ITEM_FOLDER + "/buddycard")))
                .texture("layer0", Buddycards.buddycardsLocation(ModelProvider.ITEM_FOLDER + "/" + setName + "_set/" + cardNum));
        if (foil != 0)
                card.texture("layer1", Buddycards.buddycardsLocation(ModelProvider.ITEM_FOLDER + "/foil" + foil));
        if (grade != 0)
                card.texture(foil == 0 ? "layer1" : "layer2", Buddycards.buddycardsLocation(ModelProvider.ITEM_FOLDER + "/grade" + grade));
        return card;
    }

    void genMedalModel(BuddycardSet set) {
        ItemModelBuilder medal = getBuilder(ModelProvider.ITEM_FOLDER + "/buddysteel_medal_" + set.getName())
                .parent(factory.apply(ResourceLocation.withDefaultNamespace("item/generated")))
                .texture("layer0", Buddycards.buddycardsLocation(ModelProvider.ITEM_FOLDER + "/" + set.getName() + "_set/" + "medal"));
        for (int i = 1; i < 5; i++) {
            ItemModelBuilder tierMedal = getBuilder(ModelProvider.ITEM_FOLDER + "/buddysteel_medal_" + set.getName() + i)
                    .parent(factory.apply(ResourceLocation.withDefaultNamespace("item/generated")))
                    .texture("layer0", Buddycards.buddycardsLocation(ModelProvider.ITEM_FOLDER + "/" + set.getName() + "_set/" + "medal" + i));
            medal.override().predicate(Buddycards.buddycardsLocation("tier"), i).model(tierMedal);
        }
    }
}