package net.samagames.plagiat.modules.sheepwars.sheeps;

import net.samagames.plagiat.Plagiat;
import net.samagames.plagiat.modules.sheepwars.WoolType;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;

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
public class BoardingSheep extends WoolType {
    /**
     * Boarding Sheep constructor
     *
     * @param plugin Plagiat plugin instance
     */
    public BoardingSheep(Plagiat plugin) {
        super(plugin, DyeColor.WHITE, ChatColor.WHITE, "d'abordage");
    }

    /**
     * Ride player on sheep
     *
     * @param sheep  The sheep entity
     * @param player Bukkit player instance
     */
    @Override
    protected void onLaunch(Sheep sheep, Player player) {
        sheep.setPassenger(player);
    }

    /**
     * Remove player from sheep
     *
     * @param sheep The sheep entity
     */
    @Override
    protected void onLand(Sheep sheep) {
        sheep.eject();
        sheep.remove();
    }
}
