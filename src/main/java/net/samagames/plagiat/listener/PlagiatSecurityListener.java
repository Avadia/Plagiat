package net.samagames.plagiat.listener;

import net.samagames.plagiat.Plagiat;
import net.samagames.plagiat.game.PlagiatKitSelectorGui;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.VillagerAcquireTradeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

/**
 * Plagiat main Listener
 */
public class PlagiatSecurityListener implements Listener
{
    private Plagiat plugin;

    /**
     * Plagiat listener constructor
     *
     * @param plugin Plagiat plugin instance
     */
    public PlagiatSecurityListener(Plagiat plugin)
    {
        this.plugin = plugin;
    }

    /**
     * Cancel damages before game start
     *
     * @param event Bukkit event instance
     */
    @EventHandler(ignoreCancelled = true)
    public void onDamage(EntityDamageEvent event)
    {
        if (!this.plugin.getGame().areDamagesActivated())
            event.setCancelled(true);
        if (event.getCause() == EntityDamageEvent.DamageCause.VOID && !this.plugin.getGame().isGameStarted())
            event.getEntity().teleport(this.plugin.getGame().getLobby());
    }

    /**
     * Cancel build before game start
     *
     * @param event Bukkit event instance
     */
    @EventHandler(ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event)
    {
        if (!this.plugin.getGame().isBuildActivated())
            event.setCancelled(true);
    }

    /**
     * Cancel build before game start
     *
     * @param event Bukkit event instance
     */
    @EventHandler(ignoreCancelled = true)
    public void onBlockBreak(BlockPlaceEvent event)
    {
        if (!this.plugin.getGame().isBuildActivated())
            event.setCancelled(true);
    }

    /**
     * Cancel rain and other weather changes
     *
     * @param event Bukkit event instance
     */
    @EventHandler(ignoreCancelled = true)
    public void onWeatherChange(WeatherChangeEvent event)
    {
        if (event.toWeatherState())
            event.setCancelled(true);
    }

    /**
     * Cancel food loss if game not started
     *
     * @param event Bukkit event instance
     */
    @EventHandler(ignoreCancelled = true)
    public void onFoodLevelChange(FoodLevelChangeEvent event)
    {
        if (!this.plugin.getGame().areDamagesActivated())
        {
            event.setCancelled(true);
            if (event.getEntity() instanceof Player)
                ((Player)event.getEntity()).setFoodLevel(20);
        }
    }

    /**
     * Cancel interact before start
     * Handles leave item and kit item
     *
     * @param event Bukkit event instance
     */
    @EventHandler
    public void onInteract(PlayerInteractEvent event)
    {
        if (event.getItem() != null && event.getItem().equals(this.plugin.getGame().getCoherenceMachine().getLeaveItem()))
            event.getPlayer().kickPlayer("");

        else if (!this.plugin.getGame().isBuildActivated() && (event.getItem() == null || event.getItem().getType() != Material.WRITTEN_BOOK))
            event.setCancelled(true);

        if (!this.plugin.getGame().isBuildActivated() && event.getItem() != null && event.getItem().getType() == Material.BOW)
            this.plugin.getSamaGamesAPI().getGuiManager().openGui(event.getPlayer(), new PlagiatKitSelectorGui(this.plugin));
    }

    /**
     * Cancel interact before start
     * Handles leave item and kit item
     *
     * @param event Bukkit event instance
     */
    @EventHandler(ignoreCancelled = true)
    public void onEntityInteract(PlayerInteractEntityEvent event)
    {
        ItemStack itemStack;
        itemStack = event.getHand() == EquipmentSlot.OFF_HAND ? event.getPlayer().getInventory().getItemInOffHand() : event.getPlayer().getInventory().getItemInMainHand();
        if (itemStack != null && itemStack.equals(this.plugin.getGame().getCoherenceMachine().getLeaveItem()))
            event.getPlayer().kickPlayer("");

        else if (!this.plugin.getGame().isBuildActivated() && (itemStack == null || itemStack.getType() != Material.WRITTEN_BOOK))
            event.setCancelled(true);

        if (!this.plugin.getGame().isBuildActivated() && itemStack != null && itemStack.getType() == Material.BOW)
            this.plugin.getSamaGamesAPI().getGuiManager().openGui(event.getPlayer(), new PlagiatKitSelectorGui(this.plugin));
    }

    /**
     * Cancel villager to have new MC recipes, only mine
     *
     * @param event Bukkit event instance
     */
    @EventHandler(ignoreCancelled = true)
    public void onMerchantModify(VillagerAcquireTradeEvent event)
    {
        event.setCancelled(true);
    }

    /**
     * Cancel inventory click before game
     *
     * @param event Bukkit event instance
     */
    @EventHandler(ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent event)
    {
        if (!this.plugin.getGame().isGameStarted())
            event.setCancelled(true);
    }

    /**
     * Cancel drop before game
     *
     * @param event Bukkit event instance
     */
    @EventHandler(ignoreCancelled = true)
    public void onDrop(PlayerDropItemEvent event)
    {
        if (!this.plugin.getGame().isGameStarted())
            event.setCancelled(true);
    }

    /**
     * Cancel hand switching before game
     *
     * @param event Bukkit event instance
     */
    @EventHandler(ignoreCancelled = true)
    public void onSwitch(PlayerSwapHandItemsEvent event)
    {
        if (!this.plugin.getGame().isGameStarted())
            event.setCancelled(true);
    }
}
