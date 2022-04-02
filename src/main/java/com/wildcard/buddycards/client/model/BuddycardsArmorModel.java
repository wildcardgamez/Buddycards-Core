package com.wildcard.buddycards.client.model;// Made with Blockbench 4.0.0-beta.0
// Exported for Minecraft version 1.17 with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;

public class BuddycardsArmorModel<T extends LivingEntity> extends HumanoidModel<T> {

	public BuddycardsArmorModel(ModelPart root) {
		super(root);
	}

	public static LayerDefinition createBodyLayer(EquipmentSlot slot) {
		MeshDefinition meshdefinition = HumanoidModel.createMesh(CubeDeformation.NONE, 0);
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(1.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		head.addOrReplaceChild("right_horn_r1", CubeListBuilder.create().texOffs(0, 1).addBox(-1.0F, -4.0F, -1.25F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.85F)), PartPose.offsetAndRotation(-4.0F, -6.0F, 1.25F, -0.3491F, 0.0F, -0.1309F));
		head.addOrReplaceChild("left_horn_r1", CubeListBuilder.create().texOffs(24, 1).addBox(-1.0F, -4.0F, -1.25F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.85F)), PartPose.offsetAndRotation(4.0F, -6.0F, 1.25F, -0.3491F, 0.0F, 0.1309F));

		PartDefinition left_arm = partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(40, 16).mirror().addBox(0.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(1.0F)).mirror(false), PartPose.offset(4.0F, 2.0F, 0.0F));
		left_arm.addOrReplaceChild("left_shoulder_r1", CubeListBuilder.create().texOffs(18, 33).addBox(3.5F, -3.0F, -2.0F, 5.0F, 5.0F, 4.0F, new CubeDeformation(1.1F)), PartPose.offsetAndRotation(-2.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.1745F));

		PartDefinition right_arm = partdefinition.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(40, 16).addBox(-4.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(1.0F)), PartPose.offset(-4.0F, 2.0F, 0.0F));
		right_arm.addOrReplaceChild("right_shoulder_r1", CubeListBuilder.create().texOffs(0, 33).addBox(-7.5F, -3.0F, -2.0F, 5.0F, 5.0F, 4.0F, new CubeDeformation(1.1F)), PartPose.offsetAndRotation(2.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.1745F));
		if(!slot.equals(EquipmentSlot.LEGS)) {
			partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(16, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(1.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

			PartDefinition left_leg = partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(0, 16).mirror().addBox(-2.0F, 0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(1.0F)).mirror(false), PartPose.offset(2.5F, 18.0F, 0.0F));
			left_leg.addOrReplaceChild("left_tongue_r1", CubeListBuilder.create().texOffs(18, 42).addBox(-2.0F, -1.0F, -2.0F, 5.0F, 2.0F, 4.0F, new CubeDeformation(1.1F)), PartPose.offsetAndRotation(1.0F, 5.5F, 0.0F, 0.0F, 0.0F, -0.2618F));

			PartDefinition right_leg = partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(1.0F)), PartPose.offset(-2.5F, 18.0F, 0.0F));
			right_leg.addOrReplaceChild("right_tongue_r1", CubeListBuilder.create().texOffs(0, 42).addBox(-3.0F, -1.0F, -2.0F, 5.0F, 2.0F, 4.0F, new CubeDeformation(1.1F)), PartPose.offsetAndRotation(-1.0F, 5.5F, 0.0F, 0.0F, 0.0F, 0.2618F));
		}
		else {
			partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(16, 48).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.5F)), PartPose.offset(0.0F, 0.0F, 0.0F));

			PartDefinition left_leg2 = partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(0, 48).mirror().addBox(-2.0F, 0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.5F)).mirror(false), PartPose.offset(2.5F, 16.75F, 0.0F));
			left_leg2.addOrReplaceChild("left_bump_r1", CubeListBuilder.create().texOffs(36, 37).addBox(-1.0F, -0.5F, -2.0F, 3.0F, 1.0F, 4.0F, new CubeDeformation(0.7F)), PartPose.offsetAndRotation(1.0F, 3.25F, 0.0F, 0.0F, 0.0F, -0.3927F));

			PartDefinition right_leg2 = partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 48).addBox(-2.0F, 0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.5F)), PartPose.offset(-2.5F, 16.75F, 0.0F));
			right_leg2.addOrReplaceChild("right_bump_r1", CubeListBuilder.create().texOffs(36, 32).addBox(-2.0F, -0.5F, -2.0F, 3.0F, 1.0F, 4.0F, new CubeDeformation(0.7F)), PartPose.offsetAndRotation(-1.0F, 3.25F, 0.0F, 0.0F, 0.0F, 0.3927F));
		}
		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		head.render(poseStack, buffer, packedLight, packedOverlay);
		body.render(poseStack, buffer, packedLight, packedOverlay);
		leftArm.render(poseStack, buffer, packedLight, packedOverlay);
		rightArm.render(poseStack, buffer, packedLight, packedOverlay);
		leftLeg.render(poseStack, buffer, packedLight, packedOverlay);
		rightLeg.render(poseStack, buffer, packedLight, packedOverlay);
	}
}