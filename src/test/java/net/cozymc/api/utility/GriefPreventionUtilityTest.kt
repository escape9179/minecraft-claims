package net.cozymc.api.utility

import net.cozymc.main.CozyClaimsTest
import net.cozymc.main.file.MainConfig
import net.cozymc.main.griefprevention.GriefPreventionUtility
import org.bukkit.WorldCreator
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.nio.file.Files
import java.nio.file.Paths

@DisplayName("The grief prevention utility")
internal class GriefPreventionUtilityTest : CozyClaimsTest() {
    @BeforeEach
    override fun setup() {
        super.setup()
        server.createWorld(WorldCreator.name("world"))
    }

    @Test
    @DisplayName("when loading claim data given correct path returns total claims loaded")
    fun whenLoadingClaimDataGivenCorrectPathReturnsTotalClaimsLoaded(): Unit {
        val claimCount = GriefPreventionUtility.loadAllClaims(MainConfig.getGriefPreventionClaimDataPath()).size
        val fileCount = Files.list(Paths.get(MainConfig.getGriefPreventionClaimDataPath())).count()
        assertTrue(claimCount == fileCount.toInt() - 1)
    }

    @Test
    @DisplayName("when loading claim data given incorrect path throws Exception")
    fun whenLoadingClaimDataGivenIncorrectPathThrowsException() {
        assertThrows(Exception::class.java) { GriefPreventionUtility.loadAllClaims("") }
    }
}