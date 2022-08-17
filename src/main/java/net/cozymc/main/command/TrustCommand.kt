package net.cozymc.main.command

import logan.api.command.BasicCommand
import logan.api.command.SenderTarget
import org.bukkit.entity.Player

class TrustCommand : BasicCommand<Player>(
    "trust", "cozyclaims.trust",
    1..1, target = SenderTarget.PLAYER, parentCommand = "claim"
) {
    override fun run(sender: Player, args: Array<out String>, data: Any?): Boolean {
        TODO("Not yet implemented")
    }
}