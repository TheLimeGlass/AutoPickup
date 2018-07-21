package me.limeglass.autopickup

import com.sk89q.worldguard.bukkit.WorldGuardPlugin
import com.sk89q.worldguard.protection.flags.StateFlag
import me.limeglass.autopickup.listener.EventListener
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.plugin.java.JavaPlugin

class AutoPickup : JavaPlugin() {

    var instance = this;

    var flag = StateFlag("auto-pickup", false)
    val worldGuard: WorldGuardPlugin?
        get() {
            val plugin = server.pluginManager.getPlugin("WorldGuard")
            return if (plugin == null || plugin !is WorldGuardPlugin) null else plugin
        }

    override fun onEnable() {
        instance = this
        saveDefaultConfig()
        server.pluginManager.registerEvents(EventListener(config), this)
        server.pluginManager.registerEvents(PickupListener(this), this)
    }

    companion object {
        fun getConfig(): FileConfiguration {
            return this.getConfig()
        }
    }

    override fun onLoad() {
        if (worldGuard == null) return
        val registry = worldGuard!!.flagRegistry
        registry.register(this.flag)
    }
}