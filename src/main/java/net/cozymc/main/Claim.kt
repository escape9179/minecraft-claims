package net.cozymc.main

import org.bukkit.Location
import org.bukkit.entity.Player
import java.util.UUID

class Claim(val owner: UUID, val location: Location) {
    fun addMember(player: Player) {
        TODO("Implementation")
    }

    fun removeMember(player: Player) {
        TODO("Implementation")
    }
}