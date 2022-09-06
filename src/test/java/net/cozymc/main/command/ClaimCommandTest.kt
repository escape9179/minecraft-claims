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
import net.cozymc.main.CozyClaimsPlugin
import net.cozymc.main.WorldGuardUtils
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
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.math.BigDecimal
import java.util.UUID
import kotlin.test.assertContains
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ClaimCommandTest {

    lateinit var server: ServerMock
    lateinit var plugin: CozyClaimsPlugin

    @Mock
    private lateinit var platform: WorldGuardPlatform

    @Mock
    private lateinit var user: User

    @Mock
    private lateinit var regionContainer: RegionContainer

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        MockitoAnnotations.openMocks(this)
        server = MockBukkit.mock(CustomServerMock())
        plugin = MockBukkit.load(CozyClaimsPlugin::class.java)

        WorldGuard.getInstance().platform = platform
//        every { platform.regionContainer } returns regionContainer
//        every { getEssentialsUser() } returns user
//        every { user.money } returns BigDecimal(25000)
        Mockito.`when`(platform.regionContainer).thenReturn(regionContainer)
        Mockito.`when`(getEssentialsUser()).thenReturn(user)
        Mockito.`when`(user.money).thenReturn(BigDecimal(25000))
    }

    @After
    fun tearDown() {
        DataConfig.delete()
        MockBukkit.unmock()
    }

    @Test
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