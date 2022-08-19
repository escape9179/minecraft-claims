package net.cozymc.main.command

import logan.api.command.BasicCommand
import logan.api.command.SenderTarget
import net.cozymc.main.claim.Claim
import net.cozymc.main.WorldGuard
import net.cozymc.main.file.MainConfig
import net.cozymc.main.file.DataConfig
import net.cozymc.main.util.getBalance
import org.bukkit.entity.Player

class ClaimCommand : BasicCommand<Player>(
    "claim",
    "cozyclaims.claim",
    0..0,
    target = SenderTarget.PLAYER,
) {
    override fun run(sender: Player, args: Array<out String>, data: Any?): Boolean {
        if (sender.getBalance() <= MainConfig.getClaimCost()) {
            sender.sendMessage(MainConfig.getChunkClaimFailureMoneyMessage())
            return true
        }

        if (Claim.isClaimed(sender.location)) {
            sender.sendMessage(MainConfig.getChunkClaimFailureClaimedMessage())
            return true
        }

        if (WorldGuard.isRegion(sender.location)) {
            sender.sendMessage(MainConfig.getChunkClaimFailureRegionMessage())
            return true
        }

        DataConfig.saveClaim(Claim.getOrCreate(sender.uniqueId, sender.location.chunk))
        sender.sendMessage(MainConfig.getChunkClaimSuccessMessage())
        return true
    }
}