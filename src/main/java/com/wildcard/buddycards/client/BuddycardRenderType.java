package com.wildcard.buddycards.client;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.wildcard.buddycards.Buddycards;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public class BuddycardRenderType extends RenderType {
    public static RenderType defaultGlint = buildGlintRenderType("default");
    public static RenderType defaultGlintTranslucent = buildGlintTranslucentRenderType("default");
    public static RenderType defaultGlintDirect = buildGlintDirectRenderType("default");
    public static RenderType luminisGlint = buildGlintRenderType("luminis");
    public static RenderType luminisGlintTranslucent = buildGlintTranslucentRenderType("luminis");
    public static RenderType luminisGlintDirect = buildGlintDirectRenderType("luminis");
    public static RenderType rainbowGlint = buildGlintRenderType("rainbow");
    public static RenderType rainbowGlintTranslucent = buildGlintTranslucentRenderType("rainbow");
    public static RenderType rainbowGlintDirect = buildGlintDirectRenderType("rainbow");

    public BuddycardRenderType(String p_173178_, VertexFormat p_173179_, VertexFormat.Mode p_173180_, int p_173181_, boolean p_173182_, boolean p_173183_, Runnable p_173184_, Runnable p_173185_) {
        super(p_173178_, p_173179_, p_173180_, p_173181_, p_173182_, p_173183_, p_173184_, p_173185_);
    }

    static RenderType buildGlintRenderType(String name) {
        return RenderType.create("glint_" + name, DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256, false, false, RenderType.CompositeState.builder()
                .setShaderState(RenderStateShard.RENDERTYPE_GLINT_SHADER)
                .setTextureState(new RenderStateShard.TextureStateShard(new ResourceLocation(Buddycards.MOD_ID, "textures/glint/" + name + ".png"), true, false))
                .setWriteMaskState(RenderStateShard.COLOR_WRITE)
                .setCullState(RenderStateShard.NO_CULL)
                .setDepthTestState(RenderStateShard.EQUAL_DEPTH_TEST)
                .setTransparencyState(RenderStateShard.GLINT_TRANSPARENCY)
                .setOutputState(RenderStateShard.ITEM_ENTITY_TARGET)
                .setTexturingState(RenderStateShard.GLINT_TEXTURING)
                .createCompositeState(false));
    }

    static RenderType buildGlintTranslucentRenderType(String name) {
        return RenderType.create("glint_translucent_" + name, DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256, false, false, CompositeState.builder()
                .setShaderState(RenderStateShard.RENDERTYPE_GLINT_TRANSLUCENT_SHADER)
                .setTextureState(new TextureStateShard(new ResourceLocation(Buddycards.MOD_ID, "textures/glint/" + name + ".png"), true, false))
                .setWriteMaskState(RenderStateShard.COLOR_WRITE)
                .setCullState(RenderStateShard.NO_CULL)
                .setDepthTestState(RenderStateShard.EQUAL_DEPTH_TEST)
                .setTransparencyState(RenderStateShard.GLINT_TRANSPARENCY)
                .setOutputState(RenderStateShard.ITEM_ENTITY_TARGET)
                .setTexturingState(RenderStateShard.GLINT_TEXTURING)
                .createCompositeState(false));
    }

    static RenderType buildGlintDirectRenderType(String name) {
        return RenderType.create("glint_direct_" + name, DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256, false, false, CompositeState.builder()
                .setShaderState(RenderStateShard.RENDERTYPE_GLINT_DIRECT_SHADER)
                .setTextureState(new TextureStateShard(new ResourceLocation(Buddycards.MOD_ID, "textures/glint/" + name + ".png"), true, false))
                .setWriteMaskState(RenderStateShard.COLOR_WRITE)
                .setCullState(RenderStateShard.NO_CULL)
                .setDepthTestState(RenderStateShard.EQUAL_DEPTH_TEST)
                .setTransparencyState(RenderStateShard.GLINT_TRANSPARENCY)
                .setTexturingState(RenderStateShard.GLINT_TEXTURING)
                .createCompositeState(false));
    }

    public static RenderType getRenderTypeForItem(int glint) {
        return switch (glint) {
            case 1 -> defaultGlint;
            case 2 -> luminisGlint;
            case 3 -> rainbowGlint;
            default -> RenderType.cutout();
        };
    }
}
