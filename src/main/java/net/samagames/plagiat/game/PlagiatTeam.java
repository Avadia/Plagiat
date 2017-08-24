package net.samagames.plagiat.game;

import org.bukkit.ChatColor;

import java.util.ArrayList;
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
