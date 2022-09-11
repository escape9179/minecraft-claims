package net.cozymc.main.griefprevention

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.configuration.file.YamlConfiguration
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