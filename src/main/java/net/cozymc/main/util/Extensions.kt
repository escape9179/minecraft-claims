package net.cozymc.main.util

import com.earth2me.essentials.Essentials
import net.cozymc.main.claim.Claim
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

fun Player.isRelativeOfClaim(claim: Claim): Boolean {
    return claim.owner == uniqueId || uniqueId in claim.members || uniqueId in claim.trustees
}

fun Player.isRelativeOfAnyClaim(): Boolean {
    return DataConfig.loadClaims().any { it.owner == uniqueId || uniqueId in it.members || uniqueId in it.trustees }
}

fun Player.getTrustedClaims(): List<Claim?> {
    return DataConfig.loadClaims().filter { uniqueId in it.trustees }.toList()
}

fun Player.getOccupyingClaim(): Claim? {
    return Claim.getClaimAt(location)
}

fun Player.getPreviousOccupyingClaim(): Claim? {
    return Claim.getClaimAt(lastBlockLocation)
}

fun Player.loadOwnerClaim(): Claim? {
    return DataConfig.loadOwnerClaim(uniqueId)
}

fun Player.isClaimOwner(): Boolean {
    return DataConfig.getClaimOwners().any { it == this.uniqueId }
}

fun Player.isClaimOwnerOf(claim: Claim): Boolean {
    return claim.owner == uniqueId
}

fun Player.isClaimMemberOf(claim: Claim): Boolean {
    return uniqueId in claim.members
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
    return DataConfig.loadOwnerClaim(uuid)?.chunks?.contains(this) ?: false
}