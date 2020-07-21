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
public class ThunderSheep extends WoolType {
    private static final int RADIUS = 5;
    private final Map<Integer, BukkitTask> tasks;
    private final Random random;

    /**
     * Abstract constructor
     *
     * @param plugin Plagiat's plugin
     */
    public ThunderSheep(Plagiat plugin) {
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
    protected void onLand(Sheep sheep) {
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
     * @param sheep  The sheep entity
     * @param killer The sheep killer, or null
     */
    @Override
    protected void onDeath(Sheep sheep, @Nullable Player killer) {
        BukkitTask bukkitTask = this.tasks.remove(sheep.getEntityId());
        if (bukkitTask != null)
            bukkitTask.cancel();
    }
}
