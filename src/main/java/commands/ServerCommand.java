package commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import brain.BrainSpigot;

public class ServerCommand implements CommandExecutor {

	private BrainSpigot spigot;
	
    public ServerCommand(BrainSpigot spigot) {
    	this.spigot = spigot;
	}

	public void register() {
		spigot.getCommand("hub").setExecutor(new ServerCommand(spigot));
		spigot.getCommand("creative").setExecutor(new ServerCommand(spigot));
		spigot.getCommand("survival").setExecutor(new ServerCommand(spigot));
		spigot.getCommand("minigames").setExecutor(new ServerCommand(spigot));
	}
    
    @Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player player = (Player) sender;
		
	    ByteArrayDataOutput out = ByteStreams.newDataOutput();

     	out.writeUTF("Connect");
     	out.writeUTF(cmd.getName().toLowerCase());

		player.sendMessage("archiquest.connection.to.server " + cmd.getName()+"...");
		
		player.sendPluginMessage(spigot, "BungeeCord", out.toByteArray());

		return true;
	}
    
}
