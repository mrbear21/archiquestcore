package commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import com.BrainSpigot;

import net.md_5.bungee.api.ChatColor;
import objects.Cooldown;

public class ArchiQuestCommand implements CommandExecutor, Listener {

	private BrainSpigot spigot;


	public ArchiQuestCommand(BrainSpigot spigot) {
		this.spigot = spigot;
	}
	
	public void register() {
		spigot.getServer().getPluginManager().registerEvents(this, spigot);
		spigot.getCommand("archiquest").setExecutor(this);
	}

	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		Player player = (Player) sender;
	//	BreadMaker bread = spigot.getBread(sender.getName());

		Cooldown cooldown = new Cooldown(spigot, sender.getName());
		
 		if (cooldown.hasCooldown(command.getName())) {
 			sender.sendMessage("archiquest.waitcooldown "+((cooldown.getTimeLeft(command.getName())/ 1000)) +" sec");
 			return true;
		}
		
 		if (args.length == 0) {
 			player.sendMessage(ChatColor.LIGHT_PURPLE+"mrbear22 бажає тобі гарного дня!");
 			return true;
 		}
 		
 		if (sender.hasPermission("archiquest."+args[0].toLowerCase())) {
			switch(args[0].toLowerCase()) {
			
				case "reload":
					
					spigot.reloadConfig();
					player.sendMessage("Config reloaded");
					return true;
			}
		}
 		
		return true;
	}

}
