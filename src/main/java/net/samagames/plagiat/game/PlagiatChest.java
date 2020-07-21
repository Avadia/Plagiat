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
public class PlagiatChest {
    public static final Map<Map<ItemStack, Integer>, Integer> ITEMS_MIDDLE_NORMAL = new HashMap<>();
    public static final Map<Map<ItemStack, Integer>, Integer> ITEMS_MIDDLE_INSANE = new HashMap<>();
    private static final Map<Map<ItemStack, Integer>, Integer> ITEMS_NORMAL = new HashMap<>();
    private static final Map<Map<ItemStack, Integer>, Integer> ITEMS_INSANE = new HashMap<>();

    static {
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
            for (int i = 0; i < 2; ++i) {
                Map<ItemStack, Integer> projectiles = new HashMap<>();
                projectiles.put(new ItemStack(Material.FISHING_ROD), 3333);
                projectiles.put(new ItemStack(Material.SNOW_BALL, 16), 6667);
                PlagiatChest.registerItems(PlagiatChest.ITEMS_NORMAL, 7500, projectiles);
            }

            // Blocks
            for (int i = 0; i < 2; ++i) {
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
            for (int i = 0; i < 2; i++) {
                Map<ItemStack, Integer> swords = new HashMap<>();
                swords.put(new ItemStack(Material.STONE_SWORD), 5000);
                swords.put(new ItemStack(Material.IRON_SWORD), 2000);
                ItemStack goldSword = new ItemStack(Material.GOLD_SWORD);
                goldSword.addEnchantment(Enchantment.DAMAGE_ALL, 1);
                swords.put(goldSword, 3000);
                PlagiatChest.registerItems(PlagiatChest.ITEMS_NORMAL, 10000, swords);
            }

            // Bow
            Map<ItemStack, Integer> bow = new HashMap<>();
            bow.put(new ItemStack(Material.BOW), 10000);
            PlagiatChest.registerItems(PlagiatChest.ITEMS_NORMAL, 3000, bow);

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

        // Normal Middle
        {
            // P4 iron helmet
            Map<ItemStack, Integer> helmets = new HashMap<>();
            ItemStack ironHelmet = new ItemStack(Material.IRON_HELMET);
            ironHelmet.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
            helmets.put(ironHelmet, 10000);
            PlagiatChest.registerItems(PlagiatChest.ITEMS_MIDDLE_NORMAL, 1000, helmets);

            // P4 iron chest
            Map<ItemStack, Integer> chestPlates = new HashMap<>();
            ItemStack ironChest = new ItemStack(Material.IRON_CHESTPLATE);
            ironChest.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
            chestPlates.put(ironChest, 10000);
            PlagiatChest.registerItems(PlagiatChest.ITEMS_MIDDLE_NORMAL, 1000, chestPlates);

            // P4 iron legs
            Map<ItemStack, Integer> legs = new HashMap<>();
            ItemStack ironLegs = new ItemStack(Material.IRON_LEGGINGS);
            ironLegs.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
            legs.put(ironLegs, 10000);
            PlagiatChest.registerItems(PlagiatChest.ITEMS_MIDDLE_NORMAL, 1000, legs);

            // P4 iron boots
            Map<ItemStack, Integer> boots = new HashMap<>();
            ItemStack ironBoots = new ItemStack(Material.IRON_BOOTS);
            ironBoots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
            boots.put(ironBoots, 10000);
            PlagiatChest.registerItems(PlagiatChest.ITEMS_MIDDLE_NORMAL, 1000, boots);

            // S1 iron sword
            Map<ItemStack, Integer> swords = new HashMap<>();
            ItemStack ironSword = new ItemStack(Material.IRON_SWORD);
            ironSword.addEnchantment(Enchantment.DAMAGE_ALL, 1);
            swords.put(ironBoots, 10000);
            PlagiatChest.registerItems(PlagiatChest.ITEMS_MIDDLE_NORMAL, 3000, swords);

            // P1 diamond helmet
            Map<ItemStack, Integer> diamondHelmets = new HashMap<>();
            ItemStack diamondHelmet = new ItemStack(Material.DIAMOND_HELMET);
            diamondHelmet.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
            diamondHelmets.put(diamondHelmet, 10000);
            PlagiatChest.registerItems(PlagiatChest.ITEMS_MIDDLE_NORMAL, 200, diamondHelmets);

            // P1 diamond chest
            Map<ItemStack, Integer> diamondChests = new HashMap<>();
            ItemStack diamondChest = new ItemStack(Material.DIAMOND_CHESTPLATE);
            diamondChest.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
            diamondChests.put(diamondChest, 10000);
            PlagiatChest.registerItems(PlagiatChest.ITEMS_MIDDLE_NORMAL, 200, diamondChests);

            // P1 diamond legs
            Map<ItemStack, Integer> diamondLegs = new HashMap<>();
            ItemStack diamondLeg = new ItemStack(Material.DIAMOND_LEGGINGS);
            diamondLeg.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
            diamondLegs.put(diamondLeg, 10000);
            PlagiatChest.registerItems(PlagiatChest.ITEMS_MIDDLE_NORMAL, 200, diamondLegs);

            // P1 diamond boots
            Map<ItemStack, Integer> diamondBoots = new HashMap<>();
            ItemStack diamondBoot = new ItemStack(Material.DIAMOND_BOOTS);
            diamondBoot.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
            diamondBoots.put(diamondBoot, 10000);
            PlagiatChest.registerItems(PlagiatChest.ITEMS_MIDDLE_NORMAL, 200, diamondBoots);

            // Diamond sword
            Map<ItemStack, Integer> diamondSwords = new HashMap<>();
            diamondSwords.put(new ItemStack(Material.DIAMOND_SWORD), 10000);
            PlagiatChest.registerItems(PlagiatChest.ITEMS_MIDDLE_NORMAL, 800, diamondSwords);

            // Experience bottles
            Map<ItemStack, Integer> bottles = new HashMap<>();
            bottles.put(new ItemStack(Material.EXP_BOTTLE, 24), 10000);
            PlagiatChest.registerItems(PlagiatChest.ITEMS_MIDDLE_NORMAL, 5000, bottles);

            // Splash heal potion
            Map<ItemStack, Integer> healPotions = new HashMap<>();
            //noinspection deprecation
            healPotions.put(new Potion(PotionType.INSTANT_HEAL).splash().toItemStack(1), 10000);
            PlagiatChest.registerItems(PlagiatChest.ITEMS_NORMAL, 3000, healPotions);

            // Bow
            Map<ItemStack, Integer> bow = new HashMap<>();
            bow.put(new ItemStack(Material.BOW), 10000);
            PlagiatChest.registerItems(PlagiatChest.ITEMS_NORMAL, 3000, bow);

            // Water bucket
            Map<ItemStack, Integer> buckets = new HashMap<>();
            buckets.put(new ItemStack(Material.WATER_BUCKET), 10000);
            PlagiatChest.registerItems(PlagiatChest.ITEMS_NORMAL, 1000, buckets);

            // TNT
            Map<ItemStack, Integer> tnt = new HashMap<>();
            tnt.put(new ItemStack(Material.TNT), 10000);
            PlagiatChest.registerItems(PlagiatChest.ITEMS_NORMAL, 1000, tnt);

            // Golden apple
            Map<ItemStack, Integer> goldenApple = new HashMap<>();
            goldenApple.put(new ItemStack(Material.GOLDEN_APPLE), 10000);
            PlagiatChest.registerItems(PlagiatChest.ITEMS_NORMAL, 3000, goldenApple);

            // Ender pearls
            Map<ItemStack, Integer> pearls = new HashMap<>();
            pearls.put(new ItemStack(Material.ENDER_PEARL, 2), 10000);
            PlagiatChest.registerItems(PlagiatChest.ITEMS_NORMAL, 1000, pearls);
        }
    }

    private final List<Location> locations;
    private final boolean middle;

    /**
     * Plagiat Chest constructor
     *
     * @param locations Locations of the chest
     */
    private PlagiatChest(List<Location> locations, boolean middle) {
        this.locations = locations;
        this.middle = middle;
    }

    /**
     * Create PlagiatChest instance from string
     *
     * @param jsonArray Array to parse
     * @return New PlagiatChest instance
     */
    static PlagiatChest fromString(JsonArray jsonArray, boolean middle) {
        List<Location> locations = new ArrayList<>();
        jsonArray.forEach(jsonElement -> locations.add(LocationUtils.str2loc(jsonElement.getAsString())));
        return new PlagiatChest(locations, middle);
    }

    /**
     * Register an item into chest
     *
     * @param items       The items to add
     * @param probability Probability (100% = 10000)
     * @param list        List to add
     */
    private static void registerItems(Map<Map<ItemStack, Integer>, Integer> list, int probability, Map<ItemStack, Integer> items) {
        list.put(items, probability);
    }

    /**
     * Generate chest contents
     *
     * @param insane true if game is insane
     */
    void generate(boolean insane) {
        List<Inventory> inventories = new ArrayList<>(this.locations.size());
        this.locations.forEach(location ->
        {
            BlockState blockState = location.getBlock().getState();
            if (blockState instanceof Chest)
                inventories.add(((Chest) blockState).getBlockInventory());
        });

        if (inventories.isEmpty())
            return;

        SecureRandom secureRandom = new SecureRandom();
        Map<Map<ItemStack, Integer>, Integer> list = insane ? this.middle ? PlagiatChest.ITEMS_MIDDLE_INSANE : PlagiatChest.ITEMS_INSANE : this.middle ? PlagiatChest.ITEMS_MIDDLE_NORMAL : PlagiatChest.ITEMS_NORMAL;
        list.forEach((subList, probability) ->
        {
            if (secureRandom.nextInt(10000) < probability) {
                int random = secureRandom.nextInt(10000);
                for (Map.Entry<ItemStack, Integer> item : subList.entrySet()) {
                    if (random < item.getValue()) {
                        Inventory inventory = inventories.get(secureRandom.nextInt(inventories.size()));
                        if (inventory.firstEmpty() == -1)
                            break;

                        int slot;
                        ItemStack current;
                        do {
                            slot = secureRandom.nextInt(27);
                            current = inventory.getItem(slot);
                        } while (current != null && current.getType() != Material.AIR);
                        inventory.setItem(slot, item.getKey());

                        if (item.getKey().getType() == Material.BOW && inventory.firstEmpty() != -1) {
                            do {
                                slot = secureRandom.nextInt(27);
                                current = inventory.getItem(slot);
                            } while (current != null && current.getType() != Material.AIR);
                            inventory.setItem(slot, new ItemStack(Material.ARROW, 10));
                        }
                        break;
                    }
                    random -= item.getValue();
                }
            }
        });
    }
}
