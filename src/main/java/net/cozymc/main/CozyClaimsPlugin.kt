package net.cozymc.main

import net.cozymc.api.command.CommandDispatcher
import net.cozymc.main.command.ClaimAddCommand
import net.cozymc.main.command.ClaimCommand
import org.bukkit.Location
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.plugin.java.JavaPlugin

val DATA_FOLDER_PATH = "plugins/CozyClaims/"
val CONFIG_FILE_NAME = "config.yml"
val DATA_FILE_NAME = "data.yml"

class CozyClaimsPlugin : JavaPlugin() {

    override fun onEnable() {
        CommandDispatcher.registerCommand(ClaimCommand())
        CommandDispatcher.registerCommand(ClaimAddCommand())
        logger.info("$name enabled.")
    }

    override fun onDisable() {
        logger.info("$name disabled.")
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        return CommandDispatcher.onCommand(sender, command, label, args!!)
    }

    companion object {
        fun isClaimed(location: Location): Boolean = TODO("Implementation")
    }
}