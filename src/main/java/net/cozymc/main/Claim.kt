package net.cozymc.main

import net.cozymc.main.file.DataConfig
import org.bukkit.Chunk
import org.bukkit.Location
import org.bukkit.configuration.serialization.ConfigurationSerializable
import org.bukkit.entity.Player
import java.util.UUID

class Claim(val owner: UUID, chunk: Chunk) : ConfigurationSerializable {
    val chunks = mutableSetOf(chunk)
    val members = mutableSetOf<UUID>()

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

    override fun serialize(): MutableMap<String, Any> {
        return mutableMapOf(
            "owner" to owner,
            "members" to members,
            "chunks" to chunks
        )
    }

    companion object {
        fun isClaimed(location: Location): Boolean {
            DataConfig.getClaims().forEach { if (it.chunks.contains(location.chunk)) return true }
            return false
        }

        fun getOrCreate(owner: UUID, chunk: Chunk): Claim {
            return DataConfig.getClaim(owner)?.also { it.addChunk(chunk) } ?: return Claim(owner, chunk)
        }
    }
}