package net.cozymc.main

import com.sk89q.worldedit.bukkit.BukkitAdapter
import com.sk89q.worldedit.math.BlockVector2
import com.sk89q.worldguard.WorldGuard
import com.sk89q.worldguard.bukkit.WorldGuardPlugin
import org.bukkit.Location

object WorldGuard {

    fun isRegion(location: Location): Boolean {
        return WorldGuard.getInstance().platform.regionContainer.get(BukkitAdapter.adapt(location.world))?.regions?.any {
            it.value.contains(BlockVector2.at(location.x, location.z))
        } ?: false
    }
}