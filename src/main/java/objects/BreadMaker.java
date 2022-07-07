package objects;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;

import com.BrainBungee;
import com.BrainSpigot;
import com.Mysql;
import com.SystemMessage;
import com.Utils;

import commands.Locales;

public class BreadMaker {

	private Utils utils = new Utils();
	private String name;
	private BrainBungee bungee;
	private BrainSpigot spigot;
	private String servertype;
	private String[] playerdata = new String[0];
	
	public BreadMaker(BrainSpigot spigot) {
		this.spigot = spigot;
		this.playerdata = spigot.playerdata.get(name);
		this.servertype = "client";
	}
	
	public BreadMaker(BrainBungee plugin) {
		this.bungee = plugin;
		this.playerdata = plugin.playerdata.get(name);
		this.servertype = "proxy";
	}

	public BreadMaker getBread(String usernname) {
		this.name = usernname;
		return this;
	}
	
	public String getName() {
		return name;
	}
	
	public String getData(String option) {
		if (playerdata.length==0) {
			return null;
		}
		return playerdata[utils.getOption(option)];
	}
	
	public void setData(String option, String value) {

		playerdata[utils.getOption(option)] = value;

		if (servertype.equals("proxy")) {
			bungee.playerdata.put(name, playerdata);
			try {
				new SystemMessage(bungee).newMessage("player", new String[] {name, option, value});
			} catch (IOException e) { e.printStackTrace(); }
		} else if (servertype.equals("client")) {
			spigot.playerdata.put(name, playerdata);
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

	public String getLanguage() {
		if (getData("language") == null) {
			return "ua";
		}
		return getData("language");
	}
	
	public HashMap<String, String> getLocales() {
		if (servertype.equals("proxy")) {
			return new Locales(bungee).getLocales(getLanguage());
		} else {
			return new Locales(spigot).getLocales(getLanguage());
		}
	}

}
