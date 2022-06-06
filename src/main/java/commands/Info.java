package commands;

import com.BrainBungee;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import objects.BreadMaker;

public class Info extends Command {

	private BrainBungee plugin;
	
    public Info(BrainBungee plugin) {
		super("player");
    	this.plugin = plugin;
	}

	public void execute(CommandSender sender, String[] args) {
        if ((sender instanceof ProxiedPlayer)) {
        	ProxiedPlayer p = (ProxiedPlayer) sender;
        	
        	BreadMaker bread = new BreadMaker(plugin, p.getName());
        	
			p.sendMessage(new ComponentBuilder ("Рівень "+bread.getData("level")).color(ChatColor.RED).create());  
	    }
	}
}
