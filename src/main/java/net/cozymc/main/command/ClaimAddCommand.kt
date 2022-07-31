package net.cozymc.main.command

import logan.api.command.BasicCommand
import logan.api.command.SenderTarget
import net.cozymc.main.file.DataConfig
import net.cozymc.main.file.MainConfig
import org.bukkit.Bukkit
import org.bukkit.entity.Player

class ClaimAddCommand : BasicCommand<Player>(
    "add", "cozyclaims.claim.add",
    1..1,
    target = SenderTarget.PLAYER,
    parentCommand = "claim",
) {
    override fun run(sender: Player, args: Array<out String>, data: Any?): Boolean {
        DataConfig.getClaim(sender.uniqueId)?.add(Bukkit.getPlayer(args[0]) ?: run {
            sender.sendMessage(MainConfig.getUnknownPlayerMessage())
            return true
        }) ?: run {
            sender.sendMessage(MainConfig.getUnknownClaimMessage())
            return true
        }
        return true
    }
}