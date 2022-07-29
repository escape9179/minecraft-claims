package net.cozymc.main.file

import net.cozymc.main.Claim
import net.cozymc.main.DATA_FILE_NAME
import net.cozymc.main.DATA_FOLDER_PATH
import org.bukkit.Chunk
import org.bukkit.Location
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.util.*

object Data {
    val file = File(DATA_FOLDER_PATH, DATA_FILE_NAME)
    val dataConfig = YamlConfiguration.loadConfiguration(file)

    fun getClaimAt(location: Location): Claim? = TODO("Implementation")

    fun addClaim(owner: UUID, chunk: Chunk) {
        TODO("Implementation")
    }
}