package com.ebicep.damagetintplus.mixin.fabric;

import com.ebicep.damagetintplus.DamageTintPlus;
import com.ebicep.damagetintplus.config.Config;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.armortrim.ArmorTrim;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidArmorLayer.class)
public class HumanoidArmorLayerMixin {

    @Unique
    boolean damagetintplus$hurt = false;
    @Final
    @Shadow
    private TextureAtlas armorTrimAtlas;

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
            method = "renderModel",
            at = @At("HEAD"),
            cancellable = true
    )
    public void renderModel(
            PoseStack poseStack,
            MultiBufferSource multiBufferSource,
            int i,
            HumanoidModel<?> humanoidModel,
            int j,
            ResourceLocation resourceLocation,
            CallbackInfo ci
    ) {
        if (damagetintplus$hurt && Config.INSTANCE.getValues().getShowOnPlayerArmor()) {
            VertexConsumer vertexConsumer = multiBufferSource.getBuffer(DamageTintPlus.INSTANCE.getOverrideRenderType(resourceLocation));
            humanoidModel.renderToBuffer(poseStack, vertexConsumer, i, OverlayTexture.RED_OVERLAY_V, j);
            ci.cancel();
        }
    }

    @Inject(
            method = "renderTrim",
            at = @At("HEAD"),
            cancellable = true
    )
    public void renderTrim(
            Holder<ArmorMaterial> holder,
            PoseStack poseStack,
            MultiBufferSource multiBufferSource,
            int i,
            ArmorTrim armorTrim,
            HumanoidModel<?> humanoidModel,
            boolean bl,
            CallbackInfo ci
    ) {
        if (damagetintplus$hurt && Config.INSTANCE.getValues().getShowOnPlayerArmor()) {
            TextureAtlasSprite textureAtlasSprite = this.armorTrimAtlas.getSprite(bl ? armorTrim.innerTexture(holder) : armorTrim.outerTexture(holder));
            VertexConsumer vertexConsumer = textureAtlasSprite.wrap(multiBufferSource.getBuffer(DamageTintPlus.INSTANCE.getOverrideRenderType(Sheets.ARMOR_TRIMS_SHEET)));
            humanoidModel.renderToBuffer(poseStack, vertexConsumer, i, OverlayTexture.RED_OVERLAY_V);
            ci.cancel();
        }
    }

}
