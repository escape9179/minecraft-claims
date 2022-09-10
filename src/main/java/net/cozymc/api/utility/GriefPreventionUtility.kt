package net.cozymc.main.util

import net.cozymc.main.exception.InvalidRegionException
import org.bukkit.Bukkit
import org.bukkit.Chunk
import org.bukkit.Location
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.util.*

object GriefPreventionUtility {
    fun loadClaim(file: File): GriefPreventionClaim {
        val config = YamlConfiguration.loadConfiguration(file)
        val owner = UUID.fromString(config.getString("Owner"))
        val lesserCorner = getLocation(config.getString("Lesser Boundary Corner")!!)
        val greaterCorner = getLocation(config.getString("Greater Boundary Corner")!!)
        return GriefPreventionClaim(owner, lesserCorner, greaterCorner)
    }

    private fun getLocation(string: String): Location {
        val parts = string.split(";")
        return Location(Bukkit.getWorld(parts[0]), parts[1].toDouble(), parts[2].toDouble(), parts[3].toDouble())
    }
}

class GriefPreventionClaim(val owner: UUID, val lesserCorner: Location, val greaterCorner: Location) {
    init {
        if (lesserCorner.x > greaterCorner.x || lesserCorner.y > greaterCorner.y || lesserCorner.z > greaterCorner.z)
            throw InvalidRegionException("lesser corner is further than greater corner")
        if (lesserCorner.world != greaterCorner.world)
            throw InvalidRegionException("region corners are in different worlds")
    }

    fun getChunks(): Set<Chunk> {
        val chunks = mutableSetOf<Chunk>()
        val greaterBoundaryChunk =
            Pair(this.greaterCorner.chunk.x, this.greaterCorner.chunk.z)
        val lesserBoundaryChunk =
            Pair(this.lesserCorner.chunk.x, this.lesserCorner.chunk.z)
        for (x in lesserBoundaryChunk.first..greaterBoundaryChunk.first)
            for (z in lesserBoundaryChunk.second..greaterBoundaryChunk.second)
                chunks.add(this.lesserCorner.world.getChunkAt(x, z))
        return chunks
    }

    override fun toString(): String {
        return """
            owner: $owner 
            lesser corner: $lesserCorner
            greater boundary corner: $greaterCorner""".trimIndent()
    }

    override fun equals(other: Any?): Boolean {
        return (other is GriefPreventionClaim &&
                other.owner == owner &&
                other.lesserCorner == lesserCorner &&
                other.greaterCorner == greaterCorner)
    }

    override fun hashCode(): Int {
        return 31 * owner.hashCode() + lesserCorner.hashCode() + greaterCorner.hashCode()
    }
}