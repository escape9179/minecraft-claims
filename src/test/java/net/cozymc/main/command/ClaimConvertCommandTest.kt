package net.cozymc.main.command

import be.seeseemelk.mockbukkit.command.ConsoleCommandSenderMock
import be.seeseemelk.mockbukkit.entity.PlayerMock
import net.cozymc.api.command.CommandDispatcher
import net.cozymc.main.CozyClaimsTest
import net.cozymc.main.file.MainConfig
import org.junit.jupiter.api.*
import org.mockito.Mockito
import java.util.*
import kotlin.test.assertEquals

@DisplayName("The claim convert command")
class ClaimConvertCommandTest : CozyClaimsTest() {

    @Test
    @DisplayName("when run by console sends invalid target message")
    fun whenRunByConsoleSendsInvalidTargetMessage() {
        val consoleSender = Mockito.spy(server.consoleSender)
        CommandDispatcher.onCommand(consoleSender, server.getPluginCommand("claim")!!, "convert", emptyArray())
        Mockito.verify(consoleSender).sendMessage(CommandDispatcher.getInvalidTargetMessage(ConsoleCommandSenderMock::class.java))
        assertEquals(CommandDispatcher.Result.INVALID_TARGET, CommandDispatcher.lastResult)
    }

    @Test
    @DisplayName("when run by player with no arguments converts claims and sends success message")
    fun whenRunByPlayerWithNoArgumentsConvertsClaimsAndSendsSuccessMessage(): Unit {
         val player = PlayerMock(server, "player", UUID.fromString("1d43cc59-e80c-4dd9-9afd-93faeff41acc")).run {
             server.addPlayer(this)
             Mockito.spy(this)
         }
        player.isOp = true
        CommandDispatcher.onCommand(player, server.getPluginCommand("claim")!!, "convert", emptyArray())
        Mockito.verify(player).sendMessage(MainConfig.getClaimConvertSuccessMessage())
        assertEquals(CommandDispatcher.Result.SUCCESS, CommandDispatcher.lastResult)
    }

    @Test
    @DisplayName("when run by player with no claim to convert sends no claim to convert message")
    fun whenRunByPlayerWithNoClaimToConvert(): Unit {
        val player = Mockito.spy(server.addPlayer())
        player.isOp = true
        CommandDispatcher.onCommand(player, server.getPluginCommand("claim")!!, "convert", emptyArray())
        Mockito.verify(player).sendMessage(MainConfig.getNoClaimToConvertMessage())
        assertEquals(CommandDispatcher.Result.SUCCESS, CommandDispatcher.lastResult)
    }
}