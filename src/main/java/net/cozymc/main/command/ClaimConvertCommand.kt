package net.cozymc.main.command

import logan.api.command.BasicCommand
import logan.api.command.SenderTarget
import net.cozymc.main.claim.Claim
import net.cozymc.main.CozyClaimsPlugin
import net.cozymc.main.file.DataConfig
import net.cozymc.main.file.MainConfig
import net.cozymc.main.griefprevention.GriefPreventionUtility
import org.bukkit.Bukkit
import org.bukkit.entity.Player

object ClaimConvertCommand : BasicCommand<Player>(
    "convert", "cozyclaims.convert",
    0..0,
    target = SenderTarget.PLAYER,
    parentCommand = "claim",
) {
    override fun run(sender: Player, args: Array<out String>, data: Any?): Boolean {
        val claim = GriefPreventionUtility.loadAllClaims(MainConfig.getGriefPreventionClaimDataPath())
            .firstOrNull { it.owner == sender.uniqueId } ?: run {
            sender.sendMessage(MainConfig.getNoClaimToConvertMessage())
            return true
        }
        DataConfig.saveClaim(Claim.getOrCreate(sender.uniqueId, claim.getChunks()))
        sender.sendMessage(MainConfig.getClaimConvertSuccessMessage())
        return true
    }
}