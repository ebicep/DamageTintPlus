package com.ebicep.damagetintplus.mixin;

import com.ebicep.damagetintplus.DamageTintPlus;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ItemStackRenderState.LayerRenderState.class)
public class LayerRenderStateMixin {

    @ModifyArg(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/entity/ItemRenderer;renderItem(Lnet/minecraft/world/item/ItemDisplayContext;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;II[ILnet/minecraft/client/resources/model/BakedModel;Lnet/minecraft/client/renderer/RenderType;Lnet/minecraft/client/renderer/item/ItemStackRenderState$FoilType;)V"
            ),
            index = 7
    )
    private RenderType renderType(RenderType renderType, @Local(argsOnly = true, ordinal = 1) int j) {
        if (j == OverlayTexture.RED_OVERLAY_V) {
            RenderType.CompositeRenderType type = (RenderType.CompositeRenderType) renderType;
            if ((Object) type instanceof ICompositeRenderTypeMixin compositeRenderTypeMixin) {
                RenderType.CompositeState compositeState = compositeRenderTypeMixin.callState();
                if ((Object) compositeState instanceof IMixinCompositeState mixinCompositeState) {
                    RenderStateShard.EmptyTextureStateShard textureState = mixinCompositeState.getTextureState();
                    ResourceLocation resourceLocation = null;
                    if (textureState instanceof IMixinTextureStateShard textureStateShard) {
                        resourceLocation = textureStateShard.callCutoutTexture().orElse(null);
                    } else if (textureState instanceof IMixinMultiTextureStateShard multiTextureStateShard) {
                        resourceLocation = multiTextureStateShard.callCutoutTexture().orElse(null);
                    }
                    if (resourceLocation != null) {
                        return DamageTintPlus.INSTANCE.getOverrideRenderType(resourceLocation);
                    }
                }
            }
        }
        return renderType;
    }

}
