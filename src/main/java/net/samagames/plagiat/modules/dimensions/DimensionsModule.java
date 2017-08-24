package net.samagames.plagiat.modules.dimensions;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.samagames.api.games.Status;
import net.samagames.plagiat.Plagiat;
import net.samagames.plagiat.modules.AbstractModule;
import net.samagames.plagiat.modules.MCServer;
import net.samagames.tools.Area;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
public class DimensionsModule extends AbstractModule
{
    private List<UUID> cooldown;
    private Integer offset;
    private Area firstArea;
    private Area secondArea;
    private ItemStack eye;

    /**
     * Dimensions module constructor
     *
     * @param plugin Plagiat plugin instance
     */
    public DimensionsModule(Plagiat plugin)
    {
        super(plugin, "dimensions", MCServer.SAMAGAMES);
        this.cooldown = new ArrayList<>();

        this.eye = new ItemStack(Material.EYE_OF_ENDER);
        ItemMeta meta = this.eye.getItemMeta();
        meta.setDisplayName(ChatColor.GOLD + "Oeil de téléportation");
        this.eye.setItemMeta(meta);
    }

    /**
     * Event method, load configuration for dimension offset
     * {@link AbstractModule#handleGameStart()}
     */
    @Override
    public void handleGameStart()
    {
        JsonObject jsonObject = this.getConfigRoot();
        JsonElement element = jsonObject.get("offset");
        this.offset = element == null ? null : element.getAsInt();
        element = jsonObject.get("firstArea");
        this.firstArea = element == null ? null : Area.str2area(element.getAsString());
        element = jsonObject.get("secondArea");
        this.secondArea = element == null ? null : Area.str2area(element.getAsString());
    }

    /**
     * Break a block in both dimensions
     *
     * @param block Origin block
     */
    private void breakBlock(Block block)
    {
        if (this.offset == null || this.firstArea == null || this.secondArea == null)
            return ;
        (this.firstArea.isInLimit(block.getLocation(), 200) ? block.getLocation().add(this.offset, 0D, 0D) : block.getLocation().subtract(this.offset, 0D, 0D)).getBlock().setType(Material.AIR);
    }

    /**
     * Place a block in both dimensions
     *
     * @param block Origin block
     */
    @SuppressWarnings("deprecation")
    private void placeBlock(Block block)
    {
        if (this.offset == null || this.firstArea == null || this.secondArea == null)
            return ;
        Block block2 = (this.firstArea.isInLimit(block.getLocation(), 200) ? block.getLocation().add(this.offset, 0D, 0D) : block.getLocation().subtract(this.offset, 0D, 0D)).getBlock();
        block2.setType(block.getType());
        block2.setData(block.getData());
    }

    /**
     * Detect block break by player
     *
     * @param event Bukkit event instance
     */
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onBlockBreak(BlockBreakEvent event)
    {
        if (this.plugin.getGame().getStatus() != Status.IN_GAME)
            return ;
        this.breakBlock(event.getBlock());
    }

    /**
     * Detect block place by player
     *
     * @param event Bukkit event instance
     */
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onBlockPlace(BlockPlaceEvent event)
    {
        if (this.plugin.getGame().getStatus() != Status.IN_GAME)
            return ;
        this.placeBlock(event.getBlockPlaced());
    }

    /**
     * Detect block break by block explosion
     *
     * @param event Bukkit event instance
     */
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onBlockExplosion(BlockExplodeEvent event)
    {
        event.blockList().forEach(this::breakBlock);
    }

    /**
     * Detect block break by entity explosion
     *
     * @param event Bukkit event instance
     */
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onEntityExplosion(EntityExplodeEvent event)
    {
        if (this.plugin.getGame().getStatus() != Status.IN_GAME)
            return ;
        event.blockList().forEach(this::breakBlock);
    }

    /**
     * Cancel renaming of teleport eye
     *
     * @param event Bukkit event instance
     */
    @EventHandler
    public void onAnvilUse(PrepareAnvilEvent event)
    {
        if (event.getResult().getType() == Material.EYE_OF_ENDER)
            event.getResult().setType(Material.AIR);
    }

    /**
     * Detect use of teleport eye, and teleport if needed
     *
     * @param event Bukkit event instance
     */
    @EventHandler
    public void onInteract(PlayerInteractEvent event)
    {
        if (this.plugin.getGame().getStatus() != Status.IN_GAME)
            return ;
        ItemStack itemStack;
        if (event.getHand() == EquipmentSlot.OFF_HAND)
            itemStack = event.getPlayer().getInventory().getItemInOffHand();
        else
            itemStack = event.getPlayer().getInventory().getItemInMainHand();
        if (itemStack != null && itemStack.isSimilar(this.eye))
        {
            if (this.cooldown.contains(event.getPlayer().getUniqueId()))
                return ;
            this.cooldown.add(event.getPlayer().getUniqueId());
            this.plugin.getServer().getScheduler().runTaskLater(this.plugin, () -> this.cooldown.remove(event.getPlayer().getUniqueId()), 200L);
            event.getPlayer().teleport(this.firstArea.isInLimit(event.getPlayer().getLocation(), 200) ? event.getPlayer().getLocation().add(this.offset, 0D, 0D) : event.getPlayer().getLocation().subtract(this.offset, 0D, 0D));
            event.getPlayer().sendMessage(ChatColor.YELLOW + "Vous avez changé de dimension.");
        }
    }

    /**
     * Internal event, for communication between modules
     *
     * @param event Bukkit event instance
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onInternalBlockBreak(InternalBlockBreakEvent event)
    {
        if (this.plugin.getGame().getStatus() != Status.IN_GAME)
            return ;
        this.breakBlock(event.getBlock());
    }
}
