package modules;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

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

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import objects.BreadMaker;

public class Chat implements Listener {
	
	public BrainSpigot spigot;
	private ProtocolManager manager;
	
	public Chat(BrainSpigot spigot) {
		this.spigot = spigot;
		manager = ProtocolLibrary.getProtocolManager();
	}
	
	public void initialize() {
		spigot.getServer().getPluginManager().registerEvents(new Chat(spigot), spigot);
		registerLocalesListener();
	}
	
	public HashMap<String, String> getLocales(String lang) {
		return spigot.locales.get(lang);
	}


	private ChatColor getColor(String chat) {
		switch(chat) {
			case "global": return ChatColor.YELLOW;
			case "server": return ChatColor.YELLOW;
			case "server_admin": return ChatColor.RED;
			case "discord_admin": return ChatColor.RED;
			case "admin": return ChatColor.RED;
			case "roleplay": return ChatColor.DARK_PURPLE;
			case "squad": return ChatColor.GOLD;
			case "squad_discord": return ChatColor.GOLD;
			case "squad_server": return ChatColor.GOLD;
			case "plot": return ChatColor.WHITE;
			case "discord": return ChatColor.AQUA;
			case "spy": return ChatColor.GRAY;
			case "support": return ChatColor.RED;
			case "faction": return ChatColor.GREEN;
			case "vk": return ChatColor.BLUE;
			case "vk_forward": return ChatColor.BLUE;
		}
		return ChatColor.WHITE;
	}
	
	public TextComponent newMessage(String chat, String player, String message) {

		BreadMaker bread = new BreadMaker(spigot).getBread(player);
		
		TextComponent textComponent = new TextComponent(ChatColor.WHITE+"["+getColor(chat)+String.valueOf(chat.charAt(0)).toUpperCase()+ChatColor.WHITE+"] ");
		textComponent.addExtra(bread.getDisplayName());
		textComponent.addExtra(getColor(chat)+": ");
		textComponent.addExtra(message);
		return textComponent;
		
	}
		
	@EventHandler(priority = EventPriority.LOWEST)
	public void onChat(AsyncPlayerChatEvent event) {
		
		event.setCancelled(true);
		Player player = event.getPlayer();
		String message = event.getMessage();
		
		if (message.length() == 0) {
			return;
		}

		TextComponent textComponent = new TextComponent();
		
		List<Player> players = new ArrayList<Player>();
		
		switch (String.valueOf(message.charAt(0))) {
			case "!":
				textComponent = newMessage("global", player.getName(), message.substring(1));
				Bukkit.getOnlinePlayers().stream().forEach(p -> players.add(p));
				break;
			case "\\":
				textComponent = newMessage("admin", player.getName(), message.substring(1));
				Bukkit.getOnlinePlayers().stream().filter(p -> p.hasPermission("archiquest.adminchat")).forEach(p -> players.add(p));
				break;
			default:
				textComponent = newMessage("local", player.getName(), message);
				Bukkit.getOnlinePlayers().stream().filter(p -> p.getWorld() == player.getWorld() && p.getLocation().distance(event.getPlayer().getLocation()) < 200).forEach(p -> players.add(p));
		}
		
		for (Player p : players) {
			p.spigot().sendMessage(textComponent);
		}
		
		
	}
	
	public BreadMaker getBread(String name) {
		return new BreadMaker(spigot).getBread(name);
	}
	
	public void registerLocalesListener() {
		
		try {
			new SystemMessage(spigot).getLocales();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		manager.addPacketListener(	
			new PacketAdapter(spigot, ListenerPriority.NORMAL, PacketType.Play.Server.CHAT) {
				@Override
	            public void onPacketSending(PacketEvent event) {
	                if (event.getPacketType() == PacketType.Play.Server.CHAT) {
	                	
	            	    PacketContainer packet = event.getPacket();
	            	    List<WrappedChatComponent> components = packet.getChatComponents().getValues();
	            	
	            	    for (WrappedChatComponent component : components) {
	            	    	if (component != null) {
	            	    		getBread(event.getPlayer().getName()).getLocales().entrySet().stream().forEach(locales -> component.setJson(component.getJson().replace(locales.getKey(), locales.getValue())));
	            	    		packet.getChatComponents().write(components.indexOf(component), component);
	            	    	}
	            	    }
	                }
	             }
	         }
		);
		
	}
    
}
