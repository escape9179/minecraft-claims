package net.cozymc.main.command

import be.seeseemelk.mockbukkit.MockBukkit
import be.seeseemelk.mockbukkit.ServerMock
import be.seeseemelk.mockbukkit.entity.PlayerMock
import com.earth2me.essentials.Essentials
import com.earth2me.essentials.User
import net.cozymc.main.CozyClaimsPlugin
import net.cozymc.main.file.DataConfig
import net.cozymc.main.file.MainConfig
import net.cozymc.main.getMessages
import net.cozymc.main.mock.CustomServerMock
import net.cozymc.main.util.isClaimOwner
import org.bukkit.entity.Player
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import java.math.BigDecimal
import javax.xml.crypto.Data
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ClaimCommandTest {

    lateinit var server: ServerMock
    lateinit var plugin: CozyClaimsPlugin

    @Before
    fun setup() {
        server = MockBukkit.mock(CustomServerMock())
        plugin = MockBukkit.load(CozyClaimsPlugin::class.java)
    }

    @After
    fun tearDown() {
        MockBukkit.unmock()
    }

    @Test
    fun testFreeChunkEnoughMoneyNotClaimOwnerWithClaimPermissions() {
        val player = server.addPlayer()
        player.isOp = true
        val user = Mockito.mock(User::class.java)
        val money = Mockito.mock(BigDecimal::class.java)
        Mockito.`when`(CozyClaimsPlugin.essentials.getUser(player)).thenReturn(user)
        Mockito.`when`(user.money).thenReturn(money)
        Mockito.`when`(money.intValueExact()).thenReturn(25000)
        player.performCommand("claim")
        assertTrue(player.isClaimOwner())
        assertContains(player.getMessages(), MainConfig.getChunkClaimSuccessMessage())
    }

    @Test
    fun testFreeChunkNotEnoughMoneyNotClaimOwnerWithClaimPermissions() {
        Mockito.`when`(getEssentialsUser()).thenReturn(Mockito.mock(User::class.java))
        Mockito.`when`(getEssentialsUser().money).thenReturn(BigDecimal.ZERO)
        val player = server.addPlayer().also { it.isOp = true }.also { it.performCommand("claim") }
        assertFalse(player.isClaimOwner())
        assertContains(player.getMessages(), MainConfig.getChunkClaimFailureMoneyMessage())
    }

//    @Test
//    fun testFreeChunkEnoughMoneyNotClaimOwnerWithClaimPermissions() {
//
//    }
    private fun getEssentialsUser() = CozyClaimsPlugin.essentials.getUser(Mockito.any<Player>())
}