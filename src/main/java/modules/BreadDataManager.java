package modules;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import brain.BrainBungee;
import brain.BrainSpigot;
import brain.Utils;

public class BreadDataManager {

	private String option;
	private BrainBungee bungee;
	private String value;
	private String username;
	private String servertype;
	private BrainSpigot spigot;
	
	public BreadDataManager(BrainBungee bungee, String username, String option, String value) {
		this.username = username;
		this.bungee = bungee;
		this.option = option;
		this.value = value;
		this.servertype = "proxy";
	}
	
	public BreadDataManager(String value) {
		this.value = value;
	}

	public BreadDataManager(BrainSpigot spigot, String username, String option, String value) {
		this.username = username;
		this.spigot = spigot;
		this.option = option;
		this.value = value;
		this.servertype = "client";
	}

	public int getAsInt() {
		return value != null ? Integer.valueOf(value) : 0;
	}

	public String getAsString() {
		return value != null ? value : "none";
	}
	
	public Long getAsLong() {
		return value != null ? Long.valueOf(value) : 0;
	}

	public Boolean isNotNull() {
		return value != null ? true : false;
	}
	
	public void save() {

		if (servertype.equals("proxy")) {
			
			if (bungee.getConfig().getBoolean("mysql.use")) {
			
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
					query = "INSERT INTO "+bungee.table+" (username, "+option+") VALUES ('"+username+"', "+field+")";
					PreparedStatement statement = mysql.getConnection().prepareStatement(query);
					statement.executeUpdate();
				} catch (Exception c) {
					query = "UPDATE "+bungee.table+" SET `"+option+"` = "+field+" WHERE `playerdata`.`username` = '"+username+"'";
					try {
						PreparedStatement statement = mysql.getConnection().prepareStatement(query);
						statement.executeUpdate();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				
			}
			
			bungee.getDataFile().set("players."+username.toLowerCase()+"."+option, value);
			
		
		} else {
			new SystemMessages(spigot).newMessage("playerdata", new String[] {"set", username, option, value});
		}
	}

	public boolean getAsBoolean() {
		return value != null ? Boolean.valueOf(value) : false;
	}

	public Player getAsPlayer() {
		return value != null ? Bukkit.getPlayer(value) : null;
		
	}

	public Location getAsLocation() {
		return value != null ? new Utils().stringToLoc(value) : null;
	}
	
}
