package net.cozymc.main.command

import net.cozymc.main.BukkitTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested

@DisplayName("The abandon command")
class ClaimAbandonCommandTest : BukkitTest() {
    @Nested
    @DisplayName("is performed by a player outside a claim")
    inner class IsPerformedByAPlayerOutSideAClaim {
        @Test
        @DisplayName("who is a claim owner then fail")
        fun whoIsAClaimOwnerThenFail(): Unit {

        }

        @Test
        @DisplayName("who is a trustee of a claim then fail")
        fun whoIsATrusteeOfTheClaimThenFail() {

        }

        @Test
        @DisplayName("who is a member of a claim then fail")
        fun whoIsAMemberOfAClaimThenFail(): Unit {

        }

        @Test
        @DisplayName("who is not a relative of a claim then fail")
        fun whoIsNotARelativeOfTheClaimThenFail(): Unit {

        }
    }

    @Nested
    @DisplayName("is performed by a player inside a claim")
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