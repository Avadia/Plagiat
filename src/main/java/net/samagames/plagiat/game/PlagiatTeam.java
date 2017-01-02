package net.samagames.plagiat.game;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

/**
 * Plagiat Team class
 */
class PlagiatTeam
{
    private ChatColor chatColor;
    private String name;
    private List<PlagiatPlayer> players;

    /**
     * Plagiat Team constructor
     *
     * @param chatColor Team color
     * @param name Team name
     */
    public PlagiatTeam(ChatColor chatColor, String name)
    {
        this.chatColor = chatColor;
        this.name = name;
        this.players = new ArrayList<>();
    }

    /**
     * Get this team color
     *
     * @return Color as ChatColor
     */
    ChatColor getChatColor()
    {
        return this.chatColor;
    }

    /**
     * Get this team name
     *
     * @return Name as String
     */
    String getName()
    {
        return this.name;
    }

    /**
     * Get this team members
     *
     * @return Members as List
     */
    List<PlagiatPlayer> getPlayers()
    {
        return this.players;
    }
}
