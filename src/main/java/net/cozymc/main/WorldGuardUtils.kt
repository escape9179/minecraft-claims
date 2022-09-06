package net.cozymc.main

import com.sk89q.worldedit.bukkit.BukkitAdapter
import com.sk89q.worldedit.math.BlockVector2
import com.sk89q.worldguard.WorldGuard
import org.bukkit.Location

object WorldGuardUtils {

    fun isRegion(location: Location): Boolean {
        val instance = WorldGuard.getInstance()!!
        val platform = instance.platform!!
        val regionContainer = platform.regionContainer!!
        return regionContainer.get(BukkitAdapter.adapt(location.world))?.regions?.any {
            it.value.contains(BlockVector2.at(location.x, location.z))
        } ?: false
    }
}