package me.limeglass.autopickup
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.Inventory

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
            TODO("Add support for calling an event when the inventory is full")
            player.sendMessage(colour("&cYour inventory is currently full!"))
            false
        }
    }

    private fun colour(input: String): String {
        return ChatColor.translateAlternateColorCodes('&', input);
    }
}