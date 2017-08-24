package net.samagames.plagiat.modules.sheepwars.sheeps;

import net.samagames.plagiat.Plagiat;
import net.samagames.plagiat.modules.sheepwars.WoolType;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Particle;
import org.bukkit.craftbukkit.libs.jline.internal.Nullable;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

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
public class HealingSheep extends WoolType
{
    private Map<Integer, BukkitTask> healingTask;
    private Random random;

    /**
     * Healing Sheep constructor
     *
     * @param plagiat Plagiat plugin's instance
     */
    public HealingSheep(Plagiat plagiat)
    {
        super(plagiat, DyeColor.PINK, ChatColor.LIGHT_PURPLE, "soigneur");
        this.healingTask = new HashMap<>();
        this.random = new Random();
    }

    /**
     * Event method on sheep land
     * Overridden to start healing task
     *
     * @param sheep The sheep entity
     */
    @Override
    protected void onLand(Sheep sheep)
    {
        this.healingTask.put(sheep.getEntityId(), this.plugin.getServer().getScheduler().runTaskTimer(this.plugin, () ->
        {
            sheep.getWorld().getNearbyEntities(sheep.getLocation(), 6D, 6D, 6D).forEach(entity ->
            {
                if (entity instanceof Player)
                    ((Player)entity).setHealth(((Player)entity).getHealth() >= ((Player)entity).getMaxHealth() ? ((Player)entity).getHealth() : ((Player)entity).getHealth() + 1);
            });
            sheep.getWorld().spawnParticle(Particle.HEART, sheep.getLocation(), 8, this.random.nextDouble() % 1D, this.random.nextDouble() % 1D, this.random.nextDouble() % 1D);
        }, 20L, 20L));
    }

    /**
     * Event method on sheep death
     *
     * @param sheep The sheep entity
     * @param killer The sheep killer, or null
     */
    @Override
    protected void onDeath(Sheep sheep, @Nullable Player killer)
    {
        BukkitTask bukkitTask = this.healingTask.get(sheep.getEntityId());
        if (bukkitTask != null)
            bukkitTask.cancel();
    }

    /**
     * Heal sheep should spawn near the player
     *
     * @return false to disable launch
     */
    @Override
    protected boolean shouldLaunch()
    {
        return false;
    }

    /**
     * Makes Healing sheep alive forever
     *
     * @return -1 to disable automatic despawn
     */
    @Override
    public int getSpawnTime()
    {
        return -1;
    }
}
