package net.cozymc.main.file

import net.cozymc.main.DATA_FILE_NAME
import net.cozymc.main.DATA_FOLDER_PATH
import net.cozymc.main.claim.Claim
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.nio.file.Files
import java.util.*

object DataConfig {
    private val file = File(DATA_FOLDER_PATH, DATA_FILE_NAME)
    private var config = YamlConfiguration.loadConfiguration(file)
    var nextId = 0

    init {
        nextId = config.getInt("nextId", 0)
    }

    fun getClaimOwners(): List<UUID> {
        return config.getKeys(false).map(UUID::fromString)
    }

    fun loadClaims(): Set<Claim> {
        return config.getKeys(false)
            .map { key -> config.getSerializable(key, Claim::class.java)!! }
            .toSet()
    }

    fun loadMemberClaim(member: UUID): Claim? {
        return loadClaims().firstOrNull { member in it.members }
    }

    fun loadOwnerClaim(owner: UUID): Claim? {
        return loadClaims().firstOrNull {it.owner == owner}
    }

    fun saveClaim(claim: Claim) {
        config.set("${claim.id}", claim)
        save()
    }

    fun removeClaim(claim: Claim): Claim? {
        return removeClaim(claim.owner)
    }

    fun removeClaim(owner: UUID): Claim? {
        val claim = loadClaims().firstOrNull { owner == it.owner }
        config.set(claim?.id.toString(), null)
        save()
        return claim
    }

    fun delete() {
        file.delete()
        config = YamlConfiguration.loadConfiguration(file)
    }

    private fun save() = config.save(file)
}