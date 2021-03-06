package net.samagames.plagiat.modules.sheepwars.sheeps;

import net.samagames.plagiat.Plagiat;
import net.samagames.plagiat.modules.sheepwars.WoolType;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.craftbukkit.libs.jline.internal.Nullable;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;

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
public class ExplosiveSheep extends WoolType {
    private final Map<Integer, BukkitTask> explodeTask;

    /**
     * Explosive Sheep constructor
     *
     * @param plugin Plagiat plugin instance
     */
    public ExplosiveSheep(Plagiat plugin) {
        super(plugin, DyeColor.RED, ChatColor.RED, "explosif");

        this.explodeTask = new HashMap<>();
    }

    /**
     * Schedule explosion task on land
     *
     * @param sheep The sheep entity
     */
    @Override
    protected void onLand(Sheep sheep) {
        this.explodeTask.put(sheep.getEntityId(), this.plugin.getServer().getScheduler().runTaskTimer(this.plugin, new Runnable() {
            private int time = 0;

            @Override
            public void run() {
                this.time++;
                if (this.time == 20) {
                    sheep.remove();
                    BukkitTask bukkitTask = ExplosiveSheep.this.explodeTask.get(sheep.getEntityId());
                    if (bukkitTask != null)
                        bukkitTask.cancel();
                    sheep.getWorld().createExplosion(sheep.getLocation(), 2F);
                    return;
                }
                sheep.setColor(this.time % 2 == 0 ? DyeColor.RED : DyeColor.WHITE);
            }
        }, 30L, 5));
    }

    /**
     * Cancel explosion task
     *
     * @param sheep  The sheep entity
     * @param killer The sheep killer, or null
     */
    @Override
    protected void onDeath(Sheep sheep, @Nullable Player killer) {
        BukkitTask bukkitTask = this.explodeTask.remove(sheep.getEntityId());
        if (bukkitTask != null)
            bukkitTask.cancel();
    }
}
