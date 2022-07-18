package listeners;

import java.awt.Color;
import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import com.BrainBungee;
import com.BrainSpigot;
import com.SystemMessage;
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
			String discordchannel = "";
			
			new SystemMessage(bungee).newMessage("chat", new String[] {"new", chat, playername, chatmessage, language});
			
			JDA jda = new Discord(bungee).getJda();
			if (jda != null) {
			
			EmbedBuilder builder = new EmbedBuilder();
				builder.setAuthor(playername, null, "https://minotar.net/helm/" + playername);
				builder.setTitle(chatmessage, null);
				
				switch (chat) {
					case "global":
						builder.setColor(Color.decode("#f1c40f"));
						discordchannel = language.contains("ru") ? bungee.getConfig().getString("discord.chats.server-chat-ru") : bungee.getConfig().getString("discord.chats.server-chat");
						break;
					case "admin":
						builder.setColor(Color.decode("#e74c3c"));
						discordchannel = bungee.getConfig().getString("discord.chats.admin-chat");
						break;
					case "question":
						builder.setColor(Color.decode("#e74c3c"));
						builder.setAuthor(playername+" запитує:", null, "https://minotar.net/helm/" + playername);
						discordchannel = language.contains("ru") ? bungee.getConfig().getString("discord.chats.question-chat-ru") : bungee.getConfig().getString("discord.chats.question-chat");
						break;
				}

				jda.getTextChannelById(discordchannel).sendMessageEmbeds(builder.build()).queue();
				
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
