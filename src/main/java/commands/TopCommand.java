package commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.BrainBungee;

public class TopCommand implements CommandExecutor{
	
	BrainBungee plugin;
	String PERMISSION;
	
	public TopCommand(BrainBungee plugin, String permission) {
		this.plugin = plugin;
		PERMISSION = permission;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String arg, String[] args) {
		if(sender.hasPermission(PERMISSION)) {
			Player p = Bukkit.getPlayer(sender.getName());
			Location loop = new Location(p.getLocation().getWorld(), p.getLocation().getBlockX(),
					p.getLocation().getWorld().getMaxHeight(), p.getLocation().getBlockZ(),
					p.getLocation().getYaw(), p.getLocation().getPitch());
			while(!loop.getBlock().getType().isSolid()) {
				loop.add(0,-1,0);
			}
			loop.add(0,1,0);
			p.teleport(loop);
			p.sendMessage(ChatColor.AQUA+"Woohoo!");
			return true;
		}else {
			sender.sendMessage(ChatColor.RED + "Require " + PERMISSION);
			return true;
		}
	}

}
