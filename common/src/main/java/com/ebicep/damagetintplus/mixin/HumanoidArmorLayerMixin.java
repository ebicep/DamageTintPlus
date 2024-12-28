package com.ebicep.damagetintplus.mixin;

import com.ebicep.damagetintplus.DamageTintPlus;
import com.ebicep.damagetintplus.config.Config;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.EquipmentLayerRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
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
        if (Config.INSTANCE.getValues().getShowOnPlayerArmor()) {
            DamageTintPlus.INSTANCE.setLastRenderer(equipmentRenderer);
            DamageTintPlus.INSTANCE.setLastHurt(humanoidRenderState.hasRedOverlay);
        }
    }

}
