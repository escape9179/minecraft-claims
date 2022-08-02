package net.cozymc.api

import net.cozymc.main.CozyClaimsPlugin
import org.bukkit.Bukkit
import org.bukkit.entity.Player

typealias PlayerTask = (Player) -> Unit

object PlayerThread {
    val tasks = mutableMapOf<Long, MutableList<PlayerTask>>()
    var count = 0

    init {
        Bukkit.getScheduler().runTaskTimer(CozyClaimsPlugin.instance, {
            tasks.entries.forEach { entry ->
                if (count % entry.key == 0L)
                    Bukkit.getOnlinePlayers().forEach { player ->
                        entry.value.forEach {
                            it.invoke(player)
                        }
                    }
            }
            count++
        }, 1, 1)
    }

    fun addTask(rateInTicks: Long, task: PlayerTask) {
        tasks[rateInTicks]?.add(task) ?: run { tasks[rateInTicks] = mutableListOf(task) }
    }
}