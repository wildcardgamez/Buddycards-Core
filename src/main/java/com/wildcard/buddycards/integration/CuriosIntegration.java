package com.wildcard.buddycards.integration;

import com.wildcard.buddycards.client.renderer.MedalRenderer;
import com.wildcard.buddycards.registries.BuddycardsItems;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;

public class CuriosIntegration {
    public static void setupRenderers() {
        CuriosRendererRegistry.register(BuddycardsItems.BLANK_BUDDYSTEEL_MEDAL.get(), () -> new MedalRenderer(getMedalId("blank_buddysteel_medal")));
        CuriosRendererRegistry.register(BuddycardsItems.MEDAL_BASE.get(), () -> new MedalRenderer(getMedalId("buddysteel_medal_base")));
        CuriosRendererRegistry.register(BuddycardsItems.MEDAL_NETHER.get(), () -> new MedalRenderer(getMedalId("buddysteel_medal_nether")));
        CuriosRendererRegistry.register(BuddycardsItems.MEDAL_END.get(), () -> new MedalRenderer(getMedalId("buddysteel_medal_end")));
        CuriosRendererRegistry.register(BuddycardsItems.MEDAL_CAVE.get(), () -> new MedalRenderer(getMedalId("buddysteel_medal_cave")));
    }

    protected static String getMedalId(String name) {
        return "textures/models/medal/" + name;
    }
}
