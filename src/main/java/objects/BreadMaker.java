package objects;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import brain.BrainBungee;
import brain.BrainSpigot;
import brain.Utils;
import events.LanguageChangedEvent;
import integrations.Placeholders;
import modules.BreadDataManager;
import modules.Localizations;
import modules.Mysql;
import modules.SystemMessages;

public class BreadMaker {

	private BrainBungee bungee;
	private BrainSpigot spigot;
	private String servertype;
	private String[] options = {

			 "username", "level", "experience", "hashtag", "guild", "language", "vanish", "plotchat", "marry", "loggedin", "autoafk",
			 "settings", "currentPlot", "lastPM", "joinmessage", "firstPlay",  "lastIP", "holidayBonus", "2Fa", "nickname", "tptoggle", "doublejump", 
			 "lastLogin", "isMuted", "isBanned", "discord", "prefix", "ignore", "votes", "youtube", "email", "prefixColor", "god", "back", "elevator",
			 "tempPrefix",  "yaw", "isJailed", "oldIP", "qualityfactor",  "rank", "squad", "joinTime", "shadowMute", "inMinigame", "afk", "tprequest", "lasttprequest"
			
			};
	
	public int getOption(String option) {
		return Arrays.asList(options).indexOf(option);
	}
	
	public BreadMaker(BrainSpigot spigot, String usernname) {
		this.name = usernname;
		this.spigot = spigot;
		this.servertype = "client";
		if (spigot.playerdata.containsKey(name)) {
			this.playerdata = spigot.playerdata.get(name);
		}
	}
	
	public BreadMaker(BrainBungee bungee, String usernname) {
		this.name = usernname;
		this.bungee = bungee;
		this.servertype = "proxy";		
		if (bungee.playerdata.containsKey(name)) {
			this.playerdata = bungee.playerdata.get(name);
		}
	}

	private String name;
	private String[] playerdata = new String[options.length];

	public Player getPlayer() {
		return Bukkit.getPlayer(name);
	}
	
	public Boolean isOnline() {
		return getPlayer() != null && getPlayer().isOnline() ? true : false;
	}
	
	public String getName() {
		return name;
	}
	
	public String getPrefix() {
		String prefix = isOnline() ? spigot.getServer().getPluginManager().isPluginEnabled("PlaceholderAPI") && spigot.getServer().getPluginManager().isPluginEnabled("Vault") ? new Placeholders(spigot).setPlaceholders(getPlayer(), "%vault_prefix%") : String.valueOf(ChatColor.DARK_AQUA) : String.valueOf(ChatColor.DARK_AQUA) ;
		return  prefix;
	}
	
	
	public String getDisplayName() {
		return getPrefix()+getName();
	}
	
	public BreadDataManager getData(String option) {
		return new BreadDataManager(playerdata[getOption(option)]);
	}
	
	public BreadDataManager setData(String option, String value) {

		if (getOption(option) >= 0) {
			
			playerdata[getOption(option)] = value;
			
			if (servertype.equals("proxy")) {
				bungee.playerdata.put(name, playerdata);
				new SystemMessages(bungee).newMessage("playerdata", new String[] {name, option, value});
			} else if (servertype.equals("client")) {
				spigot.playerdata.put(name, playerdata);
				if (option.equals("language")) {
					Bukkit.getServer().getPluginManager().callEvent(new LanguageChangedEvent(name, value));
				}
			}
		}
		return servertype.equals("proxy") ? new BreadDataManager(bungee, name, option, value) : new BreadDataManager(spigot, name, option, value);


	}
	
	public void loadData() {
		if (servertype.equals("client")) {
			new SystemMessages(spigot).newMessage("playerdata", new String[] {"get", name});
		} else if (servertype.equals("proxy")) {
			
			try {
				Mysql mysql = new Mysql(bungee);
				try {
					PreparedStatement statement = mysql.getConnection().prepareStatement("SELECT * FROM " + mysql.getTable() + " WHERE username=?");
					statement.setString(1, name.toLowerCase());
					ResultSet results = statement.executeQuery();
					ResultSetMetaData md = results.getMetaData();
					int columns = md.getColumnCount();
					while (results.next()) {
						for (int i = 1; i <= columns; ++i) {
							if (results.getObject(i) != null) {
								setData(md.getColumnName(i), results.getObject(i).toString());
							}
						}
					}
					results.close();
				} catch (SQLException c) { c.printStackTrace(); }
			} catch (Exception c) {
				for (String o : options) {
					if (bungee.getDataFile().get("players."+name+"."+o) != null) {
						setData(o, bungee.getDataFile().getString("players."+name+"."+o));
					}
				}		
			}

		}
	}
	
	public String getLanguage() {
		return getData("language") == null ? "ua" : getData("language").getAsString();
	}
	
	public Localizations getLocales() {
		if (servertype.equals("proxy")) {
			return new Localizations(bungee, getLanguage());
		} else {
			return new Localizations(spigot, getLanguage());
		}
	}

	public String kick(String reason) {
		
		Player player = Bukkit.getPlayerExact(name);
	
		if (player == null) {
			return "archiquest.player.is.offline";
		}

		player.kickPlayer(reason);
		return "archiquest.player.kicked: "+reason;

	}

	public String getBalance() {
		return "0";
	}
	
	public void sendBossbar(String text, int time) {
		Player player = getPlayer();
		
		if (spigot.bossbars.containsKey(player)) {
			spigot.bossbars.get(player).removePlayer(player);
		}
		
		BossBar bossBar = Bukkit.createBossBar(ChatColor.translateAlternateColorCodes('&', text), BarColor.YELLOW, BarStyle.SOLID);
		bossBar.addPlayer(player);
		spigot.bossbars.put(player, bossBar);
		spigot.getServer().getScheduler().scheduleSyncDelayedTask(spigot, new Runnable() {
			public void run() {
				bossBar.removePlayer(player);
			}
		}, 20 * time);
	}

	public void clearData() {
		spigot.playerdata.remove(name);
	}

	public void sendTitle(String title, String subtitle, int seconds) {
		getPlayer().sendTitle(ChatColor.translateAlternateColorCodes('&', new Utils().translateSmiles(title)), ChatColor.translateAlternateColorCodes('&', new Utils().translateSmiles(subtitle)), 20, seconds * 20, 20);
	
	}

	public String getFactionPrefix() {
		return spigot.getServer().getPluginManager().isPluginEnabled("PlaceholderAPI") && spigot.getServer().getPluginManager().isPluginEnabled("Factions") ? "§7[§c" + new Placeholders(spigot).setPlaceholders(getPlayer(), "%factionsuuid_faction_name%").toUpperCase() + "§7] " : "";
	}

	public void updateDisplayName() {
		Player player = getPlayer();
		player.setPlayerListName(getPrefix() + player.getName() + (getData("afk").isNotNull() ? " §7[AFK]" : ""));
		player.setDisplayName(getPrefix() + player.getName());
	}

	public void addMessageToHistory(ChatMessage message) {
		if (message.getMessage().equals("{\"text\":\"\"}")) { return; }
		List<String[]> history = spigot.chathistory.containsKey(name) ? spigot.chathistory.get(name) : new ArrayList<String[]>();
		history.add(new String[] {message.getChat(), message.getPlayer(), message.getMessage(), message.getStatus(), message.getId(), message.getLanguage()});
		spigot.chathistory.put(name, history);
	}
	
	public void clearMessagesHistory(String player) {
		spigot.chathistory.remove(player);
	}
	

}
