package net.cozymc.main.command

import be.seeseemelk.mockbukkit.entity.PlayerMock
import net.cozymc.api.command.CommandDispatcher
import net.cozymc.main.*
import net.cozymc.main.claim.Claim
import net.cozymc.main.file.DataConfig
import net.cozymc.main.file.MainConfig
import net.cozymc.main.util.getBalance
import net.cozymc.main.util.isClaimOwner
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@DisplayName("The claim command")
class ClaimCommandTest : CozyClaimsTest() {

    lateinit var player: PlayerMock

    @BeforeEach
    override fun setup() {
        super.setup()
        player = Mockito.spy(server.addPlayer())
        io.mockk.mockkStatic("net.cozymc.main.util.ExtensionsKt")
    }

    @Nested
    @DisplayName("is performed by a player")
    inner class IsPerformedByAPlayer {
        @Nested
        @DisplayName("who has enough money")
        inner class WhoHasEnoughMoney {
            @Nested
            @DisplayName("who is not in a region")
            inner class WhoIsNotInARegion {
                @Nested
                @DisplayName("who is not in a claim")
                inner class WhoIsNotInAClaim {
                    @Nested
                    @DisplayName("who is not a claim owner")
                    inner class WhoIsNotAClaimOwner {
                        @Test
                        @DisplayName("who has permissions then succeed")
                        fun whoHasPermissionsThenSucceed(): Unit {
                            player.isOp = true
                            io.mockk.every { player.getBalance() } returns MainConfig.getClaimCost()
                            player.performCommand("claim")
                            Mockito.verify(player).sendMessage(MainConfig.getChunkClaimSuccessMessage())
                            assertTrue(player.isClaimOwner())
                        }

                        @Test
                        @DisplayName("who doesn't have permissions then fail")
                        fun whoDoesntHavePermissionsThenFail(): Unit {
                            player.isOp = false
                            io.mockk.every { player.getBalance() } returns MainConfig.getClaimCost()
                            player.performCommand("claim")
                            Mockito.verify(player).sendMessage(CommandDispatcher.NO_PERMISSION_MESSAGE)
                            assertFalse(player.isClaimOwner())
                        }
                    }

                    @Test
                    @DisplayName("who is a claim owner then fail")
                    fun whoIsAClaimOwnerThenFail(): Unit {

                    }
                }

                @Test
                @DisplayName("who is in a claim then fail")
                fun whoIsInAClaimThenFail(): Unit {
                    player.isOp = true
                    io.mockk.every { player.getBalance() } returns MainConfig.getClaimCost()
                    io.mockk.mockkObject(Claim.Companion)
                    io.mockk.every { Claim.isClaimed(any()) } returns true
                    player.performCommand("claim")
                    Mockito.verify(player).sendMessage(MainConfig.getChunkClaimFailureClaimedMessage())
                    assertFalse(player.isClaimOwner())
                }
            }

            @Test
            @DisplayName("who is in a region then fail")
            fun whoIsInARegionThenFail(): Unit {
                player.isOp = true
                io.mockk.every { player.getBalance() } returns MainConfig.getClaimCost()
                io.mockk.mockkObject(WorldGuardUtils)
                io.mockk.spyk(WorldGuardUtils)
                io.mockk.every { WorldGuardUtils.isRegion(any()) } returns true
                player.performCommand("claim")
                io.mockk.verify { WorldGuardUtils.isRegion(any()) }
                Mockito.verify(player).sendMessage(MainConfig.getChunkClaimFailureRegionMessage())
                assertFalse(player.isClaimOwner())
            }
        }

        @Test
        @DisplayName("who doesn't have enough money then fail")
        fun whoDoesntHaveEnoughMoneyThenFail(): Unit {
            player.isOp = true
            io.mockk.every { player.getBalance() } returns 0
            player.performCommand("claim")
            Mockito.verify(player).sendMessage(MainConfig.getChunkClaimFailureMoneyMessage())
            assertFalse(player.isClaimOwner())
        }
    }

    @Test
    @DisplayName("is performed by the console then fail")
    fun isPerformedByTheConsoleThenFail(): Unit {
        server.executeConsole("claim")
        assertEquals(CommandDispatcher.lastResult, CommandDispatcher.Result.WRONG_TARGET)
        assertEquals(emptySet(), DataConfig.loadClaims())
    }
}