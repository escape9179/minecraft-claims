package net.cozymc.main.command

import logan.api.command.BasicCommand
import logan.api.command.SenderTarget
import net.cozymc.main.claim.Claim
import net.cozymc.main.CozyClaimsPlugin
import net.cozymc.main.file.DataConfig
import net.cozymc.main.util.GriefPreventionUtility
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.io.File

class ClaimConvertCommand : BasicCommand<Player>(
    "convert", "cozyclaims.convert",
    0..0,
    target = SenderTarget.PLAYER,
    parentCommand = "claim",
) {
    override fun run(sender: Player, args: Array<out String>, data: Any?): Boolean {
        val claimDataFolder = File("plugins/GriefPreventionData/ClaimData")
        Bukkit.getScheduler().runTaskAsynchronously(CozyClaimsPlugin.instance, Runnable {
            claimDataFolder.listFiles()?.forEach {
                val griefPreventionClaim = GriefPreventionUtility.loadClaim(it)
                CozyClaimsPlugin.log(griefPreventionClaim.toString())
                val newClaimChunks = griefPreventionClaim.getChunks()
                DataConfig.saveClaim(Claim.getOrCreate(griefPreventionClaim.owner, newClaimChunks))
                CozyClaimsPlugin.log("Converted claims of ${griefPreventionClaim.owner}.")
            }
        })
        CozyClaimsPlugin.log("Done converting claims.")
        return true
    }
}