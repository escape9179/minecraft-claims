package net.cozymc.main.command

import logan.api.command.BasicCommand
import logan.api.command.SenderTarget
import net.cozymc.main.WorldGuard
import net.cozymc.main.file.Config
import net.cozymc.main.file.Data
import net.cozymc.main.util.getBalance
import org.bukkit.entity.Player

class ClaimCommand : BasicCommand<Player>(
    "claim",
    "cozyclaims.claim",
    0..0,
    target = SenderTarget.PLAYER,
) {
    override fun run(sender: Player, args: Array<out String>, data: Any?): Boolean {
        if (sender.getBalance() >= Config.getClaimCost()
            && Data.getClaimAt(sender.location) == null
            && WorldGuard.isRegion(sender.location)) {
            //TODO("Implementation")
        }
        return true
    }
}