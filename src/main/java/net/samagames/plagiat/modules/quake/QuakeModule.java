package net.samagames.plagiat.modules.quake;

import net.minecraft.server.v1_10_R1.AxisAlignedBB;
import net.minecraft.server.v1_10_R1.BlockPosition;
import net.minecraft.server.v1_10_R1.IBlockAccess;
import net.minecraft.server.v1_10_R1.IBlockData;
import net.minecraft.server.v1_10_R1.IChatBaseComponent;
import net.minecraft.server.v1_10_R1.PacketPlayOutChat;
import net.minecraft.server.v1_10_R1.Vec3D;
import net.samagames.api.games.Status;
import net.samagames.plagiat.Plagiat;
import net.samagames.plagiat.game.PlagiatPlayer;
import net.samagames.plagiat.modules.AbstractModule;
import net.samagames.plagiat.modules.MCServer;
import net.samagames.tools.ParticleEffect;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_10_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftPlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Quake module class
 */
public class QuakeModule extends AbstractModule
{
    private List<UUID> cooldown;
    private ItemStack hoe;

    /**
     * Quake module constructor
     *
     * @param plugin Plagiat's plugin instance
     */
    public QuakeModule(Plagiat plugin)
    {
        super(plugin, "quake", MCServer.HYPIXEL);
        this.cooldown = new ArrayList<>();

        this.hoe = new ItemStack(Material.DIAMOND_HOE);
        ItemMeta itemMeta = this.hoe.getItemMeta();
        itemMeta.setDisplayName("Bling bling Thing " + this.getServerSuffix());
        itemMeta.addEnchant(Enchantment.DURABILITY, 1, true);
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        this.hoe.setItemMeta(itemMeta);
    }

    /**
     * Detect interact with entity, getting ray-gun from armorstand or shooting if ray-gun used
     *
     * @param event Bukkit event instance
     */
    @EventHandler
    public void onEntityInteract(PlayerInteractAtEntityEvent event)
    {
        if (this.plugin.getGame().getStatus() != Status.IN_GAME)
            return ;
        ItemStack itemStack;
        if (event.getHand() == EquipmentSlot.OFF_HAND)
            itemStack = event.getPlayer().getInventory().getItemInOffHand();
        else
            itemStack = event.getPlayer().getInventory().getItemInMainHand();
        if (itemStack != null && itemStack.equals(this.hoe))
        {
            event.setCancelled(true);
            this.shoot(event.getPlayer());
        }
        else if (event.getRightClicked() instanceof ArmorStand
                && ((ArmorStand)event.getRightClicked()).getItemInHand() != null
                && ((ArmorStand)event.getRightClicked()).getItemInHand().getType() == Material.DIAMOND_HOE)
        {
            if (event.getPlayer().getInventory().firstEmpty() == -1)
            {
                event.getPlayer().sendMessage(ChatColor.RED + "Votre inventaire est plein.");
                return ;
            }
            event.getRightClicked().remove();
            event.setCancelled(true);
            event.getPlayer().getInventory().addItem(this.hoe);
            event.getPlayer().sendMessage(ChatColor.GOLD + "Vous avez maintenant le " + ChatColor.AQUA + "Bling bling Thing" + ChatColor.GOLD + ". Tirez sur votre ennemis pour les pousser dans le vide.");
            event.getPlayer().getWorld().playSound(event.getPlayer().getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1F, 1F);
        }
    }

    /**
     * Detect interact, shooting if ray-gun used
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
        if (itemStack != null && itemStack.equals(this.hoe))
        {
            event.setCancelled(true);
            this.shoot(event.getPlayer());
        }
    }

    /**
     * Detect hand damage with ray-gun, and cancel it
     *
     * @param event Bukkit event instance
     */
    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event)
    {
        if (this.plugin.getGame().getStatus() != Status.IN_GAME || !(event.getDamager() instanceof Player))
            return ;
        ItemStack itemStack = ((Player)event.getDamager()).getInventory().getItemInMainHand();
        if (itemStack != null && itemStack.equals(this.hoe))
        {
            event.setCancelled(true);
            this.shoot((Player)event.getDamager());
            this.plugin.getServer().getScheduler().runTask(this.plugin, () -> itemStack.setDurability((short)0));
        }
    }

    /**
     * Player shoot with ray-gun
     *
     * @param player The shooting player
     */
    private void shoot(Player player)
    {
        if (this.cooldown.contains(player.getUniqueId()))
            return ;

        this.cooldown.add(player.getUniqueId());
        this.plugin.getServer().getScheduler().runTaskLater(this.plugin, () -> this.cooldown.remove(player.getUniqueId()), 180L);

        for (int i = 0; i < 9; ++i)
        {
            final int j = i;
            this.plugin.getServer().getScheduler().runTaskLater(this.plugin, () ->
            {
                String msg = ChatColor.RED + "Rechargement : " + ChatColor.GRAY + "[" + ChatColor.RED;

                for (int k = 0; k < j; ++k)
                    msg += "□";

                for (int k = 0; k < (8 - j); k++)
                    msg += " ";

                msg += ChatColor.GRAY + "]";

                this.sendActionBarMessage(player, msg);
            }, j * 20L);
            this.plugin.getServer().getScheduler().runTaskLater(this.plugin, () -> this.sendActionBarMessage(player, ""), 179L);
        }

        List<Player> players = this.getTargetV3(player, 100, 1.5D);
        if (!players.isEmpty())
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1F, 1F);
        players.forEach(victim -> victim.setVelocity(player.getLocation().getDirection()));
    }

    /**
     * Send an action bar message to a player
     *
     * @param player Bukkit Player instance
     * @param message Message to send
     */
    private void sendActionBarMessage(Player player, String message)
    {
        if (!(player instanceof CraftPlayer))
            return ;

        PacketPlayOutChat packetPlayOutChat = new PacketPlayOutChat(IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + message.replaceAll("\"", "\\\"") + "\"}"), (byte) 2);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packetPlayOutChat);
    }

    /**
     * Calculates players that should be shot by ray
     * (almost copied from SamaGames's Quake, just some adaptation has been made)
     *
     * @param player The shooting player
     * @param maxRange Max distance in blocks
     * @param aiming Hitbox multiplier
     * @return Targets as list
     */
    @SuppressWarnings("deprecation")
    private List<Player> getTargetV3(Player player, int maxRange, double aiming)
    {
        List<Player> target = new ArrayList<>();
        Location playerEyes = player.getEyeLocation();

        final Vector direction = playerEyes.getDirection().normalize();

        List<Player> targets = new ArrayList<>();
        for (PlagiatPlayer app : this.plugin.getGame().getInGamePlayers().values())
        {
            Player online = app.getPlayerIfOnline();
            if (online != null && online != player && online.getLocation().distanceSquared(playerEyes) < maxRange * maxRange)
                targets.add(online);
        }

        Block block;
        Location loc = playerEyes.clone();
        Location testLoc;
        double lx, ly, lz;
        double px, py, pz;
        Vector progress = direction.clone().multiply(0.70);
        maxRange = (100 * maxRange / 70);

        int loop = 0;
        while (loop < maxRange)
        {
            loop++;
            loc.add(progress);
            block = loc.getBlock();
            if (!block.getType().isTransparent())
            {
                net.minecraft.server.v1_10_R1.World w = ((CraftWorld)block.getWorld()).getHandle();

                BlockPosition var21 = new BlockPosition(block.getX(), block.getY(), block.getZ());
                IBlockData iblockdata = w.getType(var21);
                net.minecraft.server.v1_10_R1.Block b = iblockdata.getBlock();

                b.h(w, var21);
                AxisAlignedBB axis = b.a(iblockdata, (IBlockAccess)w, var21);
                if (axis != null)
                {
                    AxisAlignedBB vec3d = new AxisAlignedBB(axis.a + block.getX(), axis.b + block.getY(), axis.c + block.getZ(), axis.d + block.getX(), axis.e + block.getY(), axis.f + block.getZ());
                    vec3d = vec3d.grow(0.1F, 0.1F, 0.1F);
                    if (vec3d.a(new Vec3D(loc.getX(), loc.getY(), loc.getZ())))
                        break;
                }
            }
            lx = loc.getX();
            ly = loc.getY();
            lz = loc.getZ();

            ParticleEffect.FIREWORKS_SPARK.display(0.07F, 0.04F, 0.07F, 0.00005F, 1, loc, 75);

            for (PlagiatPlayer app : this.plugin.getGame().getInGamePlayers().values())
            {
                Player apa = app.getPlayerIfOnline();
                if (apa != null && apa.getLocation().getWorld() == loc.getWorld() && apa.getLocation().distance(loc) < 30 && loop % 10 == 0)
                    apa.getWorld().playSound(apa.getLocation(), Sound.ENTITY_FIREWORK_LAUNCH, 0.042F, 0.01F);
            }

            for (Player possibleTarget : targets)
            {
                if (possibleTarget.getUniqueId() == player.getUniqueId())
                    continue;
                testLoc = possibleTarget.getLocation().add(0, 0.85, 0);
                px = testLoc.getX();
                py = testLoc.getY();
                pz = testLoc.getZ();

                boolean dX = Math.abs(lx - px) < 0.70 * aiming;
                boolean dY = Math.abs(ly - py) < 1 * aiming;
                boolean dZ = Math.abs(lz - pz) < 0.70 * aiming;

                if (dX && dY && dZ && !target.contains(possibleTarget))
                        target.add(possibleTarget);
            }
        }

        return target;
    }
}
