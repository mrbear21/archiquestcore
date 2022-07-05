package com;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.Bukkit;

import com.google.common.collect.Iterables;

import listeners.SystemMessageReceiver;
import net.md_5.bungee.api.config.ServerInfo;

public class SystemMessage {

	BrainSpigot plugin;
	BrainBungee bungee;
	
	public SystemMessage(BrainSpigot plugin) {
		this.plugin = plugin;
	}
	
	public SystemMessage(BrainBungee bungee) {
		this.bungee = bungee;
	}
	

	public void updateLocales(String key, String lang, String locale) throws IOException {

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        out.writeUTF("locale:archiquest");
        out.writeUTF(key);
        out.writeUTF(lang);
        out.writeUTF(locale);
		
		for (Entry<String, ServerInfo> server : bungee.getProxy().getServers().entrySet()) {
			server.getValue().sendData("net:archiquest", stream.toByteArray());
		}

		bungee.log("відправлено запит: net:archiquest");
	}
	
	
	
	public String getPlayerData(String player, String option) throws IOException {

		String subChannel = "data:"+option+":"+player;

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        out.writeUTF(subChannel);
		
		Iterables.getFirst(Bukkit.getOnlinePlayers(), null).sendPluginMessage(plugin, "net:archiquest", stream.toByteArray());

		plugin.log("відправлено запит: "+subChannel);
		
		HashMap<String, String> messageReceiver = new SystemMessageReceiver(plugin).receivedMessages();
		while (messageReceiver.containsKey(subChannel)) {
			String result = messageReceiver.get(subChannel);
			messageReceiver.remove(subChannel);
			plugin.log("отримана выдповідь на запит "+subChannel+" - "+result);
			return result;
		}
		return "N\\A";
		
	}

	public void getLocales() throws IOException {
		
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        out.writeUTF("locale:archiquest");

        Iterables.getFirst(Bukkit.getOnlinePlayers(), null).sendPluginMessage(plugin, "net:archiquest", stream.toByteArray());
	}
	
}
