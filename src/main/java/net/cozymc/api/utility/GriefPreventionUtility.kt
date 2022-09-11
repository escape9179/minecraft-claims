package net.cozymc.main.util

import net.cozymc.main.exception.InvalidRegionException
import net.cozymc.main.file.MainConfig
import org.bukkit.Bukkit
import org.bukkit.Chunk
import org.bukkit.Location
import org.bukkit.configuration.file.YamlConfiguration
import org.junit.jupiter.api.Assertions
import java.io.File
import java.io.IOException
import java.nio.file.*
import java.nio.file.attribute.BasicFileAttributes
import java.util.*
import kotlin.io.path.name

object GriefPreventionUtility {
    fun loadAllClaims(path: String): Set<GriefPreventionClaim> {
        val claims = mutableSetOf<GriefPreventionClaim>()
        Files.walkFileTree(Paths.get(path), object : FileVisitor<Path> {
            override fun preVisitDirectory(dir: Path?, attrs: BasicFileAttributes?): FileVisitResult {
                return FileVisitResult.CONTINUE
            }

            override fun visitFile(file: Path?, attrs: BasicFileAttributes?): FileVisitResult {
                if (file!!.name != "_nextClaimID") {
                    println("Loading claim from file: ${file.name}")
                    claims.add(loadClaim(path, file.name))
                }
                return FileVisitResult.CONTINUE
            }

            override fun visitFileFailed(file: Path?, exc: IOException?): FileVisitResult {
                return FileVisitResult.CONTINUE
            }

            override fun postVisitDirectory(dir: Path?, exc: IOException?): FileVisitResult {
                return FileVisitResult.CONTINUE
            }
        })
        return claims
    }

    private fun loadClaim(parentName: String, fileName: String): GriefPreventionClaim {
        val config = YamlConfiguration.loadConfiguration(File(parentName, fileName))
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