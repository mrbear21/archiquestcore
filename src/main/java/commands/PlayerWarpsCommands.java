package commands;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.TabCompleteEvent;

import com.BrainSpigot;

public class PlayerWarpsCommands implements CommandExecutor, Listener{
	
	private BrainSpigot spigot;
	String[] setPointCommands = {"c", "create", "s", "set"};
	String[] deletePointCommands = {"del", "delete", "rem", "remove"};
	String[] listPointCommands = {"list", "l"};
	
	public PlayerWarpsCommands(BrainSpigot spigot) {
		this.spigot = spigot;
	}
	
	public void register() {
		spigot.getCommand("warp").setExecutor(this);
		Bukkit.getPluginManager().registerEvents(this, spigot);
	}
	
	@EventHandler
	public void onTabComplete(TabCompleteEvent event) {
		String buffer = event.getBuffer();
		List<String> completions = event.getCompletions();
		if (buffer.equals("/w ") || buffer.equals("/warp ")) {
			completions.clear();
			completions.addAll(Arrays.asList("create", "delete", "list"));
		}
	}
	

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String arg, String[] args) {
		if(sender.hasPermission("archiquest.warp")) {
			File file = new File(spigot.getDataFolder()+"/warps.yml");
			if (!file.exists()) {
				try {
					spigot.getDataFolder().mkdir();
					file.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			FileConfiguration pwarps = YamlConfiguration.loadConfiguration(file);
			if(args.length == 0) {
				sender.sendMessage(ChatColor.AQUA+"/w [name] - òåëåïîðò íà ñâîþ òî÷êó [name]");
				sender.sendMessage(ChatColor.AQUA+"/w [player] [name] - òåëåïîðò íà òî÷êó [name], èãðîêà [player]");
				sender.sendMessage(ChatColor.AQUA+"/w list [player] - ñïèñîê òî÷åê èãðîêà [player]");
				sender.sendMessage(ChatColor.AQUA+"/w set [name] - óñòàíîâèòü òî÷êó [name]");
				sender.sendMessage(ChatColor.AQUA+"/w delete [name] - óäàëèòü òî÷êó [name]");
				return true;
			}else {
				Player p = Bukkit.getPlayer(sender.getName());
				if(isCreatingPoint(args[0])) {
					Location l = p.getLocation();
					List<String> list = pwarps.getStringList(p.getName());
					if(args.length>1) {
						for(String point : list) {
							if(point.substring(0, point.indexOf("%%")).equals(args[1])) {
								list.remove(point);
								break;
							}
						}
						list.add(args[1]+"%%"+l.getX()+"%%"+l.getY()+"%%"+l.getZ()+"%%"+l.getYaw()+"%%"+l.getPitch());
					}else
						list.add("default"+"%%"+l.getX()+"%%"+l.getY()+"%%"+l.getZ()+"%%"+l.getYaw()+"%%"+l.getPitch());
					pwarps.set(p.getName(), list);
					try {
						pwarps.save(file);
					} catch (IOException e) {
						e.printStackTrace();
					}
					// config //
					sender.sendMessage(ChatColor.AQUA+"Òî÷êó óñòàíîâëåíî.");
					return true;
				}else if(isDeletingPoint(args[0])){
					if(args.length < 2) {
						// config //
						sender.sendMessage(ChatColor.AQUA+"/pw delete [name]");
						return true;
					}else {
						List<String> list = pwarps.getStringList(p.getName());
						for(String point : list) {
							if(point.substring(0, point.indexOf("%%")).equals(args[1])) {
								list.remove(point);	
								pwarps.set(p.getName(), list);
								try {
									pwarps.save(file);
								} catch (IOException e) {
									e.printStackTrace();
								}
								// config //
								sender.sendMessage(ChatColor.AQUA+"Òî÷êó óäàëåíî");	
								return true;
							}
						}
						// config //
						sender.sendMessage(ChatColor.AQUA+"Èãðîê íå èìååò òàêîé òî÷êè");		
						return true;
					}
				}else if(isListPoint(args[0])){
					String loop = p.getName();
					if(args.length > 1)
						loop = args[1];
					List<String> list = pwarps.getStringList(loop);
					String res = "";
					for(String point : list) {
						res += point.substring(0, point.indexOf("%%")) + ", ";
					}
					if(list.size() == 0) {
						// config //
						sender.sendMessage(ChatColor.AQUA+"Èãðîê íå óñòàíîâèë íè îäíîé òî÷êè!");
						return true;
					}else {
						sender.sendMessage(ChatColor.AQUA+"Òî÷êè èãðîêà " + loop + ": " + res.substring(0, res.length()-2));
						return true;
					}
				}else {
					//Teleport
					String loop = p.getName();
					String name = args[0];
					if(args.length > 1) {
						loop = args[0];
						name = args[1];
					}
					List<String> list = pwarps.getStringList(loop);
					for(String point : list) {
						if(point.substring(0, point.indexOf("%%")).equals(name)) {
							String[] pD = point.split("%%");
							Location l = new Location(p.getLocation().getWorld(), Double.valueOf(pD[1]),Double.valueOf(pD[2]),Double.valueOf(pD[3]),
									Float.valueOf(pD[4]),Float.valueOf(pD[5]));
							p.teleport(l);
							// config //
							sender.sendMessage(ChatColor.AQUA+"Woohoo!");
							return true;
						}
					}
					// config //
					sender.sendMessage(ChatColor.AQUA+"Èãðîê íå èìååò òàêîé òî÷êè");
					return true;					
				}
			}
		}else {
			sender.sendMessage("archiquest.no_permission");
			return true;
		}
	}
	
	private boolean isCreatingPoint(String cmd) {
		for (String string : setPointCommands) {
			if(cmd.equals(string))
				return true;			
		}
		return false;
	}
	
	private boolean isDeletingPoint(String cmd) {
		for (String string : deletePointCommands) {
			if(cmd.equals(string))
				return true;			
		}
		return false;
	}
	
	private boolean isListPoint(String cmd) {
		for (String string : listPointCommands) {
			if(cmd.equals(string))
				return true;			
		}
		return false;
	}

}
