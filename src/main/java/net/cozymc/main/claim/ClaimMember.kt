
package net.cozymc.main.claim

import java.util.*


class ClaimMember(val uuid: UUID) {

    var level = MemberLevel.DEFAULT

    fun promote(): Boolean {
        if (level.ordinal >= MemberLevel.values().size) return false
        level = MemberLevel.values()[level.ordinal + 1]
        return true
    }

    fun demote(): Boolean {
        if (level.ordinal <= 0) return false
        level = MemberLevel.values()[level.ordinal - 1]
        return true
    }
}

enum class MemberLevel {
    DEFAULT, OWNER
}