package net.samagames.plagiat.game;

import net.samagames.api.SamaGamesAPI;
import net.samagames.api.games.GamePlayer;
import net.samagames.api.shops.IPlayerShop;
import net.samagames.api.shops.ITransaction;
import net.samagames.tools.scoreboards.ObjectiveSign;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.logging.Level;

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
        this.objectiveSign.setLine(1, ChatColor.GRAY + "Temps :");
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
        }
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
