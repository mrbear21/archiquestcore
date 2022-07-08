package listeners;

import java.awt.Color;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import com.BrainBungee;
import com.BrainSpigot;
import com.SystemMessage;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import modules.Chat;
import modules.Discord;
import modules.Locales;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
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

		if (subchannel.equals("chat:archiquest")) {
			
			String chat = in.readUTF();
			String playername = in.readUTF();
			String chatmessage = in.readUTF();
			
			new Chat(spigot).newMessage(chat, playername, chatmessage);
		}
        
        if (subchannel.equals("playerdata:archiquest")) {
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
		
		if (subchannel.equals("chat:archiquest")) {
			
			String chat = in.readUTF();
			String playername = in.readUTF();
			String chatmessage = in.readUTF();
			String discordchannel = "";

			new SystemMessage(bungee).newMessage("chat", new String[] {chat, playername, chatmessage});
			
			JDA jda = new Discord(bungee).getJda();
			if (jda != null) {
			
			EmbedBuilder builder = new EmbedBuilder();
				builder.setAuthor(playername, null, "https://minotar.net/helm/" + playername);
				builder.setTitle(chatmessage, null);
				switch (chat) {
					case "global":
						builder.setColor(Color.decode("#f1c40f"));
						discordchannel = "993474180538433676";
						break;
					case "admin":
						builder.setColor(Color.decode("#e74c3c"));
						discordchannel = "993476444883796019";
						break;
				}

				jda.getTextChannelById(discordchannel).sendMessageEmbeds(builder.build()).queue();
			}
			return;
		}
		
		
		String command = in.readUTF();
		if (subchannel.equals("locale:archiquest") && command.equals("get")) {
			try {
				new Locales(bungee).initialiseLocales();
			} catch (SQLException | IOException e) {
				e.printStackTrace();
			}
		}
		if (subchannel.equals("playerdata:archiquest") && command.equals("get")) {
			String name = in.readUTF();
			new BreadMaker(bungee).getBread(name).loadData();
		}
		if (subchannel.equals("playerdata:archiquest") && command.equals("set")) {
			String name = in.readUTF();
			String option = in.readUTF();
			String value = in.readUTF();
			try {
				new BreadMaker(bungee).getBread(name).insertData(option, value);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
}
