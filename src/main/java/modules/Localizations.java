package modules;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.bukkit.configuration.file.FileConfiguration;

import brain.BrainBungee;
import brain.BrainSpigot;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class Localizations extends Command {

	private BrainBungee bungee;
	private BrainSpigot spigot;
	private String servertype;
	private String language;
	
    public Localizations(BrainBungee plugin) {
		super("locales");
    	this.bungee = plugin;
		this.servertype = "proxy";
	}
    
    public Localizations(BrainSpigot spigot) {
		super("locales");
    	this.spigot = spigot;
		this.servertype = "client";
	}

	public Localizations(BrainBungee bungee, String language) {
		super("locales");
    	this.bungee = bungee;
		this.language = language;
		this.servertype = "proxy";
	}

	public Localizations(BrainSpigot spigot, String language) {
		super("locales");
    	this.spigot = spigot;
		this.language = language;
		this.servertype = "client";
		
	}

	public void execute(CommandSender sender, String[] args) {
		
	     if (sender instanceof ProxiedPlayer) {
	    	 initialise();
	    	 ProxiedPlayer p = (ProxiedPlayer) sender;        	
	    	 p.sendMessage(new ComponentBuilder ("Переклади оновлені").color(ChatColor.RED).create()); 
	     }

	}
		
	public String[] languages = {"ua", "en", "ru", "kz", "ald", "ser"};
	
	public HashMap<String, String> getLocalesMap(String lang) {
		if (servertype.equals("client")) {
			return spigot.locales.containsKey(lang) ? spigot.locales.get(lang) : spigot.locales.containsKey("ua") ? spigot.locales.get("ua") : new HashMap<String, String>();
		} else {
			return bungee.locales.containsKey(lang) ? bungee.locales.get(lang) : bungee.locales.containsKey("ua") ? bungee.locales.get("ua") : new HashMap<String, String>();
		}
	}
	
	public HashMap<String, String> getLocalesMap() {
		String lang = language != null ? language : "en";
		if (servertype.equals("client")) {
			return spigot.locales.containsKey(lang) ? spigot.locales.get(lang) : spigot.locales.containsKey("ua") ? spigot.locales.get("ua") : new HashMap<String, String>();
		} else {
			return bungee.locales.containsKey(lang) ? bungee.locales.get(lang) : bungee.locales.containsKey("ua") ? bungee.locales.get("ua") : new HashMap<String, String>();
		}
	}
	
	public String translateString(String string) {

		HashMap<String, String> locales = getLocalesMap(language);
		locales = locales.entrySet().stream().sorted(Collections.reverseOrder(Map.Entry.comparingByKey())).collect(Collectors.toMap(
                Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new ));
		
		for (Entry<String, String> locale : locales.entrySet()) {
			string = string.replace(locale.getKey(), locale.getValue());
		}
		return string;
	}
	
	public String translateString(String string, String language) {
		for (Entry<String, String> locale : getLocalesMap(language).entrySet()) {
			string = string.replace(locale.getKey(), locale.getValue());
		}
		return string;
	}
	

	public void initialise() {
		
		if (servertype.equals("client")) {
			
			new SystemMessages(spigot).newMessage("locale", new String[] {"get"});
	
	        FileConfiguration conf = spigot.getLocalesFile();
	        Long start = System.currentTimeMillis(); int count = 0;
	        for (String lang : languages) {
	        	if (conf.getConfigurationSection(lang) != null) {
		            HashMap<String, String> locales = new HashMap<String, String>();
		            if (spigot.locales.containsKey(lang)) {
		                locales = spigot.locales.get(lang);
		            }
			        for (String s : conf.getConfigurationSection(lang).getKeys(false)) {
			        	String locale = conf.getString(lang+"."+s);
			            locales.put(s.replace("%1", "."), locale);
			            count++;
			        }
		            spigot.locales.put(lang, locales); 
	        	}
	        }
	        
	        spigot.log("Loaded "+count+" translations from file ("+(System.currentTimeMillis()-start)+" ms)");
			
		}
		
		if (servertype.equals("proxy")) {
			
			if (bungee.getConfig().getBoolean("mysql.use")) {
			
				Mysql mysql = new Mysql(bungee);
				String[] exceptions = new String[] {"website"};
				PreparedStatement statement;
				try {
					statement = mysql.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS `"+bungee.database+"`.`locales` ( `key` VARCHAR(50) NOT NULL , `ua` VARCHAR(100) NULL DEFAULT NULL , `by` VARCHAR(100) NULL DEFAULT NULL , `ru` VARCHAR(100) NULL DEFAULT NULL , `en` VARCHAR(100) NULL DEFAULT NULL , PRIMARY KEY (`key`)) ENGINE = InnoDB;");
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
								
					            for (String e : Arrays.asList(exceptions)) {
					            	if (key.startsWith(e)) {
					            		break;
					            	}
					            }
					            
								new SystemMessages(bungee).newMessage("locale", new String[] {key, lang, locale});
								
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
					
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
	}

}
