package net.samagames.plagiat.modules.rush;

import net.samagames.api.games.Status;
import net.samagames.plagiat.Plagiat;
import net.samagames.plagiat.modules.AbstractModule;
import net.samagames.plagiat.modules.MCServer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

/**
 * Rush module class
 */
public class RushModule extends AbstractModule
{
    private Villager ghostNPC;

    /**
     * Rush module constructor
     *
     * @param plugin Plagiat's plugin
     */
    public RushModule(Plagiat plugin)
    {
        super(plugin, "rush", MCServer.ASCENTIA);
    }

    /**
     * Create NPC on start
     * {@link AbstractModule#handleGameStart()}
     */
    @Override
    public void handleGameStart()
    {
        this.ghostNPC = this.plugin.getServer().getWorlds().get(0).spawn(new Location(this.plugin.getServer().getWorlds().get(0), 0D, 255D, 0D), Villager.class);
        this.ghostNPC.addPotionEffect(PotionEffectType.LEVITATION.createEffect(Integer.MAX_VALUE, 1));
        this.ghostNPC.addPotionEffect(PotionEffectType.INVISIBILITY.createEffect(Integer.MAX_VALUE, 1));

        List<MerchantRecipe> recipeList = new ArrayList<>();

        ItemStack itemStack = new ItemStack(Material.DIAMOND_SWORD);
        itemStack.addEnchantment(Enchantment.DAMAGE_ALL, 1);
        MerchantRecipe recipe1 = new MerchantRecipe(itemStack, Integer.MAX_VALUE);
        recipe1.addIngredient(new ItemStack(Material.DIAMOND, 3));

        itemStack = new ItemStack(Material.GOLD_CHESTPLATE);
        itemStack.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        MerchantRecipe recipe2 = new MerchantRecipe(itemStack, Integer.MAX_VALUE);
        recipe2.addIngredient(new ItemStack(Material.GOLD_INGOT, 12));

        recipeList.add(recipe1);
        recipeList.add(recipe2);
        this.ghostNPC.setRecipes(recipeList);
    }

    /**
     * Detect interact with entity, opening shop from armorstand
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
                && ((ArmorStand)event.getRightClicked()).getItemInHand().getType() == Material.EMERALD)
            event.getPlayer().openInventory(this.ghostNPC.getInventory());
    }
}
