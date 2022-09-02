package net.cozymc.main.command

import be.seeseemelk.mockbukkit.MockBukkit
import be.seeseemelk.mockbukkit.ServerMock
import com.earth2me.essentials.Essentials
import com.earth2me.essentials.User
import net.cozymc.main.CozyClaimsPlugin
import net.cozymc.main.file.MainConfig
import net.cozymc.main.mock.CustomServerMock
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import java.math.BigDecimal

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
    fun testOnFreeChunkWithPlayerNotRelativeOfAnyClaims() {
        val player = server.addPlayer()
        player.isOp = true
        val user = Mockito.mock(User::class.java)
        val money = Mockito.mock(BigDecimal::class.java)
        Mockito.`when`(CozyClaimsPlugin.essentials.getUser(player)).thenReturn(user)
        Mockito.`when`(user.money).thenReturn(money)
        Mockito.`when`(money.intValueExact()).thenReturn(25000)
        player.performCommand("claim")
    }
}