package net.cozymc.main.command

import logan.api.command.BasicCommand
import logan.api.command.SenderTarget
import net.cozymc.main.file.DataConfig
import net.cozymc.main.file.MainConfig
import org.bukkit.Bukkit
import org.bukkit.entity.Player

class ClaimRemoveMemberCommand : BasicCommand<Player>(
    "remove", "cozyclaims.member.remove",
    1..1,
    target = SenderTarget.PLAYER,
    parentCommand = "claim",
) {
    override fun run(sender: Player, args: Array<out String>, data: Any?): Boolean {
        val member = Bukkit.getPlayer(args[0]) ?: run {
            sender.sendMessage(MainConfig.getUnknownPlayerMessage())
            return true
        }
        val claim = DataConfig.loadClaim(sender.uniqueId)
        if (claim == null || claim.owner != sender.uniqueId) {
            sender.sendMessage(MainConfig.getNotClaimOwnerMessage())
            return true
        }

        if (member.uniqueId == sender.uniqueId) sender.sendMessage(MainConfig.getRemoveMemberFailureSelfMessage())
        else {
            claim.removeMember(member)
            DataConfig.saveClaim(claim)
            sender.sendMessage(MainConfig.getRemoveMemberSuccessMessage(member.name))
        }
        return true
    }
}