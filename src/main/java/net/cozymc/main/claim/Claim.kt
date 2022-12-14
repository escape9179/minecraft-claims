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

class Claim private constructor(val id: Int, val owner: UUID) : ConfigurationSerializable {
    val chunks = mutableSetOf<Chunk>()
    val members = ClaimMemberSet()
    val trustees = mutableSetOf<UUID>()
    val size
        get() = chunks.size

    init {
        members.add(owner)
    }

    constructor(id: Int, owner: UUID, chunk: Chunk) : this(id, owner) {
        chunks.add(chunk)
    }

    constructor(id: Int, owner: UUID, chunks: Set<Chunk>) : this(id, owner) {
        this.chunks.addAll(chunks)
    }

    constructor(map: Map<String, *>) : this(map["id"].toString().toInt(),UUID.fromString(map["owner"].toString())) {
        members.addAll((map["members"] as List<*>).map(Any?::toString).map(UUID::fromString))
        trustees.addAll((map["trustees"] as List<*>).map(Any?::toString).map(UUID::fromString))
        chunks.addAll(
            (map["chunks"] as List<*>)
                .map(Any?::toString)
                .map { it.split(",") }
                .map { Bukkit.getWorld(it[0])!!.getChunkAt(it[1].toInt(), it[2].toInt()) }
        )
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
            "id" to id.toString(),
            "owner" to owner.toString(),
            "members" to members.map { it.uuid.toString() },
            "trustees" to trustees.map(UUID::toString),
            "chunks" to chunks.map { "${it.world.name},${it.x},${it.z}" }
        )
    }

    override fun equals(other: Any?): Boolean {
        return other is Claim && other.owner == owner
    }

    override fun hashCode(): Int {
        return 31 * owner.hashCode()
    }

    companion object {
        fun create(owner: UUID, chunk: Chunk): Claim {
            return Claim(DataConfig.nextId++, owner, chunk)
        }

        fun create(owner: UUID, chunks: Set<Chunk>): Claim {
            return Claim(DataConfig.nextId++, owner, chunks)
        }


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
            return DataConfig.loadOwnerClaim(owner)?.also { it.chunks.add(chunk) } ?: return create(owner, chunk)
        }

        fun getOrCreate(owner: UUID, chunks: Set<Chunk>): Claim {
            return DataConfig.loadOwnerClaim(owner)?.also { it.chunks.addAll(chunks) } ?: return create(owner, chunks)
        }
    }
}