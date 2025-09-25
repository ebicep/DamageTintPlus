package com.ebicep.damagetintplus.mixin;

import com.ebicep.damagetintplus.config.Config;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.entity.state.ArmedEntityRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ItemInHandLayer.class)
public class ItemInHandLayerMixin {

    @ModifyArg(
            method = "renderArmWithItem",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/item/ItemStackRenderState;render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;II)V"
            ),
            index = 3
    )
    private int renderToBuffer(int i, @Local(argsOnly = true) ArmedEntityRenderState armedEntityRenderState) {
        if (Config.INSTANCE.getValues().getShowOnItems() && armedEntityRenderState.hasRedOverlay) {
            return OverlayTexture.RED_OVERLAY_V;
        }
        return i;
    }


}
