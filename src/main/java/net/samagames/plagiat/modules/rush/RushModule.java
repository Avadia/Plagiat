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
import java.util.Collections;
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
        this.ghostNPC.setInvulnerable(true);

        List<MerchantRecipe> recipeList = new ArrayList<>();

        ItemStack itemStack;

        itemStack = new ItemStack(Material.SANDSTONE, 8);
        MerchantRecipe recipe1 = new MerchantRecipe(itemStack, Integer.MAX_VALUE);
        recipe1.addIngredient(new ItemStack(Material.COAL, 1));

        itemStack = new ItemStack(Material.IRON_SWORD);
        itemStack.addEnchantment(Enchantment.DAMAGE_ALL, 1);
        MerchantRecipe recipe2 = new MerchantRecipe(itemStack, Integer.MAX_VALUE);
        recipe2.addIngredient(new ItemStack(Material.IRON_INGOT, 3));

        itemStack = new ItemStack(Material.DIAMOND_SWORD);
        itemStack.addEnchantment(Enchantment.DAMAGE_ALL, 1);
        MerchantRecipe recipe3 = new MerchantRecipe(itemStack, Integer.MAX_VALUE);
        recipe3.addIngredient(new ItemStack(Material.DIAMOND, 3));

        itemStack = new ItemStack(Material.GOLD_CHESTPLATE);
        itemStack.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3);
        MerchantRecipe recipe4 = new MerchantRecipe(itemStack, Integer.MAX_VALUE);
        recipe4.addIngredient(new ItemStack(Material.GOLD_INGOT, 12));

        itemStack = new ItemStack(Material.IRON_PICKAXE);
        itemStack.addEnchantment(Enchantment.DIG_SPEED, 1);
        MerchantRecipe recipe5 = new MerchantRecipe(itemStack, Integer.MAX_VALUE);
        recipe5.addIngredient(new ItemStack(Material.IRON_INGOT, 9));

        itemStack = new ItemStack(Material.TNT, 2);
        MerchantRecipe recipe6 = new MerchantRecipe(itemStack, Integer.MAX_VALUE);
        recipe6.addIngredient(new ItemStack(Material.IRON_INGOT, 6));

        itemStack = new ItemStack(Material.FLINT_AND_STEEL);
        MerchantRecipe recipe7 = new MerchantRecipe(itemStack, Integer.MAX_VALUE);
        recipe7.addIngredient(new ItemStack(Material.IRON_INGOT, 3));

        itemStack = new ItemStack(Material.COOKED_BEEF, 2);
        MerchantRecipe recipe8 = new MerchantRecipe(itemStack, Integer.MAX_VALUE);
        recipe8.addIngredient(new ItemStack(Material.COAL, 2));

        itemStack = new ItemStack(Material.GOLDEN_APPLE);
        MerchantRecipe recipe9 = new MerchantRecipe(itemStack, Integer.MAX_VALUE);
        recipe9.addIngredient(new ItemStack(Material.IRON_INGOT, 10));

        recipeList.add(recipe1);
        recipeList.add(recipe2);
        recipeList.add(recipe3);
        recipeList.add(recipe4);
        recipeList.add(recipe5);
        recipeList.add(recipe6);
        recipeList.add(recipe7);
        recipeList.add(recipe8);
        recipeList.add(recipe9);

        Collections.shuffle(recipeList);

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
        {
            event.getPlayer().openMerchant(this.ghostNPC, true);
            event.setCancelled(true);
        }
    }
}
