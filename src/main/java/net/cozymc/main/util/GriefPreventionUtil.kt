package net.cozymc.main.util

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.util.*

object GriefPreventionUtil {
    fun getClaim(file: File): GriefPreventionClaim {
        return GriefPreventionClaim(file)
    }
}

class GriefPreventionClaim(val file: File) {
    val owner: UUID
    val lesserBoundaryCorner: Location
    val greaterBoundaryCorner: Location
    val world: World

    init {
        val config = YamlConfiguration.loadConfiguration(file)
        owner = UUID.fromString(config.getString("Owner"))
        lesserBoundaryCorner = getLocation(config.getString("Lesser Boundary Corner")!!)
        greaterBoundaryCorner = getLocation(config.getString("Greater Boundary Corner")!!)
        world = lesserBoundaryCorner.world
    }

    private fun getLocation(string: String): Location {
        val parts = string.split(";")
        return Location(Bukkit.getWorld(parts[0]), parts[1].toDouble(), parts[2].toDouble(), parts[3].toDouble())
    }

    override fun toString(): String {
        return "owner: $owner lesser boundary corner: $lesserBoundaryCorner, greater boundary corner: $greaterBoundaryCorner}"
    }
}