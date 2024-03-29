package modules;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import brain.BrainBungee;

public class Mysql {
	
	private BrainBungee bungee;
	
	public Mysql(BrainBungee bungee) {
		this.bungee = bungee;
	}

	public Connection getConnection() {
		return bungee.connection;
	}
	
	public String getTable() {
		return bungee.table;
	}
	

	public void mysqlSetup() {
		
		if (bungee.getConfig().getBoolean("mysql.use")) {
		
			bungee.host = bungee.getConfig().getString("mysql.host");
			bungee.database = bungee.getConfig().getString("mysql.database");
			bungee.username = bungee.getConfig().getString("mysql.username");
			bungee.password = bungee.getConfig().getString("mysql.password");
			bungee.port = bungee.getConfig().getInt("mysql.port");
			bungee.table = bungee.getConfig().getString("mysql.table");
			
			try { 
				synchronized (this) {
					if (getConnection() != null && !getConnection().isClosed()) {
						return;
					}
					Class.forName("com.mysql.cj.jdbc.Driver");  
					setConnection(DriverManager.getConnection("jdbc:mysql://"+bungee.host+":"+bungee.port+"/"+bungee.database+"?useUnicode=true&characterEncoding=utf8&useSSL=false&autoReconnect=true", bungee.username, bungee.password));
					bungee.getLogger().info("MySQL succesfully connected!");			
					
				}
			} catch(SQLException e) {
				 e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			
		}
		
	}
	
	public void setConnection(Connection connection) {
		bungee.connection = connection;
	
	}
}
