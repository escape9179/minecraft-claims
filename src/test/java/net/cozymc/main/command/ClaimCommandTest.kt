package net.cozymc.main.command

import be.seeseemelk.mockbukkit.MockBukkit
import be.seeseemelk.mockbukkit.ServerMock
import com.earth2me.essentials.Essentials
import com.earth2me.essentials.User
import com.sk89q.worldguard.WorldGuard
import com.sk89q.worldguard.internal.platform.WorldGuardPlatform
import com.sk89q.worldguard.protection.managers.RegionManager
import com.sk89q.worldguard.protection.regions.RegionContainer
import net.cozymc.main.CozyClaimsPlugin
import net.cozymc.main.WorldGuardUtils
import net.cozymc.main.file.MainConfig
import net.cozymc.main.mock.CustomServerMock
import net.cozymc.main.util.isClaimOwner
import org.bukkit.Location
import org.bukkit.entity.Player
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.*
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
        MockitoAnnotations.openMocks(this)
        server = MockBukkit.mock(CustomServerMock())
        plugin = MockBukkit.load(CozyClaimsPlugin::class.java)

        WorldGuard.getInstance().platform = platform
        `when`(platform.regionContainer).thenReturn(regionContainer)
        `when`(getEssentialsUser()).thenReturn(user)
        `when`(user.money).thenReturn(BigDecimal(25000))
    }

    @After
    fun tearDown() {
        MockBukkit.unmock()
    }

    @Test
    fun claim_NotClaimed_NotRegion_HasEnoughMoney_NotClaimOwner_HasPermissions_Pass() {
        val player = spy(server.addPlayer())
        player.isOp = true
        player.performCommand("claim")
        assertTrue(player.isClaimOwner())
        verify(player).sendMessage(MainConfig.getChunkClaimSuccessMessage())
    }

    @Test
    fun claim_NotClaimed_NotRegion_NotEnoughMoney_NotClaimOwner_HasClaimPermissions_Fail() {
        `when`(getEssentialsUser().money).thenReturn(BigDecimal.ZERO)
        val player = spy(server.addPlayer())
        player.isOp = true
        player.performCommand("claim")
        assertFalse(player.isClaimOwner())
        verify(player).sendMessage(MainConfig.getChunkClaimFailureMoneyMessage())
    }

    @Test
    fun claim_NotClaimed_IsRegion_HasEnoughMoney_NotClaimOwner_HasPermissions_Fail() {
        val player = spy(server.addPlayer())
        player.isOp = true
        `when`(WorldGuardUtils.isRegion(mock(Location::class.java))).thenReturn(true)
        player.performCommand("claim")
        assertFalse(player.isClaimOwner())
        verify(player).sendMessage(MainConfig.getChunkClaimFailureRegionMessage())
    }

    private fun getEssentialsUser() = CozyClaimsPlugin.essentials.getUser(any<Player>())
}