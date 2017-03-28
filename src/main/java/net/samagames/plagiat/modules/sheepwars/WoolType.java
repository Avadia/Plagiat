package net.samagames.plagiat.modules.sheepwars;

import net.samagames.plagiat.Plagiat;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.libs.jline.internal.Nullable;
import org.bukkit.craftbukkit.v1_10_R1.block.CraftBlock;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Abstract class for Sheepwars's sheeps
 */
public abstract class WoolType
{
    protected Plagiat plugin;
    private DyeColor dyeColor;
    private ChatColor chatColor;
    private String name;
    private List<Sheep> sheeps;

    /**
     * Abstract constructor
     *
     * @param plugin Plagiat's plugin
     * @param dyeColor The wool color
     * @param chatColor The chat color
     * @param name This sheep's name
     */
    protected WoolType(Plagiat plugin, DyeColor dyeColor, ChatColor chatColor, String name)
    {
        this.sheeps = new ArrayList<>();
        this.plugin = plugin;
        this.dyeColor = dyeColor;
        this.chatColor = chatColor;
        this.name = name;
    }

    /**
     * Spawn and launch a sheep of this WoolType
     *
     * @param player The launching player
     * @param vector New speed of the sheep
     */
    final void launchSheep(Player player, Vector vector)
    {
        Sheep sheep = player.getWorld().spawn(player.getLocation(), Sheep.class);
        sheep.setSheared(false);
        sheep.setColor(this.dyeColor);

        if (this.shouldLaunch())
            sheep.setVelocity(vector);

        sheep.setMetadata("sg-owner", new FixedMetadataValue(this.plugin, player.getUniqueId()));
        sheep.setMetadata("sg-type", new FixedMetadataValue(this.plugin, this));

        this.onLaunch(sheep, player);

        this.sheeps.add(sheep);

        BukkitTask task = this.plugin.getServer().getScheduler().runTaskTimer(this.plugin, () ->
        {
            if (!sheep.isDead() && sheep.isOnGround())
            {
                ((BukkitTask)sheep.getMetadata("sg-land").get(0).value()).cancel();
                this.onLand(sheep);
            }
            else if (sheep.isDead())
                ((BukkitTask)sheep.getMetadata("sg-land").get(0).value()).cancel();
        }, 20L, 2);

        this.plugin.getServer().getScheduler().runTaskLater(this.plugin, () -> sheep.damage(100D), this.getSpawnTime());

        sheep.setMetadata("sg-land", new FixedMetadataValue(this.plugin, task));
    }

    /**
     * Kill a sheep
     *
     * @param sheep The sheep instance
     * @param player The killer of the sheep, or null
     */
    final void killSheep(Sheep sheep, @Nullable Player player)
    {
        this.onDeath(sheep, player);
        this.sheeps.remove(sheep);
    }

    /**
     * Event method, should be overridden to custom behaviour
     *
     * @param sheep The sheep entity
     * @param player Bukkit player instance
     */
    protected void onLaunch(Sheep sheep, Player player)
    {
    }

    /**
     * Event method, should be overridden to custom behaviour
     *
     * @param sheep The sheep entity
     */
    protected void onLand(Sheep sheep)
    {
    }

    /**
     * Event method, should be overridden to custom behaviour
     *
     * @param sheep The sheep entity
     * @param killer The sheep killer, or null
     */
    protected void onDeath(Sheep sheep, @Nullable Player killer)
    {
    }

    /**
     * If the sheep must be launched, or spawned near the player
     *
     * @return If should be launched
     */
    protected boolean shouldLaunch()
    {
        return true;
    }

    /**
     * Kill all sheeps spawned
     */
    final void killSheeps()
    {
        this.sheeps.forEach(Sheep::remove);
    }

    /**
     * Get this sheep's name, for chat purpose
     *
     * @return Name as String
     */
    String getName()
    {
        return this.name;
    }

    /**
     * Get this sheep's chat color, for chat purpose
     *
     * @return Color as ChatColor
     */
    ChatColor getChatColor()
    {
        return this.chatColor;
    }

    /**
     * Get this sheep's wool color
     *
     * @return Color as DyeColor
     */
    DyeColor getDyeColor()
    {
        return this.dyeColor;
    }


    /**
     * Get all blocks in a given radius, upon X, Y and Z axis
     *
     * @param origin Center location of the sphere to scan
     * @param radius Radius
     * @return List as a set, to avoid duplicates {@link CraftBlock#hashCode()}
     */
    protected Set<Block> getAllBlocksInSphere(Location origin, double radius)
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

    /**
     * Number of ticks this sheep should stay alive
     * 12 seconds by default
     *
     * @return -1 if infinite, tick count otherwise
     */
    public int getSpawnTime()
    {
        return 240;
    }
}