package com.wildcard.buddycards.datagen;

import com.wildcard.buddycards.Buddycards;
import com.wildcard.buddycards.item.BuddycardItem;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.ModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class CardModelGen extends ItemModelProvider {
    public CardModelGen(DataGenerator generator, String modid, ExistingFileHelper existingFileHelper) {
        super(generator, modid, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        for (BuddycardItem card: BuddycardItem.CARDS)
            genCardModel(card.getSet(), card.getCardNumber());

    }

    /**
     * Makes every model for a card, including all grades for normal and shiny cards
     * @param setName set of card to generate models for
     * @param cardNum card number of card to generate models for
     */
    void genCardModel(String setName, int cardNum) {
        final ResourceLocation location = new ResourceLocation(Buddycards.MOD_ID, ModelProvider.ITEM_FOLDER + "/buddycard_" + setName + cardNum);
        ItemModelBuilder card = factory.apply(location)
                .parent(factory.apply(new ResourceLocation(Buddycards.MOD_ID, ModelProvider.ITEM_FOLDER + "/buddycard")))
                .texture("layer0", new ResourceLocation(Buddycards.MOD_ID, ModelProvider.ITEM_FOLDER + "/" + setName + "_set/" + cardNum));
        card.override().predicate(new ResourceLocation("grade"), 1).model(genGradedCardModel(setName, cardNum, 1));
        card.override().predicate(new ResourceLocation("grade"), 2).model(genGradedCardModel(setName, cardNum, 2));
        card.override().predicate(new ResourceLocation("grade"), 3).model(genGradedCardModel(setName, cardNum, 3));
        card.override().predicate(new ResourceLocation("grade"), 4).model(genGradedCardModel(setName, cardNum, 4));
        card.override().predicate(new ResourceLocation("grade"), 5).model(genGradedCardModel(setName, cardNum, 5));
        generatedModels.put(location, card);
    }

    ModelFile genGradedCardModel(String setName, int cardNum, int grade) {
        final ResourceLocation location;
        location = new ResourceLocation(Buddycards.MOD_ID, ModelProvider.ITEM_FOLDER + "/buddycard_" + setName + cardNum + "_g" + grade);
        ItemModelBuilder card = factory.apply(location)
                .parent(factory.apply(new ResourceLocation(Buddycards.MOD_ID, ModelProvider.ITEM_FOLDER + "/buddycard")))
                .texture("layer0", new ResourceLocation(Buddycards.MOD_ID, ModelProvider.ITEM_FOLDER + "/" + setName + "_set/" + cardNum));
        generatedModels.put(location, card.texture("layer1", new ResourceLocation(Buddycards.MOD_ID,ModelProvider.ITEM_FOLDER + "/grade" + grade)));
        return card;
    }
}