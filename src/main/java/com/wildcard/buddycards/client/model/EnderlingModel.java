package com.wildcard.buddycards.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.wildcard.buddycards.entity.EnderlingEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;

public class EnderlingModel<T extends EnderlingEntity> extends EntityModel<EnderlingEntity> {
    private final ModelPart torso;
    private final ModelPart head;
    private final ModelPart ll;
    private final ModelPart rl;
    private final ModelPart la;
    private final ModelPart ra;

    public EnderlingModel(ModelPart part) {
        torso = part.getChild("torso");
        head = part.getChild("head");
        ll = part.getChild("ll");
        rl = part.getChild("rl");
        la = part.getChild("la");
        ra = part.getChild("ra");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        partdefinition.addOrReplaceChild("torso", CubeListBuilder.create()
                .texOffs(32, 4)
                .addBox(-3, -4, -2, 6, 8, 4)
                .texOffs(32, 16)
                .addBox(-4, -3, 2, 8, 6, 2)
                .texOffs(26, 16)
                .addBox(-1, -2, 3.5f, 2, 3, 1), PartPose.offset(0, 8, 0));
        partdefinition.addOrReplaceChild("head", CubeListBuilder.create()
                .texOffs(0, 0)
                .addBox(-4, -8, -4, 8, 8, 8), PartPose.offset(0, 4, 0));
        partdefinition.addOrReplaceChild("ll", CubeListBuilder.create()
                .texOffs(0, 18)
                .mirror()
                .addBox(-1, 0, -1, 2, 12, 2), PartPose.offset(1.5f, 12, 0));
        partdefinition.addOrReplaceChild("rl", CubeListBuilder.create()
                .texOffs(0, 18)
                .addBox(-1, 0, -1, 2, 12, 2), PartPose.offset(-1.5f, 12, 0));
        partdefinition.addOrReplaceChild("la", CubeListBuilder.create()
                .texOffs(8, 18)
                .mirror()
                .addBox(-1, 0, -1, 2, 12, 2), PartPose.offset(4, 4, 0));
        partdefinition.addOrReplaceChild("ra", CubeListBuilder.create()
                .texOffs(8, 18)
                .addBox(-1, 0, -1, 2, 12, 2), PartPose.offset(-4, 4, 0));
        return  LayerDefinition.create(meshdefinition, 64, 32);
    }

    public void setupAnim(EnderlingEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.head.xRot = headPitch * ((float)Math.PI / 180F);
        this.head.yRot = netHeadYaw * ((float)Math.PI / 180F);
        this.torso.xRot = 0;
        this.rl.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        this.ll.xRot = Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
        this.ra.xRot = Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
        this.la.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        if (entityIn.LookingAtItem()) {
            this.head.xRot = 0.5F;
            this.head.yRot = 0.0F;
            this.ra.yRot = -0.5F;
            this.ra.xRot = -0.9F;
        }
    }

    @Override
    public void renderToBuffer(PoseStack matrixStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
        torso.render(matrixStack, buffer, packedLight, packedOverlay);
        head.render(matrixStack, buffer, packedLight, packedOverlay);
        ll.render(matrixStack, buffer, packedLight, packedOverlay);
        rl.render(matrixStack, buffer, packedLight, packedOverlay);
        la.render(matrixStack, buffer, packedLight, packedOverlay);
        ra.render(matrixStack, buffer, packedLight, packedOverlay);
    }


}
