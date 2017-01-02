package net.samagames.plagiat;

import com.google.gson.JsonPrimitive;
import net.samagames.api.SamaGamesAPI;
import net.samagames.plagiat.game.PlagiatGame;
import net.samagames.plagiat.game.PlagiatTeamGame;
import net.samagames.plagiat.listener.PlagiatGameListener;
import net.samagames.plagiat.listener.PlagiatSecurityListener;
import net.samagames.plagiat.listener.PlagiatSpectatorListener;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Plugin main class
 */
public class Plagiat extends JavaPlugin
{
    private SamaGamesAPI samaGamesAPI;
    private PlagiatGame game;

    /**
     * Initialise all components on plugin load
     */
    @Override
    public void onEnable()
    {
        this.samaGamesAPI = SamaGamesAPI.get();

        boolean insane = this.samaGamesAPI.getGameManager().getGameProperties().getOption("insane", new JsonPrimitive(false)).getAsBoolean();
        int playersPerTeam = this.samaGamesAPI.getGameManager().getGameProperties().getOption("playersPerTeam", new JsonPrimitive(1)).getAsInt();
        this.game = playersPerTeam == 1 ? new PlagiatGame(this, insane) : new PlagiatTeamGame(this, insane, playersPerTeam);

        this.game.registerModules();
        this.samaGamesAPI.getGameManager().registerGame(this.game);

        this.getServer().getPluginManager().registerEvents(new PlagiatSecurityListener(this), this);
        this.getServer().getPluginManager().registerEvents(new PlagiatGameListener(this), this);
        this.getServer().getPluginManager().registerEvents(new PlagiatSpectatorListener(this), this);
    }

    /**
     * Get the SamaGames's API instance, to avoid static getter
     * {@link SamaGamesAPI#get()}
     *
     * @return SamaGamesAPI Instance
     */
    public SamaGamesAPI getSamaGamesAPI()
    {
        return this.samaGamesAPI;
    }

    /**
     * Get the game's instance
     *
     * @return PlagiatGame instance
     */
    public PlagiatGame getGame()
    {
        return this.game;
    }
}
