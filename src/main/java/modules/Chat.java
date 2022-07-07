package modules;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import com.BrainSpigot;
import com.SystemMessage;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedChatComponent;

import objects.BreadMaker;

public class Chat {
	
	public BrainSpigot plugin;
	private ProtocolManager manager;
	
	public Chat(BrainSpigot plugin) {
		this.plugin = plugin;
		manager = ProtocolLibrary.getProtocolManager();
	}
	
	public HashMap<String, String> getLocales(String lang) {
		return plugin.locales.get(lang);
	}

	public BreadMaker getBread(String name) {
		return new BreadMaker(plugin).getBread(name);
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
	   	
	            	    	getBread(event.getPlayer().getName()).getLocales().entrySet().stream().forEach(locales -> component.setJson(component.getJson().replace(locales.getKey(), locales.getValue())));

	            	    	packet.getChatComponents().write(components.indexOf(component), component);

	            	    }
	                }
	             }
	         }
		);
		
	}
    
    
}
