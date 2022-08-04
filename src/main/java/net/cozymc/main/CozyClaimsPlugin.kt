package net.cozymc.main

import com.earth2me.essentials.Essentials
import net.cozymc.api.OnlinePlayerIteratorThread
import net.cozymc.api.command.CommandDispatcher
import net.cozymc.main.command.ClaimAddMemberCommand
import net.cozymc.main.command.ClaimCommand
import net.cozymc.main.command.ClaimRemoveMemberCommand
import net.cozymc.main.command.ClaimUnclaimCommand
import net.cozymc.main.util.getClaim
import net.cozymc.main.util.isInOwnClaim
import net.cozymc.main.util.playParticlesAroundClaim
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.configuration.serialization.ConfigurationSerialization
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths

const val DATA_FOLDER_PATH = "plugins/CozyClaims/"
const val CONFIG_FILE_NAME = "config.yml"
const val DATA_FILE_NAME = "data.yml"

class CozyClaimsPlugin : JavaPlugin() {

    override fun onEnable() {
        instance = this
        essentials = server.pluginManager.getPlugin("Essentials") as Essentials

        File(DATA_FOLDER_PATH).mkdirs()
        try {
            Files.copy(javaClass.getResourceAsStream("/$CONFIG_FILE_NAME")!!, Paths.get("$DATA_FOLDER_PATH/$CONFIG_FILE_NAME"))
        } catch (e: IOException) {
            logger.info("Cannot create config: ${e.cause?.message}")
        }

        CommandDispatcher.registerCommand(ClaimCommand())
        CommandDispatcher.registerCommand(ClaimAddMemberCommand())
        CommandDispatcher.registerCommand(ClaimRemoveMemberCommand())
        CommandDispatcher.registerCommand(ClaimUnclaimCommand())

        ConfigurationSerialization.registerClass(Claim::class.java)

        OnlinePlayerIteratorThread.addTask(20) { player ->
            if (player.isInOwnClaim()) player.getClaim()?.let { player.playParticlesAroundClaim(it) }
        }

        logger.info("$name enabled.")
    }

    override fun onDisable() {
        logger.info("$name disabled.")
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        return CommandDispatcher.onCommand(sender, command, label, args!!)
    }

    companion object {
        lateinit var instance: CozyClaimsPlugin
        lateinit var essentials: Essentials
    }
}