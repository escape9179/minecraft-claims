package net.cozymc.main.mock

import be.seeseemelk.mockbukkit.ServerMock
import be.seeseemelk.mockbukkit.plugin.PluginManagerMock
import com.earth2me.essentials.Essentials
import com.sk89q.worldguard.bukkit.WorldGuardPlugin
import org.bukkit.plugin.Plugin
import org.mockito.Mockito

class CustomPluginManagerMock(server: ServerMock) : PluginManagerMock(server) {
    override fun getPlugin(name: String): Plugin? {
        return when (name.lowercase()) {
            "essentials" -> Mockito.mock(Essentials::class.java)
            "worldguard" -> Mockito.mock(WorldGuardPlugin::class.java)
            else ->  super.getPlugin(name)
        }
    }
}