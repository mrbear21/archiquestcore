package com;

import org.bukkit.plugin.java.JavaPlugin;

public class BrainSpigot extends JavaPlugin {
	@Override
	public void onEnable() {
		getLogger().info("archiquestcore is ready to be helpful for all beadmakers!");
	}

	public void onDisable() {
		getLogger().info("archiquestcore has stopped it's service!");

	}
}
