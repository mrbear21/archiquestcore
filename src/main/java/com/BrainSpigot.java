package com;

import java.util.HashMap;
import java.util.List;

import commands.EnderchestCommand;
import commands.EssentialCommands;
import commands.ServerCommand;
import commands.TeleportCommands;
import fun.DoubleJump;
import integrations.AureliumSkillsAPI;
import integrations.AuthmeAPI;
import integrations.Placeholders;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import listeners.SpigotListeners;
import listeners.SystemMessageReceiver;
import modules.Chat;
import objects.BreadMaker;


public class BrainSpigot extends JavaPlugin {
	
	public HashMap<String, String> RECEIVEDMESSAGES = new HashMap<String, String>();
	public HashMap<String, HashMap<String, String>> locales = new HashMap<String, HashMap<String,String>>();
	public HashMap<String, String[]> playerdata = new HashMap<String, String[]>();
	public HashMap<String, Integer> runnableTasks = new HashMap<String, Integer>();
	public HashMap<String, List<String[]>> chathistory = new HashMap<String, List<String[]>>();
	public int MESSAGE_ID = 0;
	
	@Override
	public void onEnable() {

		getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
		getServer().getMessenger().registerOutgoingPluginChannel(this, "net:archiquest");
		getServer().getMessenger().registerIncomingPluginChannel(this, "net:archiquest", new SystemMessageReceiver(this));

		new Chat(this).initialize();
		new Placeholders(this).register();
		new AureliumSkillsAPI(this).initialize();
		new AuthmeAPI(this).initialize();

		new ServerCommand(this).register();
		new EssentialCommands(this).register();
		new TeleportCommands(this).register();
		
		getCommand("enderchest").setExecutor(new EnderchestCommand(this));

		Bukkit.getPluginManager().registerEvents(new SpigotListeners(this), this);
		Bukkit.getPluginManager().registerEvents(new DoubleJump(this), this);
		
		for (Player p : Bukkit.getOnlinePlayers()) {
			new SystemMessage(this).newMessage("playerdata", new String[] {"get", p.getName()});
		}
		
		getLogger().info("archiquestcore is ready to be helpful for all beadmakers!");
	}

	public void onDisable() {
		
		getLogger().info("archiquestcore has stopped it's service!");

	}

	public void log(String string) {
		getLogger().info(string);
	}

	public BreadMaker getBread(String player) {
		return new BreadMaker(this, player);
	}
	
}


