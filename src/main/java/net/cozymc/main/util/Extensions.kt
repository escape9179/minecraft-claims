package net.cozymc.main.util

import net.cozymc.main.Claim
import net.cozymc.main.CozyClaimsPlugin
import net.cozymc.main.chunk.Adjacency
import net.cozymc.main.chunk.AdjacentChunk
import net.cozymc.main.file.DataConfig
import net.cozymc.main.file.MainConfig
import org.bukkit.Bukkit
import org.bukkit.Chunk
import org.bukkit.entity.Player
import java.util.*

fun Player.getBalance(): Int {
    return CozyClaimsPlugin.essentials.getUser(this).money.intValueExact()
}

fun Player.isInOwnClaim(): Boolean {
    return DataConfig.getClaim(uniqueId)?.chunks?.any { location.chunk == it } ?: false
}

fun Player.getOccupyingClaim(): Claim? {
    return Claim.getClaimAt(location)
}

fun Player.getPreviousOccupyingClaim(): Claim? {
    return Claim.getClaimAt(lastBlockLocation)
}

fun Player.getClaim(): Claim? {
    return DataConfig.getClaim(uniqueId)
}

private val lastBlockLocationMap = mutableMapOf<UUID, BlockLocation>()
var Player.lastBlockLocation: BlockLocation
    get() = lastBlockLocationMap[uniqueId] ?: BlockLocation.from(location)
    set(value) { lastBlockLocationMap[uniqueId] = value }

val Player.blockLocation: BlockLocation
    get() = BlockLocation.from(location)

fun Player.playParticlesAroundClaim(claim: Claim) {
    claim.forEachChunk { chunk ->
        chunk.getAdjacentChunks().forEach { adjacentChunk ->
            if (!adjacentChunk.chunk.isClaimOf(uniqueId)) {
                adjacentChunk.getBorderBlocksAtHeightRange(location.blockY - 5..location.blockY + 5)
                    .forEach { spawnParticle(MainConfig.getClaimParticleEffectType(), it.location, 1, 0.0, 0.0, 0.0) }
            }
        }
    }
}

fun UUID.getOfflinePlayer() = Bukkit.getOfflinePlayer(this)

fun Chunk.getAdjacentChunks(): List<AdjacentChunk> {
    return listOf(
        AdjacentChunk(world.getChunkAt(x + 1, z), Adjacency.POS_X),
        AdjacentChunk(world.getChunkAt(x, z + 1), Adjacency.POS_Z),
        AdjacentChunk(world.getChunkAt(x - 1, z), Adjacency.NEG_X),
        AdjacentChunk(world.getChunkAt(x, z - 1), Adjacency.NEG_Z),
    )
}

fun Chunk.isClaimOf(uuid: UUID): Boolean {
    return DataConfig.getClaim(uuid)?.chunks?.contains(this) ?: false
}