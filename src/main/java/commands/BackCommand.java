package commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.BrainBungee;

import modules.TeleportManager;

public class BackCommand implements CommandExecutor{
	
	BrainBungee plugin;
	String PERMISSION;
	
	public BackCommand(BrainBungee plugin, String permission) {
		this.plugin = plugin;
		PERMISSION = permission;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String arg, String[] args) {
		if(sender.hasPermission(PERMISSION)) {
			Player p = Bukkit.getPlayer(sender.getName());
			TeleportManager tm = new TeleportManager(plugin);
			tm.teleport(p.getName(), tm.getLastPoint(p.getName()));
			p.sendMessage("Oohoow!");
			return true;
		}else {
			sender.sendMessage(ChatColor.RED + "Require " + PERMISSION);
			return true;
		}
	}

}
