package net.cozymc.main.command

import logan.api.command.BasicCommand
import logan.api.command.SenderTarget
import net.cozymc.main.file.DataConfig
import net.cozymc.main.file.MainConfig
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class ClaimUnclaimCommand : BasicCommand<CommandSender>(
    "unclaim", "cozyclaims.unclaim",
    0..1,
    target = SenderTarget.BOTH,
    parentCommand = "claim",
) {
    override fun run(sender: CommandSender, args: Array<out String>, data: Any?): Boolean {
        if (args.isEmpty()) {
            if (sender is Player) {
                val claim = DataConfig.removeClaim(sender.uniqueId) ?: run {
                    sender.sendMessage(MainConfig.getNotClaimOwnerMessage())
                    return true
                }
                sender.sendMessage(MainConfig.getClaimAbandonSuccessMessage(claim.chunks.size))
            } else {
                sender.sendMessage(MainConfig.getUsageSpecifyClaimNameMessage())
            }
        } else {
            val player = Bukkit.getPlayer(args[0]) ?: run {
                sender.sendMessage(MainConfig.getUnknownPlayerMessage())
                return true
            }
            val claim = DataConfig.removeClaim(player.uniqueId) ?: run {
                sender.sendMessage(MainConfig.getHasNoClaimMessage(player))
                return true
            }
            sender.sendMessage(MainConfig.getClaimRemoveSuccessOtherMessage(player, claim.chunks.size))
        }
        return true
    }
}