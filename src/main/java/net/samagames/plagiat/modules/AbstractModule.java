package net.samagames.plagiat.modules;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.samagames.api.games.Game;
import net.samagames.api.games.IGameManager;
import net.samagames.api.games.IGameProperties;
import net.samagames.plagiat.Plagiat;
import org.bukkit.ChatColor;
import org.bukkit.event.Listener;

import java.util.logging.Logger;

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
public abstract class AbstractModule implements Listener
{
    protected final Plagiat plugin;
    private final String name;
    protected final Logger logger;
    private final MCServer server;

    /**
     * Abstract constructor to initialise module
     *
     * @param plugin Plagiat's plugin instance
     * @param name This module's name
     * @param server The server this game belongs to
     */
    protected AbstractModule(Plagiat plugin, String name, MCServer server)
    {
        this.plugin = plugin;
        this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
        this.name = name;
        this.logger = Logger.getLogger(name.toUpperCase());
        this.server = server;
        this.logger.info("Enabled " + this.name + " module.");
    }

    /**
     * Event method, called on game start, should be overridden
     * Use it to start tasks, etc
     * {@link Game#startGame()}
     */
    public void handleGameStart()
    {
    }

    /**
     * Event method, called on game end, should be overridden
     * Use it to clean all tasks, etc
     * {@link Game#handleGameEnd()}
     *
     * @return If end should be cancelled
     */
    public boolean handleGameEnd()
    {
        return false;
    }

    /**
     * Get this module root configuration, using SamaGames GameProperties
     * {@link IGameManager#getGameProperties()}
     *
     * @return Configuration as JsonObject
     */
    protected JsonObject getConfigRoot()
    {
        IGameProperties iGameProperties = this.plugin.getSamaGamesAPI().getGameManager().getGameProperties();
        JsonObject modulesRoot = iGameProperties.getConfig("modules", new JsonObject()).getAsJsonObject();
        JsonElement rootElement = modulesRoot.get(this.name);
        JsonObject root = rootElement == null ? null : rootElement.getAsJsonObject();
        return root == null ? new JsonObject() : root;
    }

    /**
     * Get this server suffix, formatted as " [?]"
     *
     * @return Suffix as String
     */
    public String getServerSuffix()
    {
        return ChatColor.GOLD + "[" + this.server.toString().charAt(0) + "]";
    }

    /**
     * Get this module name
     *
     * @return Name as String
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * Get this module rules
     *
     * @return Rules as String array
     */
    public String[] getRules()
    {
        return new String[0];
    }

    /**
     * Get this game developers
     *
     * @return Developers user names as String array
     */
    public String[] getDevelopers()
    {
        return new String[0];
    }
}
