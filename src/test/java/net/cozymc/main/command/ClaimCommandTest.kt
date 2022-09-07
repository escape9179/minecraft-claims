package net.cozymc.main.command

import net.cozymc.main.*
import net.cozymc.main.file.MainConfig
import net.cozymc.main.util.isClaimOwner
import org.bukkit.entity.Player
import org.hamcrest.CoreMatchers.any
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import java.math.BigDecimal
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@DisplayName("The claim command")
class ClaimCommandTest : BukkitTest() {

    @BeforeEach
    override fun setup() {
        super.setup()
        Mockito.`when`(user.money).thenReturn(BigDecimal(25000))
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
                @DisplayName("who is not a claim owner")
                inner class WhoIsNotAClaimOwner {
                    @Test
                    @DisplayName("who has permissions then succeed")
                    fun whoHasPermissionsThenSucceed(): Unit {
                        val player = Mockito.spy(server.addPlayer())
                        player.isOp = true
                        player.performCommand("claim")
                        Mockito.verify(player).sendMessage(MainConfig.getChunkClaimSuccessMessage())
                        assertTrue(player.isClaimOwner())
                    }

                    @Test
                    @DisplayName("who doesn't have permissions then fail")
                    fun whoDoesntHavePermissionsThenFail(): Unit {

                    }
                }

                @Test
                @DisplayName("who is a claim owner then fail")
                fun whoIsAClaimOwnerThenFail(): Unit {

                }
            }

            @DisplayName("who is in a region then fail")
            fun whoIsInARegionThenFail(): Unit {

            }
        }

        @Test
        @DisplayName("who doesn't have enough money then fail")
        fun whoDoesntHaveEnoughMoneyThenFail(): Unit {

        }
    }

    @Test
    @DisplayName("is performed by the console then fail")
    fun isPerformedByTheConsoleThenFail(): Unit {

    }


    @Test
    @DisplayName("Players can claim chunks when its not claimed, not a region, when they have enough money, and have permissions")
    fun claim_NotClaimed_NotRegion_HasEnoughMoney_NotClaimOwner_HasPermissions_Pass() {
        val player = Mockito.spy(server.addPlayer())
        player.isOp = true
        assertTrue(player.isOp)
//        val player = spyk(server.addPlayer())
        player.performCommand("claim")
        Mockito.verify(player).sendMessage(MainConfig.getChunkClaimSuccessMessage())
        assertTrue(player.isClaimOwner())
//        verify(player).sendMessage(MainConfig.getChunkClaimSuccessMessage())
    }

    @Test
    @DisplayName("Players cannot claim a chunk without money")
    fun claim_NotClaimed_NotRegion_NotEnoughMoney_NotClaimOwner_HasClaimPermissions_Fail() {
        Mockito.`when`(getEssentialsUser().money).thenReturn(BigDecimal.ZERO)
//        every { getEssentialsUser().money } returns BigDecimal.ZERO
        val player = Mockito.spy(server.addPlayer())
//        val player = spyk(server.addPlayer())
        player.isOp = true
        player.performCommand("claim")
        assertFalse(player.isClaimOwner())
        Mockito.verify(player).sendMessage(MainConfig.getChunkClaimFailureMoneyMessage())
//        verify { player.sendMessage(MainConfig.getChunkClaimFailureMoneyMessage()) }
    }

    @Test
    @DisplayName("Players cannot claim chunks that are within a region")
    fun claim_NotClaimed_IsRegion_HasEnoughMoney_NotClaimOwner_HasPermissions_Fail() {
        val player = Mockito.spy(server.addPlayer())
        player.isOp = true
        io.mockk.mockkObject(WorldGuardUtils)
        spyk(WorldGuardUtils)
        every { WorldGuardUtils.isRegion(any()) } returns true
        player.performCommand("claim")
        verify { WorldGuardUtils.isRegion(allAny()) }
        assertFalse(player.isClaimOwner())
        Mockito.verify(player).sendMessage(MainConfig.getChunkClaimFailureRegionMessage())
    }

    private fun getEssentialsUser() = CozyClaimsPlugin.essentials.getUser(Mockito.any<Player>())
}