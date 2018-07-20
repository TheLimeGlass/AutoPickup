package me.limeglass.autopickup

import com.sk89q.worldguard.bukkit.WorldGuardPlugin
import com.sk89q.worldguard.protection.flags.StateFlag
import org.bukkit.plugin.java.JavaPlugin

class AutoPickup : JavaPlugin() {

    var flag = StateFlag("auto-pickup", false)
    val worldGuard: WorldGuardPlugin?
        get() {
            val plugin = server.pluginManager.getPlugin("WorldGuard")
            return if (plugin == null || plugin !is WorldGuardPlugin) null else plugin
        }

    override fun onEnable() {
        server.pluginManager.registerEvents(PickupListener(this), this)
    }

    override fun onLoad() {
        if (worldGuard == null) return
        val registry = worldGuard!!.flagRegistry
        registry.register(this.flag)
    }
}