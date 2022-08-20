package net.cozymc.main.file

import net.cozymc.main.DATA_FILE_NAME
import net.cozymc.main.DATA_FOLDER_PATH
import net.cozymc.main.claim.Claim
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.util.*

object DataConfig {
    private val file = File(DATA_FOLDER_PATH, DATA_FILE_NAME)
    private val config = YamlConfiguration.loadConfiguration(file)

    fun getClaimOwners(): List<UUID> {
        return config.getKeys(false).map(UUID::fromString)
    }

    fun loadClaims(): Set<Claim> {
        return config.getKeys(false)
            .map { key -> config.getSerializable("$key.claim", Claim::class.java) }
            .toSet()
    }

    fun loadMemberClaim(member: UUID): Claim? {
        return loadClaims().firstOrNull { member in it.members }
    }

    fun loadOwnerClaim(owner: UUID): Claim? {
        return loadClaims().firstOrNull {it.owner == owner}
    }

    fun saveClaim(claim: Claim) {
        config.set("${claim.owner}.claim", claim)
        save()
    }

    fun removeClaim(owner: UUID): Claim? {
        return this.loadOwnerClaim(owner).also { config.set("$owner.claim", null); save() }
    }

    private fun save() = config.save(file)
}