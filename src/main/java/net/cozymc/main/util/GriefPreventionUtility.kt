package net.cozymc.main.util

import net.cozymc.main.exception.InvalidRegionException
import org.bukkit.Bukkit
import org.bukkit.Chunk
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.util.Vector
import org.junit.jupiter.api.assertDoesNotThrow
import java.io.File
import java.util.*

object GriefPreventionUtility {
    fun loadClaim(file: File): GriefPreventionClaim {
        TODO()
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
}

//class GriefPreventionClaim(val file: File) {
//    val owner: UUID
//    val lesserBoundaryCorner: Location
//    val greaterBoundaryCorner: Location
//    val world: World
//
//    init {
//        val config = YamlConfiguration.loadConfiguration(file)
//        owner = UUID.fromString(config.getString("Owner"))
//        lesserBoundaryCorner = getLocation(config.getString("Lesser Boundary Corner")!!)
//        greaterBoundaryCorner = getLocation(config.getString("Greater Boundary Corner")!!)
//        world = lesserBoundaryCorner.world
//    }
//
//    private fun getLocation(string: String): Location {
//        val parts = string.split(";")
//        return Location(Bukkit.getWorld(parts[0]), parts[1].toDouble(), parts[2].toDouble(), parts[3].toDouble())
//    }
//
//    fun getChunks(): Set<Chunk> {
//        val chunks = mutableSetOf<Chunk>()
//        val greaterBoundaryChunk =
//            Pair(this.greaterBoundaryCorner.chunk.x, this.greaterBoundaryCorner.chunk.z)
//        val lesserBoundaryChunk =
//            Pair(this.lesserBoundaryCorner.chunk.x, this.lesserBoundaryCorner.chunk.z)
//        for (x in lesserBoundaryChunk.first..greaterBoundaryChunk.first)
//            for (z in lesserBoundaryChunk.second..greaterBoundaryChunk.second)
//                chunks.add(this.world.getChunkAt(x, z))
//        return chunks
//    }
//
//    override fun toString(): String {
//        return "owner: $owner lesser boundary corner: $lesserBoundaryCorner, greater boundary corner: $greaterBoundaryCorner}"
//    }
//}