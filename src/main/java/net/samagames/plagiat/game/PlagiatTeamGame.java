package net.samagames.plagiat.game;

import net.samagames.plagiat.Plagiat;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Plagiat Team Game class
 */
public class PlagiatTeamGame extends PlagiatGame
{
    private List<PlagiatTeam> teams;
    private int playersPerTeam; // TODO: Teams

    /**
     * Plagiat Team Game constructor
     *
     * @param plugin Plagiat plugin instance
     */
    public PlagiatTeamGame(Plagiat plugin, boolean insane, int playersPerTeam)
    {
        super(plugin, insane);

        this.teams = new ArrayList<>();
        this.playersPerTeam = playersPerTeam;
    }

    /**
     * Teleport teams to their spawn
     */
    @Override
    protected void teleport()
    {
        List<PlagiatTeam> players = new ArrayList<>(this.teams);
        Collections.shuffle(players);
        Iterator<Location> iterator = this.spawns.iterator();
        players.forEach(plagiatTeam ->
        {
            if (!iterator.hasNext())
                plagiatTeam.getPlayers().forEach(plagiatPlayer -> this.gameManager.kickPlayer(plagiatPlayer.getPlayerIfOnline(), "Il n'y a plus de place dans la partie."));
            else if (!plagiatTeam.getPlayers().isEmpty())
            {
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
    public void checkEnd(boolean forceEnd)
    {
        // TODO: Teams
    }
}
