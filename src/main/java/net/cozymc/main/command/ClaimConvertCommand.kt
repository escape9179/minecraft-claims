package net.cozymc.main.command

import logan.api.command.BasicCommand
import logan.api.command.SenderTarget
import net.cozymc.main.claim.Claim
import net.cozymc.main.CozyClaimsPlugin
import net.cozymc.main.file.DataConfig
import net.cozymc.main.file.MainConfig
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
        val claims = GriefPreventionUtility.loadAllClaims(MainConfig.getGriefPreventionClaimDataPath())
        Bukkit.getScheduler().runTaskAsynchronously(CozyClaimsPlugin.instance, Runnable {
            claims.forEach {
                CozyClaimsPlugin.log(it.toString())
                val newClaimChunks = it.getChunks()
                DataConfig.saveClaim(Claim.getOrCreate(it.owner, newClaimChunks))
                CozyClaimsPlugin.log("Converted claims of ${it.owner}.")
            }
        })
        CozyClaimsPlugin.log("Done converting claims.")
        return true
    }
}