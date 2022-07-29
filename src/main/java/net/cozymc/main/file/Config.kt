package net.cozymc.main.file

import net.cozymc.main.CONFIG_FILE_NAME
import net.cozymc.main.DATA_FOLDER_PATH
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

object Config {
    var file = File(DATA_FOLDER_PATH, CONFIG_FILE_NAME)
    var config = YamlConfiguration.loadConfiguration(file)

    fun getClaimCost(): Int = TODO("Implementation")

    fun getChunkClaimSuccessMessage(): String = TODO("Implementation")

    fun getChunkClaimFailureMoneyMessage(): String = TODO("Implementation")

    fun getChunkClaimFailureClaimedMessage(): String = TODO("Implementation")

    fun getChunkClaimFailureRegionMessage(): String = TODO("Implementation")

    fun reload() {
        config = YamlConfiguration.loadConfiguration(file)
    }
}