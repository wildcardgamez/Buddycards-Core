package com.wildcard.buddycards.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.LivingEntity;

public class MedalModel<T extends LivingEntity> extends HumanoidModel<LivingEntity> {
    public MedalModel(ModelPart part) {
        super(part);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = createMesh(CubeDeformation.NONE, 0);
        PartDefinition partdefinition = meshdefinition.getRoot();

        partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 2).addBox(1.0F, 2.5F, -2.75F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.2F))
                .texOffs(0, 0).addBox(0.0F, 0.5F, -2.25F, 4.0F, 2.0F, 0.0F, new CubeDeformation(0.2F, 0.2F, 0.4F)), PartPose.offset(0.0F, 0.0F, 0.5F));
        return LayerDefinition.create(meshdefinition, 16, 16);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        body.render(poseStack, buffer, packedLight, packedOverlay);
    }
}
