package net.samagames.plagiat.game;

import net.samagames.tools.ItemUtils;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Arrays;
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

        for (int i = 0; i < lore.length; ++i)
            lore[i] = ChatColor.GRAY + " " + lore[i];

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
        PlagiatKit defaultKit = new PlagiatKit("Défaut", -1, new ItemStack(Material.STONE_PICKAXE), new String[] { "Kit par défaut", "", "Pioche en pierre", "Hache en pierre", "Pelle en pierre" });
        defaultKit.addItem(new ItemStack(Material.STONE_PICKAXE));
        defaultKit.addItem(new ItemStack(Material.STONE_AXE));
        defaultKit.addItem(new ItemStack(Material.STONE_SPADE));
        PlagiatKit.kits.add(defaultKit);

        ItemStack frogHead = ItemUtils.getCustomHead("eyJ0aW1lc3RhbXAiOjE0OTA1MzMyMzE0NjMsInByb2ZpbGVJZCI6Ijc5ZmI4NDEwNWQ2ZDQ2YmY5MGFmOTA1NzE5YjliMzAzIiwicHJvZmlsZU5hbWUiOiJGcm9nXyIsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9mYTcyZDdkMjE5NWE1MWZiZTU0NmU3MzQ4YTZiNzVkZTExOTdiY2UyMWVhMWRlYzYyMjdkZjFmMmVkOGI3MyJ9fX0=");
        PlagiatKit frogKit = new PlagiatKit("Grenouille", -1, frogHead, new String[] { "Kit Grenouille", "", "Tête de grenouille", "Plastron en cuir", "Jambes en cuir", "Bottes en cuir", "Potion de grenouille" });
        frogKit.addItem(frogHead);
        ItemStack greenChest = new ItemStack(Material.LEATHER_CHESTPLATE);
        LeatherArmorMeta greenChestMeta = (LeatherArmorMeta)greenChest.getItemMeta();
        greenChestMeta.setColor(Color.GREEN);
        greenChest.setItemMeta(greenChestMeta);
        frogKit.addItem(greenChest);
        ItemStack greenLegs = new ItemStack(Material.LEATHER_CHESTPLATE);
        LeatherArmorMeta greenLegsMeta = (LeatherArmorMeta)greenLegs.getItemMeta();
        greenLegsMeta.setColor(Color.GREEN);
        greenLegs.setItemMeta(greenLegsMeta);
        frogKit.addItem(greenLegs);
        ItemStack greenBoots = new ItemStack(Material.LEATHER_CHESTPLATE);
        LeatherArmorMeta greenBootsMeta = (LeatherArmorMeta)greenBoots.getItemMeta();
        greenBootsMeta.setColor(Color.GREEN);
        greenBoots.setItemMeta(greenBootsMeta);
        frogKit.addItem(greenBoots);
        ItemStack frogPotion = new ItemStack(Material.SPLASH_POTION);
        PotionMeta frogPotionMeta = (PotionMeta)frogPotion.getItemMeta();
        frogPotionMeta.setDisplayName(ChatColor.GREEN + "Potion de grenouille");
        frogPotionMeta.addCustomEffect(PotionEffectType.JUMP.createEffect(200, 2), true);
        frogPotionMeta.addCustomEffect(PotionEffectType.SPEED.createEffect(200, 2), true);
        frogPotion.setItemMeta(frogPotionMeta);
        frogKit.addItem(frogPotion);
        PlagiatKit.kits.add(frogKit);

        // TODO
    }
}
