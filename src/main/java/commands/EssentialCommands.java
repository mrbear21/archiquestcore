package commands;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.BrainSpigot;
import com.SystemMessage;
import com.Utils;

import objects.BreadMaker;

public class EssentialCommands implements CommandExecutor {

	private BrainSpigot spigot;
	
	public EssentialCommands(BrainSpigot spigot) {
		this.spigot = spigot;
	}
	
	public void register() {
		spigot.getCommand("fly").setExecutor(this);
		spigot.getCommand("speed").setExecutor(this);
		spigot.getCommand("seen").setExecutor(this);
		spigot.getCommand("time").setExecutor(this);
		spigot.getCommand("weather").setExecutor(this);
		spigot.getCommand("repair").setExecutor(this);
		spigot.getCommand("help").setExecutor(this);
		spigot.getCommand("links").setExecutor(this);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if (sender.hasPermission("archiquest."+label)) {
		
		/*
		 * General commands
		 */
		
			switch(label) {
			
			case "seen":
				
				if (args.length == 0) { return false;}

				if (Bukkit.getPlayer(args[0]) == null && spigot.getBread(args[0]).getData("nickname") == null) {
					new SystemMessage(spigot).newMessage("playerdata", new String[] {"get", args[0]});
				}
				
				if (spigot.runnableTasks.containsKey("seen:"+sender.getName())) { sender.sendMessage("archiquest.pleasewait"); return true; }
				
				spigot.runnableTasks.put("seen:"+sender.getName(), Bukkit.getScheduler().scheduleSyncRepeatingTask(spigot, new Runnable() {
    				int i = 0;
				    @Override
				    public void run() {
                        if (spigot.getBread(args[0]).getData("nickname") != null) {
                        	
	                        BreadMaker bread = spigot.getBread(args[0]);
	        				
	        				sender.sendMessage(args[0]+":");
	        				sender.sendMessage("§earchiquest.lastlogin: §f" + (bread.getData("lastLogin") == null ? "N\\A" : new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").format((new Date(Long.valueOf(bread.getData("lastLogin")))))));
	        				sender.sendMessage("§earchiquest.firstjoin: §f" + (bread.getData("lastLogin") == null ? "N\\A" : new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").format((new Date(Long.valueOf(bread.getData("firstPlay")))))));
	        				sender.sendMessage("§earchiquest.lastip: §f" + (bread.getData("lastIP") == null ? "N\\A" : bread.getData("lastIP")));
	        				sender.sendMessage("§earchiquest.language: §f" + (bread.getData("language") == null ? "N\\A" : bread.getData("language")));
	        				
	        				if (Bukkit.getPlayer(args[0]) != null) {
	        					sender.sendMessage("archiquest.online: §f"+ new Utils().longToTime(System.currentTimeMillis() - Long.valueOf(bread.getData("lastLogin"))));
	        				} else {
	        					sender.sendMessage("archiquest.offline: §f"+ new Utils().longToTime(System.currentTimeMillis() - Long.valueOf(bread.getData("lastLogin"))));
	        				}
	
	        				sender.sendMessage("§escoreboard.level: §f"+(bread.getData("level") == null ? "N\\A" : bread.getData("level")));
	        				sender.sendMessage("§escoreboard.balance: §f"+bread.getBalance());
	        				sender.sendMessage("§earchiquest.discord: §f" + (bread.getData("discord") == null ? "none" : bread.getData("discord")));
	        				sender.sendMessage("§earchiquest.2fa: §f" + (bread.getData("2Fa") == null ? "none" : bread.getData("2Fa")));
	        				sender.sendMessage("§earchiquest.muted: §f");
	        				sender.sendMessage("§earchiquest.banned: §f");
        				
                            Bukkit.getScheduler().cancelTask(spigot.runnableTasks.get("seen:"+sender.getName()));
                            spigot.runnableTasks.remove("seen:"+sender.getName());
                            return;
                        }
                        
                        if (i++ == 2) {
                        	sender.sendMessage("archiquest.playerdoesntexist"); 
                        	Bukkit.getScheduler().cancelTask(spigot.runnableTasks.get("seen:"+sender.getName()));
                            spigot.runnableTasks.remove("seen:"+sender.getName());
                            return;
                        }
				    }
				}, 0L, 20L));
				

				return true;
				
			}
                
		
		/*
		 * Player commands 
		 */
		
		
		if (!(sender instanceof Player)) { sender.sendMessage("This command can be executed only by player"); return true; }

		Player player = (Player) sender;
		
			switch (label) {
			
				case "fly":
					if (player.getAllowFlight()) {
						player.setAllowFlight(false);
						player.setFlying(false);
						player.sendMessage("archiquest.fly-disabled");
					} else {
						player.setAllowFlight(true);
						player.setFlying(true);
						player.sendMessage("archiquest.fly-enabled");
					}
					return true;
					
				case "help":
					player.sendMessage("archiquest.help");
					return true;
					
				case "links":
					player.sendMessage("archiquest.links");
					return true;
					
				case "speed":
					if (args.length > 0 && Float.valueOf(args[0]) >= 0 && Float.valueOf(args[0]) <= 9) {
						player.setFlySpeed((float) ((Double.valueOf(args[0]) + 0.5) / 10));
						player.setWalkSpeed((float) ((Double.valueOf(args[0]) + 1) / 10));
						player.sendMessage("archiquest.speed-set " + args[0]);
						return true;
					} else {
						return false;
					}

				case "time":
					if (args.length == 0) { return false; }
					switch(args[0]) {
						case "night": 
					    	  player.getWorld().setTime(18000);
					    	  break;
						case "day": 
					    	  player.getWorld().setTime(0);
					    	  break;
					    default:
					    	return false;
					}
					player.sendMessage("archiquest.time-set "+player.getWorld().getTime());
					return true;
					
				case "weather":
					if (args.length == 0) { return false; }
					switch(args[0]) {
						case "sun": 
					    	  player.getWorld().setStorm(false);
					    	  break;
						case "rain": 
					    	  player.getWorld().setStorm(true);
					    	  break;
					    default:
					    	return false;
					}
					player.sendMessage("archiquest.weather-set "+args[0].toLowerCase());
					return true;	

				case "repair":
					if (player.getInventory().getItemInMainHand().getType() != Material.AIR) {
						try {
							if (player.getInventory().getItemInMainHand().getDurability() == 0) {
								player.sendMessage("archiquest.item-durability-is-full}");
								return true;
							} 
							player.getInventory().getItemInMainHand().setDurability((short)0);
							player.sendMessage("archiquest.item-repaired");
							return true;
						} catch (Exception c) {
							player.sendMessage("archiquest.item-cant-be-repaired");
						} 
					} else {
						player.sendMessage("archiquest.air-cant-be-repaired");
					}
					return true;
					
					
					
	
				case "cmd":
					player.sendMessage("");
					return true;
					
			}
			
		} else {
			sender.sendMessage("archiquest.no_permission");
		}
		

		return true;
	}

}
