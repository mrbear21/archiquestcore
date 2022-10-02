package listeners;

import java.awt.Color;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import com.BrainBungee;
import com.BrainSpigot;
import com.SystemMessage;
import com.Utils;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import fun.CharliesComeback;
import modules.Chat;
import modules.Discord;
import modules.Locales;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import objects.BreadMaker;
import objects.ChatMessage;

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


		if (subchannel.equals("chat:archiquest")) {
			
			String command = in.readUTF();
			
			if (command.equals("id")) {
				
				int id = Integer.valueOf(in.readUTF())+1;
				
				spigot.MESSAGE_ID = id > spigot.MESSAGE_ID ? id : spigot.MESSAGE_ID;
				
			} else if (command.equals("new"))  {
				
				String chat = in.readUTF();
				String playername = in.readUTF();
				String chatmessage = in.readUTF();
				String language = in.readUTF();
						
				new Chat(spigot).newMessage(new ChatMessage().setChat(chat).setId(String.valueOf(spigot.MESSAGE_ID)).setMessage(chatmessage).setPlayer(playername).setLanguage(language));
				new Chat(spigot).updateMessageId();
				
			} else if (command.equals("delete") || command.equals("undo"))  {
					
					String id = in.readUTF();
					new Chat(spigot).editMessage(id, command);
					
				}
		}
        
        if (subchannel.equals("playerdata:archiquest")) {
        	
            String name = in.readUTF();
            String option = in.readUTF();
            String value = in.readUTF();
            
            BreadMaker bread = spigot.getBread(name);
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
            
            spigot.getLocalesFile().set(lang+"."+key.replace(".", "%1"), locale);
            
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
		
		if (subchannel.equals("chat:archiquest") && command.equals("delete") || command.equals("undo")) {
			
			String id = in.readUTF();
			new SystemMessage(bungee).newMessage("chat", new String[] {command, id});
			
		}
		
		if (subchannel.equals("chat:archiquest") && command.equals("id")) {
			
			String id = in.readUTF();
			new SystemMessage(bungee).newMessage("chat", new String[] {"id", id});
			
		}
		
		if (subchannel.equals("chat:archiquest") && command.equals("charlie")) {
			
			String phrase = in.readUTF();
			new CharliesComeback(bungee).checkPhrase(phrase);
			
		}
		
		if (subchannel.equals("chat:archiquest") && command.equals("proxy")) {
			
			String chat = in.readUTF();
			String playername = in.readUTF();
			String chatmessage = in.readUTF();
			String language = in.readUTF();
			String alphabet = new Utils().checkAlphabet(chatmessage);
			
			HashMap<String, Integer> languages = new HashMap<String, Integer>();
			
			if (!alphabet.equals("latin")) {
				bungee.charliepatterns.entrySet().stream().forEach(pattern -> {
					Arrays.asList(chatmessage.split(" ")).stream().forEach(word -> { 
						if (word.length() > 2 && pattern.getKey().toLowerCase().contains(word.toLowerCase())) {
							languages.put(pattern.getValue().get(0), languages.containsKey(pattern.getValue().get(0)) ? languages.get(pattern.getValue().get(0))+1 : 1);
							bungee.log(chatmessage + " > " +word+ " (" +pattern.getValue().get(0) + languages.get(pattern.getValue().get(0))+")");
						}
					});
				});
				if (languages.size() > 0) {
					Map<String, Integer> sorted = languages.entrySet().stream().sorted(Collections.reverseOrder(Map.Entry.comparingByValue())).collect( Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));
					language = sorted.keySet().iterator().next();
				}
			}
			
			new SystemMessage(bungee).newMessage("chat", new String[] {"new", chat, playername, chatmessage, language});
			
			
			
			JDA jda = new Discord(bungee).getJda();
			
			if (jda != null) {
			
				jda.getTextChannelById(bungee.getConfig().getString("discord.chats.log-chat")).sendMessage("```["+chat+"] "+playername+": "+chatmessage+"```").queue();
				
				
				EmbedBuilder builder = new EmbedBuilder();
					builder.setAuthor(playername, null, "https://minotar.net/helm/" + playername);
					builder.setTitle(chatmessage, null);
					
				switch (chat) {
				
					case "global":
						builder.setColor(Color.decode("#f1c40f"));
						jda.getTextChannelById(bungee.getConfig().getString("discord.chats.server-chat-en")).sendMessageEmbeds(builder.build()).queue();
						jda.getTextChannelById(bungee.getConfig().getString("discord.chats.server-chat-ru")).sendMessageEmbeds(builder.build()).queue();
						break;
					case "admin":
						builder.setColor(Color.decode("#e74c3c"));
						jda.getTextChannelById(bungee.getConfig().getString("discord.chats.admin-chat")).sendMessageEmbeds(builder.build()).queue();
						break;
					case "question":
						builder.setColor(Color.decode("#e74c3c"));
						builder.setAuthor(playername+" запитує:", null, "https://minotar.net/helm/" + playername);
						jda.getTextChannelById(alphabet.equals("latin") ? bungee.getConfig().getString("discord.chats.question-chat-en") : language.contains("ua") ? bungee.getConfig().getString("discord.chats.question-chat") : bungee.getConfig().getString("discord.chats.question-chat-ru")).sendMessageEmbeds(builder.build()).queue();;
						break;
				}

			}
			return;
		}
		
		if (subchannel.equals("locale:archiquest") && command.equals("get")) {
			new Locales(bungee).initialise();
			return;
		}
		if (subchannel.equals("playerdata:archiquest") && command.equals("get")) {
			String name = in.readUTF();
			bungee.getBread(name).loadData();
			return;
		}
		if (subchannel.equals("playerdata:archiquest") && command.equals("set")) {
			String name = in.readUTF();
			String option = in.readUTF();
			String value = in.readUTF();
			bungee.getBread(name).setData(option, value).save();
			return;
		}
	}
	
}
