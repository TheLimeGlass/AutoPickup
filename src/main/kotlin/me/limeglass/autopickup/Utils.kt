package me.limeglass.autopickup

import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import java.util.regex.Pattern

object Utils {

    private fun freeSpace(inventory: Inventory): Boolean {
        for (itemStack in inventory.contents) if (itemStack == null || itemStack.type == Material.AIR) {
            return true
        }
        return false
    }

    private fun canFitItemStack(inventory: Inventory, itemStack: ItemStack): Boolean {
        if (freeSpace(inventory)) return true
        for (inventoryItem in inventory.contents) {
            if (inventoryItem.isSimilar(itemStack) && inventoryItem.amount < inventoryItem.maxStackSize) {
                return true
            }
        }
        return false
    }

    fun addItem(player: Player, itemStack: ItemStack): Boolean {
        return if (canFitItemStack(player.inventory, itemStack)) {
            player.inventory.addItem(itemStack)
            true
        } else {
            false
        }
    }

    fun colour(input: String): String {
        return ChatColor.translateAlternateColorCodes('&', input);
    }

    fun placeholder(string: String, player: Player, block: Block): String {
        var input = string
        input = input.replace(Pattern.quote("{PLAYER}"), player.name)
        input = input.replace(Pattern.quote("{PREFIX}"), AutoPickup.getConfig().getString("prefix"))
        input = input.replace(Pattern.quote("{BLOCK}"), block.type.toString().toLowerCase().replace("_", " "))
        return input
    }
}