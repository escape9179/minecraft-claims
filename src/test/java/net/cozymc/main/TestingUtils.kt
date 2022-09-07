package net.cozymc.main

import be.seeseemelk.mockbukkit.MockBukkit
import be.seeseemelk.mockbukkit.ServerMock
import com.earth2me.essentials.User
import com.sk89q.worldguard.WorldGuard
import com.sk89q.worldguard.internal.platform.WorldGuardPlatform
import com.sk89q.worldguard.protection.regions.RegionContainer
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import net.cozymc.main.file.DataConfig
import net.cozymc.main.mock.CustomServerMock
import org.bukkit.entity.Player
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.math.BigDecimal
import kotlin.reflect.KClass

lateinit var server: ServerMock
lateinit var plugin: CozyClaimsPlugin
lateinit var platform: WorldGuardPlatform
lateinit var user: User
lateinit var regionContainer: RegionContainer

fun setUpInit(obj: Any) {
    MockKAnnotations.init(obj)
    MockitoAnnotations.openMocks(obj)
    server = MockBukkit.mock(CustomServerMock())
    plugin = MockBukkit.load(CozyClaimsPlugin::class.java)

    user = Mockito.mock(User::class.java)
    platform = Mockito.mock(WorldGuardPlatform::class.java)
    regionContainer = Mockito.mock(RegionContainer::class.java)

    WorldGuard.getInstance().platform = platform
    Mockito.`when`(platform.regionContainer).thenReturn(regionContainer)
    Mockito.`when`(CozyClaimsPlugin.essentials.getUser(Mockito.any<Player>())).thenReturn(user)
}

fun tearDownInit() {
    DataConfig.delete()
    MockBukkit.unmock()
}

