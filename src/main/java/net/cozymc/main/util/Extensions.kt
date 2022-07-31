package net.cozymc.main.util

import com.earth2me.essentials.Essentials
import net.cozymc.main.CozyClaimsPlugin
import org.bukkit.entity.Player

fun Player.getBalance(): Int {
    return CozyClaimsPlugin.getEssentials().getUser(this).money.intValueExact()
}