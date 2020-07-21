package net.samagames.plagiat.listener;

import net.samagames.plagiat.Plagiat;
import net.samagames.plagiat.game.PlagiatPlayer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.UUID;

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
public class PlagiatSpectatorListener implements Listener {
    private final Plagiat plugin;

    /**
     * Plagiat Spectator Listener constructor
     *
     * @param plugin Plagiat plugin instance
     */
    public PlagiatSpectatorListener(Plagiat plugin) {
        this.plugin = plugin;
    }

    /**
     * Cancel inventory change
     *
     * @param event Bukkit event instance
     */
    @EventHandler(ignoreCancelled = true)
    public void onInventoryChange(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player)
            this.cancelIfSpectator(event.getWhoClicked().getUniqueId(), event);
    }

    /**
     * Cancel damages
     *
     * @param event Bukkit event instance
     */
    @EventHandler(ignoreCancelled = true)
    public void onDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player)
            this.cancelIfSpectator(event.getEntity().getUniqueId(), event);
    }

    /**
     * Cancel damages
     *
     * @param event Bukkit event instance
     */
    @EventHandler(ignoreCancelled = true)
    public void onDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player)
            this.cancelIfSpectator(event.getDamager().getUniqueId(), event);
    }

    /**
     * Cancel drops
     *
     * @param event Bukkit event instance
     */
    @EventHandler(ignoreCancelled = true)
    public void onDrop(PlayerDropItemEvent event) {
        this.cancelIfSpectator(event.getPlayer().getUniqueId(), event);
    }

    /**
     * Cancel interact
     * Kick spectator if clicking the leave bed
     *
     * @param event Bukkit event instance
     */
    @EventHandler(ignoreCancelled = true)
    public void onInteract(PlayerInteractEvent event) {
        PlagiatPlayer plagiatPlayer = this.plugin.getGame().getPlayer(event.getPlayer().getUniqueId());
        if (plagiatPlayer != null && plagiatPlayer.isSpectator()) {
            event.setCancelled(true);
            if (event.getItem() != null && event.getItem().getType() == Material.BED)
                this.plugin.getSamaGamesAPI().getGameManager().kickPlayer(event.getPlayer(), null);
        }
    }

    /**
     * Cancel block breaking
     *
     * @param event Bukkit event instance
     */
    @EventHandler
    public void onBlockBread(BlockBreakEvent event) {
        this.cancelIfSpectator(event.getPlayer().getUniqueId(), event);
    }

    /**
     * Cancel block placing
     *
     * @param event Bukkit event instance
     */
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        this.cancelIfSpectator(event.getPlayer().getUniqueId(), event);
    }

    /**
     * Cancel an event if the player is spectator
     *
     * @param uuid  Player uuid
     * @param event Bukkit event instance
     */
    private void cancelIfSpectator(UUID uuid, Cancellable event) {
        PlagiatPlayer plagiatPlayer = this.plugin.getGame().getPlayer(uuid);
        if (plagiatPlayer == null || plagiatPlayer.isSpectator())
            event.setCancelled(true);
    }
}
