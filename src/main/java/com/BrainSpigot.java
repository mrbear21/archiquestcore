package com;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import commands.EnderchestCommand;
import commands.EssentialCommands;
import commands.PlayerWarpsCommands;
import commands.ServerCommand;
import commands.TeleportCommands;
import fun.DoubleJump;
import integrations.AureliumSkillsAPI;
import integrations.AuthmeAPI;
import integrations.Placeholders;
import integrations.PlotSquaredAPI;

import org.bukkit.Bukkit;
import org.bukkit.boss.BossBar;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import listeners.SpigotListeners;
import listeners.SystemMessageReceiver;
import modules.Chat;
import modules.Locales;
import modules.RepeatingTasks;
import objects.BreadMaker;


public class BrainSpigot extends JavaPlugin {
	
	public HashMap<String, String> RECEIVEDMESSAGES = new HashMap<String, String>();
	public HashMap<String, HashMap<String, String>> locales = new HashMap<String, HashMap<String,String>>();
	public HashMap<String, String[]> playerdata = new HashMap<String, String[]>();
	public HashMap<String, Integer> runnableTasks = new HashMap<String, Integer>();
	public HashMap<String, List<String[]>> chathistory = new HashMap<String, List<String[]>>();
	public int MESSAGE_ID = 0;
	public HashMap<String, HashMap<String, Long>> cooldowns = new HashMap<String, HashMap<String, Long>>();
	public HashMap<Player, ItemStack[]> inventorySaves = new HashMap<Player, ItemStack[]>();
	public HashMap<Player, ItemStack[]> ArmorSaves = new HashMap<Player, ItemStack[]>();
	public HashMap<Player, List<String>> pressfactions = new HashMap<Player, List<String>>();
	public HashMap<Player, BossBar> bossbars = new HashMap<Player, BossBar>();

	public List<String> doublejump = new ArrayList<String>();
	public int repeatingtask = 0;
	public Player chatquestion;
	
	public FileConfiguration localesFile = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "locales.yml"));
	
	@Override
	public void onEnable() {

		getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
		getServer().getMessenger().registerOutgoingPluginChannel(this, "net:archiquest");
		getServer().getMessenger().registerIncomingPluginChannel(this, "net:archiquest", new SystemMessageReceiver(this));

		if (!new File(getDataFolder(), "locales.yml").exists()) {
			getLogger().info("Creating locales file...");
			try {
				saveResource("locales.yml", false);
			} catch (Exception c) { c.printStackTrace(); }
		}

		new Locales(this).initialise();
		new Chat(this).register();
		if (getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")) {
		new Placeholders(this).register(); }
		new AureliumSkillsAPI(this).initialize();
		new AuthmeAPI(this).initialize();
		new PlotSquaredAPI(this).register();

		new ServerCommand(this).register();
		new EssentialCommands(this).register();
		new TeleportCommands(this).register();
		new RepeatingTasks(this).start();
		new PlayerWarpsCommands(this).register();
		
		getCommand("enderchest").setExecutor(new EnderchestCommand(this));

		Bukkit.getPluginManager().registerEvents(new SpigotListeners(this), this);
		Bukkit.getPluginManager().registerEvents(new DoubleJump(this), this);
		
		for (Player p : Bukkit.getOnlinePlayers()) {
			new SystemMessage(this).newMessage("playerdata", new String[] {"get", p.getName()});
		}
		
		getLogger().info("archiquestcore is ready to be helpful for all beadmakers!");
	}

	public void onDisable() {
		bossbars.entrySet().stream().forEach(p -> p.getValue().removePlayer(p.getKey()));
		new RepeatingTasks(this).stop();
		try {
			getLocalesFile().save(new File(getDataFolder(), "locales.yml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		getLogger().info("archiquestcore has stopped it's service!");

	}

	public void log(String string) {
		getLogger().info(string);
	}

	public BreadMaker getBread(String player) {
		return new BreadMaker(this, player);
	}
	
	public FileConfiguration getLocalesFile() {
		return localesFile;
	}
}


