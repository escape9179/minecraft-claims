package net.cozymc.main

import be.seeseemelk.mockbukkit.MockBukkit
import be.seeseemelk.mockbukkit.ServerMock
import be.seeseemelk.mockbukkit.plugin.PluginManagerMock
import com.earth2me.essentials.Essentials
import com.sk89q.worldguard.bukkit.WorldGuardPlugin
import net.cozymc.main.mock.CustomServerMock
import org.bukkit.Keyed
import org.bukkit.Registry
import org.bukkit.plugin.Plugin
import org.junit.After
import org.junit.Before
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import kotlin.test.assertNotNull

class CozyClaimsPluginTest {
    private lateinit var server: ServerMock
    private lateinit var plugin: CozyClaimsPlugin

    @BeforeEach
    fun before() {
        server = MockBukkit.mock(CustomServerMock())
        plugin = MockBukkit.load(CozyClaimsPlugin::class.java)
    }

    @AfterEach
    fun after() {
        MockBukkit.unmock()
    }

    @Test
    @DisplayName("The server has necessary dependencies")
    fun testDependenciesExist() {
        assertNotNull(server.pluginManager.getPlugin("Essentials"))
        assertNotNull(server.pluginManager.getPlugin("WorldGuard"))
    }
}