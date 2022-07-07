package commands;

import com.BrainBungee;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import objects.BreadMaker;

public class Language extends Command {

	private BrainBungee bungee;
	
    public Language(BrainBungee plugin) {
		super("language");
    	this.bungee = plugin;
	}

	public void execute(CommandSender sender, String[] args) {
		
	     if ((sender instanceof ProxiedPlayer)) {

	    	 ProxiedPlayer p = (ProxiedPlayer) sender;
				        
	    	 BreadMaker bread = new BreadMaker(bungee).getBread(p.getName());
	    	 
	    	 bread.setData("language", args[0]);
     	
	    	 p.sendMessage(new ComponentBuilder (new Locales(bungee).translateString("archiquest.selected.language" + args[0], args[0])).color(ChatColor.RED).create()); 
	    	 
	     }

	}

}
