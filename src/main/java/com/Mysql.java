package com;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Mysql {
	
	private BrainBungee plugin;
	
	public Mysql(BrainBungee plugin) {
		this.plugin = plugin;
	}

	public Connection getConnection() {
		return plugin.connection;
	}
	
	public String getTable() {
		return plugin.table;
	}
	
	public void mysqlSetup() {
		plugin.host = plugin.getConfig().getString("mysql.host");
		plugin.database = plugin.getConfig().getString("mysql.database");
		plugin.username = plugin.getConfig().getString("mysql.username");
		plugin.password = plugin.getConfig().getString("mysql.password");
		plugin.port = plugin.getConfig().getInt("mysql.port");
		plugin.table = plugin.getConfig().getString("mysql.table");
		
		try { 
			synchronized (this) {
				if (getConnection() != null && !getConnection().isClosed()) {
					return;
				}
				Class.forName("com.mysql.cj.jdbc.Driver");  
				setConnection(DriverManager.getConnection("jdbc:mysql://"+plugin.host+":"+plugin.port+"/"+plugin.database+"?useUnicode=true&characterEncoding=utf8&useSSL=false", plugin.username, plugin.password));
				plugin.getLogger().info("MySQL succesfully connected!");			
				
			}
		} catch(SQLException e) {
			 e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	
	public void setConnection(Connection connection) {
		plugin.connection = connection;
	
	}
}
