package net.cozymc.main.file

import net.cozymc.main.claim.Claim
import net.cozymc.main.DATA_FILE_NAME
import net.cozymc.main.DATA_FOLDER_PATH
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.util.*

object DataConfig {
    private val file = File(DATA_FOLDER_PATH, DATA_FILE_NAME)
    val config = YamlConfiguration.loadConfiguration(file)

    fun getClaims(): List<UUID> {
        return config.getKeys(false).map(UUID::fromString)
    }

    fun loadClaims(): Set<Claim> {
        return config.getKeys(false)
            .map { key -> config.getSerializable("$key.claim", Claim::class.java) }
            .toSet()
    }

    fun loadClaim(owner: UUID): Claim? {
        return config.getSerializable("$owner.claim", Claim::class.java)
    }

    fun saveClaim(claim: Claim) {
        config.set("${claim.owner}.claim", claim)
        save()
    }

    fun removeClaim(owner: UUID): Claim? {
        return loadClaim(owner).also { config.set("$owner.claim", null); save() }
    }

    private fun save() = config.save(file)
}