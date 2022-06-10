package commands;

import org.bukkit.command.CommandExecutor;

import com.BrainBungee;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import objects.BreadMaker;

public class Info extends Command implements CommandExecutor  {

	private BrainBungee plugin;
	private String PERMISSION;
	
	
    public Info(BrainBungee plugin, String permission) {
		super("player");
    	this.plugin = plugin;
    	this.PERMISSION = permission;
	}

	public void execute(CommandSender sender, String[] args) {
		if (sender.hasPermission(PERMISSION)) {
	        if ((sender instanceof ProxiedPlayer)) {
	        	ProxiedPlayer p = (ProxiedPlayer) sender;
	        	
	        	BreadMaker bread = new BreadMaker(plugin, p.getName());
	        	
				p.sendMessage(new ComponentBuilder ("Рівень "+bread.getData("level")).color(ChatColor.RED).create());  
		    }
		}
	}

	@Override
	public boolean onCommand(org.bukkit.command.CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {

		if (sender.hasPermission(PERMISSION)) {
		
			
			
			
		}
		
		return true;
	}
}
