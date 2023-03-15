package com.wildcard.buddycards.datagen;

import com.wildcard.buddycards.Buddycards;
import com.wildcard.buddycards.core.BuddycardSet;
import com.wildcard.buddycards.core.BuddycardsAPI;
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
        for (BuddycardItem card: BuddycardsAPI.getAllCards())
            genCardModel(card.getSet(), card.getCardNumber());

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
        for (int i = 1; i <= 5; i++) {
            card.override().predicate(new ResourceLocation("grade"), i).model(genGradedCardModel(setName, cardNum, i));
        }
    }

    ModelFile genGradedCardModel(String setName, int cardNum, int grade) {
        return getBuilder(ModelProvider.ITEM_FOLDER + "/buddycard_" + setName + cardNum + "_g" + grade)
                .parent(factory.apply(new ResourceLocation(Buddycards.MOD_ID, ModelProvider.ITEM_FOLDER + "/buddycard")))
                .texture("layer0", new ResourceLocation(Buddycards.MOD_ID, ModelProvider.ITEM_FOLDER + "/" + setName + "_set/" + cardNum))
                .texture("layer1", new ResourceLocation(Buddycards.MOD_ID,ModelProvider.ITEM_FOLDER + "/grade" + grade));
    }
}