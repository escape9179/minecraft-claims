package net.cozymc.main.file

import net.cozymc.main.CONFIG_FILE_NAME
import net.cozymc.main.DATA_FOLDER_PATH
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

    fun getAddMemberSuccessMessage(name: String): String {
        return String.format(config.getString("message.addMemberSuccess"), name)
    }

    fun getRemoveMemberSuccessMessage(name: String): String {
        return String.format(config.getString("message.removeMemberSuccess"), name)
    }

    fun getUnknownPlayerMessage(): String {
        return config.getString("message.unknownPlayer")
    }

    fun getClaimRemoveSuccessMessage(chunkCount: Int): String {
        return String.format(config.getString("message.claimRemoveSuccess"), chunkCount)
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

    fun reload() {
        config = YamlConfiguration.loadConfiguration(file)
    }
}