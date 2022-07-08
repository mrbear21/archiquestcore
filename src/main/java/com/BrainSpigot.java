package com;

import java.util.HashMap;

import commands.EnderchestCommand;
import commands.TeleportCommands;
import integrations.AureliumSkillsAPI;
import integrations.AuthmeAPI;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import listeners.Placeholders;
import listeners.SystemMessageReceiver;
import modules.Chat;


public class BrainSpigot extends JavaPlugin {
	
	public HashMap<String, String> RECEIVEDMESSAGES = new HashMap<String, String>();
	public HashMap<String, HashMap<String, String>> locales = new HashMap<String, HashMap<String,String>>();
	public HashMap<String, String[]> playerdata = new HashMap<String, String[]>();
	
	@Override
	public void onEnable() {
		
		getServer().getMessenger().registerOutgoingPluginChannel(this, "net:archiquest");
		getServer().getMessenger().registerIncomingPluginChannel(this, "net:archiquest", new SystemMessageReceiver(this));

		new Chat(this).initialize();
		new Placeholders(this).register();
		new AureliumSkillsAPI(this).initialize();
		new AuthmeAPI(this).initialize();
		
		getCommand("tp").setExecutor(new TeleportCommands(this));
		getCommand("enderchest").setExecutor(new EnderchestCommand(this));

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
	
}


