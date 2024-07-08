package com.ebicep.damagetintplus.mixin.neoforge;

import com.ebicep.damagetintplus.DamageTintPlus;
import com.ebicep.damagetintplus.config.Config;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import org.checkerframework.checker.units.qual.A;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidArmorLayer.class)
public class HumanoidArmorLayerMixin {

    @Unique
    boolean damagetintplus$hurt = false;

    @Inject(
            method = "renderArmorPiece",
            at = @At("HEAD")
    )
    public void renderArmorPiece(
            PoseStack poseStack,
            MultiBufferSource multiBufferSource,
            LivingEntity livingEntity,
            EquipmentSlot equipmentSlot,
            int i,
            HumanoidModel<?> humanoidModel,
            CallbackInfo ci
    ) {
        damagetintplus$hurt = livingEntity.hurtTime > 0;
    }

    @Inject(
            method = "renderModel(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/client/model/HumanoidModel;ILnet/minecraft/resources/ResourceLocation;)V",
            at = @At("HEAD"),
            cancellable = true
    )
    public void renderModel(
            PoseStack poseStack,
            MultiBufferSource multiBufferSource,
            int i,
            HumanoidModel<?> model,
            int j,
            ResourceLocation resourceLocation,
            CallbackInfo ci
    ) {
        if (damagetintplus$hurt && Config.INSTANCE.getValues().getShowOnPlayerArmor()) {
            VertexConsumer vertexConsumer = multiBufferSource.getBuffer(DamageTintPlus.INSTANCE.getOverrideRenderType(resourceLocation));
            model.renderToBuffer(poseStack, vertexConsumer, i, OverlayTexture.RED_OVERLAY_V, j);
            ci.cancel();
        }
    }

}
