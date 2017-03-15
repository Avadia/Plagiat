package net.samagames.plagiat.game;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Plagiat Kit Class
 */
class PlagiatKit
{
    private static List<PlagiatKit> kits = new ArrayList<>();

    private final String name;
    private final int dbId;
    private final ItemStack icon;
    private final ItemStack lockedIcon;
    private final List<ItemStack> items;

    /**
     * Plagiat Kit Constructor
     *
     * @param name Kit display name
     * @param dbId Kit database id
     * @param icon Kit icon
     * @param lore Kit icon lore
     */
    private PlagiatKit(String name, int dbId, ItemStack icon, String[] lore)
    {
        this.name = name;
        this.dbId = dbId;
        this.icon = icon;
        this.items = new ArrayList<>();

        ItemMeta itemMeta = this.icon.getItemMeta();
        itemMeta.setDisplayName(ChatColor.GREEN + this.name);
        itemMeta.setLore(Arrays.asList(lore));
        this.icon.setItemMeta(itemMeta);

        this.lockedIcon = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte)14);
        itemMeta = this.lockedIcon.getItemMeta();
        itemMeta.setDisplayName(ChatColor.RED + this.name);
        itemMeta.setLore(Arrays.asList(lore));
        this.lockedIcon.setItemMeta(itemMeta);
    }

    /**
     * Add an item into this kit
     *
     * @param itemStack Bukkit ItemStack instance
     */
    private void addItem(ItemStack itemStack)
    {
        this.items.add(itemStack);
    }

    /**
     * Get this kit display name for GUI
     *
     * @return Name as String
     */
    String getName()
    {
        return this.name;
    }

    /**
     * Get database id for this kit
     *
     * @return Database id as Integer
     */
    int getDbId()
    {
        return this.dbId;
    }

    /**
     * Get this kit icon for GUI
     *
     * @return Icon as Bukkit ItemStack instance
     */
    ItemStack getIcon()
    {
        return this.icon;
    }

    /**
     * Get all items in this kit
     *
     * @return Items as a List of Bukkit ItemStack instances
     */
    List<ItemStack> getItems()
    {
        return this.items;
    }

    /**
     * Get this kit locked icon for GUI
     *
     * @return Icon as Bukkit ItemStack instance
     */
    ItemStack getLockedIcon()
    {
        return this.lockedIcon;
    }

    /**
     * Get all kits
     *
     * @return List of PlagiatKit instances
     */
    static List<PlagiatKit> getKits()
    {
        return PlagiatKit.kits;
    }

    static
    {
        // Register all kits
        PlagiatKit defaultKit = new PlagiatKit("Défaut", 0, new ItemStack(Material.STONE_PICKAXE), new String[] { "Kit par défaut", "", "Pioche en pierre", "Hache en pierre", "Pelle en pierre" });
        defaultKit.addItem(new ItemStack(Material.STONE_PICKAXE));
        defaultKit.addItem(new ItemStack(Material.STONE_AXE));
        defaultKit.addItem(new ItemStack(Material.STONE_SPADE));
        PlagiatKit.kits.add(defaultKit);

        // TODO
    }
}
