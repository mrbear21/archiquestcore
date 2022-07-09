package com;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.util.HashMap;

import commands.LanguageCommand;
import listeners.BungeeListeners;
import listeners.SystemMessageReceiver;
import modules.Discord;
import modules.Locales;
import modules.Mysql;
import modules.WebServer;
import net.dv8tion.jda.api.JDA;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import objects.BreadMaker;


public class BrainBungee extends Plugin {
	
	private ConfigurationProvider provider = ConfigurationProvider.getProvider(YamlConfiguration.class);
	public Configuration config;
	public Connection connection;
	public String host, database, username, password, table; public int port;
	public HashMap<String, HashMap<String, String>> locales = new HashMap<String, HashMap<String,String>>();
	public HashMap<String, String[]> playerdata = new HashMap<String, String[]>();
	public JDA jda;
	public boolean botActivation = true;
	
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
		//	new Messages(this).Setup();
		} catch (Exception e) {
			e.printStackTrace();
		}

		ProxyServer.getInstance().getPluginManager().registerCommand(this, new Locales(this));
		ProxyServer.getInstance().getPluginManager().registerCommand(this, new LanguageCommand(this));

		this.getProxy().getPluginManager().registerListener(this, new BungeeListeners(this));
		
		getLogger().info("archiquestcore is ready to be helpful for all beadmakers!");
			
	}

	public void onDisable() {
		/*try {
			new Messages(this).Stop();
		} catch (Exception e){
			e.printStackTrace();
		}*/
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

	public BreadMaker getBread(String name) {
		return new BreadMaker(this, name);
	}
	
}
