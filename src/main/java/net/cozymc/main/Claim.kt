package net.cozymc.main

import net.cozymc.main.file.DataConfig
import net.cozymc.main.util.BlockLocation
import org.bukkit.BlockChangeDelegate
import org.bukkit.Bukkit
import org.bukkit.Chunk
import org.bukkit.Location
import org.bukkit.configuration.MemorySection
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.configuration.serialization.ConfigurationSerializable
import org.bukkit.entity.Player
import java.util.UUID

class Claim(val owner: UUID) : ConfigurationSerializable {
    val chunks = mutableSetOf<Chunk>()
    val members = mutableSetOf<UUID>()

    constructor(owner: UUID, chunk: Chunk) : this(owner) {
        chunks.add(chunk)
    }

    constructor(map: Map<String, Object>) : this(UUID.fromString(map["owner"].toString())) {
        chunks.addAll(
            (map["chunks"] as List<String>)
            .map { it.split(",") }
            .map { Bukkit.getWorld(it[0]).getChunkAt(it[1].toInt(), it[2].toInt()) }
        )
    }

    fun addChunk(chunk: Chunk) {
        chunks.add(chunk)
    }

    fun removeChunk(chunk: Chunk) {
        chunks.remove(chunk)
    }

    fun addMember(player: Player): Boolean {
        return members.add(player.uniqueId)
    }

    fun removeMember(player: Player): Boolean {
        return members.remove(player.uniqueId)
    }

    fun forEachChunk(func: (Chunk) -> Unit) {
        chunks.forEach(func)
    }

    fun containsLocation(location: Location): Boolean {
        return chunks.any { location.chunk == it }
    }

    fun containsLocation(location: BlockLocation): Boolean {
        return chunks.any { location.toBukkitLocation().chunk == it }
    }

    override fun serialize(): MutableMap<String, Any> {
        return mutableMapOf(
            "owner" to owner.toString(),
            "chunks" to chunks.map { "${it.world.name},${it.x},${it.z}" }
        )
    }

    companion object {
        fun isClaimed(location: Location): Boolean {
            DataConfig.getClaims().forEach { if (it.chunks.contains(location.chunk)) return true }
            return false
        }

        fun getClaimAt(location: Location): Claim? {
            return DataConfig.getClaims().firstOrNull { it.containsLocation(location) }
        }

        fun getClaimAt(location: BlockLocation): Claim? {
            return DataConfig.getClaims().firstOrNull { it.containsLocation(location) }
        }

        fun getOrCreate(owner: UUID, chunk: Chunk): Claim {
            return DataConfig.getClaim(owner)?.also { it.addChunk(chunk) } ?: return Claim(owner, chunk)
        }
    }
}