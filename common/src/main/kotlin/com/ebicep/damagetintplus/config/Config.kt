package com.ebicep.damagetintplus.config

import com.ebicep.damagetintplus.DamageTintPlus
import com.ebicep.damagetintplus.MOD_ID
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.awt.Color
import java.io.File

private val json = Json {
    encodeDefaults = true
    ignoreUnknownKeys = true
    prettyPrint = true
}
val configDirectoryPath: String
    get() = ConfigDirectory.getConfigDirectory().toString() + "\\damagetintplus"

object Config {

    var values = ConfigVariables()

    fun save() {
        val configDirectory = File(configDirectoryPath)
        if (!configDirectory.exists()) {
            configDirectory.mkdir()
        }
        val configFile = File(configDirectory, "$MOD_ID.json")
        configFile.writeText(json.encodeToString(ConfigVariables.serializer(), values))
    }

    fun load() {
        DamageTintPlus.LOGGER.info("Config Directory: ${ConfigDirectory.getConfigDirectory().toAbsolutePath().normalize()}")
        val configDirectory = File(configDirectoryPath)
        if (!configDirectory.exists()) {
            return
        }
        val configFile = File(configDirectory, "$MOD_ID.json")
        if (configFile.exists()) {
            val json = Json {
                prettyPrint = true
                ignoreUnknownKeys = true
                encodeDefaults = true
            }
            values = json.decodeFromString(ConfigVariables.serializer(), configFile.readText())
            loadValues()
        }
    }

    private fun loadValues() {
        if (values.overrideVanillaColor) {
            DamageTintPlus.updateTintColor = true
        }
    }

}

@Serializable
data class ConfigVariables(
    var showOnPlayerArmor: Boolean = true,
    var showOnHorseArmor: Boolean = true,
    var showOnItems: Boolean = true,
) {
    var overrideVanillaColor: Boolean = true
        set(value) {
            if (value != field) {
                field = value
                DamageTintPlus.updateTintColor = true
            }
        }
    var overrideColor: Int = Color(255, 0, 0, 178).rgb // -1291911168
        set(value) {
            if (value != field) {
                field = value
                DamageTintPlus.updateTintColor = true
            }
        }
}
