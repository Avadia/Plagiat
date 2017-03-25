package net.samagames.plagiat.modules.sheepwars.sheeps;

import net.samagames.plagiat.Plagiat;
import net.samagames.plagiat.modules.sheepwars.WoolType;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;

/**
 * Boarding sheep class
 */
public class BoardingSheep extends WoolType
{
    /**
     * Boarding Sheep constructor
     *
     * @param plugin Plagiat plugin instance
     */
    public BoardingSheep(Plagiat plugin)
    {
        super(plugin, DyeColor.WHITE, ChatColor.WHITE, "d'abordage");
    }

    /**
     * Ride player on sheep
     *
     * @param sheep The sheep entity
     * @param player Bukkit player instance
     */
    @Override
    protected void onLaunch(Sheep sheep, Player player)
    {
        sheep.setPassenger(player);
    }

    /**
     * Remove player from sheep
     *
     * @param sheep The sheep entity
     */
    @Override
    protected void onLand(Sheep sheep)
    {
        sheep.eject();
        sheep.remove();
    }
}
