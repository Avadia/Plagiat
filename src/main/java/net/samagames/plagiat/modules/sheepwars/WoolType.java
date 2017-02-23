package net.samagames.plagiat.modules.sheepwars;

import net.samagames.plagiat.Plagiat;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.craftbukkit.libs.jline.internal.Nullable;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

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
        sheep.setVelocity(vector);
        sheep.setMetadata("sg-owner", new FixedMetadataValue(this.plugin, player.getUniqueId()));
        sheep.setMetadata("sg-type", new FixedMetadataValue(this.plugin, this));
        this.onLaunch(sheep, player);
        this.sheeps.add(sheep);
        this.plugin.getServer().getScheduler().runTaskLater(this.plugin, () ->
        {
            if (!sheep.isDead())
                this.onLand(sheep);
        }, 70L);
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
}