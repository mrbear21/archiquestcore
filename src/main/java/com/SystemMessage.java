package com;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;

import org.bukkit.Bukkit;

import com.google.common.collect.Iterables;

import listeners.SystemMessageReceiver;

public class SystemMessage {

	BrainSpigot plugin;
	
	public SystemMessage(BrainSpigot plugin) {
		this.plugin = plugin;
	}
	
	public String getPlayerData(String player, String option) throws IOException {

		String subChannel = "data:"+option+":"+player;

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        out.writeUTF(subChannel);
		
		Iterables.getFirst(Bukkit.getOnlinePlayers(), null).sendPluginMessage(plugin, "ArchiQuest", stream.toByteArray());

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
	
}
