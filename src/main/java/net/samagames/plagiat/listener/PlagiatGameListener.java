package net.samagames.plagiat.listener;

import net.samagames.plagiat.Plagiat;
import net.samagames.plagiat.game.PlagiatPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

/**
 * Plagiat Game Listener class
 */
public class PlagiatGameListener implements Listener
{
    private Plagiat plugin;

    /**
     * Plagiat Game Listener constructor
     *
     * @param plugin Plagiat plugin instance
     */
    public PlagiatGameListener(Plagiat plugin)
    {
        this.plugin = plugin;
    }

    /**
     * Instant kill players on void damage
     * Monitor event, to be cancellable by modules
     *
     * @param event Bukkit event instance
     */
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onVoidDamage(EntityDamageEvent event)
    {
        if (event.getCause() == EntityDamageEvent.DamageCause.VOID)
            event.setDamage(Double.MAX_VALUE);
    }

    /**
     * Set player as spectator on death
     *
     * @param event Bukkit event instance
     */
    @EventHandler
    public void onDeath(PlayerDeathEvent event)
    {
        event.setDeathMessage(null);
        PlagiatPlayer plagiatPlayer = this.plugin.getGame().getPlayer(event.getEntity().getUniqueId());
        if (plagiatPlayer != null)
            this.plugin.getServer().getScheduler().runTaskLater(this.plugin, () -> this.plugin.getGame().stumpPlayer(plagiatPlayer, event.getEntity(), false), 1L);
    }
}
