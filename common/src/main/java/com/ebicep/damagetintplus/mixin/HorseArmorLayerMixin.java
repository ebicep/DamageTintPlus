package com.ebicep.damagetintplus.mixin;

import com.ebicep.damagetintplus.config.Config;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.HorseArmorLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.animal.horse.Horse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HorseArmorLayer.class)
public class HorseArmorLayerMixin {

    @Unique
    boolean damagetintplus$hurt = false;

    @Inject(
            method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/animal/horse/Horse;FFFFFF)V",
            at = @At("HEAD")
    )
    public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int i, Horse horse, float f, float g, float h, float j, float k, float l, CallbackInfo ci) {
        damagetintplus$hurt = horse.hurtTime > 0;
    }

    @ModifyArg(
            method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/animal/horse/Horse;FFFFFF)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/model/HorseModel;renderToBuffer(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;III)V"
            ),
            index = 3
    )
    public int renderToBuffer(int i) {
        if (damagetintplus$hurt && Config.INSTANCE.getValues().getShowOnHorseArmor()) {
            return OverlayTexture.RED_OVERLAY_V;
        }
        return i;
    }


}
