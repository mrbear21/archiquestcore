package modules;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map.Entry;

import com.BrainBungee;
import com.BrainSpigot;
import com.Mysql;
import com.SystemMessage;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class Locales extends Command {

	private BrainBungee bungee;
	private BrainSpigot spigot;
	private String servertype;
	
    public Locales(BrainBungee plugin) {
		super("locales");
    	this.bungee = plugin;
		this.servertype = "proxy";
	}
    
    public Locales(BrainSpigot spigot) {
		super("locales");
    	this.spigot = spigot;
		this.servertype = "client";
	}

	public void execute(CommandSender sender, String[] args) {
		
	     if (sender instanceof ProxiedPlayer) {
	    	 try {
				initialiseLocales();
			} catch (SQLException | IOException e) {
				e.printStackTrace();
			}
	    	 
	    	 ProxiedPlayer p = (ProxiedPlayer) sender;        	
	    	 p.sendMessage(new ComponentBuilder ("Переклади оновлені").color(ChatColor.RED).create()); 
	    	 
	     }

	}
	
	public HashMap<String, String> getLocales(String lang) {
		if (servertype.equals("client")) {
			return spigot.locales.containsKey(lang) ? spigot.locales.get(lang) : spigot.locales.containsKey("ua") ? spigot.locales.get("ua") : new HashMap<String, String>();
		} else {
			return bungee.locales.containsKey(lang) ? bungee.locales.get(lang) : bungee.locales.containsKey("ua") ? bungee.locales.get("ua") : new HashMap<String, String>();
		}
	}
	
	public String translateString(String string, String language) {
		for (Entry<String, String> locale : getLocales(language).entrySet()) {
			string = string.replace(locale.getKey(), locale.getValue());
		}
		return string;
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
					
		            String key = results.getObject(1).toString();
		            String lang = md.getColumnName(i);
		            String locale = results.getObject(i).toString();
					
					new SystemMessage(bungee).newMessage("locale", new String[] {key, lang, locale});
					
		            HashMap<String, String> locales = new HashMap<String, String>();
		            if (bungee.locales.containsKey(lang)) {
		                locales = bungee.locales.get(lang);
		            }
		            locales.put(key, locale);
		            bungee.locales.put(lang, locales);
					
				}
			}
		}
		results.close();
		
	}

}
