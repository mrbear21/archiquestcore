package commands;

import java.util.Arrays;

import com.BrainBungee;

import modules.Locales;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import objects.BreadMaker;

public class LanguageCommand extends Command {

	private BrainBungee bungee;
	
    public LanguageCommand(BrainBungee plugin) {
		super("language");
    	this.bungee = plugin;
	}

	public void execute(CommandSender sender, String[] args) {
		
	     if ((sender instanceof ProxiedPlayer)) {
	    	 ProxiedPlayer p = (ProxiedPlayer) sender;
	    	 Locales locales = new Locales(bungee);
	    	 if (args.length>0 && Arrays.asList(locales.languages).contains(args[0])) {
		    	 BreadMaker bread = bungee.getBread(p.getName());
		    	 bread.setData("language", args[0], true);
		    	 p.sendMessage(new ComponentBuilder (new Locales(bungee).translateString("archiquest.selected.language " + args[0], args[0])).create()); 
	    	 } else {
	    		 p.sendMessage(new ComponentBuilder (new Locales(bungee).translateString("archiquest.choose.language " + String.join(" | ", new Locales(bungee).languages), "en")).create());
	    	 }
	     }

	}

}
