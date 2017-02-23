package net.samagames.plagiat.game;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import net.samagames.api.games.Game;
import net.samagames.api.games.GamePlayer;
import net.samagames.api.games.IGameProperties;
import net.samagames.api.games.Status;
import net.samagames.plagiat.Plagiat;
import net.samagames.plagiat.modules.AbstractModule;
import net.samagames.plagiat.modules.gravity.GravityModule;
import net.samagames.plagiat.modules.quake.QuakeModule;
import net.samagames.plagiat.modules.rush.RushModule;
import net.samagames.plagiat.modules.sheepwars.SheepWarsModule;
import net.samagames.plagiat.modules.splegg.SpleggModule;
import net.samagames.plagiat.modules.ultralucky.UltraLuckyModule;
import net.samagames.tools.Area;
import net.samagames.tools.LocationUtils;
import net.samagames.tools.Titles;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.logging.Level;

/**
 * Plagiat Game class
 */
public class PlagiatGame extends Game<PlagiatPlayer>
{
    protected Plagiat plugin;
    protected List<AbstractModule> modules;
    private Location lobby;
    private Area lobbyArea;
    private boolean damagesActivated;
    private boolean buildActivated;
    private List<PlagiatChest> chests;
    private boolean insane;
    List<Location> spawns;

    /**
     * PlagiatGame constructor
     *
     * @param plugin Plagiat's plugin instance
     */
    public PlagiatGame(Plagiat plugin, boolean insane)
    {
        super("plagiat", "Plagiat" + (insane ? ChatColor.RED + " " + ChatColor.BOLD + "INSANE" : ""), "CTRL-C -> CTRL-V", PlagiatPlayer.class);
        this.plugin = plugin;
        this.modules = new ArrayList<>();
        this.spawns = new ArrayList<>();
        this.chests = new ArrayList<>();
        this.damagesActivated = false;
        this.buildActivated = false;
        this.insane = insane;

        this.gameManager.getGameProperties().getConfig("spawns", new JsonArray()).getAsJsonArray().forEach(element -> this.spawns.add(LocationUtils.str2loc(element.getAsString())));
        this.gameManager.getGameProperties().getConfig("chests", new JsonArray()).getAsJsonArray().forEach(element -> this.chests.add(PlagiatChest.fromString(this.plugin, element.getAsString())));
        this.lobby = LocationUtils.str2loc(this.gameManager.getGameProperties().getConfig("lobby", new JsonPrimitive("world, 0, 128, 0")).getAsString());
        this.lobbyArea = Area.str2area(this.gameManager.getGameProperties().getConfig("lobbyArea", new JsonPrimitive("world, -10, 120, -10, 10, 128, 10")).getAsString());

        this.plugin.getServer().getScheduler().runTaskLater(this.plugin, () -> this.chests.forEach(plagiatChest -> plagiatChest.generate(this.insane)), 10L);

        this.plugin.getServer().getWorlds().get(0).setStorm(false);
    }

    /**
     * Register all modules in the game
     */
    public void registerModules()
    {
        this.registerModule(SheepWarsModule.class);
        this.registerModule(QuakeModule.class);
        this.registerModule(GravityModule.class);
        //TODO this.registerModule(DimensionsModule.class);
        this.registerModule(SpleggModule.class);
        this.registerModule(RushModule.class);
        this.registerModule(UltraLuckyModule.class);
        //TODO this.registerModule(DragonEscapeModule.class);
        //TODO this.registerModule(SplatoonModule.class);
        //TODO this.registerModule(WallsModule.class);
    }

    /**
     * Register a module in the game, checking if disabled in configuration
     *
     * @param moduleClass Module class
     */
    private void registerModule(Class<? extends AbstractModule> moduleClass)
    {
        try
        {
            IGameProperties gameProperties = this.gameManager.getGameProperties();
            JsonArray blacklist = gameProperties.getConfig("disableModules", new JsonArray()).getAsJsonArray();
            boolean ok = true;
            for (JsonElement element : blacklist)
                if (element.getAsString().equalsIgnoreCase(moduleClass.getSimpleName()))
                    ok = false;
            if (ok)
                this.modules.add(moduleClass.getConstructor(Plagiat.class).newInstance(this.plugin));
        }
        catch (Exception ex)
        {
            this.plugin.getLogger().log(Level.SEVERE, "Error loading module " + moduleClass.getName(), ex);
        }
    }

    /**
     * Event method overridden to handle start of the game
     */
    @Override
    public void startGame()
    {
        super.startGame();
        this.giveKits();
        this.teleport();
        this.destroyLobby();
        this.modules.forEach(AbstractModule::handleGameStart);
        new BukkitRunnable()
        {
            int n = 5;
            @Override
            public void run()
            {
                String msg = ChatColor.GOLD + ChatColor.BOLD.toString() + this.n;
                PlagiatGame.this.getCoherenceMachine().getMessageManager().writeCustomMessage(ChatColor.YELLOW + "Début dans " + this.n + " seconde" + (this.n == 1 ? "" : "s"), true);
                PlagiatGame.this.plugin.getServer().getOnlinePlayers().forEach(player -> Titles.sendTitle(player, 1, 18, 1, msg, ""));
                this.n--;
                if (this.n == 0)
                    this.cancel();
            }
        }.runTaskTimer(this.plugin, 100L, 20L);
        this.plugin.getServer().getScheduler().runTaskLater(this.plugin, () ->
        {
            this.destroyCages();
            this.buildActivated = true;
            this.plugin.getServer().getScheduler().runTaskLater(this.plugin, () -> this.damagesActivated = true, 40L);
            this.getInGamePlayers().forEach((uuid, plagiatPlayer) ->
            {
                Player player = plagiatPlayer.getPlayerIfOnline();
                if (player != null)
                    player.setGameMode(GameMode.SURVIVAL);
            });
        }, 200L);
    }

    /**
     * Give kits to players
     */
    private void giveKits()
    {
        // TODO
        this.getInGamePlayers().values().forEach(plagiatPlayer -> plagiatPlayer.getPlayerIfOnline().getInventory().clear());
    }

    /**
     * Teleport players to their spawn
     * Reimplement it to handle team system
     */
    protected void teleport()
    {
        List<PlagiatPlayer> players = new ArrayList<>(this.getInGamePlayers().values());
        Iterator<Location> iterator = this.spawns.iterator();
        players.forEach(plagiatPlayer ->
        {
            if (!iterator.hasNext())
                this.gameManager.kickPlayer(plagiatPlayer.getPlayerIfOnline(), "Il n'y a plus de place dans la partie.");
            else
            {
                Location location = iterator.next();
                this.createCage(plagiatPlayer.getCage(), location);
                plagiatPlayer.getPlayerIfOnline().teleport(location);
            }
        });
    }

    /**
     * Destroy all cages
     */
    private void destroyCages()
    {
        this.spawns.forEach(location ->
        {
            for (int i = -1; i < 4; i++)
                for (int j = -1; j <= 1; j++)
                    for (int k = -1; k <= 1; k++)
                            location.getWorld().getBlockAt(location.getBlockX() + j, location.getBlockY() + i, location.getBlockZ() + k).setType(Material.AIR);
            location.getWorld().getBlockAt(location.getBlockX(), location.getBlockY() - 1, location.getBlockZ()).setType(Material.AIR);
            location.getWorld().getBlockAt(location.getBlockX(), location.getBlockY() + 3, location.getBlockZ()).setType(Material.AIR);
        });
    }

    /**
     * Respawn a player far away from battles
     * Need two arguments for optimisation
     *
     * @param player Bukkit player instance
     * @param plagiatPlayer Plagiat player instance
     */
    @SuppressWarnings("UnusedParameters") //TODO: Teams
    public void respawnPlayer(Player player, PlagiatPlayer plagiatPlayer)
    {
        Location better = null;
        double distance = -1;
        for (Location spawn : this.spawns)
        {
            if (better == null)
                better = spawn;
            else
            {
                double d = -1;
                double tmp;
                for (PlagiatPlayer player2 : this.getInGamePlayers().values())
                    if ((tmp = player2.getPlayerIfOnline().getLocation().distanceSquared(spawn)) < d || d == -1)
                        d = tmp;
                if (distance == -1 || d > distance)
                {
                    distance = d;
                    better = spawn;
                }
            }
        }
        player.teleport(better);
    }

    /**
     * Creates a small cage at given location
     *
     * @param cage Type of the cage
     * @param location Base location of the cage
     */
    void createCage(EnumCage cage, Location location)
    {
        for (int i = -1; i < 4; i++)
            for (int j = -1; j <= 1; j++)
                for (int k = -1; k <= 1; k++)
                    if (j != 0 || k != 0)
                        cage.set(location.getWorld().getBlockAt(location.getBlockX() + j, location.getBlockY() + i, location.getBlockZ() + k));
        cage.set(location.getWorld().getBlockAt(location.getBlockX(), location.getBlockY() - 1, location.getBlockZ()));
        cage.set(location.getWorld().getBlockAt(location.getBlockX(), location.getBlockY() + 3, location.getBlockZ()));
    }

    /**
     * Handle player login, load his cages
     *
     * @param player Bukkit player instance
     */
    @Override
    public void handleLogin(Player player)
    {
        super.handleLogin(player);
        this.plugin.getServer().getScheduler().runTaskAsynchronously(this.plugin, () -> this.getPlayer(player.getUniqueId()).loadCage());
        player.teleport(this.lobby);
        player.getInventory().clear();
    }

    /**
     * Destroy lobby zone
     */
    private void destroyLobby()
    {
        Location min = this.lobbyArea.getMin();
        for (int i = 0; i <= this.lobbyArea.getSizeX(); i++)
            for (int j = 0; j <= this.lobbyArea.getSizeY(); j++)
                for (int k = 0; k <= this.lobbyArea.getSizeZ(); k++)
                    min.getWorld().getBlockAt(min.getBlockX() + i, min.getBlockY() + j, min.getBlockZ() + k).setType(Material.AIR);
    }

    /**
     * Get whether damages are activated or not
     *
     * @return {@code true} if damages are activated
     */
    public boolean areDamagesActivated()
    {
        return this.damagesActivated;
    }

    /**
     * Get whether build is activated or not
     *
     * @return {@code true} if build is activated
     */
    public boolean isBuildActivated()
    {
        return this.buildActivated;
    }

    /**
     * Get whether this game is insane or not
     *
     * @return {@code true} if game is insane
     */
    public boolean isInsane()
    {
        return this.insane;
    }

    /**
     * Stump a player
     *
     * @param plagiatPlayer Plagiat player instance
     * @param player Bukkit player instance
     * @param logout {@code true} if this player has disconnected
     */
    public void stumpPlayer(PlagiatPlayer plagiatPlayer, Player player, boolean logout)
    {
        plagiatPlayer.setSpectator();
        player.setMaxHealth(20D);
        player.setHealth(20D);
        player.setGameMode(GameMode.ADVENTURE);
        player.setAllowFlight(true);
        player.setFlying(true);
        player.spigot().respawn();
        player.setExp(0F);
        player.setLevel(0);
        player.getInventory().clear();
        player.setVelocity(new Vector().zero());
        player.playSound(player.getLocation(), Sound.ENTITY_WITHER_DEATH, 1F, 1F);

        //TODO Death Message
        if (player.getKiller() != null)
            player.getKiller().playSound(player.getKiller().getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1F, 1F);
        if (logout) //Do not give spectator inventory to disconnected player, to reduce CPU usage
            return ;
        {
            ItemStack itemStack = new ItemStack(Material.COMPASS);
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(ChatColor.GREEN + ChatColor.BOLD.toString() + "Téléporteur" + ChatColor.GRAY + " (Clic droit)");
            itemMeta.setLore(Collections.singletonList("Clic droit pour vous téléporter aux autres joueurs"));
            itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            itemStack.setItemMeta(itemMeta);
            player.getInventory().setItem(0, itemStack);
        }

        {
            ItemStack itemStack = new ItemStack(Material.REDSTONE_COMPARATOR);
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(ChatColor.AQUA + ChatColor.BOLD.toString() + "Paramètres" + ChatColor.GRAY + " (Clic droit)");
            itemMeta.setLore(Collections.singletonList("Clic droit pour régler vos paramètres"));
            itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            itemStack.setItemMeta(itemMeta);
            player.getInventory().setItem(0, itemStack);
        }

        {
            ItemStack itemStack = new ItemStack(Material.PAPER);
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(ChatColor.AQUA + ChatColor.BOLD.toString() + "Rejouer" + ChatColor.GRAY + " (Clic droit)");
            itemMeta.setLore(Collections.singletonList("Clic droit pour rejouer"));
            itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            itemStack.setItemMeta(itemMeta);
            player.getInventory().setItem(0, itemStack);
        }

        {
            ItemStack itemStack = new ItemStack(Material.BED);
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(ChatColor.AQUA + ChatColor.BOLD.toString() + "Quitter" + ChatColor.GRAY + " (Clic droit)");
            itemMeta.setLore(Collections.singletonList("Clic droit pour revenir au hub"));
            itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            itemStack.setItemMeta(itemMeta);
            player.getInventory().setItem(0, itemStack);
        }

        this.checkEnd(false);
    }

    /**
     * Check if game should end
     *
     * @param forceEnd If game should end anyway
     */
    void checkEnd(boolean forceEnd)
    {
        Map<UUID, PlagiatPlayer> playerMap = this.getInGamePlayers();
        if (playerMap.size() > 1 && !forceEnd)
            return;

        this.damagesActivated = false;
        this.buildActivated = false;

        GamePlayer gamePlayer = playerMap.size() != 1 ? null : playerMap.entrySet().iterator().next().getValue();
        Player player = gamePlayer == null ? null : gamePlayer.getPlayerIfOnline();
        this.getCoherenceMachine().getMessageManager().writeCustomMessage(gamePlayer == null ? ChatColor.YELLOW + "Personne ne remporte la partie." : (player == null ? gamePlayer.getOfflinePlayer().getName() : player.getDisplayName()) + ChatColor.YELLOW + " remporte la partie !", true);

        if (gamePlayer != null)
            this.handleWinner(gamePlayer.getUUID());

        if (player != null)
            this.effectsOnWinner(player);

        this.plugin.getServer().getScheduler().runTaskLater(this.plugin, this::handleGameEnd, 100L);
    }

    /**
     * Handle player logout
     *
     * @param player Bukkit player instance
     */
    @Override
    public void handleLogout(Player player)
    {
        PlagiatPlayer plagiatPlayer = this.getPlayer(player.getUniqueId());
        super.handleLogout(player);
        if (plagiatPlayer != null && this.status == Status.IN_GAME)
            this.stumpPlayer(plagiatPlayer, player, true);
    }
}
