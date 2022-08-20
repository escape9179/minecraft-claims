package net.cozymc.main.command

import logan.api.command.BasicCommand
import logan.api.command.SenderTarget
import net.cozymc.main.file.DataConfig
import net.cozymc.main.file.MainConfig
import net.cozymc.main.util.isClaimOwner
import org.bukkit.Bukkit
import org.bukkit.entity.Player

class TrustCommand : BasicCommand<Player>(
    "trust", "cozyclaims.trust",
    1..1, target = SenderTarget.PLAYER, parentCommand = "claim"
) {
    override fun run(sender: Player, args: Array<out String>, data: Any?): Boolean {
        val trustee = Bukkit.getPlayer(args[0]) ?: kotlin.run {
            sender.sendMessage(MainConfig.getUnknownPlayerMessage())
            return true
        }

        if (!sender.isClaimOwner()) {
            sender.sendMessage(MainConfig.getNotClaimOwnerMessage())
            return true
        }

        val claim = DataConfig.loadOwnerClaim(sender.uniqueId)
        if (claim!!.addTrustee(trustee)) {
            sender.sendMessage(MainConfig.getAddTrusteeSuccessMessage(trustee.name))
            DataConfig.saveClaim(claim)
        } else sender.sendMessage(MainConfig.getPersonAlreadyTrustedMessage())

        return true
    }
}