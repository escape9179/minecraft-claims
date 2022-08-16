package net.cozymc.main.claim

import net.cozymc.main.file.DataConfig
import net.cozymc.main.util.BlockLocation
import net.cozymc.main.util.ClaimMemberSet
import org.bukkit.Bukkit
import org.bukkit.Chunk
import org.bukkit.Location
import org.bukkit.configuration.serialization.ConfigurationSerializable
import org.bukkit.entity.Player
import java.util.UUID

class Claim(val owner: UUID) : ConfigurationSerializable {
    val chunks = mutableSetOf<Chunk>()
    val members = ClaimMemberSet()
    val trustees = mutableSetOf<UUID>()

    init {
        members.add(owner)
    }

    constructor(owner: UUID, chunk: Chunk) : this(owner) {
        chunks.add(chunk)
    }

    constructor(owner: UUID, chunks: Set<Chunk>) : this(owner) {
        this.chunks.addAll(chunks)
    }

    constructor(map: Map<String, *>) : this(UUID.fromString(map["owner"].toString())) {
        members.addAll((map["members"] as List<*>).map(Any?::toString).map(UUID::fromString))
        chunks.addAll(
            (map["chunks"] as List<*>)
                .map(Any?::toString)
                .map { it.split(",") }
                .map { Bukkit.getWorld(it[0]).getChunkAt(it[1].toInt(), it[2].toInt()) }
        )
    }

    fun addChunk(chunk: Chunk) {
        chunks.add(chunk)
    }

    fun addChunks(chunks: Set<Chunk>) {
        this.chunks.addAll(chunks)
    }

    fun removeChunk(chunk: Chunk) {
        chunks.remove(chunk)
    }

    fun addMember(player: Player): Boolean {
        return members.add(player.uniqueId)
    }

    fun addTrustee(): Boolean {
        TODO()
    }

    fun removeTrustee(): Boolean {
        TODO()
    }

    fun removeMember(player: Player): Boolean {
        return members.remove(player.uniqueId)
    }

    fun forEachChunk(func: (Chunk) -> Unit) {
        chunks.forEach(func)
    }

    fun containsLocation(location: Location): Boolean {
        return chunks.any { location.chunk == it }
    }

    fun containsLocation(location: BlockLocation): Boolean {
        return chunks.any { location.toBukkitLocation().chunk == it }
    }

    override fun serialize(): MutableMap<String, Any> {
        return mutableMapOf(
            "owner" to owner.toString(),
            "members" to members.map { it.uuid.toString() },
            "chunks" to chunks.map { "${it.world.name},${it.x},${it.z}" }
        )
    }

    override fun equals(other: Any?): Boolean {
        return other is Claim && other.owner == owner
    }

    companion object {
        fun isClaimed(location: Location): Boolean {
            DataConfig.loadClaims().forEach { if (it.chunks.contains(location.chunk)) return true }
            return false
        }

        fun getClaimAt(location: Location): Claim? {
            return DataConfig.loadClaims().firstOrNull { it.containsLocation(location) }
        }

        fun getClaimAt(location: BlockLocation): Claim? {
            return DataConfig.loadClaims().firstOrNull { it.containsLocation(location) }
        }

        fun getOrCreate(owner: UUID, chunk: Chunk): Claim {
            return DataConfig.loadClaim(owner)?.also { it.addChunk(chunk) } ?: return Claim(owner, chunk)
        }

        fun getOrCreate(owner: UUID, chunks: Set<Chunk>): Claim {
            return DataConfig.loadClaim(owner)?.also { it.addChunks(chunks) } ?: return Claim(owner, chunks)
        }
    }
}