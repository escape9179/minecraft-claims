package net.cozymc.main.command

import logan.api.command.BasicCommand
import logan.api.command.SenderTarget
import net.cozymc.main.file.MainConfig
import net.cozymc.main.util.loadClaim
import org.bukkit.Bukkit
import org.bukkit.entity.Player

class MemberDemoteCommand : BasicCommand<Player>(
    "demote", "cozyclaims.member.demote",
    1..1, target = SenderTarget.PLAYER, parentCommand = "claim"
) {
    override fun run(sender: Player, args: Array<out String>, data: Any?): Boolean {
        val otherPlayer = Bukkit.getPlayer(args[0]) ?: run {
            sender.sendMessage(MainConfig.getUnknownPlayerMessage())
            return true
        }

        val claim = sender.loadClaim() ?: run {
            sender.sendMessage(MainConfig.getNotClaimOwnerMessage())
            return true
        }

        val member = claim.members[otherPlayer.uniqueId] ?: run {
            sender.sendMessage(MainConfig.getNotMemberOfClaimMessage())
            return true
        }

        if (member.demote()) sender.sendMessage(MainConfig.getMemberChangeLevelSuccessMessage(otherPlayer.name, member.level))
        else sender.sendMessage(MainConfig.getMemberChangeLevelFailureMessage(otherPlayer.name, member.level))
        return true

    }
}