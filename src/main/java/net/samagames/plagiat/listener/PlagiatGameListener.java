package net.samagames.plagiat.listener;

import net.samagames.plagiat.Plagiat;
import net.samagames.plagiat.game.PlagiatPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

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
public class PlagiatGameListener implements Listener {
    private final Plagiat plugin;

    /**
     * Plagiat Game Listener constructor
     *
     * @param plugin Plagiat plugin instance
     */
    public PlagiatGameListener(Plagiat plugin) {
        this.plugin = plugin;
    }

    /**
     * Instant kill players on void damage
     * Monitor event, to be cancellable by modules
     *
     * @param event Bukkit event instance
     */
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onVoidDamage(EntityDamageEvent event) {
        if (event.getCause() == EntityDamageEvent.DamageCause.VOID)
            event.setDamage(Double.MAX_VALUE);
    }

    /**
     * Set player as spectator on death
     *
     * @param event Bukkit event instance
     */
    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        event.setDeathMessage(null);
        PlagiatPlayer plagiatPlayer = this.plugin.getGame().getPlayer(event.getEntity().getUniqueId());
        if (plagiatPlayer != null)
            this.plugin.getServer().getScheduler().runTaskLater(this.plugin, () -> this.plugin.getGame().stumpPlayer(plagiatPlayer, event.getEntity(), false), 1L);
    }
}
