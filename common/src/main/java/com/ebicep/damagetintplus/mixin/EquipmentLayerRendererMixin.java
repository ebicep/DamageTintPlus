package com.ebicep.damagetintplus.mixin;

import com.ebicep.damagetintplus.DamageTintPlus;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.layers.EquipmentLayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(EquipmentLayerRenderer.class)
public class EquipmentLayerRendererMixin {

    @ModifyArg(
            method = "renderLayers(Lnet/minecraft/world/item/equipment/EquipmentModel$LayerType;Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/client/model/Model;Lnet/minecraft/world/item/ItemStack;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/resources/ResourceLocation;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/entity/ItemRenderer;getArmorFoilBuffer(Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/client/renderer/RenderType;Z)Lcom/mojang/blaze3d/vertex/VertexConsumer;"
            ),
            index = 1
    )
    private RenderType renderToBuffer(RenderType arg2, @Local(ordinal = 2) ResourceLocation resourceLocation3) {
        if (
                DamageTintPlus.INSTANCE.getLastRenderer() == (Object) this &&
                        DamageTintPlus.INSTANCE.getLastHurt()
        ) {
            return DamageTintPlus.INSTANCE.getOverrideRenderType(resourceLocation3);
        }
        return arg2;
    }

    @ModifyArg(
            method = "renderLayers(Lnet/minecraft/world/item/equipment/EquipmentModel$LayerType;Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/client/model/Model;Lnet/minecraft/world/item/ItemStack;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/resources/ResourceLocation;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/model/Model;renderToBuffer(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;III)V"
            ),
            index = 3
    )
    private int renderToBuffer(int i) {
        if (
                DamageTintPlus.INSTANCE.getLastRenderer() == (Object) this &&
                        DamageTintPlus.INSTANCE.getLastHurt()
        ) {
            return OverlayTexture.RED_OVERLAY_V;
        }
        return i;
    }

}
