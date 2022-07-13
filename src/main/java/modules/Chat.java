package modules;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.json.JSONException;

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

import integrations.Google;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import objects.BreadMaker;
import objects.ChatMessage;
import objects.MessagesHistory;

public class Chat implements Listener, CommandExecutor {
	
	public BrainSpigot spigot;
	private ProtocolManager manager;
	
	public Chat(BrainSpigot spigot) {
		this.spigot = spigot;
		manager = ProtocolLibrary.getProtocolManager();
	}
	
	public void initialize() {
		spigot.getServer().getPluginManager().registerEvents(new Chat(spigot), spigot);
		registerLocalesListener();
		spigot.getCommand("chat").setExecutor(this);
	}

	public void editMessage(String message_id, String option) {
		editMessage(message_id, option, "");
	}
	
	public void editMessage(String message_id, String option, String individual) {

		for (Entry<String, List<String[]>> h : spigot.chathistory.entrySet()) {

			List<String[]> history = h.getValue();
	
			Player p = Bukkit.getPlayer(h.getKey());
			
			for (int j = 0; j<100; j++) {
				p.sendMessage("");
			}
			
			int size = history.size();
			for (int i = size < 20 ? 0 : size - 20; i < size; i++) {
				String chat = history.get(i)[0];
				String player = history.get(i)[1];
				String message = history.get(i)[2];
				String status = history.get(i)[3];
				String id = history.get(i)[4];
				
				if (message_id.equals(id)) {
					switch (option.toLowerCase()) {	
						case "undo": 
							status = "";
							break;
						case "delete": 
							status = "deleted";
							break;
						case "translate": 
							if (individual.equals(h.getKey())) {
								status = "translated";
								try {
									String lang = spigot.getBread(p.getName()).getLanguage().equals("ua") ? "uk" : spigot.getBread(p.getName()).getLanguage();
									message = Google.translate(lang, message);
								} catch (IOException | JSONException e) {
									e.printStackTrace();
								}
							}
							break;
					}
					history.set(i, new String[] {chat, player, message, status, id});
	
				}
				try {
					p.spigot().sendMessage(chat.equals("") ? new TextComponent(ComponentSerializer.parse(message)) : getChatComponent(p, new ChatMessage(new String[] {chat, player, message, status, id})));
				} catch (Exception c) {
					c.printStackTrace();
				}
			}

			spigot.chathistory.put(h.getKey(), history);
		}
			
		
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (args.length < 2) { return false; }

		if (args[0].equals("translate")) {
			editMessage(args[1], args[0], sender.getName());
		} else {
			new SystemMessage(spigot).newMessage("chat", args);
		}
		
		return true;
	}
    
	private ChatColor getColor(String chat) {
		switch(chat) {
			case "global": return ChatColor.YELLOW;
			case "discord_admin": return ChatColor.RED;
			case "admin": return ChatColor.RED;
			case "roleplay": return ChatColor.DARK_PURPLE;
			case "plot": return ChatColor.WHITE;
			case "discord": return ChatColor.AQUA;
			case "spy": return ChatColor.GRAY;
			case "support": return ChatColor.RED;
			case "faction": return ChatColor.GREEN;
		}
		return ChatColor.WHITE;
	}
	
	private String getLangWritingSystem(String lang) {
		switch (lang) {
			case "ua": return "cyrillic";
			case "by": return "cyrillic";
			case "ru": return "cyrillic";
			case "lv": return "latin";
			case "en": return "latin";
		}
		return "latin";
	}
	
	private String latin = "qwertyuiopasdfghjklzxcvbnm", cyrillic = "йцукенгшщзхїфівапролджєячсмитьбю";
	
	public String checkAlphabet(String message) {
		int l = 0, c = 0;
		for (String s : message.split("")) {
			if (latin.contains(s)) { l++; }
			if (cyrillic.contains(s)) { c++; }
		}
		return l > c ? "latin" : "cyrillic";
	}
	
	@SuppressWarnings("deprecation")
	public TextComponent getChatComponent(Player p, ChatMessage message) {

		BreadMaker bread = spigot.getBread(message.getPlayer());

		TextComponent textComponent = new TextComponent();
		
		if (p.hasPermission("archiquest.adminchat")) {
			TextComponent delete = new TextComponent(ChatColor.RED+"✗ ");
				delete.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Delete message").create()));
				delete.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/chat delete " + message.getId()));
			TextComponent restore = new TextComponent(ChatColor.GREEN+"⤾ ");
				restore.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Undo").create()));
				restore.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/chat undo " + message.getId()));	
			textComponent.addExtra(message.getStatus().equals("deleted") ? restore : delete);
		}

		textComponent.addExtra(ChatColor.GRAY+"["+getColor(message.getChat())+String.valueOf(message.getChat().charAt(0)).toUpperCase()+ChatColor.GRAY+"]");
		textComponent.addExtra(bread.getPrefix()+message.getPlayer());
		textComponent.addExtra(getColor(message.getChat())+": ");
		textComponent.addExtra(message.getStatus().equals("deleted") ? ChatColor.GRAY+""+ChatColor.ITALIC+"<message removed>" : getColor(message.getChat())+message.getMessage());

		if (message.getStatus().equals("") && !p.getName().equals(message.getPlayer()) && !checkAlphabet(message.getMessage()).equals(getLangWritingSystem(spigot.getBread(p.getName()).getLanguage()))) {
			TextComponent translate = new TextComponent(" [Translate]");
				translate.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Translate message").create()));
				translate.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/chat translate " + message.getId()));
				translate.setColor(ChatColor.GRAY);
			textComponent.addExtra(translate);
		}
		
		return textComponent;
	}
	
	public void newMessage(ChatMessage message) {

		List<Player> players = new ArrayList<Player>();
		
		switch (message.getChat().equals("discord") ? "global" : message.getChat().equals("discord_admin") ? "admin" : message.getChat()) {
		
			case "global": 
				Bukkit.getOnlinePlayers().stream().forEach(p -> players.add(p));
				break;
			case "admin":
				Bukkit.getOnlinePlayers().stream().filter(p -> p.hasPermission("archiquest.adminchat")).forEach(p -> players.add(p));
				break;
			case "local":
				Bukkit.getOnlinePlayers().stream().filter(p -> p.getWorld() == Bukkit.getPlayer(message.getPlayer()).getWorld() && p.getLocation().distance(Bukkit.getPlayer(message.getPlayer()).getLocation()) < 200).forEach(p -> players.add(p));
				break;
		}
		
		for (Player p : players) {
			TextComponent textComponent = getChatComponent(p, message);
			new MessagesHistory(spigot).add(p.getName(), message);
			p.spigot().sendMessage(textComponent);
		}
		
		spigot.log("["+String.valueOf(message.getChat().charAt(0)).toUpperCase()+"] "+message.getPlayer()+": "+message.getMessage());

	}
	

	@EventHandler(priority = EventPriority.LOWEST)
	public void onChat(AsyncPlayerChatEvent event) {
		
		event.setCancelled(true);
		Player player = event.getPlayer();
		String message = event.getMessage();
		
		if (message.length() == 0) {
			return;
		}
		
		if (player.hasPermission("archiquest.chat.color")) {
			message = ChatColor.translateAlternateColorCodes('&', message);
		}

		switch (String.valueOf(message.charAt(0))) {
			case "!":
				new SystemMessage(spigot).newMessage("chat", new String[] {"proxy", "global", player.getName(), message.substring(1)});
				break;
			case "\\":
				new SystemMessage(spigot).newMessage("chat", new String[] {"proxy", "admin", player.getName(), message.substring(1)});
				break;
			default:
				newMessage(new ChatMessage(new String[] {"local", player.getName(), message, "", String.valueOf(spigot.MESSAGE_ID)}));		
		}
		
		updateMessageId();
	}
	
	public void updateMessageId() {
		new SystemMessage(spigot).newMessage("chat", new String[] {"id", String.valueOf(spigot.MESSAGE_ID)});
	}
	
	public void registerLocalesListener() {
		
		new SystemMessage(spigot).newMessage("locale", new String[] {"get"});
		
		manager.addPacketListener(	
			new PacketAdapter(spigot, ListenerPriority.NORMAL, PacketType.Play.Server.CHAT) {
				@Override
	            public void onPacketSending(PacketEvent event) {
	                if (event.getPacketType() == PacketType.Play.Server.CHAT) {
	                	
	            	    PacketContainer packet = event.getPacket();
	            	    List<WrappedChatComponent> components = packet.getChatComponents().getValues();
	            	
	            	    for (WrappedChatComponent component : components) {
	            	    	if (component != null) {
	            	    		spigot.getBread(event.getPlayer().getName()).getLocales().entrySet().stream().forEach(locales -> component.setJson(component.getJson().replace(locales.getKey(), locales.getValue())));
	            	    		packet.getChatComponents().write(components.indexOf(component), component);
	            	    		new MessagesHistory(spigot).add(event.getPlayer().getName(), new ChatMessage(new String[] {"", event.getPlayer().getName(), new String(component.getJson().getBytes()), "", String.valueOf(spigot.MESSAGE_ID)}));
	            	    	}
	            	    }
	                }
	             }
	         }
		);
		
	}

}
