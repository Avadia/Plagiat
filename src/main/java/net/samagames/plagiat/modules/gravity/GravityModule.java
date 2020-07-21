package net.samagames.plagiat.modules.gravity;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.samagames.api.games.Status;
import net.samagames.plagiat.Plagiat;
import net.samagames.plagiat.game.PlagiatPlayer;
import net.samagames.plagiat.modules.AbstractModule;
import net.samagames.plagiat.modules.MCServer;
import net.samagames.tools.Area;
import net.samagames.tools.LocationUtils;
import net.samagames.tools.Titles;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

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
public class GravityModule extends AbstractModule {
    private final SecureRandom random;
    private List<Location> parkourSpawns;
    private Area parkourLandingArea;

    /**
     * Gravity module's constructor
     *
     * @param plugin Plagiat's plugin instance
     */
    public GravityModule(Plagiat plugin) {
        super(plugin, "gravity", MCServer.HIVEMC);
        this.random = new SecureRandom();
    }

    /**
     * Event method, to load configuration
     * {@link AbstractModule#handleGameStart()}
     */
    @Override
    public void handleGameStart() {
        JsonObject jsonObject = this.getConfigRoot();
        JsonArray array = jsonObject.get("spawns").getAsJsonArray();
        this.parkourSpawns = new ArrayList<>();
        array.forEach(jsonElement -> this.parkourSpawns.add(LocationUtils.str2loc(jsonElement.getAsString())));
        JsonElement element = jsonObject.get("landingArea");
        this.parkourLandingArea = element == null ? null : Area.str2area(element.getAsString());
    }

    /**
     * Event method, teleport player to gravity parkour on void damage
     *
     * @param event Bukkit event instance
     */
    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (this.plugin.getGame().getStatus() != Status.IN_GAME || this.parkourLandingArea == null || this.parkourSpawns.isEmpty())
            return;

        if (event.getEntity() instanceof Player && event.getCause() == EntityDamageEvent.DamageCause.VOID) {
            event.setCancelled(true);
            event.setDamage(0D);
            Player player = (Player) event.getEntity();
            player.teleport(this.parkourSpawns.get(this.random.nextInt(this.parkourSpawns.size())));
            Titles.sendTitle(player, 0, 40, 0, "", ChatColor.GOLD + "Tombez dans l'eau !");

            player.setAllowFlight(true);
            player.setFlying(true);
            float old = player.getFlySpeed();
            player.setFlySpeed(0F);

            this.plugin.getServer().getScheduler().runTaskLater(this.plugin, () ->
            {
                player.setFlySpeed(old);
                player.setFlying(false);
                player.setAllowFlight(false);
            }, 80L);

            this.plugin.getServer().getScheduler().runTaskLater(this.plugin, () -> Titles.sendTitle(player, 0, 60, 0, ChatColor.GOLD + "Tombez dans l'eau", "Si vous souhaitez revivre !"), 10L);
        }
    }

    /**
     * Event method, respawn player if he lands in water
     *
     * @param event Bukkit event instance
     */
    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (this.plugin.getGame().getStatus() != Status.IN_GAME || this.parkourLandingArea == null || this.parkourSpawns.isEmpty())
            return;

        if (event.getTo().getBlock().getType() == Material.STATIONARY_WATER && this.parkourLandingArea.isInArea(event.getTo())) {
            PlagiatPlayer plagiatPlayer = this.plugin.getGame().getPlayer(event.getPlayer().getUniqueId());
            if (plagiatPlayer == null || plagiatPlayer.isSpectator())
                return;

            this.plugin.getGame().getCoherenceMachine().getMessageManager().writeCustomMessage(event.getPlayer().getDisplayName() + ChatColor.YELLOW + " a réussi son saut de dropper. Il revient donc en jeu.", true);
            event.getPlayer().setFallDistance(0F);
            event.getPlayer().setHealth(event.getPlayer().getMaxHealth());
            event.getPlayer().setFoodLevel(30);
            this.plugin.getServer().getScheduler().runTask(this.plugin, () -> this.plugin.getGame().respawnPlayer(event.getPlayer(), plagiatPlayer));
        }
    }
}
