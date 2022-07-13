package objects;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.BrainBungee;
import com.BrainSpigot;
import com.SystemMessage;

import modules.Mysql;

public class BreadData {

	private String option;
	private BrainBungee bungee;
	private String value;
	private String username;
	private String servertype;
	private BrainSpigot spigot;
	
	public BreadData(BrainBungee bungee, String username, String option, String value) {
		this.username = username;
		this.bungee = bungee;
		this.option = option;
		this.value = value;
		this.servertype = "proxy";
	}
	
	public BreadData(String option) {
		this.option = option;
	}

	public BreadData(BrainSpigot spigot, String username, String option, String value) {
		this.username = username;
		this.spigot = spigot;
		this.option = option;
		this.value = value;
		this.servertype = "client";
	}

	public int getAsInt() {
		return Integer.valueOf(option);
	}

	public String getAsString() {
		return option;
	}
	
	public Long getAsLong() {
		return Long.valueOf(option);
	}

	
	public void save() {

		if (servertype.equals("proxy")) {
			
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
			bungee.log("загружаю дані для ігрока "+username+": "+option+"="+value);
		
		} else {
			new SystemMessage(spigot).newMessage("playerdata", new String[] {"set", username, option, value});
		}
	}
	
}