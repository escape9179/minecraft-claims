
package net.cozymc.main.claim

import net.cozymc.main.CozyClaimsPlugin
import java.util.*


class ClaimMember(val uuid: UUID) {

    var level = MemberLevel.DEFAULT

    fun promote(): Boolean {
        if (level.ordinal >= MemberLevel.values().size - 1) return false
        level = MemberLevel.values()[level.ordinal + 1]
        return true
    }

    fun demote(): Boolean {
        if (level.ordinal <= 0) return false
        level = MemberLevel.values()[level.ordinal - 1]
        return true
    }

    override fun equals(other: Any?): Boolean {
        return other is ClaimMember && other.uuid == uuid
    }

    override fun hashCode(): Int {
        return 31 * uuid.hashCode()
    }
}

enum class MemberLevel {
    DEFAULT, OWNER
}