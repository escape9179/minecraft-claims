package net.cozymc.main.util

import net.cozymc.main.claim.ClaimMember
import org.bukkit.configuration.serialization.ConfigurationSerializable
import java.util.*

class ClaimMemberSet() : ConfigurationSerializable {

    private val memberSet = mutableSetOf<ClaimMember>()

    constructor(map: Map<String, *>) : this() {
        memberSet.addAll((map["members"] as List<*>).map { ClaimMember(UUID.fromString(it.toString())) })
    }

    operator fun get(uuid: UUID): ClaimMember? {
        return memberSet.firstOrNull { it.uuid == uuid }
    }

    fun add(uuid: UUID): Boolean {
        return memberSet.add(ClaimMember(uuid))
    }

    fun addAll(uuids: Collection<UUID>) {
        memberSet.addAll(uuids.map(::ClaimMember))
    }

    fun remove(uuid: UUID): Boolean {
        return memberSet.removeIf { it.uuid == uuid }
    }

    fun <T> map(func: (ClaimMember) -> T): List<T> {
        return memberSet.map { func.invoke(it) }
    }

    override fun serialize(): MutableMap<String, Any> {
        return mutableMapOf(
            "members" to memberSet.map { it.uuid.toString() }
        )
    }
}