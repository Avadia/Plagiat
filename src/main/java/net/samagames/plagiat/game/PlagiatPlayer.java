package net.samagames.plagiat.game;

import net.samagames.api.SamaGamesAPI;
import net.samagames.api.games.GamePlayer;
import net.samagames.api.shops.IPlayerShop;
import net.samagames.api.shops.ITransaction;
import org.bukkit.entity.Player;

import java.util.logging.Level;

/**
 * Plagiat's GamePlayer class, to store player statistics
 */
public class PlagiatPlayer extends GamePlayer
{
    private EnumCage cage;

    /**
     * PlagiatPlayer constructor
     *
     * @param player The player instance
     */
    public PlagiatPlayer(Player player)
    {
        super(player);
        this.cage = EnumCage.GLASS;
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
}
