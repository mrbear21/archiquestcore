package commands;

import java.io.IOException;
import java.sql.SQLException;

import com.BrainBungee;

import modules.Locales;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class Info extends Command {

	private BrainBungee bungee;
//	private String PERMISSION;
	
	
    public Info(BrainBungee plugin, String permission) {
		super("locale");
    	this.bungee = plugin;
    //	this.PERMISSION = permission;
	}

	public void execute(CommandSender sender, String[] args) {
		
	     if ((sender instanceof ProxiedPlayer)) {
	    	 try {
				new Locales(bungee).initialiseLocales();
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
	    	 
	    	 ProxiedPlayer p = (ProxiedPlayer) sender;
				        	
	    	 p.sendMessage(new ComponentBuilder ("Переклади оновлені").color(ChatColor.RED).create()); 
	     }
		
		
		
		
		//if (sender.hasPermission(PERMISSION)) {
	    //    if ((sender instanceof ProxiedPlayer)) {
	    //    	ProxiedPlayer p = (ProxiedPlayer) sender;
	        	
	     //   	BreadMaker bread = new BreadMaker(bungee, p.getName());
	        	
		//		p.sendMessage(new ComponentBuilder ("Рівень "+bread.getData("level")).color(ChatColor.RED).create());  
		 //   }
		//}
	}

}
