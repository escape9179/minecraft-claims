package net.cozymc.main.util

import org.bukkit.Location
import org.bukkit.World

class BlockLocation private constructor(val world: World, val x: Int, val y: Int, val z: Int) {
    fun toBukkitLocation() = Location(world, x.toDouble(), y.toDouble(), z.toDouble())
    override fun equals(other: Any?) = other is BlockLocation && x == other.x && y == other.y && z == other.z
    companion object {
        fun from(location: Location) = BlockLocation(location.world, location.blockX, location.blockY, location.blockZ)
    }
}