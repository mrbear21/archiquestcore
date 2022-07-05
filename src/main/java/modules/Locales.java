package modules;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import com.BrainBungee;
import com.BrainSpigot;
import com.Mysql;
import com.SystemMessage;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedChatComponent;

public class Locales {
	
	private BrainSpigot plugin;
	private BrainBungee bungee;
	private ProtocolManager manager;
	
	public Locales(BrainSpigot plugin) {
		this.plugin = plugin;
		manager = ProtocolLibrary.getProtocolManager();
	}
	
	public Locales(BrainBungee bungee) {
		this.bungee = bungee;
		manager = ProtocolLibrary.getProtocolManager();
	}

	public HashMap<String, String> getLocales(String lang) {
		return plugin.locales.get(lang);
	}
	
	public void initialiseLocales() throws SQLException, IOException {
		
		Mysql mysql = new Mysql(bungee);
		
		PreparedStatement statement = mysql.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS `"+bungee.database+"`.`locales` ( `key` VARCHAR(50) NOT NULL , `ua` VARCHAR(100) NULL DEFAULT NULL , `by` VARCHAR(100) NULL DEFAULT NULL , `ru` VARCHAR(100) NULL DEFAULT NULL , `en` VARCHAR(100) NULL DEFAULT NULL , PRIMARY KEY (`key`)) ENGINE = InnoDB;");
		statement.executeUpdate();
		
		statement = mysql.getConnection().prepareStatement("SELECT * FROM `"+bungee.database+"`.`locales`");
		ResultSet results = statement.executeQuery();
		ResultSetMetaData md = results.getMetaData();
		int columns = md.getColumnCount();
		while (results.next()) {
			for (int i = 2; i <= columns; ++i) {
				if (results.getObject(i) != null) {
					new SystemMessage(bungee).updateLocales(results.getObject(1).toString(), md.getColumnName(i), results.getObject(i).toString());
				}
			}
		}
		results.close();
		
	}
	
	public void registerLocalesListener() {
		
		try {
			new SystemMessage(plugin).getLocales();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		manager.addPacketListener(	
			new PacketAdapter(plugin, ListenerPriority.NORMAL, PacketType.Play.Server.CHAT) {
				@Override
	            public void onPacketSending(PacketEvent event) {
	                if (event.getPacketType() == PacketType.Play.Server.CHAT) {
	                	
	            	    PacketContainer packet = event.getPacket();
	            	    List<WrappedChatComponent> components = packet.getChatComponents().getValues();
	            	
	            	    for (WrappedChatComponent component : components) {
	   
	            	    	getLocales("ua").entrySet().stream().forEach(locales -> component.setJson(component.getJson().replace(locales.getKey(), locales.getValue())));

	            	    	packet.getChatComponents().write(components.indexOf(component), component);

	            	    }
	                }
	             }
	         }
		);
	}
    
    
}
