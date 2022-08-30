package net.cozymc.main

import be.seeseemelk.mockbukkit.MockBukkit
import be.seeseemelk.mockbukkit.MockPlugin
import be.seeseemelk.mockbukkit.ServerMock
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertNotNull

class CozyClaimsPluginTest {
    private lateinit var server: ServerMock
    private lateinit var plugin: CozyClaimsPlugin

    @Before
    fun before() {
        server = MockBukkit.mock()
        plugin = MockBukkit.load(CozyClaimsPlugin::class.java)
    }

    @After
    fun after() {
        MockBukkit.unmock()
    }

    @Test
    fun testDependenciesExist() {
        assertNotNull(server.pluginManager.getPlugin("Essentials"))
    }
}