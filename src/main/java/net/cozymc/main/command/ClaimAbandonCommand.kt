package net.cozymc.main.command

import logan.api.command.BasicCommand
import logan.api.command.SenderTarget
import net.cozymc.main.file.DataConfig
import net.cozymc.main.file.MainConfig
import net.cozymc.main.util.getOccupyingClaim
import net.cozymc.main.util.isClaimOwnerOf
import net.cozymc.main.util.isRelativeOfAnyClaim
import org.bukkit.entity.Player

class ClaimAbandonCommand : BasicCommand<Player>(
    "abandon", "cozyclaims.abandon", 0..1,
    target = SenderTarget.PLAYER
) {
    override fun run(sender: Player, args: Array<out String>, data: Any?): Boolean {
        if (!sender.isRelativeOfAnyClaim()) {
            sender.sendMessage(MainConfig.getNotRelativeOfAnyClaimMessage())
            return true
        }
        val claim = sender.getOccupyingClaim() ?: run {
            sender.sendMessage(MainConfig.getNotInsideClaimMessage())
            return true
        }
        if (!sender.isClaimOwnerOf(claim)) {
            sender.sendMessage(MainConfig.getNotOwnerOfOccupyingClaimMessage())
            return true
        }
        if (args.isEmpty()) {
            val success = claim.chunks.removeIf { it.x == sender.location.chunk.x && it.z == sender.location.chunk.z }
            if (success) sender.sendMessage(MainConfig.getClaimAbandonSuccessMessage())
            else sender.sendMessage(MainConfig.getClaimAbandonFailureMessage())
        } else if (args[0].equals("all", true)) {
            val size = claim.chunks.size
            claim.chunks.clear()
            sender.sendMessage(MainConfig.getClaimAbandonAllSuccessMessage(size))
        }
        DataConfig.saveClaim(claim)
        return true
    }
}