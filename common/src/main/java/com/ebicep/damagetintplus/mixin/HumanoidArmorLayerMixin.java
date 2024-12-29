package com.ebicep.damagetintplus.mixin;

import com.ebicep.damagetintplus.DamageTintPlus;
import com.ebicep.damagetintplus.config.Config;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.EquipmentLayerRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidArmorLayer.class)
public class HumanoidArmorLayerMixin {

    @Final
    @Shadow
    private EquipmentLayerRenderer equipmentRenderer;

    @Inject(
            method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/client/renderer/entity/state/HumanoidRenderState;FF)V",
            at = @At("HEAD")
    )
    public void render(
            PoseStack poseStack,
            MultiBufferSource multiBufferSource,
            int i,
            HumanoidRenderState humanoidRenderState,
            float f,
            float g,
            CallbackInfo ci
    ) {
        DamageTintPlus.INSTANCE.setLastRenderer(equipmentRenderer);
        DamageTintPlus.INSTANCE.setLastHurt(humanoidRenderState.hasRedOverlay && Config.INSTANCE.getValues().getShowOnPlayerArmor());
    }

    @Inject(
            method = "renderArmorPiece",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/entity/layers/EquipmentLayerRenderer;renderLayers(Lnet/minecraft/client/resources/model/EquipmentClientInfo$LayerType;Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/client/model/Model;Lnet/minecraft/world/item/ItemStack;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V"
            )
    )
    public void render(
            PoseStack poseStack,
            MultiBufferSource multiBufferSource,
            ItemStack itemStack,
            EquipmentSlot equipmentSlot,
            int i,
            HumanoidModel<?> humanoidModel,
            CallbackInfo ci,
            @Local EquipmentClientInfo.LayerType layerType
    ) {
        DamageTintPlus.INSTANCE.setEquipmentLayerType(layerType);
    }

}
