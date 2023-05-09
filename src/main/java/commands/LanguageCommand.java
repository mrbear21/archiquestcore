package commands;

import java.util.Arrays;

import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;

import brain.BrainBungee;
import brain.BrainSpigot;
import modules.Localizations;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import objects.BreadMaker;
import objects.MenuBuilder;

public class LanguageCommand extends Command implements CommandExecutor {

	private BrainBungee bungee;
	private BrainSpigot spigot;
	
    public LanguageCommand(BrainBungee plugin) {
		super("language");
    	this.bungee = plugin;
	}
    
    public LanguageCommand(BrainSpigot spigot) {
		super("language");
    	this.spigot = spigot;
	}

    public void register() {
		spigot.getCommand("lang").setExecutor(this);
    }
    
	public void execute(CommandSender sender, String[] args) {
		
	     if ((sender instanceof ProxiedPlayer)) {
	    	 ProxiedPlayer p = (ProxiedPlayer) sender;
	    	 Localizations locales = new Localizations(bungee);
	    	 if (args.length>0 && Arrays.asList(locales.languages).contains(args[0])) {
		    	 BreadMaker bread = bungee.getBread(p.getName());
		    	 bread.setData("language", args[0]).save();
		    	 p.sendMessage(new ComponentBuilder (new Localizations(bungee).translateString("archiquest.selected.language " + args[0], args[0])).create()); 
	    	 } else {
	    		 p.sendMessage(new ComponentBuilder (new Localizations(bungee).translateString("archiquest.choose.language " + String.join(" | ", new Localizations(bungee).languages), "en")).create());
	    	 }
	     }

	}

	public void langSelector(Player player) {
		MenuBuilder menu = new MenuBuilder(spigot, player, "LANGUAGE");
		int i = 0; 

		for (String s : spigot.getConfig().getConfigurationSection("languages").getKeys(false)) {
			if (BrainSpigot.version > 12) {
				menu.setOption(spigot.getConfig().getString("languages."+s+".name"), i++, "lang "+s, spigot.getConfig().getItemStack("languages."+s+".icon"), new String[] {new Localizations(spigot).translateString("archiquest.click-to-select",s), "", "Author(s): " + (new Localizations(spigot).translateString("0authors.authors",s))});
			} else {
				menu.setOption(spigot.getConfig().getString("languages."+s+".name"), i++, "lang "+s, "paper", new String[] {new Localizations(spigot).translateString("archiquest.click-to-select",s), "", "Author(s): " + (new Localizations(spigot).translateString("0authors.authors",s))});
			}
		}
		menu.build();
	}

	
	
	@Override
	public boolean onCommand(org.bukkit.command.CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {

		Player player = (Player) sender;
		
		if (args.length == 0) {
			langSelector(player);
		} else {
			if (args[0].equals("seticon")) {
				spigot.getConfig().set("languages."+args[1]+".icon", player.getInventory().getItemInMainHand());
				player.sendMessage("ok");
				return true;
			}
			if (args[0].equals("setname")) {
				spigot.getConfig().set("languages."+args[1]+".name", args[2]);
				player.sendMessage("ok");
				return true;
			}
	    	 Localizations locales = new Localizations(spigot);
			 if (Arrays.asList(locales.languages).contains(args[0])) {
				spigot.getBread(player.getName()).setData("language", args[0]).save();
				player.sendMessage("archiquest.selected.language " + args[0]);
				return true;
			} else {
				return false;
			}

		}
		
		
		return true;
	}

}
