package net.cozymc.main.file

import net.cozymc.main.Claim
import net.cozymc.main.DATA_FILE_NAME
import net.cozymc.main.DATA_FOLDER_PATH
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.util.*

object DataConfig {
    private val file = File(DATA_FOLDER_PATH, DATA_FILE_NAME)
    private val config = YamlConfiguration.loadConfiguration(file)

    fun getClaim(owner: UUID): Claim? {
        return config.getSerializable("$owner.claim", Claim::class.java) //TODO Add default value.
    }

    fun saveClaim(owner: UUID, claim: Claim) {
        config.set("$owner.claims", claim)
        save()
    }

    private fun save() = config.save(file)
}