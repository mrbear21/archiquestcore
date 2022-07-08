package objects;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.BrainBungee;
import com.BrainSpigot;
import com.SystemMessage;
import com.Utils;

import events.LanguageChangedEvent;
import modules.Locales;
import modules.Mysql;
import net.md_5.bungee.api.ChatColor;

public class BreadMaker {

	private Utils utils = new Utils();
	private String name;
	private BrainBungee bungee;
	private BrainSpigot spigot;
	private String servertype;
	private String[] playerdata = new String[utils.options.length];
	
	public BreadMaker(BrainSpigot spigot) {
		this.spigot = spigot;
		this.servertype = "client";
	}
	
	public BreadMaker(BrainBungee bungee) {
		this.bungee = bungee;
		this.servertype = "proxy";
	}

	public BreadMaker getBread(String usernname) {
		this.name = usernname;
		if (servertype.equals("proxy")) {
			if (bungee.playerdata.containsKey(name)) {
				this.playerdata = bungee.playerdata.get(name);
			}
		}
		if (servertype.equals("client")) {
			if (spigot.playerdata.containsKey(name)) {
				this.playerdata = spigot.playerdata.get(name);
			}
		}
		return this;
	}
	
	public Player getPlayer() {
		return Bukkit.getPlayer(name);
	}
	
	public Boolean isOnline() {
		return getPlayer() != null && getPlayer().isOnline() ? true : false;
	}
	
	public String getName() {
		return name;
	}
	
	public String getDisplayName() {
		return ChatColor.YELLOW+name;
	}
	
	public String getData(String option) {
		return playerdata[utils.getOption(option)];
	}
	
	public void setData(String option, String value) {
		setData(option, value, false);
	}
	
	public void setData(String option, String value, Boolean save) {

		if (utils.getOption(option) >= 0) {
			
			playerdata[utils.getOption(option)] = value;

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

}
