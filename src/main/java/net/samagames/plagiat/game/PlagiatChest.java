package net.samagames.plagiat.game;

import com.google.gson.JsonArray;
import net.samagames.tools.LocationUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Plagiat Chest class
 */
public class PlagiatChest
{
    private static final Map<Map<ItemStack, Integer>, Integer> ITEMS_NORMAL = new HashMap<>();
    private static final Map<Map<ItemStack, Integer>, Integer> ITEMS_INSANE = new HashMap<>();
    public static final Map<Map<ItemStack, Integer>, Integer> ITEMS_MIDDLE_NORMAL = new HashMap<>();
    public static final Map<Map<ItemStack, Integer>, Integer> ITEMS_MIDDLE_INSANE = new HashMap<>();

    private List<Location> locations;
    private boolean middle;

    /**
     * Plagiat Chest constructor
     *
     * @param locations Locations of the chest
     */
    private PlagiatChest(List<Location> locations, boolean middle)
    {
        this.locations = locations;
        this.middle = middle;
    }

    /**
     * Generate chest contents
     *
     * @param insane true if game is insane
     */
    void generate(boolean insane)
    {
        List<Inventory> inventories = new ArrayList<>(this.locations.size());
        this.locations.forEach(location ->
        {
            BlockState blockState = location.getBlock().getState();
            if (blockState instanceof Chest)
                inventories.add(((Chest)blockState).getBlockInventory());
        });

        if (inventories.isEmpty())
            return ;

        SecureRandom secureRandom = new SecureRandom();
        Map<Map<ItemStack, Integer>, Integer> list = insane ? this.middle ? PlagiatChest.ITEMS_MIDDLE_INSANE : PlagiatChest.ITEMS_INSANE : this.middle ? PlagiatChest.ITEMS_MIDDLE_NORMAL : PlagiatChest.ITEMS_NORMAL;
        list.forEach((subList, probability) ->
        {
            if (secureRandom.nextInt(10000) < probability)
            {
                int random = secureRandom.nextInt(10000);
                for (Map.Entry<ItemStack, Integer> item : subList.entrySet())
                {
                    if (random < item.getValue())
                    {
                        Inventory inventory = inventories.get(secureRandom.nextInt(inventories.size()));
                        if (inventory.firstEmpty() == -1)
                            break ;

                        int slot;
                        ItemStack current;
                        do
                        {
                            slot = secureRandom.nextInt(27);
                            current = inventory.getItem(slot);
                        } while (current != null && current.getType() != Material.AIR);
                        inventory.setItem(slot, item.getKey());

                        if (item.getKey().getType() == Material.BOW && inventory.firstEmpty() != -1)
                        {
                            do
                            {
                                slot = secureRandom.nextInt(27);
                                current = inventory.getItem(slot);
                            } while (current != null && current.getType() != Material.AIR);
                            inventory.setItem(slot, new ItemStack(Material.ARROW));
                        }
                        break;
                    }
                    random -= item.getValue();
                }
            }
        });
    }

    /**
     * Create PlagiatChest instance from string
     *
     * @param jsonArray Array to parse
     * @return New PlagiatChest instance
     */
    static PlagiatChest fromString(JsonArray jsonArray, boolean middle)
    {
        List<Location> locations = new ArrayList<>();
        jsonArray.forEach(jsonElement -> locations.add(LocationUtils.str2loc(jsonElement.getAsString())));
        return new PlagiatChest(locations, middle);
    }

    /**
     * Register an item into chest
     *
     * @param items The items to add
     * @param probability Probability (100% = 10000)
     * @param list List to add
     */
    private static void registerItems(Map<Map<ItemStack, Integer>, Integer> list, int probability, Map<ItemStack, Integer> items)
    {
        list.put(items, probability);
    }

    static
    {
        // Normal
        {
            // Helmets
            Map<ItemStack, Integer> helmets = new HashMap<>();
            helmets.put(new ItemStack(Material.CHAINMAIL_HELMET), 4000);
            helmets.put(new ItemStack(Material.IRON_HELMET), 3333);
            ItemStack goldHelmet = new ItemStack(Material.GOLD_HELMET);
            goldHelmet.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
            helmets.put(goldHelmet, 2666);
            PlagiatChest.registerItems(PlagiatChest.ITEMS_NORMAL, 7500, helmets);

            // Chest plates
            Map<ItemStack, Integer> chests = new HashMap<>();
            chests.put(new ItemStack(Material.CHAINMAIL_CHESTPLATE), 4000);
            chests.put(new ItemStack(Material.IRON_CHESTPLATE), 3333);
            ItemStack goldChest = new ItemStack(Material.GOLD_CHESTPLATE);
            goldChest.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
            chests.put(goldChest, 2666);
            PlagiatChest.registerItems(PlagiatChest.ITEMS_NORMAL, 7500, chests);

            // Legs
            Map<ItemStack, Integer> legs = new HashMap<>();
            legs.put(new ItemStack(Material.CHAINMAIL_LEGGINGS), 4000);
            legs.put(new ItemStack(Material.IRON_LEGGINGS), 3333);
            ItemStack goldLegs = new ItemStack(Material.GOLD_LEGGINGS);
            goldLegs.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
            legs.put(goldLegs, 2666);
            PlagiatChest.registerItems(PlagiatChest.ITEMS_NORMAL, 7500, legs);

            // Boots
            Map<ItemStack, Integer> boots = new HashMap<>();
            boots.put(new ItemStack(Material.CHAINMAIL_BOOTS), 4000);
            boots.put(new ItemStack(Material.IRON_BOOTS), 3333);
            ItemStack goldBoots = new ItemStack(Material.GOLD_BOOTS);
            goldBoots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
            boots.put(goldBoots, 2666);
            PlagiatChest.registerItems(PlagiatChest.ITEMS_NORMAL, 7500, boots);

            // Projectiles
            for (int i = 0; i < 2; ++i)
            {
                Map<ItemStack, Integer> projectiles = new HashMap<>();
                projectiles.put(new ItemStack(Material.FISHING_ROD), 3333);
                projectiles.put(new ItemStack(Material.SNOW_BALL, 16), 6667);
                PlagiatChest.registerItems(PlagiatChest.ITEMS_NORMAL, 7500, projectiles);
            }

            // Blocks
            for (int i = 0; i < 2; ++i)
            {
                Map<ItemStack, Integer> blocks = new HashMap<>();
                blocks.put(new ItemStack(Material.WOOD, 32), 4500);
                blocks.put(new ItemStack(Material.STONE, 32), 4500);
                blocks.put(new ItemStack(Material.LOG, 8), 1000);
                PlagiatChest.registerItems(PlagiatChest.ITEMS_NORMAL, 10000, blocks);
            }

            // Food
            Map<ItemStack, Integer> food = new HashMap<>();
            food.put(new ItemStack(Material.GRILLED_PORK, 16), 1900);
            food.put(new ItemStack(Material.COOKED_BEEF, 16), 1900);
            food.put(new ItemStack(Material.COOKED_CHICKEN, 16), 1900);
            food.put(new ItemStack(Material.COOKED_RABBIT, 16), 1900);
            food.put(new ItemStack(Material.COOKED_MUTTON, 16), 1900);
            food.put(new ItemStack(Material.GOLDEN_CARROT, 8), 500);
            PlagiatChest.registerItems(PlagiatChest.ITEMS_NORMAL, 10000, food);

            // Swords
            Map<ItemStack, Integer> swords = new HashMap<>();
            swords.put(new ItemStack(Material.STONE_SWORD), 5000);
            swords.put(new ItemStack(Material.IRON_SWORD), 2000);
            ItemStack goldSword = new ItemStack(Material.GOLD_SWORD);
            goldSword.addEnchantment(Enchantment.DAMAGE_ALL, 1);
            swords.put(goldSword, 3000);
            PlagiatChest.registerItems(PlagiatChest.ITEMS_NORMAL, 10000, swords);

            // Bow
            Map<ItemStack, Integer> bow = new HashMap<>();
            bow.put(new ItemStack(Material.BOW), 10000);
            PlagiatChest.registerItems(PlagiatChest.ITEMS_NORMAL, 1500, bow);

            // Pickaxes
            Map<ItemStack, Integer> pickaxes = new HashMap<>();
            pickaxes.put(new ItemStack(Material.IRON_PICKAXE), 4000);
            pickaxes.put(new ItemStack(Material.STONE_PICKAXE), 3000);
            ItemStack stonePickaxe = new ItemStack(Material.STONE_PICKAXE);
            stonePickaxe.addEnchantment(Enchantment.DIG_SPEED, 1);
            pickaxes.put(stonePickaxe, 3000);
            PlagiatChest.registerItems(PlagiatChest.ITEMS_NORMAL, 10000, pickaxes);

            // Potion
            Map<ItemStack, Integer> potions = new HashMap<>();
            //noinspection deprecation
            potions.put(new Potion(PotionType.INSTANT_HEAL).splash().toItemStack(1), 10000);
            PlagiatChest.registerItems(PlagiatChest.ITEMS_NORMAL, 1000, potions);
        }

        // Insane
        {

        }
    }
}
