package net.cozymc.main.file

import net.cozymc.main.CONFIG_FILE_NAME
import net.cozymc.main.DATA_FOLDER_PATH
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import java.io.File

object MainConfig {
    private var file = File(DATA_FOLDER_PATH, CONFIG_FILE_NAME)
    private var config = YamlConfiguration.loadConfiguration(file)

    fun getClaimCost(): Int {
        return config.getInt("claim.cost")
    }

    fun getChunkClaimSuccessMessage(): String {
        return config.getString("message.chunkClaimSuccessMessage")
    }

    fun getChunkClaimFailureMoneyMessage(): String {
        return config.getString("message.chunkClaimFailureMoneyMessage")
    }

    fun getChunkClaimFailureClaimedMessage(): String {
        return config.getString("message.chunkClaimFailureClaimedMessage")
    }

    fun getChunkClaimFailureRegionMessage(): String {
        return config.getString("message.chunkClaimFailureRegionMessage")
    }

    fun getNotClaimOwnerMessage(): String {
        return config.getString("message.notClaimOwnerMessage")
    }

    fun getAddMemberSuccessMessage(name: String): String {
        return config.getString("message.addMemberSuccessMessage")
    }

    fun getRemoveMemberSuccessMessage(name: String): String {
        return config.getString("message.removeMemberSuccessMessage")
    }

    fun getUnknownPlayerMessage(): String {
        return config.getString("message.unknownPlayerMessage")
    }

    fun getClaimRemoveSuccessMessage(chunkCount: Int): String {
        return config.getString("message.claimRemoveSuccessMessage")
    }

    fun getUsageSpecifyClaimNameMessage(): String {
        return config.getString("message.usageSpecifyClaimNameMessage")
    }

    fun getHasNoClaimMessage(player: Player): String {
        return config.getString("message.hasNoClaimMessage")
    }

    fun getClaimRemoveSuccessOtherMessage(player: Player, chunkCount: Int): String {
        return config.getString("message.claimRemoveSuccessOtherMessage")
    }

    fun reload() {
        config = YamlConfiguration.loadConfiguration(file)
    }
}