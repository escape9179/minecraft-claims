package net.cozymc.main.file

import net.cozymc.main.CONFIG_FILE_NAME
import net.cozymc.main.DATA_FOLDER_PATH
import net.cozymc.main.claim.MemberLevel
import org.bukkit.Particle
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import java.io.File

object MainConfig {
    private var file = File(DATA_FOLDER_PATH, CONFIG_FILE_NAME)
    private var config = YamlConfiguration.loadConfiguration(file)

    fun getClaimCost(): Int {
        return config.getInt("claim.cost")
    }

    fun getClaimInfoDisplay(): List<String> {
        return config.getStringList("claim.info-display")
    }

    fun getClaimParticleEffectType(): Particle {
        return Particle.valueOf(config.getString("claim.particleType", Particle.VILLAGER_HAPPY.name).uppercase())
    }

    fun getChunkClaimSuccessMessage(): String {
        return config.getString("message.chunkClaimSuccess")
    }

    fun getChunkClaimFailureMoneyMessage(): String {
        return String.format(config.getString("message.chunkClaimFailureMoney"), getClaimCost())
    }

    fun getChunkClaimFailureClaimedMessage(): String {
        return config.getString("message.chunkClaimFailureClaimed")
    }

    fun getChunkClaimFailureRegionMessage(): String {
        return config.getString("message.chunkClaimFailureRegion")
    }

    fun getNotClaimOwnerMessage(): String {
        return config.getString("message.notClaimOwner")
    }

    fun getNotOwnerOfOccupyingClaimMessage(): String {
        return config.getString("message.notOwnerOfOccupyingClaim")
    }

    fun getAddTrusteeSuccessMessage(name: String): String {
        return String.format(config.getString("message.addTrusteeSuccess"), name)
    }

    fun getRemoveTrusteeSuccessMessage(name: String): String {
        return String.format(config.getString("message.removeTrusteeSuccess"), name)
    }

    fun getPersonAlreadyTrustedMessage(): String {
        return config.getString("message.personAlreadyTrusted")
    }

    fun getAddMemberSuccessMessage(name: String): String {
        return String.format(config.getString("message.addMemberSuccess"), name)
    }

    fun getRemoveMemberSuccessMessage(name: String): String {
        return String.format(config.getString("message.removeMemberSuccess"), name)
    }

    fun getRemoveMemberFailureSelfMessage(): String {
        return config.getString("message.removeMemberFailureSelf")
    }

    fun getMemberChangeLevelSuccessMessage(name: String, level: MemberLevel): String {
        return String.format(config.getString("message.memberChangeLevelSuccess"), name, level.name.lowercase())
    }

    fun getMemberChangeLevelFailureMessage(name: String, level: MemberLevel): String {
        return String.format(config.getString("message.memberChangeLevelFailure"), name, level.name.lowercase())
    }

    fun getNotMemberOfClaimMessage(): String {
        return config.getString("message.notMemberOfClaim")
    }

    fun getNotTrusteeOfClaimMessage(): String {
        return config.getString("message.notTrusteeOfClaim")
    }

    fun getNotInsideClaimMessage(): String {
        return config.getString("message.notInsideClaim")
    }

    fun getUnknownPlayerMessage(): String {
        return config.getString("message.unknownPlayer")
    }

    fun getClaimAbandonSuccessMessage(): String {
        return config.getString("message.claimAbandonSuccess")
    }

    fun getClaimAbandonSuccessMessage(chunkCount: Int): String {
        return String.format(config.getString("message.claimAbandonAllSuccess"), chunkCount)
    }

    fun getClaimAbandonFailureMessage(): String {
        return config.getString("message.claimAbandonFailure")
    }

    fun getClaimAbandonAllSuccessMessage(size: Int): String {
        return String.format(config.getString("message.claimAbandonAllSuccess"), size)
    }
    fun getUsageSpecifyClaimNameMessage(): String {
        return config.getString("message.usageSpecifyClaimName")
    }

    fun getHasNoClaimMessage(player: Player): String {
        return String.format(config.getString("message.hasNoClaim"), player.name)
    }

    fun getClaimRemoveSuccessOtherMessage(player: Player, chunkCount: Int): String {
        return String.format(config.getString("message.claimRemoveSuccessOther"), player.name, chunkCount)
    }

    fun getPersonAlreadyMemberMessage(): String {
        return config.getString("message.personAlreadyMember")
    }

    fun getInteractNotAllowedMessage(): String {
        return config.getString("message.interactNotAllowed")
    }

    fun reload() {
        config = YamlConfiguration.loadConfiguration(file)
    }
}