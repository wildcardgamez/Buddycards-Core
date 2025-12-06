package com.wildcard.buddycards.client.renderer;

import com.wildcard.buddycards.Buddycards;
import com.wildcard.buddycards.client.model.EnderlingModel;
import com.wildcard.buddycards.entity.EnderlingEntity;
import com.wildcard.buddycards.util.ClientStuff;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class EnderlingRenderer extends MobRenderer<EnderlingEntity, EnderlingModel<EnderlingEntity>> {
    public EnderlingRenderer(EntityRendererProvider.Context context) {
        super(context, new EnderlingModel(context.bakeLayer(ClientStuff.ENDERLING_LAYER)), .6f);
        this.addLayer(new EnderlingEyesLayer(this));
        this.addLayer(new ItemInHandLayer<>(this, context.getItemInHandRenderer()));
    }

    @Override
    public ResourceLocation getTextureLocation(EnderlingEntity p_114482_) {
        return new ResourceLocation(Buddycards.MOD_ID, "textures/entity/enderling.png");
    }

    @OnlyIn(Dist.CLIENT)
    public class EnderlingEyesLayer extends EyesLayer<EnderlingEntity, EnderlingModel<EnderlingEntity>> {
        private final RenderType RENDER_TYPE = RenderType.eyes(new ResourceLocation("textures/entity/enderman/enderman_eyes.png"));

        public EnderlingEyesLayer(RenderLayerParent<EnderlingEntity, EnderlingModel<EnderlingEntity>> rendererIn) {
            super(rendererIn);
        }

        public RenderType renderType() {
            return RENDER_TYPE;
        }
    }
}
