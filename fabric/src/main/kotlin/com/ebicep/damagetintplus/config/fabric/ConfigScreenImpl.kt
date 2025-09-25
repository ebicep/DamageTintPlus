package com.ebicep.damagetintplus.config.fabric

import com.ebicep.damagetintplus.config.Config
import me.shedaniel.clothconfig2.api.ConfigBuilder
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder
import me.shedaniel.math.Color
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component
import java.util.function.Consumer

object ConfigScreenImpl {

    private fun ConfigEntryBuilder.booleanToggle(translatable: String, variable: Boolean, saveConsumer: Consumer<Boolean>) =
        startBooleanToggle(Component.translatable(translatable), variable)
            .setDefaultValue(variable)
            .setTooltip(Component.translatable("$translatable.tooltip"))
            .setSaveConsumer(saveConsumer)
            .build()


    @JvmStatic
    fun getConfigScreen(previousScreen: Screen? = null): Screen {
        val builder: ConfigBuilder = ConfigBuilder.create()
            .setParentScreen(previousScreen)
            .setTitle(Component.translatable("damageTintPlus.title"))
            .setSavingRunnable(Config::save)
            .transparentBackground()
        val entryBuilder: ConfigEntryBuilder = builder.entryBuilder()
        val general = builder.getOrCreateCategory(Component.translatable("damageTintPlus.config.general.title"))
        general.addEntry(
            entryBuilder.booleanToggle("damageTintPlus.config.showOnPlayerArmor.enabled", Config.values.showOnPlayerArmor)
            { Config.values.showOnPlayerArmor = it }
        )
        general.addEntry(
            entryBuilder.booleanToggle("damageTintPlus.config.showOnHorseArmor.enabled", Config.values.showOnHorseArmor)
            { Config.values.showOnHorseArmor = it }
        )
        general.addEntry(
            entryBuilder.booleanToggle("damageTintPlus.config.showOnItems.enabled", Config.values.showOnItems)
            { Config.values.showOnItems = it }
        )
        general.addEntry(
            entryBuilder.booleanToggle("damageTintPlus.config.overrideColorEnabled", Config.values.overrideVanillaColor)
            { Config.values.overrideVanillaColor = it }
        )
        var color = Color.ofTransparent(Config.values.overrideColor)
        color = Color.ofRGBA(color.red, color.green, color.blue, 255 - color.alpha)
        general.addEntry(
            entryBuilder.startAlphaColorField(Component.translatable("damageTintPlus.config.overrideColor"), color)
                .setTooltip(Component.translatable("damageTintPlus.config.overrideColor.tooltip"))
                .setAlphaMode(true)
                .setDefaultValue2 { color }
                .setSaveConsumer2 { Config.values.overrideColor = Color.ofRGBA(it.red, it.green, it.blue, 255 - it.alpha).color }
                .build()
        )
        return builder.build()
    }

}
