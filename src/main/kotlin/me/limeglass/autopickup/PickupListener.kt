package me.limeglass.autopickup
import com.sk89q.worldguard.protection.flags.StateFlag
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.inventory.ItemStack
import java.util.*

class PickupListener(private val plugin: AutoPickup) : Listener {

    private val random: Random = Random()

    private fun toMineral(ore: Material): ItemStack? {
        return when (ore) {
            Material.COAL_ORE -> ItemStack(Material.COAL, 1)
            Material.IRON_ORE -> ItemStack(Material.IRON_INGOT, 1)
            Material.GOLD_ORE -> ItemStack(Material.GOLD_INGOT, 1)
            Material.DIAMOND_ORE -> ItemStack(Material.DIAMOND, 1)
            Material.EMERALD_ORE -> ItemStack(Material.EMERALD, 1)
            Material.LAPIS_ORE -> ItemStack(Material.INK_SACK, 1, 4)
            Material.REDSTONE_ORE -> ItemStack(Material.REDSTONE, 1)
            else -> null
        }
    }


    @EventHandler
    fun onNewBreak(event: BlockBreakEvent) {
        if (event.isCancelled) return
        val block = event.block
        val applicable = plugin.worldGuard!!.getRegionManager(block.world).getApplicableRegions(block.location)
        val state = applicable.queryState(null, plugin.flag)
        if (state != StateFlag.State.ALLOW) return
        val itemInHand = event.player.inventory.itemInMainHand
        val level = itemInHand.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS)
        var blockType = block.type
        if (blockType == Material.GLOWING_REDSTONE_ORE) blockType = Material.REDSTONE_ORE
        for (itemStack in event.block.drops) {
            var drop: ItemStack?
            drop = toMineral(itemStack.type)
            if (drop == null) drop = itemStack
            if (itemInHand.containsEnchantment(Enchantment.SILK_TOUCH)) {
                drop = ItemStack(blockType, 1)
            }
            val finalItemStack = ItemStack(drop!!.type, getDropCount(itemStack.type, level, this.random), drop.durability)
            val canFit = Utils.addItem(event.player, finalItemStack)
            if (canFit) {
                block.drops.clear()
                block.type = Material.AIR
            } else {
                //TODO Call an event and add configurations for this.
                event.isCancelled = true
            }
        }
    }

    private fun getDropCount(material: Material, level: Int, random: Random): Int {
        if (level > 0) {
            var drops = random.nextInt(level + 2) - 1
            if (drops < 0) drops = 0
            return multiplyOres(material, random) * (drops + 1)
        }
        return multiplyOres(material, random)
    }

    private fun multiplyOres(material: Material, random: Random): Int {
        return if (material == Material.REDSTONE_ORE || material == Material.LAPIS_ORE) 2 + random.nextInt(3) else 1
    }
}