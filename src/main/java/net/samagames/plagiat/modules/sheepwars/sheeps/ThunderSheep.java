package net.samagames.plagiat.modules.sheepwars.sheeps;

import net.samagames.plagiat.Plagiat;
import net.samagames.plagiat.modules.sheepwars.WoolType;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.libs.jline.internal.Nullable;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.scheduler.BukkitTask;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Thunder Sheep Class
 */
public class ThunderSheep extends WoolType
{
    private static final int RADIUS = 5;
    private Map<Integer, BukkitTask> tasks;
    private Random random;

    /**
     * Abstract constructor
     *
     * @param plugin    Plagiat's plugin
     */
    public ThunderSheep(Plagiat plugin)
    {
        super(plugin, DyeColor.YELLOW, ChatColor.YELLOW, "invocateur de foudre");

        this.tasks = new HashMap<>();
        this.random = new SecureRandom();
    }

    /**
     * Start lightning task
     *
     * @param sheep The sheep entity
     */
    @Override
    protected void onLand(Sheep sheep)
    {
        this.tasks.put(sheep.getEntityId(), this.plugin.getServer().getScheduler().runTaskTimer(this.plugin, () ->
        {
            Location location = sheep.getLocation();
            double dx = this.random.nextDouble() % ThunderSheep.RADIUS;
            double dz = this.random.nextDouble() % ThunderSheep.RADIUS;
            location.add(dx, 0, dz);
            int y = location.getWorld().getHighestBlockYAt(location);
            location.setY(y);
            location.getWorld().strikeLightning(location);
            location.getWorld().strikeLightning(location);
        }, 1L, 30L));
    }

    /**
     * Stop lightning task
     *
     * @param sheep The sheep entity
     * @param killer The sheep killer, or null
     */
    @Override
    protected void onDeath(Sheep sheep, @Nullable Player killer)
    {
        BukkitTask bukkitTask = this.tasks.remove(sheep.getEntityId());
        if (bukkitTask != null)
            bukkitTask.cancel();
    }
}
