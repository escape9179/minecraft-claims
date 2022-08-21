package net.cozymc.main.util

import net.cozymc.main.CozyClaimsPlugin
import net.cozymc.main.claim.ClaimMember
import org.bukkit.OfflinePlayer
import org.bukkit.configuration.serialization.ConfigurationSerializable
import java.util.*

class ClaimMemberSet {

    private val memberSet = mutableSetOf<ClaimMember>()

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

    operator fun contains(uuid: UUID): Boolean = memberSet.any { it.uuid == uuid }

    override fun toString(): String {
        val joiner = StringJoiner(", ")
        memberSet.map(ClaimMember::uuid)
            .map(CozyClaimsPlugin.instance.server::getOfflinePlayer)
            .map(OfflinePlayer::getName)
            .forEach(joiner::add)
        return joiner.toString()
    }
}