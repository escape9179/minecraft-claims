package net.cozymc.main.util

import net.cozymc.main.CozyClaimsPlugin
import net.cozymc.main.claim.ClaimMember
import org.bukkit.configuration.serialization.ConfigurationSerializable
import java.util.*

class ClaimMemberSet {

    private val memberSet = mutableSetOf<ClaimMember>()

    operator fun get(uuid: UUID): ClaimMember? {
        return memberSet.firstOrNull { it.uuid == uuid }
    }

    fun add(uuid: UUID): Boolean {
        if (memberSet.add(ClaimMember(uuid))) {
            CozyClaimsPlugin.log("ClaimMemberSet.add() returned true.")
            return true
        }
//        return memberSet.add(ClaimMember(uuid))
        return false
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

    operator fun contains(uuid: UUID): Boolean = memberSet.any { it.uuid == uuid }
}