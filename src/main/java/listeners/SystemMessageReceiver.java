package listeners;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import com.BrainBungee;
import com.BrainSpigot;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import commands.Locales;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import objects.BreadMaker;

public class SystemMessageReceiver implements PluginMessageListener, Listener {
	
	private BrainSpigot spigot;
	private BrainBungee bungee;
	
	public HashMap<String, String> receivedMessages() {
		return spigot.RECEIVEDMESSAGES;
	}
	
	public SystemMessageReceiver(BrainSpigot plugin) {
		this.spigot = plugin;
	}
	
	public SystemMessageReceiver(BrainBungee plugin) {
		this.bungee = plugin;
	}
	
	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] message) {

		if (!channel.contains("net:archiquest")) {
		    return;
		}
		
		ByteArrayDataInput in = ByteStreams.newDataInput(message);
		
        String subchannel = in.readUTF();
        
        if (subchannel.equals("player:archiquest")) {
            String name = in.readUTF();
            String option = in.readUTF();
            String value = in.readUTF();
            
            spigot.log("отримано дані гравця "+name+": "+option+" = "+value);
            
            BreadMaker bread = new BreadMaker(spigot).getBread(name);
            bread.setData(option, value);
        }
        
        
        if (subchannel.equals("locale:archiquest")) {
        	
            String key = in.readUTF();
            String lang = in.readUTF();
            String locale = in.readUTF();
            
            HashMap<String, String> locales = new HashMap<String, String>();
            if (spigot.locales.containsKey(lang)) {
                locales = spigot.locales.get(lang);
            }
            locales.put(key, locale);
            spigot.locales.put(lang, locales);
            
        }

	}
	
	@EventHandler
	public void onPluginMessage(PluginMessageEvent event) {
				
		if (!event.getTag().equals("net:archiquest")) {
			return;
		}
		
		ByteArrayDataInput in = ByteStreams.newDataInput(event.getData());
		String subchannel = in.readUTF();
		String command = in.readUTF();
		if (subchannel.equals("locale:archiquest") && command.equals("get")) {
			try {
				new Locales(bungee).initialiseLocales();
			} catch (SQLException | IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
}
