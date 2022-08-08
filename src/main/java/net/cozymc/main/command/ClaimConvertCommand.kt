package net.cozymc.main.command

import logan.api.command.BasicCommand
import logan.api.command.SenderTarget
import net.cozymc.main.Claim
import net.cozymc.main.CozyClaimsPlugin
import net.cozymc.main.file.DataConfig
import net.cozymc.main.util.GriefPreventionClaim
import net.cozymc.main.util.GriefPreventionUtil
import org.bukkit.Bukkit
import org.bukkit.Chunk
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
        Bukkit.getScheduler().runTaskAsynchronously(CozyClaimsPlugin.instance) {
            claimDataFolder.listFiles()?.forEach {
                val griefPreventionClaim = GriefPreventionUtil.getClaim(it)
                CozyClaimsPlugin.log(griefPreventionClaim.toString())
                val newClaimChunks = getIntersectedChunks(griefPreventionClaim)
                DataConfig.saveClaim(Claim.getOrCreate(griefPreventionClaim.owner, newClaimChunks))
                CozyClaimsPlugin.log("Converted claims of ${griefPreventionClaim.owner}.")
            }
        }
        CozyClaimsPlugin.log("Done converting claims.")
        return true
    }

    private fun getIntersectedChunks(griefPreventionClaim: GriefPreventionClaim): Set<Chunk> {
        val chunks = mutableSetOf<Chunk>()
        val greaterBoundaryChunk =
            Pair(griefPreventionClaim.greaterBoundaryCorner.chunk.x, griefPreventionClaim.greaterBoundaryCorner.chunk.z)
        val lesserBoundaryChunk =
            Pair(griefPreventionClaim.lesserBoundaryCorner.chunk.x, griefPreventionClaim.lesserBoundaryCorner.chunk.z)
        for (x in lesserBoundaryChunk.first..greaterBoundaryChunk.first)
            for (z in lesserBoundaryChunk.second..greaterBoundaryChunk.second)
                chunks.add(griefPreventionClaim.world.getChunkAt(x, z))
        return chunks
    }
}