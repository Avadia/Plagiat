package net.samagames.plagiat.modules.splegg;

import net.samagames.api.games.Status;
import net.samagames.plagiat.Plagiat;
import net.samagames.plagiat.modules.AbstractModule;
import net.samagames.plagiat.modules.MCServer;
import net.samagames.plagiat.modules.dimensions.InternalBlockBreakEvent;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.ArmorStand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Splegg module class
 */
public class SpleggModule extends AbstractModule
{
    private ItemStack egg;

    /**
     * Splegg module constructor
     *
     * @param plugin Plagiat's plugin instance
     */
    public SpleggModule(Plagiat plugin)
    {
        super(plugin, "quake", MCServer.HIVEMC);

        this.egg = new ItemStack(Material.EGG, 64);
        ItemMeta itemMeta = this.egg.getItemMeta();
        itemMeta.setDisplayName("Oeuf " + this.getServerSuffix());
        itemMeta.addEnchant(Enchantment.DURABILITY, 1, true);
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE);
        itemMeta.spigot().setUnbreakable(true);
    }

    /**
     * Detect interact with entity, getting egg from armorstand
     *
     * @param event Bukkit event instance
     */
    @EventHandler
    public void onEntityInteract(PlayerInteractAtEntityEvent event)
    {
        if (this.plugin.getGame().getStatus() != Status.IN_GAME)
            return ;
        if (event.getRightClicked() instanceof ArmorStand
                && ((ArmorStand)event.getRightClicked()).getItemInHand() != null
                && ((ArmorStand)event.getRightClicked()).getItemInHand().getType() == Material.EGG)
        {
            if (event.getPlayer().getInventory().firstEmpty() == -1)
            {
                event.getPlayer().sendMessage(ChatColor.RED + "Votre inventaire est plein.");
                return ;
            }
            ((ArmorStand)event.getRightClicked()).setItemInHand(new ItemStack(Material.AIR));
            event.getPlayer().getInventory().addItem(this.egg);
            event.getPlayer().sendMessage(ChatColor.GOLD + "Vous avez maintenant l'" + ChatColor.AQUA + "Oeuf" + ChatColor.GOLD + ". Tirez sur des blocs pour les casser.");
            event.getPlayer().getWorld().playSound(event.getPlayer().getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1F, 1F);
        }
    }

    /**
     * Destroy block on egg land
     *
     * @param event Bukkit event instance
     */
    @EventHandler
    public void onEggLand(PlayerEggThrowEvent event)
    {
        event.getEgg().getLocation().add(event.getEgg().getVelocity()).getBlock().setType(Material.AIR);
        event.setHatching(false);
        event.setNumHatches((byte)0);
        this.plugin.getServer().getPluginManager().callEvent(new InternalBlockBreakEvent(event.getEgg().getLocation().getBlock()));
    }
}