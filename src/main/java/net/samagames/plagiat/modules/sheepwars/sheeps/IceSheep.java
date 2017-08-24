package net.samagames.plagiat.modules.sheepwars.sheeps;

import net.samagames.plagiat.Plagiat;
import net.samagames.plagiat.modules.sheepwars.WoolType;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.libs.jline.internal.Nullable;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

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
public class IceSheep extends WoolType
{
    private final Map<Integer, BukkitTask> tasks;
    private final Map<Integer, Set<Block>> blocks;
    private final Random random;

    /**
     * Ice Sheep constructor
     *
     * @param plugin    Plagiat plugin instance
     */
    public IceSheep(Plagiat plugin)
    {
        super(plugin, DyeColor.LIGHT_BLUE, ChatColor.AQUA, "de glace");

        this.tasks = new HashMap<>();
        this.blocks = new HashMap<>();
        this.random = new SecureRandom();
    }

    /**
     * Cancel task on sheep death
     * Remove all snow blocks
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

        Set<Block> blocks = this.blocks.remove(sheep.getEntityId());
        if (blocks != null)
            blocks.forEach(block -> block.setType(Material.AIR));
    }

    /**
     * Start task to add snow layers around the sheep and slowness to players
     *
     * @param sheep The sheep entity
     */
    @SuppressWarnings("deprecation")
    @Override
    protected void onLand(Sheep sheep)
    {
        double radius = 5D;
        this.tasks.put(sheep.getEntityId(), this.plugin.getServer().getScheduler().runTaskTimer(this.plugin, () ->
        {
            sheep.getWorld().getNearbyEntities(sheep.getLocation(), radius, radius, radius).forEach(entity ->
            {
                if (entity instanceof Player)
                    ((Player)entity).addPotionEffect(PotionEffectType.SLOW.createEffect(40, 1));
            });

            Set<Block> blocks = this.getAllBlocksInSphere(sheep.getLocation(), radius);
            Set<Block> modifiedBlocks = new HashSet<>();
            blocks.forEach(block ->
            {
               Material material = block.getType();
               if (material == Material.SNOW)
               {
                   if (block.getData() < 3 && this.random.nextInt(20) < 6)
                   {
                       block.setData((byte) (block.getData() + 1));
                       modifiedBlocks.add(block);
                   }
               }
               else if (!material.isSolid() && block.getRelative(BlockFace.DOWN).getType().isSolid())
               {
                   block.setType(Material.SNOW);
                   block.setData((byte)0);
                   modifiedBlocks.add(block);
               }
            });

            if (this.blocks.containsKey(sheep.getEntityId()))
                this.blocks.get(sheep.getEntityId()).addAll(modifiedBlocks);
            else
                this.blocks.put(sheep.getEntityId(), modifiedBlocks);

        }, 1L, 20L));
    }
}
