package net.cozymc.main

import com.earth2me.essentials.Essentials
import com.sk89q.worldguard.bukkit.WorldGuardPlugin
import org.bukkit.entity.Player
import org.mockito.Mock
import org.mockito.Mockito

//fun getEssentialsMock(player: Player? = null): Essentials {
//    val essentials = Mockito.mock(Essentials::class.java)
//    Mockito.`when`(essentials.getUser(player).thenRetu
//    return essentials
//}

fun getWorldGuardPluginMock(): WorldGuardPlugin {
    return Mockito.mock(WorldGuardPlugin::class.java)
}