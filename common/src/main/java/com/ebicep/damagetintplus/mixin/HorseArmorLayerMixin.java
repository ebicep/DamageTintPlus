package com.ebicep.damagetintplus.mixin;

import com.ebicep.damagetintplus.DamageTintPlus;
import com.ebicep.damagetintplus.config.Config;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.EquipmentLayerRenderer;
import net.minecraft.client.renderer.entity.layers.HorseArmorLayer;
import net.minecraft.client.renderer.entity.state.HorseRenderState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HorseArmorLayer.class)
public class HorseArmorLayerMixin {

    @Final
    @Shadow
    private EquipmentLayerRenderer equipmentRenderer;

    @Inject(
            method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/client/renderer/entity/state/HorseRenderState;FF)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/entity/layers/EquipmentLayerRenderer;renderLayers(Lnet/minecraft/world/item/equipment/EquipmentModel$LayerType;Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/client/model/Model;Lnet/minecraft/world/item/ItemStack;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V"
            )
    )
    public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int i, HorseRenderState horseRenderState, float f, float g, CallbackInfo ci) {
        if (Config.INSTANCE.getValues().getShowOnHorseArmor()) {
            DamageTintPlus.INSTANCE.setLastRenderer(equipmentRenderer);
            DamageTintPlus.INSTANCE.setLastHurt(horseRenderState.hasRedOverlay);
        }
    }


}
