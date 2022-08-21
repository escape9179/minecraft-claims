package net.cozymc.main.claim

import logan.api.command.BasicCommand
import logan.api.command.SenderTarget
import net.cozymc.main.file.MainConfig
import net.cozymc.main.util.getOccupyingClaim
import net.cozymc.main.util.loadOwnerClaim
import org.bukkit.entity.Player

class ClaimInfoCommand : BasicCommand<Player>(
    "info", "claim.info", 0..0,
    target = SenderTarget.PLAYER, parentCommand = "claim", usage = """
        Error.
        Usage: /claim info
    """.trimIndent()
) {
    override fun run(sender: Player, args: Array<out String>, data: Any?): Boolean {
        val claim = sender.getOccupyingClaim() ?: run {
            sender.sendMessage(MainConfig.getNotInsideClaimMessage())
            return true
        }

        MainConfig.getClaimInfoDisplay().map {
            it.replace("%owner", claim.owner.toString())
                .replace("%members", claim.members.toString())
                .replace("%trustees", claim.trustees.toString())
                .replace("%size", claim.size.toString())
        }.forEach(sender::sendMessage)
        return true
    }
}