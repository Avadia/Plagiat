package net.samagames.plagiat.game;

import net.samagames.plagiat.Plagiat;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

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
public class PlagiatTeamGame extends PlagiatGame {
    private final List<PlagiatTeam> teams;
    private final int playersPerTeam; // TODO: Teams

    /**
     * Plagiat Team Game constructor
     *
     * @param plugin Plagiat plugin instance
     */
    public PlagiatTeamGame(Plagiat plugin, boolean insane, int playersPerTeam) {
        super(plugin, insane);

        this.teams = new ArrayList<>();
        this.playersPerTeam = playersPerTeam;
    }

    /**
     * Teleport teams to their spawn
     */
    @Override
    protected void teleport() {
        List<PlagiatTeam> players = new ArrayList<>(this.teams);
        Collections.shuffle(players);
        Iterator<Location> iterator = this.spawns.iterator();
        players.forEach(plagiatTeam ->
        {
            if (!iterator.hasNext())
                plagiatTeam.getPlayers().forEach(plagiatPlayer -> this.gameManager.kickPlayer(plagiatPlayer.getPlayerIfOnline(), "Il n'y a plus de place dans la partie."));
            else if (!plagiatTeam.getPlayers().isEmpty()) {
                Location location = iterator.next();
                this.createCage(plagiatTeam.getPlayers().get(0).getCage(), location);
                plagiatTeam.getPlayers().forEach(plagiatPlayer -> plagiatPlayer.getPlayerIfOnline().teleport(location));
            }
        });
    }

    /**
     * Check if game should end
     *
     * @param forceEnd If game should end anyway
     */
    @Override
    public void checkEnd(boolean forceEnd) {
        // TODO: Teams
    }
}
