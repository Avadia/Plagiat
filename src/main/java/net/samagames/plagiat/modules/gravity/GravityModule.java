package net.samagames.plagiat.modules.gravity;

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

/**
 * Gravity module class
 */
public class GravityModule extends AbstractModule
{
    private Location parkourSpawn;
    private Area parkourLandingArea;

    /**
     * Gravity module's constructor
     *
     * @param plugin Plagiat's plugin instance
     */
    public GravityModule(Plagiat plugin)
    {
        super(plugin, "gravity", MCServer.HIVEMC);
    }

    /**
     * Event method, to load configuration
     * {@link AbstractModule#handleGameStart()}
     */
    @Override
    public void handleGameStart()
    {
        JsonObject jsonObject = this.getConfigRoot();
        JsonElement element = jsonObject.get("spawn");
        this.parkourSpawn = element == null ? null : LocationUtils.str2loc(element.getAsString());
        element = jsonObject.get("landingArea");
        this.parkourLandingArea = element == null ? null : Area.str2area(element.getAsString());
    }

    /**
     * Event method, teleport player to gravity parkour on void damage
     *
     * @param event Bukkit event instance
     */
    @EventHandler
    public void onDamage(EntityDamageEvent event)
    {
        if (this.plugin.getGame().getStatus() != Status.IN_GAME || this.parkourLandingArea == null || this.parkourSpawn == null)
            return ;
        if (event.getEntity() instanceof Player && event.getCause() == EntityDamageEvent.DamageCause.VOID)
        {
            event.setCancelled(true);
            event.setDamage(0D);
            Player player = (Player)event.getEntity();
            player.teleport(this.parkourSpawn);
            Titles.sendTitle(player, 0, 40, 0, "", ChatColor.GOLD + "Tombez dans l'eau !");
        }
    }

    /**
     * Event method, respawn player if he lands in water
     *
     * @param event Bukkit event instance
     */
    @EventHandler
    public void onMove(PlayerMoveEvent event)
    {
        if (this.plugin.getGame().getStatus() != Status.IN_GAME || this.parkourLandingArea == null || this.parkourSpawn == null)
            return ;
        if (event.getTo().getBlock().getType() == Material.WATER && this.parkourLandingArea.isInArea(event.getTo()))
        {
            PlagiatPlayer plagiatPlayer = this.plugin.getGame().getPlayer(event.getPlayer().getUniqueId());
            if (plagiatPlayer == null || plagiatPlayer.isSpectator())
                return ;
            this.plugin.getGame().getCoherenceMachine().getMessageManager().writeCustomMessage(event.getPlayer().getDisplayName() + ChatColor.YELLOW + " a réussi son saut de dé à coudre. Il revient donc en jeu.", true);
            event.getPlayer().setFallDistance(0F);
            event.getPlayer().setHealth(event.getPlayer().getMaxHealth());
            event.getPlayer().setFoodLevel(30);
            this.plugin.getServer().getScheduler().runTask(this.plugin, () -> this.plugin.getGame().respawnPlayer(event.getPlayer(), plagiatPlayer));
        }
    }
}
