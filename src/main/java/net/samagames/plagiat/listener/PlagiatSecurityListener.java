package net.samagames.plagiat.listener;

import net.samagames.plagiat.Plagiat;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

/**
 * Plagiat main Listener
 */
public class PlagiatSecurityListener implements Listener
{
    private Plagiat plugin;

    /**
     * Plagiat listener constructor
     *
     * @param plugin Plagiat plugin instance
     */
    public PlagiatSecurityListener(Plagiat plugin)
    {
        this.plugin = plugin;
    }

    /**
     * Cancel damages before game start
     *
     * @param event Bukkit event instance
     */
    @EventHandler(ignoreCancelled = true)
    public void onDamage(EntityDamageEvent event)
    {
        if (!this.plugin.getGame().areDamagesActivated())
            event.setCancelled(true);
    }

    /**
     * Cancel build before game start
     *
     * @param event Bukkit event instance
     */
    @EventHandler(ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event)
    {
        if (!this.plugin.getGame().isBuildActivated())
            event.setCancelled(true);
    }

    /**
     * Cancel build before game start
     *
     * @param event Bukkit event instance
     */
    @EventHandler(ignoreCancelled = true)
    public void onBlockBreak(BlockPlaceEvent event)
    {
        if (!this.plugin.getGame().isBuildActivated())
            event.setCancelled(true);
    }

    /**
     * Cancel rain and other weather changes
     *
     * @param event Bukkit event instance
     */
    @EventHandler(ignoreCancelled = true)
    public void onWeatherChange(WeatherChangeEvent event)
    {
        if (event.toWeatherState())
            event.setCancelled(true);
    }
}
