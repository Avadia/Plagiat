package net.samagames.plagiat.modules.sheepwars;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.samagames.api.games.Status;
import net.samagames.plagiat.Plagiat;
import net.samagames.plagiat.modules.AbstractModule;
import net.samagames.plagiat.modules.MCServer;
import net.samagames.plagiat.modules.sheepwars.sheeps.BlindnessSheep;
import net.samagames.plagiat.modules.sheepwars.sheeps.BoardingSheep;
import net.samagames.plagiat.modules.sheepwars.sheeps.EarthQuakeSheep;
import net.samagames.plagiat.modules.sheepwars.sheeps.ExplosiveSheep;
import net.samagames.plagiat.modules.sheepwars.sheeps.HealingSheep;
import net.samagames.plagiat.modules.sheepwars.sheeps.IceSheep;
import net.samagames.plagiat.modules.sheepwars.sheeps.ThunderSheep;
import net.samagames.tools.LocationUtils;
import net.samagames.tools.Reflection;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityBreedEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.BlockIterator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;

/**
 * Module for SheepWars game
 *  (cc @Epicube)
 */
public class SheepWarsModule extends AbstractModule
{
    private List<WoolType> woolTypes;
    private List<Location> woolLocations;
    private BukkitTask respawnTask;
    private BukkitTask updateTask;
    private BukkitTask particleTask;
    private Random random;

    /**
     * SheepWard Module constructor
     * {@link AbstractModule}
     *
     * @param plugin Plagiat's plugin instance
     */
    public SheepWarsModule(Plagiat plugin)
    {
        super(plugin, "sheepwars", MCServer.EPICUBE);
        this.woolTypes = new ArrayList<>();
        this.woolLocations = new ArrayList<>();
        this.respawnTask = null;
        this.updateTask = null;
        this.particleTask = null;
        this.random = new Random();
    }

    /**
     * Event method, overridden to load config and spawn wools
     * {@link AbstractModule#handleGameStart()}
     */
    @Override
    public void handleGameStart()
    {
        JsonObject jsonObject = this.getConfigRoot();
        JsonArray wools = jsonObject.getAsJsonArray("wools");
        if (wools != null)
            wools.forEach(element -> this.woolLocations.add(LocationUtils.str2loc(element.getAsString())));
        JsonElement element = jsonObject.get("respawnDelay");
        long respawnDelay = element == null ? 2400 : element.getAsLong();
        this.respawnTask = this.plugin.getServer().getScheduler().runTaskTimer(this.plugin, this::respawnWools, 1L, respawnDelay);
        this.updateTask = this.plugin.getServer().getScheduler().runTaskTimer(this.plugin, this::changeColors, 10L, 10L);
        this.particleTask = this.plugin.getServer().getScheduler().runTaskTimer(this.plugin, this::spawnParticles, 2L, 2L);

        this.woolTypes.add(new HealingSheep(this.plugin));
        this.woolTypes.add(new BlindnessSheep(this.plugin));
        this.woolTypes.add(new ExplosiveSheep(this.plugin));
        this.woolTypes.add(new BoardingSheep(this.plugin));
        this.woolTypes.add(new IceSheep(this.plugin));
        this.woolTypes.add(new ThunderSheep(this.plugin));
        this.woolTypes.add(new EarthQuakeSheep(this.plugin));
        //TODO this.woolTypes.add(new FiringSheep(this.plugin));
        //TODO this.woolTypes.add(new FragmentationSheep(this.plugin));
        //TODO this.woolTypes.add(new SeekerSheep(this.plugin));
        //TODO this.woolTypes.add(new EatingSheep(this.plugin));
        //TODO this.woolTypes.add(new DistortionSheep(this.plugin));
        //TODO this.woolTypes.add(new GalacticSheep(this.plugin));
    }

    /**
     * Spawn particles around wools
     */
    private void spawnParticles()
    {
        this.woolLocations.forEach(location -> location.getWorld().spawnParticle(Particle.PORTAL, location.clone().add(0.5D, 0.5D, 0.5D), 3, 0.5F, 0.5D, 0.5D, 0.5D));
    }

    /**
     * Respawn wools if they are broken
     */
    @SuppressWarnings("deprecation")
    private void respawnWools()
    {
        this.woolLocations.forEach(location ->
        {
            Block block = location.getBlock();
            if (block.getType() != Material.WOOL)
            {
                block.setType(Material.WOOL);
                block.setData(this.woolTypes.get(this.random.nextInt(this.woolTypes.size())).getDyeColor().getWoolData());
            }
        });
    }

    /**
     * Change the wools colors randomly
     */
    @SuppressWarnings("deprecation")
    private void changeColors()
    {
        this.woolLocations.forEach(location ->
        {
            Block block = location.getBlock();
            if (block.getType() == Material.WOOL)
                block.setData(this.woolTypes.get(this.random.nextInt(this.woolTypes.size())).getDyeColor().getWoolData());
        });
    }

    /**
     * Event method, overridden to cancel all tasks
     * {@link AbstractModule#handleGameEnd()}
     *
     * @return false, to avoid disabling end
     */
    @Override
    public boolean handleGameEnd()
    {
        if (this.respawnTask != null)
            this.respawnTask.cancel();
        if (this.updateTask != null)
            this.updateTask.cancel();
        if (this.particleTask != null)
            this.particleTask.cancel();
        this.woolTypes.forEach(WoolType::killSheeps);

        return false;
    }

    /**
     * Event method, to catch sheep death
     *
     * @param event Bukkit event instance
     */
    @SuppressWarnings("deprecation")
    @EventHandler
    public void onSheepDeath(EntityDeathEvent event)
    {
        if (this.plugin.getGame().getStatus() != Status.IN_GAME)
            return ;
        if (event.getEntityType() == EntityType.SHEEP && event.getEntity().hasMetadata("sg-type"))
        {
            event.getDrops().clear();
            WoolType woolType = (WoolType)event.getEntity().getMetadata("sg-type").get(0).value();
            woolType.killSheep((Sheep)event.getEntity(), event.getEntity().getKiller());
            if (event.getEntity().getKiller() != null)
            {
                ItemStack itemStack = new ItemStack(Material.WOOL, 1, woolType.getDyeColor().getWoolData());
                ItemMeta itemMeta = itemStack.getItemMeta();
                itemMeta.setDisplayName(woolType.getChatColor() + "Mouton " + woolType.getName());
                itemStack.setItemMeta(itemMeta);

                event.getEntity().getKiller().getInventory().addItem(itemStack);
                event.getEntity().getKiller().sendMessage(ChatColor.YELLOW + "Vous avez récupéré : " + woolType.getChatColor() + "Mouton " + woolType.getName());
            }
        }
    }

    /**
     * Event method, to handle wool hit with arrows
     *
     * @param event Bukkit event instance
     */
    @SuppressWarnings("deprecation")
    @EventHandler
    public void onWoolShoot(ProjectileHitEvent event)
    {
        if (this.plugin.getGame().getStatus() != Status.IN_GAME || event.getEntity().getShooter() == null || !(event.getEntity().getShooter() instanceof Player))
            return ;
        BlockIterator blockIterator = new BlockIterator(event.getEntity().getWorld(), event.getEntity().getLocation().toVector(), event.getEntity().getVelocity().normalize(), 0.0D, 4);

        Block hitBlock = null;
        while (blockIterator.hasNext())
        {
            hitBlock = blockIterator.next();
            if (hitBlock.getType() != Material.AIR)
                break ;
        }

        final Block block = hitBlock;

        if (block != null && this.woolLocations.contains(block.getLocation()) && block.getType() == Material.WOOL)
        {
            WoolType woolType = this.woolTypes.stream().filter(type -> type.getDyeColor().getWoolData() == block.getData()).findFirst().orElse(null);
            event.getEntity().remove();
            if (woolType == null)
                return ;
            Player shooter = (Player)event.getEntity().getShooter();
            if (shooter.getInventory().firstEmpty() == -1)
            {
                shooter.sendMessage(ChatColor.RED + "Votre inventaire est plein.");
                return ;
            }

            block.setType(Material.AIR);
            ItemStack itemStack = new ItemStack(Material.WOOL, 1, woolType.getDyeColor().getWoolData());
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(woolType.getChatColor() + "Mouton " + woolType.getName());
            itemStack.setItemMeta(itemMeta);

            shooter.getInventory().addItem(itemStack);
            shooter.sendMessage(ChatColor.YELLOW + "Vous avez récupéré : " + woolType.getChatColor() + "Mouton " + woolType.getName());
        }
    }

    /**
     * Event method, to shoot sheeps
     *
     * @param event Bukkit event instance
     */
    @SuppressWarnings("deprecation")
    @EventHandler
    public void onWoolInteract(PlayerInteractEvent event)
    {
        if (this.plugin.getGame().getStatus() != Status.IN_GAME)
            return ;
        ItemStack itemStack;
        WoolType woolType;
        if (event.getHand() == EquipmentSlot.OFF_HAND)
            itemStack = event.getPlayer().getInventory().getItemInOffHand();
        else
            itemStack = event.getPlayer().getInventory().getItemInMainHand();
        ItemMeta meta;
        if (itemStack != null && itemStack.getType() == Material.WOOL && (meta = itemStack.getItemMeta()) != null && meta.getDisplayName() != null && meta.getDisplayName().contains("Mouton") &&
                (woolType = this.woolTypes.stream().filter(type -> type.getDyeColor().getWoolData() == itemStack.getDurability()).findFirst().orElse(null)) != null)
        {
            event.setCancelled(true);

            ItemStack newItemStack = new ItemStack(Material.WOOL, itemStack.getAmount() - 1, itemStack.getDurability());
            ItemMeta itemMeta = newItemStack.getItemMeta();
            itemMeta.setDisplayName(woolType.getChatColor() + "Mouton " + woolType.getName());
            newItemStack.setItemMeta(itemMeta);

            if (event.getHand() == EquipmentSlot.OFF_HAND)
                event.getPlayer().getInventory().setItemInOffHand(itemStack.getAmount() == 1 ? new ItemStack(Material.AIR) : newItemStack);
            else
                event.getPlayer().getInventory().setItemInMainHand(itemStack.getAmount() == 1 ? new ItemStack(Material.AIR) : newItemStack);
            woolType.launchSheep(event.getPlayer(), event.getPlayer().getLocation().getDirection().multiply(3D));
        }
    }

    /**
     * Event method to disable craft of wool blocks
     *
     * @param event Bukkit event instance
     */
    @EventHandler
    public void onPreCraft(PrepareItemCraftEvent event)
    {
        if (event.getRecipe().getResult().getType() == Material.WOOL)
            try
            {
                Reflection.setValue(event.getRecipe(), "output", new ItemStack(Material.AIR));
            }
            catch (Exception ex)
            {
                this.logger.log(Level.SEVERE, "Reflection error", ex);
            }
    }

    /**
     * Event method to disable craft of wool blocks
     *
     * @param event Bukkit event instance
     */
    @EventHandler
    public void onCraft(CraftItemEvent event)
    {
        if (event.getRecipe().getResult().getType() == Material.WOOL)
        {
            try
            {
                Reflection.setValue(event.getRecipe(), "output", new ItemStack(Material.AIR));
            }
            catch (Exception ex)
            {
                this.logger.log(Level.SEVERE, "Reflection error", ex);
            }
            event.setResult(Event.Result.DENY);
            event.setCancelled(true);
        }
    }

    /**
     * Security to disable creating fake sheep wools
     *
     * @param event Bukkit event instance
     */
    @EventHandler
    public void onRename(PrepareAnvilEvent event)
    {
        if (event.getResult().getItemMeta().getDisplayName().contains("Mouton"))
            event.setResult(new ItemStack(Material.AIR));
    }

    /**
     * Disable breeding sheeps
     *
     * @param event Bukkit event instance
     */
    @EventHandler
    public void onBreed(EntityBreedEvent event)
    {
        if (event.getEntity() instanceof Sheep)
            event.setCancelled(true);
    }

    /**
     * Cancel lightning and fire damage on thunder sheep
     *
     * @param event Bukkit event instance
     */
    @EventHandler
    public void onSheepDamage(EntityDamageEvent event)
    {
        if (event.getEntity() instanceof Sheep && event.getEntity().hasMetadata("sg-type") && (event.getCause() == EntityDamageEvent.DamageCause.FALL
                || (event.getEntity().getMetadata("sg-type").get(0).value() instanceof ThunderSheep
                && Arrays.asList(EntityDamageEvent.DamageCause.FIRE, EntityDamageEvent.DamageCause.FIRE_TICK, EntityDamageEvent.DamageCause.LIGHTNING).contains(event.getCause()))))
                event.setCancelled(true);
    }
}
