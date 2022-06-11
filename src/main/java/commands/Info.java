package commands;

import java.io.IOException;

import org.bukkit.command.CommandExecutor;

import com.BrainBungee;
import com.BrainSpigot;
import com.SystemMessage;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import objects.BreadMaker;

public class Info extends Command implements CommandExecutor  {

	private BrainBungee bungee;
	private String PERMISSION;
	private BrainSpigot spigot;
	
	
    public Info(BrainBungee plugin, String permission) {
		super("player");
    	this.bungee = plugin;
    	this.PERMISSION = permission;
	}
    
    public Info(BrainSpigot plugin, String permission) {
		super("player");
    	this.spigot = plugin;
    	this.PERMISSION = permission;
	}

	public void execute(CommandSender sender, String[] args) {
		if (sender.hasPermission(PERMISSION)) {
	        if ((sender instanceof ProxiedPlayer)) {
	        	ProxiedPlayer p = (ProxiedPlayer) sender;
	        	
	        	BreadMaker bread = new BreadMaker(bungee, p.getName());
	        	
				p.sendMessage(new ComponentBuilder ("Рівень "+bread.getData("level")).color(ChatColor.RED).create());  
		    }
		}
	}

	@Override
	public boolean onCommand(org.bukkit.command.CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {

		if (sender.hasPermission(PERMISSION)) {
			
			SystemMessage sysMsg = new SystemMessage(spigot);
			String data;
			try {
				data = sysMsg.getPlayerData(sender.getName(), "level");
				sender.sendMessage("Рівень "+data);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
		return true;
	}
}
