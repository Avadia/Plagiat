package net.samagames.plagiat.modules.splegg;

import net.samagames.api.games.Status;
import net.samagames.plagiat.Plagiat;
import net.samagames.plagiat.modules.AbstractModule;
import net.samagames.plagiat.modules.MCServer;
import net.samagames.plagiat.modules.dimensions.InternalBlockBreakEvent;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Egg;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.BlockIterator;

/*
 * This file is part of Plagiat.
 *
 * Plagiat is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Plagiat is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Plagiat.  If not, see <http://www.gnu.org/licenses/>.
 */
public class SpleggModule extends AbstractModule {
    private final ItemStack egg;

    /**
     * Splegg module constructor
     *
     * @param plugin Plagiat's plugin instance
     */
    public SpleggModule(Plagiat plugin) {
        super(plugin, "quake", MCServer.HIVEMC);

        this.egg = new ItemStack(Material.EGG, 64);
        ItemMeta itemMeta = this.egg.getItemMeta();
        itemMeta.setDisplayName("Oeuf " + this.getServerSuffix());
        itemMeta.addEnchant(Enchantment.DURABILITY, 1, true);
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE);
        itemMeta.spigot().setUnbreakable(true);
    }

    /**
     * Start the egg trail on start
     */
    @Override
    public void handleGameStart() {
        this.plugin.getServer().getScheduler().runTaskTimer(this.plugin, () ->
        {
            World world = this.plugin.getServer().getWorlds().get(0);
            world.getEntitiesByClass(Egg.class).forEach(e -> world.spawnParticle(Particle.REDSTONE, e.getLocation(), 0, 0D, 0D, 0D, 0D));
            world.getEntitiesByClass(Egg.class).forEach(e -> world.spawnParticle(Particle.REDSTONE, e.getLocation(), 0, 0D, 0D, 0D, 0D));
        }, 1L, 1L);
    }

    /**
     * Detect interact with entity, getting egg from armorstand
     *
     * @param event Bukkit event instance
     */
    @EventHandler
    public void onEntityInteract(PlayerInteractAtEntityEvent event) {
        if (this.plugin.getGame().getStatus() != Status.IN_GAME)
            return;
        if (event.getRightClicked() instanceof ArmorStand
                && ((ArmorStand) event.getRightClicked()).getItemInHand() != null
                && ((ArmorStand) event.getRightClicked()).getItemInHand().getType() == Material.EGG) {
            if (event.getPlayer().getInventory().firstEmpty() == -1) {
                event.getPlayer().sendMessage(ChatColor.RED + "Votre inventaire est plein.");
                return;
            }
            event.getRightClicked().remove();
            event.setCancelled(true);
            event.getPlayer().getInventory().addItem(this.egg);
            event.getPlayer().sendMessage(ChatColor.GOLD + "Vous avez maintenant l'" + ChatColor.AQUA + "Oeuf" + ChatColor.GOLD + ". Tirez sur des blocs pour les casser.");
            event.getPlayer().getWorld().playSound(event.getPlayer().getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1F, 1F);
        }
    }

    /**
     * Destroy block on egg land
     *
     * @param event Bukkit event instance
     */
    @EventHandler
    public void onEggLand(PlayerEggThrowEvent event) {
        BlockIterator blockIterator = new BlockIterator(event.getEgg().getWorld(), event.getEgg().getLocation().toVector(), event.getEgg().getVelocity().normalize(), 0.0D, 4);
        Block hitBlock = null;
        while (blockIterator.hasNext()) {
            hitBlock = blockIterator.next();
            if (hitBlock.getType() != Material.AIR)
                break;
        }

        if (hitBlock != null)
            hitBlock.setType(Material.AIR);

        event.setHatching(false);
        event.setNumHatches((byte) 0);
        this.plugin.getServer().getPluginManager().callEvent(new InternalBlockBreakEvent(hitBlock));
    }
}