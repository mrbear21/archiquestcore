package com;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.google.common.collect.Iterables;

import net.md_5.bungee.api.config.ServerInfo;

public class SystemMessage {

	BrainSpigot plugin;
	BrainBungee bungee;
	private String servertype;
	
	public SystemMessage(BrainSpigot plugin) {
		this.plugin = plugin;
		this.servertype = "client";
	}
	
	public SystemMessage(BrainBungee bungee) {
		this.bungee = bungee;
		this.servertype = "proxy";
	}
	
	public void getLocales() throws IOException {
		
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        out.writeUTF("locale:archiquest");
        out.writeUTF("get");

        Player player = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);
        
        if (player != null) {
            player.sendPluginMessage(plugin, "net:archiquest", stream.toByteArray());
        }
	}

	public void newMessage(String subchannel, String[] args) throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        out.writeUTF(subchannel+":archiquest");
        for (String arg : args) {
            out.writeUTF(arg);
        }
		if (servertype.equals("proxy")) {
			for (Entry<String, ServerInfo> server : bungee.getProxy().getServers().entrySet()) {
				server.getValue().sendData("net:archiquest", stream.toByteArray());
			}
		}
		if (servertype.equals("client")) {
	        Player player = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);
	        if (player != null) {
	            player.sendPluginMessage(plugin, "net:archiquest", stream.toByteArray());
	        }
		}
	}


	
}
