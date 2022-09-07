package net.cozymc.main.command

import be.seeseemelk.mockbukkit.MockBukkit
import be.seeseemelk.mockbukkit.ServerMock
import com.earth2me.essentials.Essentials
import com.earth2me.essentials.User
import com.sk89q.worldguard.WorldGuard
import com.sk89q.worldguard.internal.platform.WorldGuardPlatform
import com.sk89q.worldguard.protection.managers.RegionManager
import com.sk89q.worldguard.protection.regions.RegionContainer
import io.mockk.*
import io.mockk.impl.annotations.MockK
import net.cozymc.main.*
import net.cozymc.main.file.DataConfig
import net.cozymc.main.file.MainConfig
import net.cozymc.main.mock.CustomServerMock
import net.cozymc.main.util.isClaimOwner
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.entity.Player
import org.hamcrest.CoreMatchers.any
import org.junit.After
import org.junit.Before
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.math.BigDecimal
import java.util.UUID
import kotlin.test.assertContains
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@DisplayName("The claim command")
class ClaimCommandTest {

    @BeforeEach
    fun setUp() {
        setUpInit(this)
        Mockito.`when`(user.money).thenReturn(BigDecimal(25000))
    }

    @AfterEach
    fun tearDown() {
        tearDownInit()
    }

    @Nested
    @DisplayName("is performed by a player")
    inner class IsPerformedByAPlayer {

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
//        val player = spyk(server.addPlayer())
        player.isOp = true
        mockkObject(WorldGuardUtils)
        spyk(WorldGuardUtils)
        every { WorldGuardUtils.isRegion(any()) } returns true
//        `when`(WorldGuardUtils.isRegion(player.location)).thenReturn(true)
//        every { WorldGuardUtils.isRegion(an)}
        player.performCommand("claim")
//        verify(WorldGuardUtils.isRegion(any()), times(1)).isRegion(any(Location::class.java))
        verify { WorldGuardUtils.isRegion(allAny()) }
        assertFalse(player.isClaimOwner())
        Mockito.verify(player).sendMessage(MainConfig.getChunkClaimFailureRegionMessage())
//        verify { player.sendMessage(MainConfig.getChunkClaimFailureRegionMessage()) }
    }

    private fun getEssentialsUser() = CozyClaimsPlugin.essentials.getUser(Mockito.any<Player>())
}