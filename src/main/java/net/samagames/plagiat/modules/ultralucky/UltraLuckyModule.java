package net.samagames.plagiat.modules.ultralucky;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.samagames.plagiat.Plagiat;
import net.samagames.plagiat.game.PlagiatChest;
import net.samagames.plagiat.modules.AbstractModule;
import net.samagames.plagiat.modules.MCServer;
import net.samagames.tools.LocationUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

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
public class UltraLuckyModule extends AbstractModule
{
    private List<Location> blocks;
    private Random random;
    private BukkitTask effectTask;
    private boolean insane;

    /**
     * UltraLuckyModule constructor
     *
     * @param plugin Plagiat's plugin instance
     */
    public UltraLuckyModule(Plagiat plugin)
    {
        super(plugin, "ultralucky", MCServer.UHCGAMES);

        this.blocks = new ArrayList<>();
        this.random = new Random();
        this.effectTask = null;
        this.insane = plugin.getGame().isInsane();
    }

    /**
     * Load configuration and run tasks
     * {@link AbstractModule#handleGameStart()}
     */
    @Override
    public void handleGameStart()
    {
        JsonObject jsonObject = this.getConfigRoot();
        JsonArray jsonArray = jsonObject.getAsJsonArray("blocks");
        if (jsonArray != null)
            jsonArray.forEach(element -> this.blocks.add(LocationUtils.str2loc(element.getAsString())));
        this.blocks.forEach(location -> location.getBlock().setType(Material.SPONGE));

        this.effectTask = this.plugin.getServer().getScheduler().runTaskTimer(this.plugin, () -> this.blocks.stream().filter(location -> location.getBlock().getType() == Material.SPONGE).forEach(location -> location.getWorld().spawnParticle(Particle.PORTAL, location.clone().add(0.5D, 0.5D, 0.5D), 5, 0.1D, 0.1D, 0.1D, 0.5D)), 2L, 2L);
    }

    /**
     * Cancel effect task
     * {@link AbstractModule#handleGameEnd()}
     *
     * @return false, to avoid disabling end
     */
    @Override
    public boolean handleGameEnd()
    {
        if (this.effectTask != null)
            this.effectTask.cancel();

        return false;
    }

    /**
     * Give stuff on sponge break
     *
     * @param event Bukkit event instance
     */
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntitySpawn(ItemSpawnEvent event)
    {
        if (event.getEntity().getItemStack().getType() != Material.SPONGE)
            return ;

        Map<Map<ItemStack, Integer>, Integer> list = this.insane ? PlagiatChest.ITEMS_MIDDLE_INSANE : PlagiatChest.ITEMS_MIDDLE_NORMAL;
        Map<ItemStack, Integer> stuff = new ArrayList<>(list.keySet()).get(this.random.nextInt(list.size()));
        event.getEntity().setItemStack(new ArrayList<>(stuff.keySet()).get(this.random.nextInt(stuff.size())));
        event.getEntity().setVelocity(new Vector(0, 0, 0));

        event.getEntity().getWorld().getNearbyEntities(event.getEntity().getLocation(), 5D, 5D, 5D).stream().filter(entity -> entity instanceof ArmorStand && entity.getCustomName() != null && entity.getCustomName().contains("LuckyBlock")).forEach(Entity::remove);
    }
}
