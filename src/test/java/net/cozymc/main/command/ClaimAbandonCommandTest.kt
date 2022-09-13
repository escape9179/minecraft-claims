package net.cozymc.main.command

import be.seeseemelk.mockbukkit.entity.PlayerMock
import net.cozymc.main.CozyClaimsTest
import net.cozymc.main.claim.Claim
import net.cozymc.main.file.DataConfig
import net.cozymc.main.file.MainConfig
import net.cozymc.main.util.isClaimOwner
import net.cozymc.main.util.isRelativeOfClaim
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.mockito.Mockito
import java.util.*

@DisplayName("The abandon command")
class ClaimAbandonCommandTest : CozyClaimsTest() {

    lateinit var player: PlayerMock

    @BeforeEach
    override fun setup() {
        super.setup()
        player = Mockito.spy(server.addPlayer())
    }

    @Nested
    @DisplayName("is performed by a player outside a claim")
    inner class IsPerformedByAPlayerOutsideAClaim {
        @Test
        @DisplayName("who is a claim owner then fail")
        fun whoIsAClaimOwnerThenFail(): Unit {
            player.isOp = true
            val claim = Claim.create(player.uniqueId, server.getWorld("world")!!.getChunkAt(10, 10))
            DataConfig.saveClaim(claim)
            player.performCommand("claim abandon")
            assertTrue(player.isClaimOwner())
            Mockito.verify(player).sendMessage(MainConfig.getNotInsideClaimMessage())
        }

        @Test
        @DisplayName("who is a trustee of a claim then fail")
        fun whoIsATrusteeOfAClaimThenFail() {
            player.isOp = true
            val claim = Claim.create(UUID.randomUUID(), server.getWorld("world")!!.getChunkAt(10, 10))
            claim.trustees.add(player.uniqueId)
            DataConfig.saveClaim(claim)
            player.performCommand("claim abandon")
            Mockito.verify(player).sendMessage(MainConfig.getNotInsideClaimMessage())
            assertTrue(player.isRelativeOfClaim(claim))
        }

        @Test
        @DisplayName("who is a member of a claim then fail")
        fun whoIsAMemberOfAClaimThenFail(): Unit {
            player.isOp = true
            val claim = Claim.create(UUID.randomUUID(), server.getWorld("world")!!.getChunkAt(10, 10))
            claim.members.add(player.uniqueId)
            DataConfig.saveClaim(claim)
            player.performCommand("claim abandon")
            Mockito.verify(player).sendMessage(MainConfig.getNotInsideClaimMessage())
            assertTrue(player.isRelativeOfClaim(claim))
        }

        @Test
        @DisplayName("who is not a relative of a claim then fail")
        fun whoIsNotARelativeOfAClaimThenFail(): Unit {
            player.isOp = true
            DataConfig.saveClaim(Claim.create(UUID.randomUUID(), server.getWorld("world")!!.getChunkAt(10, 10)))
            player.performCommand("claim abandon")
            Mockito.verify(player, Mockito.times(1)).sendMessage(MainConfig.getNotRelativeOfAnyClaimMessage())
            assertTrue(DataConfig.loadClaims().size == 1)
        }
    }

    @Test
    @DisplayName("removes the claim when run by a claim member inside the claim")
    fun removesTheClaimWhenRunByAClaimMemberInsideTheClaim(): Unit {
        val owner = UUID.randomUUID()
        val claim = Claim.create(owner, player.chunk)
        DataConfig.saveClaim(claim)
        assertEquals(1, DataConfig.loadClaims().size)
        player.isOp = true
        claim.addMember(player)
        player.performCommand("claim abandon")
        Mockito.verify(player).sendMessage(MainConfig.getClaimAbandonSuccessMessage())
        assertNull(DataConfig.loadOwnerClaim(owner))
    }

    @Test
    @DisplayName("removes the claim when run by the claim owner inside the claim")
    fun removesTheClaimWhenRunByTheClaimOwnerInsideTheClaim(): Unit {
        val claim = Claim.create(player.uniqueId, player.chunk)
        DataConfig.saveClaim(claim)
        player.isOp = true
        assertEquals(1, DataConfig.loadClaims().size)
        player.performCommand("claim abandon")
        Mockito.verify(player).sendMessage(MainConfig.getClaimAbandonSuccessMessage())
        assertNull(DataConfig.loadOwnerClaim(player.uniqueId))
    }

    @Test
    @DisplayName("sends an error message when run by a relative of a different claim")
    fun sendsAnErrorMessageWhenRunByARelativeOfADifferentClaim(): Unit {


    }

    @Nested
    @DisplayName(" performed by a player inside a claim")
    inner class IsPerformedByAPlayerInsideAClaim {
        @Test
        @DisplayName("who is the claim owner then succeed")
        fun whoIsTheClaimOwnerThenSucceed(): Unit {

        }

        @Test
        @DisplayName("who is a claim owner then fail")
        fun whoIsAClaimOwnerThenFail(): Unit {

        }

        @Test
        @DisplayName("who is a trustee of the claim then fail")
        fun whoIsATrusteeOfTheClaimThenFail() {

        }

        @Test
        @DisplayName("who is a member of the claim then fail")
        fun whoIsAMemberOfAClaimThenFail(): Unit {

        }

        @Test
        @DisplayName("who is not a relative of the claim then fail")
        fun whoIsNotARelativeOfTheClaimThenFail(): Unit {

        }
    }

    @Test
    @DisplayName("is performed by the console then fail")
    fun isPerformedByConsoleThenFail(): Unit {

    }
}