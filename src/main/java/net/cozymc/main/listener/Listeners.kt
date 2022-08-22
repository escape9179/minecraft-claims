package net.cozymc.main.listener

import net.cozymc.main.file.MainConfig
import net.cozymc.main.util.getOccupyingClaim
import net.cozymc.main.util.isRelativeOfClaim
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent

class PlayerInteractListener : Listener {
    @EventHandler
    fun onPlayerInteract(event: PlayerInteractEvent) {
        if (!event.player.isRelativeOfClaim(event.player.getOccupyingClaim() ?: return)) {
            event.isCancelled = true
            event.player.sendMessage(MainConfig.getInteractNotAllowedMessage())
            return
        }
    }
}