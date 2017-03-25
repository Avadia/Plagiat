package net.samagames.plagiat.modules.sheepwars.sheeps;

import net.samagames.plagiat.Plagiat;
import net.samagames.plagiat.modules.sheepwars.WoolType;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.libs.jline.internal.Nullable;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.material.MaterialData;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class EarthQuakeSheep extends WoolType
{
    private Map<Integer, BukkitTask> tasks;

    /**
     * Abstract constructor
     *
     * @param plugin    Plagiat's plugin
     */
    public EarthQuakeSheep(Plagiat plugin)
    {
        super(plugin, DyeColor.BROWN, ChatColor.GOLD, "tremblement de terre");

        this.tasks = new HashMap<>();
    }

    /**
     * Start earthquake task
     *
     * @param sheep The sheep entity
     */
    @Override
    protected void onLand(Sheep sheep)
    {
        double radius = 5D;
        this.tasks.put(sheep.getEntityId(), this.plugin.getServer().getScheduler().runTaskTimer(this.plugin, () ->
        {
            sheep.getWorld().getNearbyEntities(sheep.getLocation(), radius, radius, radius).forEach(entity ->
            {
                if (entity instanceof Player && entity.isOnGround())
                {
                    Vector vector = entity.getVelocity();
                    vector.setY(0.8D);
                    entity.setVelocity(vector);
                }
            });
            Set<Block> blocks = this.getAllBlocksInSphere(sheep.getLocation(), radius);
            blocks.forEach(block ->
            {
                if (!block.getType().isSolid() && block.getRelative(BlockFace.DOWN).getType().isSolid())
                    block.getWorld().spawnParticle(Particle.BLOCK_DUST, block.getLocation().add(0.5D, 0.2D, 0.5D), 6, 0D, 1D, 1D, 1D, new MaterialData(Material.DIRT));
            });
        }, 1L, 40L));
    }

    /**
     * Stop earthquake task
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
