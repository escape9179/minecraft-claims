package net.cozymc.main

import com.earth2me.essentials.Essentials
import net.cozymc.api.OnlinePlayerIteratorThread
import net.cozymc.api.command.CommandDispatcher
import net.cozymc.main.claim.Claim
import net.cozymc.main.claim.ClaimInfoCommand
import net.cozymc.main.command.*
import net.cozymc.main.util.playParticlesAroundClaim
import net.cozymc.main.command.MemberAddCommand
import net.cozymc.main.command.ClaimCommand
import net.cozymc.main.command.RemoveMemberCommand
import net.cozymc.main.command.ClaimUnclaimCommand
import net.cozymc.main.listener.PlayerInteractListener
import net.cozymc.main.util.*
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
        CommandDispatcher.registerCommand(MemberAddCommand())
        CommandDispatcher.registerCommand(RemoveMemberCommand())
        CommandDispatcher.registerCommand(ClaimUnclaimCommand())
        CommandDispatcher.registerCommand(ClaimConvertCommand())
        CommandDispatcher.registerCommand(MemberPromoteCommand())
        CommandDispatcher.registerCommand(MemberDemoteCommand())
        CommandDispatcher.registerCommand(TrustCommand())
        CommandDispatcher.registerCommand(DistrustCommand())
        CommandDispatcher.registerCommand(ClaimInfoCommand())

        server.pluginManager.registerEvents(PlayerInteractListener(), this)

        ConfigurationSerialization.registerClass(Claim::class.java)

        createParticleSpawnTask()
        createTitleSendTask()

        OnlinePlayerIteratorThread.start()

        logger.info("$name enabled.")
    }

    override fun onDisable() {
        logger.info("$name disabled.")
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        return CommandDispatcher.onCommand(sender, command, label, args!!)
    }

    fun createParticleSpawnTask() {
        OnlinePlayerIteratorThread.addTask(20) { player ->
            val occupyingClaim = player.getOccupyingClaim() ?: return@addTask
            if (player.isRelativeOfClaim(occupyingClaim)) { player.playParticlesAroundClaim(occupyingClaim) }
        }
    }

    fun createTitleSendTask() {
        OnlinePlayerIteratorThread.addTask(5) {
            val previousClaim = it.getPreviousOccupyingClaim()
            val currentClaim = it.getOccupyingClaim()
            if (previousClaim != currentClaim) {
                if (currentClaim == null) it.sendTitle("Leaving", "${previousClaim?.owner?.getOfflinePlayer()?.name}'s claim", 20, 20, 20)
                else it.sendTitle("Entering", "${currentClaim.owner.getOfflinePlayer().name}'s claim", 20, 20, 20)
            }
            it.lastBlockLocation = it.blockLocation
        }
    }

    companion object {
        lateinit var instance: CozyClaimsPlugin
        lateinit var essentials: Essentials

        fun log(message: String) {
            instance.logger.info(message)
        }
    }
}