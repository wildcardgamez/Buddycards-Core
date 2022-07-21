package com.wildcard.buddycards.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.wildcard.buddycards.entity.EnderlingEntity;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;

public class EnderlingModel<T extends EnderlingEntity> extends HumanoidModel<EnderlingEntity> {
    public EnderlingModel(ModelPart part) {
        super(part);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        partdefinition.addOrReplaceChild("body", CubeListBuilder.create()
                .texOffs(32, 4)
                .addBox(-3, -4, -2, 6, 8, 4)
                .texOffs(32, 16)
                .addBox(-4, -3, 2, 8, 6, 2)
                .texOffs(26, 16)
                .addBox(-1, -2, 3.5f, 2, 3, 1), PartPose.offset(0, 8, 0));
        partdefinition.addOrReplaceChild("head", CubeListBuilder.create()
                .texOffs(0, 0)
                .addBox(-4, -8, -4, 8, 8, 8), PartPose.offset(0, 4, 0));
        partdefinition.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.offset(0, 4, 0));
        partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create()
                .texOffs(0, 18)
                .mirror()
                .addBox(-1, 0, -1, 2, 12, 2), PartPose.offset(1.5f, 12, 0));
        partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create()
                .texOffs(0, 18)
                .addBox(-1, 0, -1, 2, 12, 2), PartPose.offset(-1.5f, 12, 0));
        partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create()
                .texOffs(8, 18)
                .mirror()
                .addBox(-1, 0, -1, 2, 12, 2), PartPose.offset(4, 4, 0));
        partdefinition.addOrReplaceChild("right_arm", CubeListBuilder.create()
                .texOffs(8, 18)
                .addBox(-1, 0, -1, 2, 12, 2), PartPose.offset(-4, 4, 0));
        return  LayerDefinition.create(meshdefinition, 64, 32);
    }

    public void setupAnim(EnderlingEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.head.xRot = headPitch * ((float)Math.PI / 180F);
        this.head.yRot = netHeadYaw * ((float)Math.PI / 180F);
        this.body.xRot = 0;
        this.rightLeg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        this.leftLeg.xRot = Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
        this.rightArm.xRot = Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
        this.leftArm.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        if (entityIn.LookingAtItem()) {
            this.head.xRot = 0.5F;
            this.head.yRot = 0.0F;
            this.rightArm.yRot = -0.5F;
            this.rightArm.xRot = -0.9F;
        }
        else
            this.rightArm.yRot = 0;
    }

    @Override
    public void renderToBuffer(PoseStack matrixStack, VertexConsumer buffer, int packedLight, int packedOverightLegay, float red, float green, float blue, float alpha){
        body.render(matrixStack, buffer, packedLight, packedOverightLegay);
        head.render(matrixStack, buffer, packedLight, packedOverightLegay);
        leftLeg.render(matrixStack, buffer, packedLight, packedOverightLegay);
        rightLeg.render(matrixStack, buffer, packedLight, packedOverightLegay);
        leftArm.render(matrixStack, buffer, packedLight, packedOverightLegay);
        rightArm.render(matrixStack, buffer, packedLight, packedOverightLegay);
    }

    @Override
    public void translateToHand(HumanoidArm p_102854_, PoseStack p_102855_) {
        super.translateToHand(p_102854_, p_102855_);
    }
}
