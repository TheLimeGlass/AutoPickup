package me.limeglass.autopickup.listener

import me.limeglass.autopickup.Utils
import me.limeglass.autopickup.events.PickupFullEvent
import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import java.util.concurrent.ThreadLocalRandom

class EventListener(private val config: FileConfiguration): Listener {

    private val NODE_UNSET = "configuration node not set"

    @EventHandler
    fun onPickFull(event: PickupFullEvent) {
        val player = event.player
        if (config.getBoolean("inventory-full.actionbar.enabled", false)) {
            var component = config.getString("inventory-full.actionbar.message", NODE_UNSET)
            component = Utils.placeholder(component, player, event.block)
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent(component))
        }
        if (config.getBoolean("inventory-full.chat.enabled", false)) {
            var message = config.getString("inventory-full.chat.message", NODE_UNSET)
            message = Utils.placeholder(message, player, event.block)
            player.sendMessage(Utils.colour(message))
        }
        if (config.getBoolean("inventory-full.sound.enabled", false)) {
            val volume = config.getInt("inventory-full.sound.volume", 1).toFloat()
            var pitch = config.getInt("inventory-full.sound.pitch", -1).toFloat()
            if (pitch <0) pitch = ThreadLocalRandom.current().nextDouble(0.0, 1.0).toFloat()
            player.playSound(player.location, config.getString("inventory-full.sound.sound"), volume, pitch)
        }
    }

}