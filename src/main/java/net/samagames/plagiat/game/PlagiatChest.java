package net.samagames.plagiat.game;

import net.samagames.plagiat.Plagiat;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Plagiat Chest class
 */
public class PlagiatChest
{
    private static final Map<ItemStack, Integer> ITEMS_NORMAL = new HashMap<>();
    private static final Map<ItemStack, Integer> ITEMS_INSANE = new HashMap<>();
    public static final Map<ItemStack, Integer> ITEMS_MIDDLE_NORMAL = new HashMap<>();
    public static final Map<ItemStack, Integer> ITEMS_MIDDLE_INSANE = new HashMap<>();

    private Location location;
    private int groupId;

    /**
     * Plagiat Chest constructor
     *
     * @param location Location of the chest
     * @param groupId Group id for this chest
     */
    private PlagiatChest(Location location, int groupId)
    {
        this.location = location;
        this.groupId = groupId;
    }

    /**
     * Generate chest contents
     *
     * @param insane true if game is insane
     */
    void generate(boolean insane)
    {
        Block block = location.getBlock();
        BlockState blockState = block.getState();
        if (!(blockState instanceof Chest))
            return ;
        boolean middle = this.groupId == -1;
        Random random = new Random();
        Inventory inventory = ((Chest)block.getState()).getBlockInventory();
        Map<ItemStack, Integer> list = insane && middle ? PlagiatChest.ITEMS_INSANE : insane ? PlagiatChest.ITEMS_MIDDLE_INSANE : middle ? PlagiatChest.ITEMS_NORMAL : PlagiatChest.ITEMS_MIDDLE_NORMAL;
        int i = 0;
        for (Map.Entry<ItemStack, Integer> entry : list.entrySet())
        {
            int rand = random.nextInt(10000);
            if (rand < entry.getValue())
                inventory.setItem(i, entry.getKey());
            i++;
            i %= 27;
        }
    }

    /**
     * Create PlagiatChest instance from string
     *
     * @param plugin Plagiat plugin instance
     * @param string String to parse
     * @return New PlagiatChest instance
     */
    static PlagiatChest fromString(Plagiat plugin, String string)
    {
        String[] args = string.split(", ");
        return new PlagiatChest(new Location(plugin.getServer().getWorld(args[0]), Double.parseDouble(args[1]), Double.parseDouble(args[2]), Double.parseDouble(args[3])), args.length == 4 ? -1 : Integer.parseInt(args[4]));
    }

    /**
     * Register an item into chest
     *
     * @param itemStack The itemstack to add
     * @param probability Probability (100% = 10000)
     * @param list List to add
     */
    private static void registerItem(ItemStack itemStack, int probability, Map<ItemStack, Integer> list)
    {
        list.put(itemStack, probability);
    }

    static
    {
        PlagiatChest.registerItem(new ItemStack(Material.STONE_SWORD), 5000, PlagiatChest.ITEMS_NORMAL);
        PlagiatChest.registerItem(new ItemStack(Material.DIAMOND_SWORD), 5000, PlagiatChest.ITEMS_INSANE);
        PlagiatChest.registerItem(new ItemStack(Material.IRON_SWORD), 5000, PlagiatChest.ITEMS_MIDDLE_NORMAL);
        PlagiatChest.registerItem(new ItemStack(Material.DIAMOND_SWORD), 5000, PlagiatChest.ITEMS_MIDDLE_INSANE);
    }
}
