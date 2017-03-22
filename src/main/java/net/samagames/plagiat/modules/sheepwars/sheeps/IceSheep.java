package net.samagames.plagiat.modules.sheepwars.sheeps;

import net.samagames.plagiat.Plagiat;
import net.samagames.plagiat.modules.sheepwars.WoolType;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.libs.jline.internal.Nullable;
import org.bukkit.craftbukkit.v1_10_R1.block.CraftBlock;
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
                    ((Player)entity).addPotionEffect(PotionEffectType.SLOW.createEffect(20, 1));
            });

            Set<Block> blocks = this.getAllBlocksInSphere(sheep.getLocation(), radius);
            blocks.forEach(block ->
            {
               Material material = block.getType();
               if (material == Material.SNOW)
               {
                   if (block.getData() < 8 && this.random.nextInt(10) < 6)
                       block.setData((byte)(block.getData() + 1));
               }
               else if (!material.isSolid() && block.getRelative(BlockFace.DOWN).getType().isSolid())
               {
                   block.setType(Material.SNOW);
                   block.setData((byte)0);
               }
            });

            if (this.blocks.containsKey(sheep.getEntityId()))
                this.blocks.get(sheep.getEntityId()).addAll(blocks);
            else
                this.blocks.put(sheep.getEntityId(), blocks);

        }, 1L, 20L));
    }

    /**
     * Get all blocks in a given radius, upon X, Y and Z axis
     *
     * @param origin Center location of the sphere to scan
     * @param radius Radius
     * @return List as a set, to avoid duplicates {@link CraftBlock#hashCode()}
     */
    private Set<Block> getAllBlocksInSphere(Location origin, double radius)
    {
        double radiusSquared = radius * radius;
        Set<Block> blocks = new HashSet<>();

        for (double x = origin.getX() - radius; x <= origin.getX() + radius; x += 0.5D)
            for (double y = origin.getY() - radius; y <= origin.getY() + radius; y += 0.5D)
                for (double z = origin.getZ() - radius; z <= origin.getZ() + radius; z += 0.5D)
                    if ((x - origin.getX()) * (x - origin.getX()) + (z - origin.getZ()) * (z - origin.getZ()) + (y - origin.getY()) * (y - origin.getY()) <= radiusSquared)
                        blocks.add(new Location(origin.getWorld(), x, y, z).getBlock());

        return blocks;
    }
}
