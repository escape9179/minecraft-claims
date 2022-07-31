package net.cozymc.main

import org.bukkit.Chunk
import org.bukkit.Location
import org.bukkit.configuration.serialization.ConfigurationSerializable
import org.bukkit.entity.Player
import java.util.UUID

class Claim(val owner: UUID, val location: Location) : ConfigurationSerializable {

    val chunks = mutableSetOf<Location>()

    fun addChunk(chunk: Chunk) {
        TODO("Implementation")
    }

    fun removeChunk(chunk: Chunk) {
        TODO("Implementation")
    }

    fun addMember(player: Player) {
        TODO("Implementation")
    }

    fun removeMember(player: Player) {
        TODO("Implementation")
    }

    override fun serialize(): MutableMap<String, Any> {
        TODO("Not yet implemented")
    }
}