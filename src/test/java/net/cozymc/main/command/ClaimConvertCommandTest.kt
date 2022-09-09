package net.cozymc.main.command

import be.seeseemelk.mockbukkit.command.ConsoleCommandSenderMock
import net.cozymc.api.command.CommandDispatcher
import net.cozymc.main.CozyClaimsTest
import net.cozymc.main.file.MainConfig
import org.junit.jupiter.api.*
import org.mockito.Mockito
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@DisplayName("The claim convert command")
class ClaimConvertCommandTest : CozyClaimsTest() {

    @Nested
    @DisplayName("when run by console")
    inner class IsRunByConsole {
        @Test
        @DisplayName("with no arguments then succeed")
        fun withNoArgumentsThenSucceed(): Unit {
        }

        @Test
        @DisplayName("with arguments then fail")
        fun withAnyArgumentsThenFail(): Unit {

        }
    }

    @Nested
    @DisplayName("when run by a player")
    inner class IsRunByAPlayer {
        @Test
        @DisplayName("with no arguments then succeed")
        fun withNoArgumentsThenSucceed(): Unit {

        }

        @Test
        @DisplayName("with arguments then fail")
        fun withArgumentsThenFail(): Unit {

        }
    }
}