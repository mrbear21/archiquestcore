package objects;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.BrainBungee;
import com.BrainSpigot;
import com.SystemMessage;

import events.LanguageChangedEvent;
import integrations.Placeholders;
import modules.Locales;
import modules.Mysql;
import net.md_5.bungee.api.ChatColor;

public class BreadMaker {

	private BrainBungee bungee;
	private BrainSpigot spigot;
	private String servertype;
	private String[] options = {

			 "username", "level", "experience", "hashtag", "guild", "isAfk", "likes", "dislikes", "2faAuth", "language", "rainbowprefix", "inVanish", "plotchat", "marry", "isLogged",
			 "playtime", "settings", "guildtag", "guildinvite", "currentPlot", "lastPM", "joinmessage", "rpname", "voiceChat", "job", "keys",  "rpprefix", "firstPlay",  "lastIP",
			 "donatereward", "holidayBonus", "2Fa", "nickname", "lastLogin", "invitedBy", "isMuted", "isBanned", "discord", "prefix", "ignore", "votes", "achievements", "group", "youtube",
			 "email", "prefixColor", "kills", "deaths", "god", "back", "tempPrefix", "yaw", "isJailed", "oldIP", "qualityfactor",  "rank", "lastEmptyPlot", "emptyPlotsTravelled", "chestUse",
			 "lastChestUse", "squad", "slave", "squadJoinTime", "ramson", "joinTime", "faction", "tabprefix", "shadowMute", "inMinigame"
			
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
		return isOnline() ? " "+new Placeholders(spigot).setPlaceholders(getPlayer(), "%vault_prefix%")+" " : " "+ChatColor.YELLOW;
	}
	
	public String getData(String option) {
		return playerdata[getOption(option)];
	}
	
	public void setData(String option, String value) {
		setData(option, value, false);
	}
	
	public void setData(String option, String value, Boolean save) {

		if (getOption(option) >= 0) {
			
			playerdata[getOption(option)] = value;

			if (servertype.equals("proxy")) {
				bungee.playerdata.put(name, playerdata);
				new SystemMessage(bungee).newMessage("playerdata", new String[] {name, option, value});
				if (save) {
					try {
						insertData(option, value);
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			} else if (servertype.equals("client")) {
				spigot.playerdata.put(name, playerdata);
				if (save) {
					new SystemMessage(bungee).newMessage("playerdata", new String[] {"set", name, option, value});
				}
				if (option.equals("language")) {
					LanguageChangedEvent event = new LanguageChangedEvent(name, value);
					Bukkit.getServer().getPluginManager().callEvent(event);
				}
			}
		}

	}
	
	public void loadData() {
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
	}
	
	
	public void insertData(String option, String value) throws SQLException {

		Mysql mysql = new Mysql(bungee);
		
		String type = "varchar"; 
		if ("votes,kills,deaths".contains(option)) { type = "int"; }
		
		String query = "ALTER TABLE "+bungee.table+" ADD `"+option+"` "+type+"(250) NULL;";
		try {
			mysql.getConnection().prepareStatement(query).executeUpdate();
		} catch (Exception c) {
		} 
		String field = "NULL"; if (value != null) { field = "'"+value+"'"; }

		try {
			query = "INSERT INTO "+bungee.table+" (username, "+option+") VALUES ('"+name+"', "+field+")";
			PreparedStatement statement = mysql.getConnection().prepareStatement(query);
			statement.executeUpdate();
		} catch (Exception c) {
			query = "UPDATE "+bungee.table+" SET `"+option+"` = "+field+" WHERE `playerdata`.`username` = '"+name+"'";
			PreparedStatement statement = mysql.getConnection().prepareStatement(query);
			statement.executeUpdate();
		}
		
		bungee.log("загружаю дані для ігрока "+name+": "+option+"="+value);
		
	}

	public String getLanguage() {
		return getData("language") == null ? "ua" : getData("language");
	}
	
	public HashMap<String, String> getLocales() {
		if (servertype.equals("proxy")) {
			return new Locales(bungee).getLocales(getLanguage());
		} else {
			return new Locales(spigot).getLocales(getLanguage());
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
	
		


}
