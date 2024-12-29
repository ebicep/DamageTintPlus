package com.ebicep.damagetintplus

import com.ebicep.damagetintplus.config.Config
import com.ebicep.damagetintplus.config.ConfigScreen
import com.mojang.blaze3d.platform.NativeImage
import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.blaze3d.vertex.DefaultVertexFormat
import com.mojang.blaze3d.vertex.VertexFormat
import dev.architectury.event.events.client.ClientTickEvent
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.RenderStateShard
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.entity.layers.EquipmentLayerRenderer
import net.minecraft.client.resources.model.EquipmentClientInfo
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.TriState
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import java.awt.Color

const val MOD_ID = "damagetintplus"

object DamageTintPlus {

    val LOGGER: Logger = LogManager.getLogger(MOD_ID)
    var updateTintColor = false
    var equipmentLayerType: EquipmentClientInfo.LayerType? = null
    var lastRenderer: EquipmentLayerRenderer? = null
    var lastHurt: Boolean = false

    fun init() {
        Config.load()

        ClientTickEvent.CLIENT_POST.register {
            ConfigScreen.handleOpenScreen()
        }
    }

    fun getOverrideRenderType(resourceLocation: ResourceLocation): RenderType.CompositeRenderType {
        return RenderType.create(
            "damagetintplus_override",
            DefaultVertexFormat.NEW_ENTITY,
            VertexFormat.Mode.QUADS,
            256,
            true,
            false,
            RenderType.CompositeState
                .builder()
                .setShaderState(RenderStateShard.RENDERTYPE_ENTITY_CUTOUT_NO_CULL_SHADER)
                .setTextureState(RenderStateShard.TextureStateShard(resourceLocation, TriState.FALSE, false))
                .setTransparencyState(RenderStateShard.NO_TRANSPARENCY)
                .setCullState(RenderStateShard.NO_CULL)
                .setLightmapState(RenderStateShard.LIGHTMAP)
                .setOverlayState(RenderStateShard.OVERLAY)
                .setLayeringState(RenderStateShard.VIEW_OFFSET_Z_LAYERING)
                .setDepthTestState(RenderStateShard.LEQUAL_DEPTH_TEST)
                .createCompositeState(true)
        )
    }

    fun resetTintColor() {
        updateTintColor = false
        val texture = Minecraft.getInstance().gameRenderer.overlayTexture().texture
        val nativeImage: NativeImage? = texture.pixels

        if (nativeImage == null) {
            LOGGER.error("Failed to allocate memory for overlay color texture")
            return
        }

        val color: Int = getTintColor()

        LOGGER.info("Setting overlay color to $color")

        for (i in 0..15) {
            for (j in 0..15) {
                if (i < 8) {
                    nativeImage.setPixel(j, i, color)
                } else {
                    val k = ((1.0f - j.toFloat() / 15.0f * 0.75f) * 255.0f).toInt()
                    nativeImage.setPixel(j, i, k shl 24 or 16777215)
                }
            }
        }

        RenderSystem.activeTexture(33985)
        texture.bind()
        texture.setFilter(false, false)
        texture.setClamp(true)
        nativeImage.upload(0, 0, 0, 0, 0, nativeImage.width, nativeImage.height, false)
        RenderSystem.activeTexture(33984)
    }

    private fun getTintColor(): Int {
        return if (Config.values.overrideVanillaColor) {
            Color(Config.values.overrideColor, true).rgb
        } else {
            -1291911168
        }
    }

}