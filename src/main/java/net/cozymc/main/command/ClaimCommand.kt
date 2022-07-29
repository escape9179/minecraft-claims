package net.cozymc.main.command

import logan.api.command.BasicCommand
import logan.api.command.SenderTarget
import net.cozymc.main.CozyClaimsPlugin
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
        // If the player doesn't have enough money to claim the chunk, return.
        if (sender.getBalance() <= MainConfig.getClaimCost()) {
            sender.sendMessage(MainConfig.getChunkClaimFailureMoneyMessage())
            return true
        }

        // If the chunk the player is attempting to claim is already claimed, return.
        if (CozyClaimsPlugin.isClaimed(sender.location)) {
            sender.sendMessage(MainConfig.getChunkClaimFailureClaimedMessage())
            return true
        }

        // If the chunk the player is attempting to claim is a WorldGuard region, return.
        if (WorldGuard.isRegion(sender.location)) {
            sender.sendMessage(MainConfig.getChunkClaimFailureRegionMessage())
            return true
        }

        DataConfig.addClaim(sender.uniqueId, sender.location.chunk)
        sender.sendMessage(MainConfig.getChunkClaimSuccessMessage())

        return true
    }
}