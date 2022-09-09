package net.cozymc.main.util

import be.seeseemelk.mockbukkit.WorldMock
import net.cozymc.main.CozyClaimsTest
import net.cozymc.main.exception.InvalidRegionException
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.WorldCreator
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.aggregator.ArgumentsAccessor
import org.junit.jupiter.params.provider.CsvSource
import org.mockito.Mockito
import java.util.*
import kotlin.math.ceil
import kotlin.math.pow

@DisplayName("The grief prevention claim")
internal class GriefPreventionClaimTest : CozyClaimsTest() {
    @BeforeEach
    override fun setup() {
        super.setup()
        server.addWorld(WorldMock(WorldCreator.name("world")))
        server.addWorld(WorldMock(WorldCreator.name("world_nether")))
    }

    @ParameterizedTest
    @CsvSource("1, 0, 0", "0, 1, 0", "0, 0, 1")
    @DisplayName("when created with lesser boundary further than greater boundary throws InvalidRegionException")
    fun whenCreatedWithLesserBoundaryFurtherThanGreaterBoundaryThrowsInvalidRegionException(accessor: ArgumentsAccessor): Unit {
        val world = Mockito.mock(World::class.java)
        val lesser = Location(world, 2.0, 2.0, 2.0)
        val greater = lesser.clone().subtract(accessor.getDouble(0), accessor.getDouble(1), accessor.getDouble(2))
        assertThrows(InvalidRegionException::class.java) { GriefPreventionClaim(UUID.randomUUID(), lesser, greater) }
    }

    @Test
    @DisplayName("when created with corners in different worlds throws InvalidRegionException")
    fun whenCreatedWithCornersInDifferentWorldsThrowsInvalidRegionException(): Unit {
        val lesser = Location(server.worlds[0], 1.0, 1.0, 1.0)
        val greater = Location(server.worlds[1], 2.0, 2.0, 2.0)
        assertThrows(InvalidRegionException::class.java) { GriefPreventionClaim(UUID.randomUUID(), lesser, greater) }
    }

    @Test
    @DisplayName("when created with lesser boundary less than greater boundary")
    fun whenCreatedWithLesserBoundaryLessThanGreaterBoundary(): Unit {
        val lesser = Location(server.worlds[0], 100.0, 0.0, 100.0)
        val greater = Location(server.worlds[0], 0.0, 0.0, 0.0)
        GriefPreventionClaim(UUID.randomUUID(), lesser, greater)
    }

    @ParameterizedTest
    @CsvSource("100, 100", "100, 50", "50, 100")
    @DisplayName("when getting chunks of varying region dimensions")
    fun whenGettingChunksOfVaryingRegionDimensions(accessor: ArgumentsAccessor): Unit {
        val x = accessor.getDouble(0)
        val z = accessor.getDouble(1)
        val lesser = Location(server.worlds[0], 0.0, 0.0, 0.0)
        val greater = Location(server.worlds[0], x, 10.0, z)
        val claim = GriefPreventionClaim(UUID.randomUUID(), lesser, greater).getChunks()
        val numOfChunks = ceil(x / 16.0) * ceil(z / 16.0)
        assertEquals(numOfChunks.toInt(), claim.size)
    }
}