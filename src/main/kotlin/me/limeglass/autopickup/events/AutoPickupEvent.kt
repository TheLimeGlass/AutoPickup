package me.limeglass.autopickup.events

import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

data class AutoPickupEvent(val player: Player, val block: Block): Event() {

    val eventHandlers = HandlerList()

    override fun getHandlers(): HandlerList {
        return eventHandlers
    }

    fun getHandlerList(): HandlerList {
        return eventHandlers
    }
}