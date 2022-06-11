package com;

import java.util.HashMap;

import org.bukkit.plugin.java.JavaPlugin;

import commands.Info;
import listeners.SystemMessageReceiver;

public class BrainSpigot extends JavaPlugin {
	
	public HashMap<String, String> RECEIVEDMESSAGES = new HashMap<String, String>();
	
	@Override
	public void onEnable() {
		
		getServer().getMessenger().registerOutgoingPluginChannel(this, "ArchiQuest");
		getServer().getMessenger().registerIncomingPluginChannel(this, "ArchiQuest", new SystemMessageReceiver(this));
		
		getCommand("bread").setExecutor(new Info(this, "archiquest.player"));
		
		getLogger().info("archiquestcore is ready to be helpful for all beadmakers!");
	}

	public void onDisable() {
		
		getLogger().info("archiquestcore has stopped it's service!");

	}

	public void log(String string) {
		getLogger().info(string);
	}
	
}


