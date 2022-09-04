package net.cozymc.main

import com.sk89q.worldedit.bukkit.BukkitAdapter
import com.sk89q.worldedit.math.BlockVector2
import com.sk89q.worldguard.WorldGuard
import org.bukkit.Location

object WorldGuardUtils {

    fun isRegion(location: Location): Boolean {
        val instance = WorldGuard.getInstance() ?: return false
        val platform = instance.platform ?: return false
        val regionContainer = platform.regionContainer ?: return false
        return regionContainer.get(BukkitAdapter.adapt(location.world))?.regions?.any {
            it.value.contains(BlockVector2.at(location.x, location.z))
        } ?: false
    }
}