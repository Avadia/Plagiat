package net.samagames.plagiat.game;

import net.samagames.api.games.GamePlayer;
import net.samagames.tools.scoreboards.ObjectiveSign;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * Plagiat's GamePlayer class, to store player statistics
 */
public class PlagiatPlayer extends GamePlayer
{
    private EnumCage cage;
    private final ObjectiveSign objectiveSign;

    /**
     * PlagiatPlayer constructor
     *
     * @param player The player instance
     */
    public PlagiatPlayer(Player player)
    {
        super(player);

        this.cage = EnumCage.GLASS;

        this.objectiveSign = new ObjectiveSign("plagiat", ChatColor.GOLD + "Plagiat");
        this.objectiveSign.setLine(0, "");
        this.objectiveSign.setLine(1, ChatColor.GRAY + "Temps de jeu :");
        this.objectiveSign.setLine(3, " ");
        this.objectiveSign.setLine(4, ChatColor.GRAY + "Kills :");
        this.objectiveSign.setLine(6, "  ");
        this.objectiveSign.setLine(7, ChatColor.GRAY + "Morts :");
        this.objectiveSign.setLine(9, "   ");
    }

    /**
     * Load the cage this player owns
     * Should be called async for better performance
     */
    void loadCage()
    {
        /* TODO: Item description are not done in production, so give glass cage to everyone except Plagiat's developers & builders
        IPlayerShop playerShop = SamaGamesAPI.get().getShopsManager().getPlayer(this.uuid);
        for (EnumCage enumCage : EnumCage.values())
        {
            try
            {
                ITransaction transaction = playerShop.getTransactionsByID(enumCage.getDatabaseId());
                if (transaction != null && transaction.isSelected())
                {
                    this.cage = enumCage;
                    break ;
                }
            }
            catch (Exception exception)
            {
                SamaGamesAPI.get().getPlugin().getLogger().log(Level.SEVERE, "Error loading cages", exception);
            }
        }*/

        Map<String, EnumCage> specialCages = new HashMap<>();
        specialCages.put("c1f45796-d2f9-4622-9475-2afe58324dee", EnumCage.SLIME); // Rigner
        specialCages.put("8502878d-22ba-4a93-bcbd-9319caa9b555", EnumCage.VOID); // Aweser
        specialCages.put("83748c1b-fd1d-4823-865c-f68bdf106c43", EnumCage.RAINBOW); // LordFinn
        specialCages.put("29b2b527-1b59-45df-b7b0-d5ab20d8731a", EnumCage.NICOLAS); // IamBlueSlime
        specialCages.put("a4ab5d2c-1046-4317-9977-fe410fd2fd36", EnumCage.ARITCHE); // Aritche
        specialCages.put("937a49ec-1a04-484c-b801-924f362ced8c", EnumCage.MOSCOU); // BirchLog
        specialCages.put("70481513-fe72-4184-8da2-4c16b92ea98a", EnumCage.OBSIDIAN); // Reelwens
        specialCages.put("ee02e58e-b1d8-4535-8dac-3dd23e6814ed", EnumCage.SHEEP); // Nyroldiin

        EnumCage cage = specialCages.get(this.uuid.toString());
        if (cage != null)
            this.cage = cage;
    }

    /**
     * Get the cage this player owns
     *
     * @return Case as EnumCage
     */
    EnumCage getCage()
    {
        return this.cage;
    }

    /**
     * Get the scoreboard for this player
     *
     * @return SamaGamesAPI's scoreboard instance
     */
    ObjectiveSign getObjectiveSign()
    {
        return this.objectiveSign;
    }
}
