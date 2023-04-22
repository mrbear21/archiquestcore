package brain;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import commands.*;
import fr.xephi.authme.api.v3.AuthMeApi;
import handlers.*;
import integrations.ArchiQuestAPI;
import integrations.AureliumSkillsAPI;
import integrations.AuthmeAPI;
import integrations.Placeholders;
import integrations.PlotSquaredAPI;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.boss.BossBar;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import modules.ChatManager;
import modules.ExploitsFixes;
import modules.Localizations;
import modules.RepeatingTasks;
import modules.SystemMessages;
import objects.BreadMaker;
import objects.MenuBuilder;


public class BrainSpigot extends JavaPlugin {
	
	public HashMap<String, String> RECEIVEDMESSAGES = new HashMap<String, String>();
	public HashMap<String, HashMap<String, String>> locales = new HashMap<String, HashMap<String,String>>();
	public HashMap<String, String[]> playerdata = new HashMap<String, String[]>();
	public HashMap<String, Integer> runnableTasks = new HashMap<String, Integer>();
	public HashMap<String, List<String[]>> chathistory = new HashMap<String, List<String[]>>();
	public HashMap<String, HashMap<String, Long>> cooldowns = new HashMap<String, HashMap<String, Long>>();
	public HashMap<Player, ItemStack[]> inventorySaves = new HashMap<Player, ItemStack[]>();
	public HashMap<Player, ItemStack[]> ArmorSaves = new HashMap<Player, ItemStack[]>();
	public HashMap<Player, HashMap<String, String>> pressfactions = new HashMap<Player, HashMap<String, String>>();
	public HashMap<Player, BossBar> bossbars = new HashMap<Player, BossBar>();
	public HashMap<String, Location> marrigePoints = new HashMap<String, Location>();
	public HashMap<Player, String> name = new HashMap<Player, String>();
	public HashMap<Player, String[]> optionNames = new HashMap<Player, String[]>();
	public HashMap<Player, ItemStack[]> optionIcons = new HashMap<Player, ItemStack[]>();
	public HashMap<Player, HashMap<Integer, String[]>> optionCommands = new HashMap<Player, HashMap<Integer, String[]>>();
	public HashMap<Player, Location> doublejump = new HashMap<Player, Location>();
	
	public Player chatquestion;
	public AuthMeApi authMeApi;
	public int MESSAGE_ID = 0;
	private BrainSpigot instance;
	
	public FileConfiguration localesFile = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "locales.yml"));
	public static int version = 0;
	public HashMap<String, String> npccommands = new HashMap<String, String>();
	
	@Override
	public void onEnable() {

		if (!new File(getDataFolder(), "locales.yml").exists()) {
			getLogger().info("creating locales file...");
			try {
				saveResource("locales.yml", false);
			} catch (Exception c) { c.printStackTrace(); }
		}
		
		if (!new File(getDataFolder(), "config.yml").exists()) {
			getLogger().info("creating config file...");
			try {
				saveResource("config.yml", false);
			} catch (Exception c) { c.printStackTrace(); }
		}
		
		if (getServer().getPluginManager().isPluginEnabled("ProtocolLib")) {
			new ChatManager(this).register();
		}
		if (getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")) {
			new Placeholders(this).register();
		}
		
		new Localizations(this).initialise();
		new AureliumSkillsAPI(this).initialize();
		new AuthmeAPI(this).initialize();
		new PlotSquaredAPI(this).register();
		new LanguageCommand(this).register();
		new ServerCommand(this).register();
		new EssentialCommands(this).register();
		new AutoArmorEquipHandler(this).register();
		new BetterTeleportCommands(this).register();
		new RepeatingTasks(this).start();
		new PlayerWarpsCommands(this).register();
		new ArchiQuestCommand(this).register();
		new NPCCmdHandler(this).register();
		new PlayerWarpsSignHandler(this).register();
		new PlayerClickListener(this).register();
		new PlayerSettingsCommand(this).register();
		new HintListener(this).register();
		new PressFKeyHandler(this).register();
		new ExploitsFixes(this).register();	
		
		version = Integer.valueOf(Bukkit.getBukkitVersion().split("-")[0].substring(2, Bukkit.getBukkitVersion().split("-")[0].length()-2));
		
		System.out.print("server version: "+version);
		
		if (version > 12) {
			new ElevatorHandler(this).register();
			new ItemFrameHandler(this).register();
			new GradientCommand(this).register();
			new GradientSpecialCommand(this).register();
			new GradientMenuHandler(this).register();		
			try {
				if (getConfig().getString("npc-commands") != null) {
					for (String key : getConfig().getConfigurationSection("npc-commands").getKeys(false)) {
						npccommands.put(ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', key)),
								getConfig().getString("npc-commands." + key));
					}
					getLogger().info("loaded " + npccommands.size() + " NPC commands");
				}
			
				if (getConfig().get("openworld") != null && getConfig().getBoolean("openworld")) {
					new WorldCreator("openworld").generateStructures(false).environment(Environment.NORMAL).type(WorldType.LARGE_BIOMES).createWorld();
				}
			} catch (Exception c) {
				log(c.getCause().toString());
			}
		}
		
		ArchiQuestAPI.register(this);

		Bukkit.getPluginManager().registerEvents(new SpigotListeners(this), this);
		Bukkit.getPluginManager().registerEvents(new DoubleJumpHandler(this), this);
		Bukkit.getPluginManager().registerEvents(new MenuBuilder(this), this);
		
		for (Player p : Bukkit.getOnlinePlayers()) {
			new SystemMessages(this).newMessage("playerdata", new String[] {"get", p.getName()});
	    	BreadMaker bread = getBread(p.getName());
			bread.setData("loggedin", "true");
		}
		
		instance = this;

		getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
		getServer().getMessenger().registerOutgoingPluginChannel(this, "net:archiquest");
		getServer().getMessenger().registerIncomingPluginChannel(this, "net:archiquest", new SystemMessagesListener(this));
		
		getLogger().info("archiquestcore is ready to be helpful for all beadmakers!");

		if (getServer().getPluginManager().isPluginEnabled("archiquestextra")) {
			Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "plugman reload archiquestextra");
		}
		
	}

	public void onDisable() {
		
		bossbars.entrySet().stream().forEach(p -> p.getValue().removePlayer(p.getKey()));
		new RepeatingTasks(this).stop();
		try { getLocalesFile().save(new File(getDataFolder(), "locales.yml"));
			saveConfig();
		} catch (IOException e) { e.printStackTrace(); }
		getLogger().info("archiquestcore has stopped it's service!");

	}

	public void log(String string) {
		getLogger().info(string);
		for (Player p : Bukkit.getOnlinePlayers().stream().filter(p -> p.hasPermission("archiquest.sudo")).collect(Collectors.toList())) {
			p.sendMessage(ChatColor.GRAY+""+ChatColor.ITALIC+"[Console] "+string);
		}
	}

	public BreadMaker getBread(String player) {
		return new BreadMaker(this, player);
	}
	
	public FileConfiguration getLocalesFile() {
		return localesFile;
	}
	
	public BrainSpigot getInstance() {
		return instance;
	}
}


