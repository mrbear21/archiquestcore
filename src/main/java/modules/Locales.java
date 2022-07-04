package modules;

import java.util.List;

import com.BrainSpigot;
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
	private ProtocolManager manager;
	
	public Locales(BrainSpigot plugin) {
		this.plugin = plugin;
		manager = ProtocolLibrary.getProtocolManager();
	}

	public void registerLocalesListener() {
		manager.addPacketListener(	
			new PacketAdapter(plugin, ListenerPriority.NORMAL, PacketType.Play.Server.CHAT) {
				@Override
	            public void onPacketSending(PacketEvent event) {
	                if (event.getPacketType() == PacketType.Play.Server.CHAT) {
	                	
	            	    PacketContainer packet = event.getPacket();
	            	    List<WrappedChatComponent> components = packet.getChatComponents().getValues();
	            	
	            	    for (WrappedChatComponent component : components) {
	            	        component.setJson(component.getJson().replace("<hello>", "привіт"));
	            	        packet.getChatComponents().write(components.indexOf(component), component);
	            	    }
	                }
	             }
	         }
		);
	}
    
    
}
