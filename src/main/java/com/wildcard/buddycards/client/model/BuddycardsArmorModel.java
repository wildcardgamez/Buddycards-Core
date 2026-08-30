package com.wildcard.buddycards.client.model;

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

		//Helmet
		PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(1.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		head.addOrReplaceChild("horn_r1", CubeListBuilder.create().texOffs(0, 1).addBox(0.0F, -6.0F, 1.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.5F)), PartPose.offsetAndRotation(-5.0F, -5.5F, -1.0F, -0.2618F, -0.0436F, -0.0873F));
		head.addOrReplaceChild("horn_l1", CubeListBuilder.create().texOffs(24, 1).addBox(-2.0F, -6.0F, 1.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.5F)), PartPose.offsetAndRotation(5.0F, -5.5F, -1.0F, -0.2618F, 0.0436F, 0.0873F));
		head.addOrReplaceChild("horn_r2", CubeListBuilder.create().texOffs(56, 0).addBox(-1.0F, -3.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.5F)), PartPose.offsetAndRotation(-5.0F, -4.5F, 0.0F, -0.5672F, 0.0F, -1.309F));
		head.addOrReplaceChild("horn_l2", CubeListBuilder.create().texOffs(32, 0).addBox(-1.0F, -3.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.5F)), PartPose.offsetAndRotation(5.0F, -4.5F, 0.0F, -0.5672F, 0.0F, 1.309F));
		//Torso
		partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(16, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(1.01F))
				.texOffs(16, 32).addBox(-9.0F, -3.0F, 3.0F, 18.0F, 13.0F, 1.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		partdefinition.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(32, 48).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(1.0F)), PartPose.offset(-5.0F, 2.0F, 0.0F));
		partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(40, 16).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(1.0F)), PartPose.offset(5.0F, 2.0F, 0.0F));

		if (slot.equals(EquipmentSlot.LEGS)) {
			//Legs
			partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(16, 48).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.9F)).mirror(false)
					.texOffs(0, 48).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(1.4F)).mirror(false), PartPose.offset(-1.9F, 12.0F, 0.0F));
			partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(0, 16).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.9F)).mirror(false)
					.texOffs(0, 32).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(1.4F)).mirror(false), PartPose.offset(1.9F, 12.0F, 0.0F));
		} else {
			//Feet
			partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(16, 48).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(1.0F)).mirror(false)
					.texOffs(0, 48).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(1.5F)).mirror(false), PartPose.offset(-1.9F, 12.0F, 0.0F));
			partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(0, 16).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(1.0F)).mirror(false)
					.texOffs(0, 32).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(1.5F)).mirror(false), PartPose.offset(1.9F, 12.0F, 0.0F));
		}
		return LayerDefinition.create(meshdefinition, 64, 64);
	}
}