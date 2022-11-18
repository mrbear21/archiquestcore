package modules;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.json.JSONException;

import com.BrainSpigot;
import com.SystemMessage;
import com.Utils;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.StructureModifier;
import com.comphenix.protocol.wrappers.WrappedChatComponent;

import integrations.AuthmeAPI;
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
	
	public void register() {
		spigot.getServer().getPluginManager().registerEvents(this, spigot);
		spigot.getCommand("chat").setExecutor(this);
		spigot.getCommand("rp").setExecutor(this);
		registerLocalesListener();
	}

	public void editMessage(String message_id, String option) {
		editMessage(message_id, option, "");
	}
	
	public void editMessage(String message_id, String option, String individual) {

		HashMap<String, List<String[]>> entry = spigot.chathistory;
		if (!individual.equals("")) {
			entry = new HashMap<String, List<String[]>>();
			entry.put(individual, spigot.chathistory.get(individual));
		}
		
		for (Entry<String, List<String[]>> h : entry.entrySet()) {

			List<String[]> history = h.getValue();
	
			Player p = Bukkit.getPlayer(h.getKey());
			
			if (p != null) {
			
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
					String language = history.get(i)[5];
					
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
						history.set(i, new String[] {chat, player, message, status, id, language});
		
					}
					try {
						p.spigot().sendMessage(chat.equals("") ? new TextComponent(ComponentSerializer.parse(message)) : getChatComponent(p, new ChatMessage(new String[] {chat, player, message, status, id, language})));
					} catch (Exception c) {
						c.printStackTrace();
					}
				}
	
				spigot.chathistory.put(h.getKey(), history);
			
			}
		}

	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (command.getName().equalsIgnoreCase("rp")) {
		
			Player player = (Player) sender;
		
			if (args.length == 0) { 
				player.sendMessage("");
				player.sendMessage("/rp <text>");
				player.sendMessage("/me <text>");
				player.sendMessage("/do <text>");
				player.sendMessage("/n <text>");
				player.sendMessage("/ws <text>");
				player.sendMessage("/c <text>");
				player.sendMessage("/try <text>");
				player.sendMessage("");
				return true;
			}
			
			switch (label) {
				
				case "rp": 
					
					String message = "&7[&5R&7] " +ChatColor.GRAY + String.join(" ", args);
					message = ChatColor.translateAlternateColorCodes('&', message);
					for (Player p : Bukkit.getOnlinePlayers()) {
					      if (p.getWorld() == player.getWorld() && p.getLocation().distance(player.getLocation()) <= 5.0D) {  
					    	  p.sendMessage(message);
					      }
					}
					return true;
				case "ws": 
					message = "&7[&5R&7] " +ChatColor.GRAY + sender.getName()+ChatColor.GRAY + " archiquest.whisper: " + String.join(" ", args); 
					message = ChatColor.translateAlternateColorCodes('&', message);
					for (Player p : Bukkit.getOnlinePlayers()) {
					      if (p.getWorld() == player.getWorld() && p.getLocation().distance(player.getLocation()) <= 5.0D) {  
					    	  p.sendMessage(message);
					      }
					}
					return true;				
				case "me":
		    	    message = "&7[&5R&7] " +ChatColor.LIGHT_PURPLE + " *" + sender.getName() + "&d " + String.join(" ", args) + "&d *";  
					message = ChatColor.translateAlternateColorCodes('&', message);
					for (Player p : Bukkit.getOnlinePlayers()) {
					      if (p.getWorld() == ((Entity) sender).getWorld() && p.getLocation().distance(((Entity) sender).getLocation()) <= 500.0D) {  
					    	  p.sendMessage(message);
					      }
					}
					return true;
				case "do":
					message = "&7[&5R&7] &3" + sender.getName()+"&3 " + String.join(" ", args);  
					message = ChatColor.translateAlternateColorCodes('&', message);
					for (Player p : Bukkit.getOnlinePlayers()) {
					      if (p.getWorld() == ((Entity) sender).getWorld() && p.getLocation().distance(((Entity) sender).getLocation()) <= 500.0D) {  
					    	  p.sendMessage(message);
					      }
					}
					return true;
				case "n":
					for (Player p : Bukkit.getOnlinePlayers()) {
						message = "&7[&5R&7] " +ChatColor.GRAY + "" + ChatColor.ITALIC + " (" + sender.getName() + ChatColor.GRAY + "" + ChatColor.ITALIC + String.join(" ", args) + "&7&o)";
						message = ChatColor.translateAlternateColorCodes('&', message);
						if (p.getWorld() == ((Entity) sender).getWorld()
								&& p.getLocation().distance(((Entity) sender).getLocation()) <= 500.0D) {
							p.sendMessage(message);
						}
					}
					return true;			
				case "c":
					for (Player p : Bukkit.getOnlinePlayers()) {
						message = "&7[&5R&7] " +ChatColor.YELLOW + sender.getName() +"&e"+ " archiquest.shout: " + String.join(" ", args);
						message = ChatColor.translateAlternateColorCodes('&', message);
						if (p.getWorld() == ((Entity) sender).getWorld() && p.getLocation().distance(((Entity) sender).getLocation()) <= 500.0D) {  
							p.sendMessage(message);
						}
					}
					return true;	
				case "try":
		    	    int prob = new Random().nextInt((2));
					String prob2;
		    	    if (prob == 1) { prob2 = "archiquest.lucky";
		    	    } else { prob2 = "archiquest.unlucky"; }
		    	    
		    	    message = "&7[&5R&7] &b" + sender.getName() +"&b "+  String.join(" ", args) + " &b("+prob2+"&b)"; 
					message = ChatColor.translateAlternateColorCodes('&', message);
					
					for (Player p : Bukkit.getOnlinePlayers()) {
					      if (p.getWorld() == ((Entity) sender).getWorld() && p.getLocation().distance(((Entity) sender).getLocation()) <= 500.0D) {  
					    	  p.sendMessage(message);
					      }
					}
					return true;
			}
		
		} else if (command.getName().equalsIgnoreCase("chat")) {
		
			if (args.length < 2) { return false; }
	
			if (args[0].equals("translate")) {
				editMessage(args[1], args[0], sender.getName());
			} else if (sender.hasPermission("archiquest.chat."+args[0])) {
				new SystemMessage(spigot).newMessage("chat", args);
			} else {
				sender.sendMessage("archiquest.no_permission");
			}
			
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
			case "question": return ChatColor.GOLD;
			case "faction": return ChatColor.GREEN;
		}
		return ChatColor.WHITE;
	}
	
	@SuppressWarnings("deprecation")
	public TextComponent getChatComponent(Player p, ChatMessage message) {

		BreadMaker bread = spigot.getBread(message.getPlayer());

		TextComponent textComponent = new TextComponent();
		
		if (p != null && p.hasPermission("archiquest.chat.admin")) {
			TextComponent delete = new TextComponent(ChatColor.RED+"✗ ");
				delete.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Delete message").create()));
				delete.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/chat delete " + message.getId()));
			TextComponent restore = new TextComponent(ChatColor.GREEN+"⤾ ");
				restore.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Undo").create()));
				restore.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/chat undo " + message.getId()));	
			textComponent.addExtra(message.getStatus().equals("deleted") ? restore : delete);
		}

		textComponent.addExtra(ChatColor.GRAY+"["+getColor(message.getChat())+String.valueOf(message.getChat().charAt(0)).toUpperCase()+ChatColor.GRAY+"] ");
		
		if (p != null) {

			TextComponent player = new TextComponent(bread.getFactionPrefix()+bread.getPrefix()+message.getPlayer());
				player.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(spigot.getBread(p.getName()).getLocales().translateString("archiquest.click-to-pm")).create()));
				player.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/msg " + message.getPlayer()));
			textComponent.addExtra(player);
		}
		
		textComponent.addExtra(getColor(message.getChat())+": ");
		
		TextComponent chatmessage = new TextComponent();
		chatmessage.setColor(getColor(message.getChat()));
		chatmessage.addExtra(message.getStatus().equals("deleted") ? ChatColor.GRAY+""+ChatColor.ITALIC+"<message removed>" : getColor(message.getChat())+message.getMessage());
		if (message.getHoverText() != null) {
			chatmessage.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(message.getHoverText()).create()));
		}
		if (p != null && message.getStatus().equals("deleted") && p.hasPermission("archiquest.chat.admin")) {
			chatmessage.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Original: "+message.getMessage()).create()));
		}
		textComponent.addExtra(chatmessage);
		
		if (message.getMessage().split(" ").length > 3 && message.getStatus().equals("") && !p.getName().equals(message.getPlayer()) && !new Utils().checkAlphabet(message.getMessage()).equals(new Utils().getLangWritingSystem(spigot.getBread(p.getName()).getLanguage()))) {
			TextComponent translate = new TextComponent(" ["+spigot.getBread(p.getName()).getLocales().translateString("archiquest.translate")+"]");
				translate.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(spigot.getBread(p.getName()).getLocales().translateString("archiquest.translate")).create()));
				translate.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/chat translate " + message.getId()));
				translate.setColor(ChatColor.GRAY);
			textComponent.addExtra(translate);
		}
		

		
		return textComponent;
	}
	
	public void newMessage(ChatMessage message) {

		List<Player> players = new ArrayList<Player>();
		List<String> seen = new ArrayList<String>();
		
		message.setMessage(new Utils().translateSmiles(message.getMessage()));
		
		switch (message.getChat().equals("discord") ? "global" : message.getChat().equals("discord_admin") ? "admin" : message.getChat()) {
		
			case "global": 
				Bukkit.getOnlinePlayers().stream().forEach(p -> {players.add(p); seen.add(p.getName());});
				break;
			case "admin":
				Bukkit.getOnlinePlayers().stream().filter(p -> p.hasPermission("archiquest.chat.admin")).forEach(p -> {players.add(p); seen.add(p.getName());});
				break;
			case "question":
				Bukkit.getOnlinePlayers().stream().filter(p -> p.hasPermission("archiquest.chat.admin")).forEach(p -> {players.add(p); seen.add(p.getName());});
				if (!players.contains(spigot.chatquestion)) { players.add(spigot.chatquestion); }
				break;
			case "local":
				Bukkit.getOnlinePlayers().stream().filter(p -> p.getWorld() == Bukkit.getPlayer(message.getPlayer()).getWorld() && p.getLocation().distance(Bukkit.getPlayer(message.getPlayer()).getLocation()) < 500).forEach(p ->  {players.add(p); seen.add(p.getName());});
				break;
		}
		
		for (Player p : players) {
			if (message.getChat().equals("local")) {
				message.setHoverText(new Locales(spigot).translateString("archiquest.playersthatsawmsg", spigot.getBread(p.getName()).getLanguage())+": "+String.join(", ", seen));
			}
			if (spigot.version > 12) {
				TextComponent textComponent = getChatComponent(p, message);
				new MessagesHistory(spigot).add(p.getName(), message);
				p.spigot().sendMessage(textComponent);

				Location player_location = p.getLocation();

				switch( message.getChat() )
				{
					case "roleplay":
						p.playSound(player_location, Sound.UI_BUTTON_CLICK, 0.5f, 2f);
						break;
					case "admin":
					case "discord_admin": 
						p.playSound(player_location, Sound.BLOCK_NOTE_BLOCK_BIT, 0.5f, 2.0f);
						break;
					case "spy":
						p.playSound(player_location, Sound.ITEM_BOOK_PUT, 0.4f, 2.0f);
						break;
					case "faction":
						p.playSound(player_location, Sound.BLOCK_END_PORTAL_FRAME_FILL, 0.4f, 2.0f);
						break;
					case "question":
						p.playSound(player_location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.7f, 1.0f);
						break;
					case "local":
					case "plot":
						p.playSound(player_location, Sound.ENTITY_ITEM_PICKUP, 0.5f, 1.2f);
						break;
					case "global":
					case "discord": 
					default:
						p.playSound(player_location, Sound.BLOCK_BUBBLE_COLUMN_BUBBLE_POP, 0.5f, -5.0f);
						break;
				}
			} else {
				p.sendMessage(ChatColor.GRAY+"["+getColor(message.getChat())+String.valueOf(message.getChat().charAt(0)).toUpperCase()+ChatColor.GRAY+"] "+message.getPlayer()+": "+getColor(message.getChat())+message.getMessage());
			}
		}
		
		spigot.log("["+String.valueOf(message.getChat().charAt(0)).toUpperCase()+"] "+message.getPlayer()+": "+message.getMessage());

	}
	

	@EventHandler(priority = EventPriority.LOWEST)
	public void onChat(AsyncPlayerChatEvent event) {
		
		event.setCancelled(true);
		Player player = event.getPlayer();
		String message = event.getMessage();
		BreadMaker bread = spigot.getBread(player.getName());
		
		if (spigot.getServer().getPluginManager().isPluginEnabled("Authme") && new AuthmeAPI(spigot).isLoggedIn(player) == false) {
		//	event.getPlayer().sendMessage("authme.denied_chat");
		//	return;
		}
		
		if (message.length() == 0) {
			return;
		}
		
		if (player.hasPermission("archiquest.chat.color")) {
			message = message.replace("&", "§");
		} else {
			message = message.replace("§", "&");
		}

		switch (String.valueOf(message.charAt(0))) {
			case "!":
				new SystemMessage(spigot).newMessage("chat", new String[] {"proxy", "global", player.getName(), message.substring(1), bread.getLanguage()});
				new SystemMessage(spigot).newMessage("chat", new String[] {"charlie", message.substring(1)});
				break;
			case "\\":
				if (player.hasPermission("archiquest.chat.admin")) {
					new SystemMessage(spigot).newMessage("chat", new String[] {"proxy", "admin", player.getName(), message.substring(1), bread.getLanguage()});
					break;
				}
			case "?":
				if (message.length() > 5) {
					new SystemMessage(spigot).newMessage("chat", new String[] {"proxy", "question", player.getName(), message.substring(1), bread.getLanguage()});
					spigot.chatquestion = player;
					break;
				}
			default:
				newMessage(new ChatMessage(new String[] {"local", player.getName(), message, "", String.valueOf(spigot.MESSAGE_ID), bread.getLanguage()}));	
				new SystemMessage(spigot).newMessage("chat", new String[] {"charlie", message});
		}
		
		updateMessageId();
	}
	
	public void updateMessageId() {
		new SystemMessage(spigot).newMessage("chat", new String[] {"id", String.valueOf(spigot.MESSAGE_ID)});
	}
	
	public void registerLocalesListener() {
		

		manager.addPacketListener(
				new PacketAdapter(spigot, ListenerPriority.MONITOR, PacketType.Play.Server.SET_ACTION_BAR_TEXT) {
						@Override
			            public void onPacketSending(PacketEvent event) {


			                if (event.getPacketType() == PacketType.Play.Server.SET_ACTION_BAR_TEXT) {
			 			       //         PacketContainer packet = event.getPacket();
					       //     List<WrappedChatComponent> components = packet.getChatComponents().getValues();
					       //     for (WrappedChatComponent component : components) {
					        //    	spigot.log(component.getJson());

					        //    }
								  
			                }
							
						}
					}
				);
		
		
		manager.addPacketListener(
				new PacketAdapter(spigot, ListenerPriority.MONITOR, PacketType.Play.Server.SET_SLOT, PacketType.Play.Server.WINDOW_ITEMS) {
						@Override
			            public void onPacketSending(PacketEvent event) {

			                PacketContainer packet = event.getPacket();
			                
			                if (event.getPacketType() == PacketType.Play.Server.SET_SLOT) {
			                	
			                    ItemStack itemStack = packet.getItemModifier().read(0);
			                    ItemMeta itemMeta = itemStack.getItemMeta();
				                if (itemStack != null && itemMeta != null) {
					                BreadMaker bread = spigot.getBread(event.getPlayer().getName());
				                    String translate = itemMeta.getDisplayName() != null ? bread.getLocales().translateString(itemMeta.getDisplayName()) : itemMeta.getDisplayName();
				                    itemMeta.setDisplayName(translate);
				                    if (itemMeta.hasLore()) {
				                        List<String> lore = itemMeta.getLore();
				                        for (int i1 = 0; i1 < lore.size(); i1++) {
						                    translate = bread.getLocales().translateString(lore.get(i1));
				                            lore.set(i1, translate);
				                            itemMeta.setLore(lore);
				                        }
				                    }
				                    itemStack.setItemMeta(itemMeta);
				                }
				                packet.getItemModifier().write(0, itemStack);
				                
			                } else if (event.getPacketType() == PacketType.Play.Server.WINDOW_ITEMS) {
			                    StructureModifier<ItemStack[]> itemArrayModifier = packet.getItemArrayModifier();
			                    for (int i = 0; i < itemArrayModifier.size(); i++) {
			                        ItemStack[] itemStacks = itemArrayModifier.read(i);
			                        if (itemStacks != null) {
			                            for (int j = 0; j < itemStacks.length; j++) {
			                                ItemStack itemStack = itemStacks[i];
						                    ItemMeta itemMeta = itemStack.getItemMeta();
							                if (itemStack != null && itemMeta != null) {
								                BreadMaker bread = spigot.getBread(event.getPlayer().getName());
							                    String translate = itemMeta.getDisplayName() != null ? bread.getLocales().translateString(itemMeta.getDisplayName()) : itemMeta.getDisplayName();
							                    itemMeta.setDisplayName(translate);
							                    if (itemMeta.hasLore()) {
							                        List<String> lore = itemMeta.getLore();
							                        for (int i1 = 0; i1 < lore.size(); i1++) {
									                    translate = bread.getLocales().translateString(lore.get(i1));
							                            lore.set(i1, translate);
							                            itemMeta.setLore(lore);
							                        }
							                    }
							                    itemStack.setItemMeta(itemMeta);
							                }
			                            }
			                        }
			                    }
			                }
			                
			            //    event.setPacket(packet);
							
						}
					}
				);

		manager.addPacketListener(	
			new PacketAdapter(spigot, ListenerPriority.MONITOR, PacketType.Play.Server.CHAT) {
				@Override
	            public void onPacketSending(PacketEvent event) {
					
            	    PacketContainer packet = event.getPacket();
            	    BreadMaker bread = spigot.getBread(event.getPlayer().getName());

	                if (event.getPacketType() == PacketType.Play.Server.CHAT) {
	                	   
	            	    List<WrappedChatComponent> components = packet.getChatComponents().getValues();
	            	    for (WrappedChatComponent component : components) {

	            	    	if (component != null) {
	            	    		
	            	    		HashMap<String, String> locales = bread.getLocales().getLocalesMap();
	            	    		locales = locales.entrySet().stream().sorted(Collections.reverseOrder(Map.Entry.comparingByKey())).collect(Collectors.toMap(
	            	                    Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new ));
	            	    		
	            	    		locales.entrySet().stream().forEach(l ->
	            	    		component.setJson(component.getJson().replace(l.getKey(), ChatColor.GOLD + l.getValue()).replace("%nl%", System.lineSeparator())));
	            	    		packet.getChatComponents().write(components.indexOf(component), component);
	            	    		new MessagesHistory(spigot).add(event.getPlayer().getName(), new ChatMessage(new String[] {"", event.getPlayer().getName(), new String(component.getJson().getBytes()), "", String.valueOf(spigot.MESSAGE_ID), ""}));
	            	    	}
	            	    }
	                }
	             }
	         }
		);
		
	}

}
