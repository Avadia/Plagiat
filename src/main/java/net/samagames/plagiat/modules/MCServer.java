package net.samagames.plagiat.modules;

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
public enum MCServer
{
    SAMAGAMES("SamaGames"),
    EPICUBE("Epicube"),
    HYPIXEL("Hypixel"),
    HIVEMC("HiveMC"),
    ASCENTIA("Ascentia"),
    UHCGAMES("UHCGames"),
    //TODO MINEPLEX("MinePlex"),
    //TODO FANTABOBWORLD("FantaBobWorld"),
    //TODO RELAXCUBE("Relaxcube"),
    //TODO PLAYMINITY("PlayMinity"),
    //TODO HEXION("Hexion")
    ;

    private String name;

    /**
     * Private enum constructor
     *
     * @param name The server name, formatted with case
     */
    MCServer(String name)
    {
        this.name = name;
    }

    /**
     * Override of toString method, with good case handling
     *
     * @return The real server name
     */
    @Override
    public String toString()
    {
        return this.name;
    }
}
