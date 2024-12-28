package com.ebicep.damagetintplus.mixin;

import com.ebicep.damagetintplus.config.Config;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(CustomHeadLayer.class)
public class CustomHeadLayerMixin {

    @ModifyArg(
            method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;FF)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/entity/ItemRenderer;render(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemDisplayContext;ZLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;IILnet/minecraft/client/resources/model/BakedModel;)V"
            ),
            index = 6
    )
    private int renderToBuffer(int i, @Local(argsOnly = true) LivingEntityRenderState livingEntityRenderState) {
        if (livingEntityRenderState.hasRedOverlay && Config.INSTANCE.getValues().getShowOnPlayerArmor()) {
            return OverlayTexture.RED_OVERLAY_V;
        }
        return i;
    }

}
