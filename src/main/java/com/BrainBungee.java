package com;

import net.md_5.bungee.api.plugin.Plugin;


public class BrainBungee extends Plugin {
	@Override
	public void onEnable() {
		getLogger().info("archiquestcore is ready to be helpful for all beadmakers!");
	}

	public void onDisable() {
		getLogger().info("archiquestcore has stopped it's service!");

	}
}
