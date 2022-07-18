package com;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import commands.LanguageCommand;
import fun.CharliesComeback;
import listeners.BungeeListeners;
import listeners.SystemMessageReceiver;
import modules.Discord;
import modules.Locales;
import modules.Mysql;
import modules.RepeatingTasks;
import modules.WebServer;
import net.dv8tion.jda.api.JDA;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.scheduler.ScheduledTask;
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
	public ScheduledTask repeatingtask;
	private Configuration data;
	
	public HashMap<String, List<String>> charliepatterns = new HashMap<String, List<String>>();
	public String[] possiblePattern = new String[2]; 
	public List<String> possibleAnswers = new ArrayList<String>();
	
	public static void main(String[] args) {
		
	}
	
	@Override
	public void onEnable() {
		
		getProxy().registerChannel("net:archiquest");
		this.getProxy().getPluginManager().registerListener(this, new SystemMessageReceiver(this));
		
		loadConfig();
		loadDataFile();
		
		try {
			new Mysql(this).mysqlSetup();
			new Locales(this).initialise();
			new Discord(this).login();
			new WebServer(this).start();
			new RepeatingTasks(this).start();
			new CharliesComeback(this).register();
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
		saveDataFile();
		new RepeatingTasks(this).stop();
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
	
	public Configuration getDataFile() {
		return data;
	}
	
	private void loadDataFile() {
		File conf = new File(getDataFolder(), "data.yml");
		if (!conf.exists()) {
			getLogger().info("Generating data file.");
			try {
				getDataFolder().mkdir();
				conf.createNewFile();
			} catch (IOException e) {
			e.printStackTrace();
			} 
		}
		try {
			Configuration data = provider.load(conf);
			this.data = data;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void saveDataFile() {
		File conf = new File(getDataFolder(), "data.yml");
		if (conf.exists()) {
			getLogger().info("Saving data file.");
			try {
				provider.save(data, conf);
			} catch (IOException e) {
			e.printStackTrace();
			} 
		}
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
				if (config.getStringList("mysql").isEmpty()) {
					config.set("mysql.use", false);
					config.set("mysql.host", "");
					config.set("mysql.port", 3306);
					config.set("mysql.username", "");
					config.set("mysql.password", "");
					config.set("mysql.database", "");
					config.set("mysql.table", "playerdata");
				}
				if (config.getStringList("votifier").isEmpty()) {
					config.set("votifier", "pass-word-123");
				}
				if (config.getStringList("discord.token").isEmpty()) {
					config.set("discord.token", "token");
				}
				if (config.getStringList("discord.chats.server-chat").isEmpty()) {
					config.set("discord.chats.server-chat", "id");
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
