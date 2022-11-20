package modules;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.google.common.collect.Iterables;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import brain.BrainBungee;
import brain.BrainSpigot;
import net.md_5.bungee.api.config.ServerInfo;

public class SystemMessages {

	BrainSpigot plugin;
	BrainBungee bungee;
	private String servertype;
	
	public SystemMessages(BrainSpigot plugin) {
		this.plugin = plugin;
		this.servertype = "client";
	}
	
	public SystemMessages(BrainBungee bungee) {
		this.bungee = bungee;
		this.servertype = "proxy";
	}
	
	public void newMessage(String subchannel, String[] args) {
		try {
			if (servertype.equals("proxy")) {
	
		        ByteArrayOutputStream stream = new ByteArrayOutputStream();
		        DataOutputStream out = new DataOutputStream(stream);
		        
		        out.writeUTF(subchannel+":archiquest");
		        for (String arg : args) { out.writeUTF(arg);}
		        
				for (Entry<String, ServerInfo> server : bungee.getProxy().getServers().entrySet()) {
					server.getValue().sendData("net:archiquest", stream.toByteArray());
				}
			}
			if (servertype.equals("client")) {
	
				ByteArrayDataOutput out = ByteStreams.newDataOutput();
				out.writeUTF(subchannel+":archiquest");
				ByteArrayOutputStream msgbytes = new ByteArrayOutputStream();
				DataOutputStream msgout = new DataOutputStream(msgbytes);
				try { 
					
					for (String arg : args) { msgout.writeUTF(arg);}
	
				} catch (IOException exception) { exception.printStackTrace(); }
	
				out.write(msgbytes.toByteArray());
	
		        Player player = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);
		        if (player != null) {
		            player.sendPluginMessage(plugin, "net:archiquest", out.toByteArray());
		        }
			}
		} catch (Exception c) {
			c.printStackTrace();
		}
	}

	public void newMessage(String subchannel, String[] args, Player player) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF(subchannel+":archiquest");
		ByteArrayOutputStream msgbytes = new ByteArrayOutputStream();
		DataOutputStream msgout = new DataOutputStream(msgbytes);
		try { 
			
			for (String arg : args) { msgout.writeUTF(arg);}

		} catch (IOException exception) { exception.printStackTrace(); }

		out.write(msgbytes.toByteArray());

        if (player != null) {
            player.sendPluginMessage(plugin, "net:archiquest", out.toByteArray());
        }
	}


	
}
