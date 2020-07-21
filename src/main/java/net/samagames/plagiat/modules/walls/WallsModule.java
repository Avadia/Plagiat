package net.samagames.plagiat.modules.walls;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.samagames.plagiat.Plagiat;
import net.samagames.plagiat.game.PlagiatPlayer;
import net.samagames.plagiat.modules.AbstractModule;
import net.samagames.plagiat.modules.MCServer;
import net.samagames.tools.Area;
import net.samagames.tools.LocationUtils;
import net.samagames.tools.Titles;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
public class WallsModule extends AbstractModule {
    private boolean run;
    private Location[] locations;
    private Area[] walls;

    /**
     * Walls Module constructor
     *
     * @param plugin Plagiat's plugin instance
     */
    public WallsModule(Plagiat plugin) {
        super(plugin, "walls", MCServer.HYPIXEL);
        this.run = false;
    }

    /**
     * Load configuration and run tasks
     * {@link AbstractModule#handleGameStart()}
     */
    @Override
    public void handleGameStart() {
        JsonObject jsonObject = this.getConfigRoot();
        JsonArray spawnsArray = jsonObject.get("spawns").getAsJsonArray();
        JsonArray wallsArray = jsonObject.get("walls").getAsJsonArray();

        this.locations = new Location[spawnsArray.size()];
        for (int i = 0; i < spawnsArray.size(); ++i)
            this.locations[i] = LocationUtils.str2loc(spawnsArray.get(i).getAsString());

        this.walls = new Area[wallsArray.size()];
        for (int i = 0; i < wallsArray.size(); ++i)
            this.walls[i] = Area.str2area(wallsArray.get(i).getAsString());
    }

    /**
     * Run death match if needed
     * {@link AbstractModule#handleGameEnd()}
     *
     * @return false if deathmatch should start
     */
    @Override
    public boolean handleGameEnd() {
        Map<UUID, PlagiatPlayer> players = this.plugin.getGame().getInGamePlayers();
        if (players.size() > 1 && players.size() <= 4 && !this.run) {
            this.run = true;
            int[] i = {0};
            players.forEach((uuid, plagiatPlayer) ->
            {
                Player player = plagiatPlayer.getPlayerIfOnline();
                if (player != null)
                    player.teleport(this.locations[i[0]]);
                ++i[0];
            });

            this.plugin.getServer().getOnlinePlayers().forEach(player -> Titles.sendTitle(player, 3, 50, 3, ChatColor.GOLD + "Deathmatch", ChatColor.YELLOW + "Les murs tomberont dans 10 secondes"));

            this.plugin.getServer().getScheduler().runTaskLater(this.plugin, this::breakWalls, 600L);
            this.plugin.getServer().getScheduler().runTaskLater(this.plugin, () -> this.plugin.getGame().checkEnd(true), 6600L);

            return true;
        }

        if (players.size() > 4 && !this.run)
            this.plugin.getGame().getCoherenceMachine().getMessageManager().writeCustomMessage(ChatColor.RED + "Il y a trop de joueurs en vie pour commencer un deathmatch. Match nul.", true);

        return false;
    }

    /**
     * Break the walls 10 seconds after start
     */
    private void breakWalls() {
        List<Material> blacklist = Arrays.asList(Material.GRASS, Material.DIRT);
        for (Area wall : this.walls)
            for (int i = 0; i <= wall.getSizeX(); i++)
                for (int j = 0; j <= wall.getSizeY(); j++)
                    for (int k = 0; k <= wall.getSizeZ(); k++) {
                        Block block = wall.getMin().getWorld().getBlockAt(wall.getMin().getBlockX() + i, wall.getMin().getBlockY() + j, wall.getMin().getBlockZ() + k);
                        if (!blacklist.contains(block.getType()))
                            block.setType(Material.AIR);
                    }
    }

    /**
     * Cancel block breaking during deathmatch
     *
     * @param event Bukkit event instance
     */
    @EventHandler(ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        event.setCancelled(this.run);

        if (this.run)
            event.getPlayer().sendMessage(this.plugin.getGame().getCoherenceMachine().getGameTag() + ChatColor.RED + " Vous ne pouvez pas casser de blocs pendant le deathmatch.");
    }

    /**
     * Cancel block placing during deathmatch
     *
     * @param event Bukkit event instance
     */
    @EventHandler(ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        event.setCancelled(this.run);

        if (this.run)
            event.getPlayer().sendMessage(this.plugin.getGame().getCoherenceMachine().getGameTag() + ChatColor.RED + " Vous ne pouvez pas placer de blocs pendant le deathmatch.");
    }
}
