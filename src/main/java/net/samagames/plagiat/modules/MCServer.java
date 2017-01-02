package net.samagames.plagiat.modules;

/**
 * Enum of other servers
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
