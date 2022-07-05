package com;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;

import commands.Info;
import listeners.SystemMessageReceiver;
import modules.Discord;
import modules.Locales;
import modules.Messages;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;


public class BrainBungee extends Plugin {
	
	private ConfigurationProvider provider = ConfigurationProvider.getProvider(YamlConfiguration.class);
	public Configuration config;
	public Connection connection;
	public String host, database, username, password, table; public int port;
	
	public static void main(String[] args) {
		
	}
	
	@Override
	public void onEnable() {
		
		getProxy().registerChannel("net:archiquest");
		this.getProxy().getPluginManager().registerListener(this, new SystemMessageReceiver(this));
		
		loadConfig();
		try {
			new Mysql(this).mysqlSetup();
			new Locales(this).initialiseLocales();
			new Discord(this).login();
			new WebServer(this).start();
			new Messages(this).Setup();
		} catch (Exception e) {
			e.printStackTrace();
		}

		ProxyServer.getInstance().getPluginManager().registerCommand(this, new Info(this, "archiquestcore.player"));
		
		getLogger().info("archiquestcore is ready to be helpful for all beadmakers!");
			
	}

	public void onDisable() {
		try {
			new Messages(this).Stop();
		} catch (Exception e){
			e.printStackTrace();
		}
		getLogger().info("archiquestcore has stopped it's service!");

	}
	
	public Configuration getConfig() {
		return config;
	}
	
	private void loadConfig() {
		File conf = new File(getDataFolder(), "config.yml");
		if (!conf.exists()) {
			getLogger().info("Generating default configuration file.");
			try {
				getDataFolder().mkdir();
				conf.createNewFile();
				Configuration config = provider.load(conf);
				this.config = config;
				if (config.getString("port").isEmpty()) {
					config.set("port", 8454);
				}
				if (config.getStringList("votifier").isEmpty()) {
					config.set("votifier", "pass-word-123");
				}
				if (config.getStringList("discord.token").isEmpty()) {
					config.set("discord.token", "token");
				}
				provider.save(config, conf);
			} catch (IOException e) {
			e.printStackTrace();
			} 
		}
		try {
			Configuration config = provider.load(conf);
			this.config = config;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void log(String string) {
		getLogger().info(string);
	}
	
}
