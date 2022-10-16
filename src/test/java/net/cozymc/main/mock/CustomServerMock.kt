package net.cozymc.main.mock

import be.seeseemelk.mockbukkit.ServerMock
import be.seeseemelk.mockbukkit.plugin.PluginManagerMock
import org.bukkit.Keyed
import org.bukkit.Registry

class CustomServerMock : ServerMock() {

    private val pluginManager = CustomPluginManagerMock(this)
    override fun getPluginManager(): PluginManagerMock {
        return pluginManager
    }

    override fun shouldSendChatPreviews(): Boolean {
        TODO("Not yet implemented")
    }

    override fun isEnforcingSecureProfiles(): Boolean {
        TODO("Not yet implemented")
    }

    override fun getMaxChainedNeighborUpdates(): Int {
        TODO("Not yet implemented")
    }

    override fun <T : Keyed?> getRegistry(p0: Class<T>): Registry<T>? {
        TODO("Not yet implemented")
    }

    override fun isTickingWorlds(): Boolean {
        TODO("Not yet implemented")
    }
}