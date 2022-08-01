package net.cozymc.main.command

import logan.api.command.BasicCommand
import logan.api.command.SenderTarget
import net.cozymc.main.file.DataConfig
import net.cozymc.main.file.MainConfig
import org.bukkit.Bukkit
import org.bukkit.entity.Player

class ClaimAddMemberCommand : BasicCommand<Player>(
    "add", "cozyclaims.claim.add",
    1..1,
    target = SenderTarget.PLAYER,
    parentCommand = "claim",
) {
    override fun run(sender: Player, args: Array<out String>, data: Any?): Boolean {
        val member = Bukkit.getPlayer(args[0]) ?: run {
            sender.sendMessage(MainConfig.getUnknownPlayerMessage())
            return true
        }

        if (DataConfig.getClaim(sender.uniqueId)?.addMember(member) ?: run {
            sender.sendMessage(MainConfig.getNotClaimOwnerMessage())
            return true
        }) sender.sendMessage(MainConfig.getAddMemberSuccessMessage(member.name))
        else sender.sendMessage(MainConfig.getPersonAlreadyMemberMessage())

        return true
    }
}