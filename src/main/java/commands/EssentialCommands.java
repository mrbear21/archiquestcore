package commands;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommandYamlParser;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import com.BrainSpigot;
import com.SystemMessage;
import com.Utils;

import modules.MenuDrawer;
import net.md_5.bungee.api.ChatColor;
import objects.BreadMaker;
import objects.RandomTeleport;

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
		spigot.getCommand("menu").setExecutor(this);
		spigot.getCommand("settings").setExecutor(this);
		spigot.getCommand("bread").setExecutor(this);
		spigot.getCommand("rtp").setExecutor(this);
		spigot.getCommand("spawn").setExecutor(this);
		spigot.getCommand("top").setExecutor(this);
		spigot.getCommand("hat").setExecutor(this);
		spigot.getCommand("bag").setExecutor(this);
		spigot.getCommand("commands").setExecutor(this);
		spigot.getCommand("heal").setExecutor(this);
		spigot.getCommand("feed").setExecutor(this);
		spigot.getCommand("near").setExecutor(this);
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
	        				sender.sendMessage("§earchiquest.lastlogin: §f" + (bread.getData("lastLogin") == null ? "N\\A" : new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").format((new Date(bread.getData("lastLogin").getAsLong())))));
	        				sender.sendMessage("§earchiquest.firstjoin: §f" + (bread.getData("lastLogin") == null ? "N\\A" : new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").format((new Date(bread.getData("firstPlay").getAsLong())))));
	        				sender.sendMessage("§earchiquest.lastip: §f" + (bread.getData("lastIP") == null ? "N\\A" : bread.getData("lastIP").getAsString()));
	        				sender.sendMessage("§earchiquest.language: §f" + (bread.getData("language") == null ? "N\\A" : bread.getData("language").getAsString()));
	        				
	        				if (Bukkit.getPlayer(args[0]) != null) {
	        					sender.sendMessage("archiquest.online: §f"+ new Utils().longToTime(System.currentTimeMillis() - bread.getData("lastLogin").getAsLong()));
	        				} else {
	        					sender.sendMessage("archiquest.offline: §f"+ new Utils().longToTime(System.currentTimeMillis() - bread.getData("lastLogin").getAsLong()));
	        				}
	
	        				sender.sendMessage("§escoreboard.level: §f"+(bread.getData("level") == null ? "N\\A" : bread.getData("level").getAsString()));
	        				sender.sendMessage("§escoreboard.balance: §f"+bread.getBalance());
	        				sender.sendMessage("§earchiquest.discord: §f" + (bread.getData("discord") == null ? "none" : bread.getData("discord").getAsString()));
	        				sender.sendMessage("§earchiquest.2fa: §f" + (bread.getData("2Fa") == null ? "none" : bread.getData("2Fa").getAsString()));
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
						
				case "menu":
					
					MenuDrawer menu = new MenuDrawer(spigot, player);
					menu.setOption("настройки", 0, "settings", new ItemStack(Material.COMPARATOR), new String [] {"глянути шо"}, false);
					menu.setOption("правила", 1, "rules", new ItemStack(Material.DIAMOND), new String [] {"глянути шо"}, true);
					menu.setOption("плот інфо", 2, "p i", new ItemStack(Material.PAPER), new String [] {"ти подивсь"}, true);
					menu.setOption("лінки", 5, "links", new ItemStack(Material.GOLDEN_AXE), new String [] {"глянути шо"}, true);
					menu.openMenu("MENU");
					return true;
					
				case "settings":
					
					menu = new MenuDrawer(spigot, player);
					menu.setOption("роздільник чату", 1, "seen mrbear22", new ItemStack(Material.GOLD_INGOT), new String [] {"глянути шо"}, true);
					menu.openMenu("SETTINGS");
					return true;
					
				case "bread":
					
					if (args.length == 0) { return false;}
					
					menu = new MenuDrawer(spigot, player);
					menu.setOption("добавити в плот", 0, "p add "+args[0], new ItemStack(Material.GOLD_INGOT), new String [] {"глянути шо"}, true);
					menu.setOption("видалити з плота", 1, "p remove "+args[0], new ItemStack(Material.PAPER), new String [] {"глянути шо"}, true);
					menu.openMenu(args[0]);
					return true;
					
				case "rtp":
					
					player.sendMessage("archiquest.randomteleport");
					Bukkit.getScheduler().runTaskAsynchronously(spigot, new Runnable() {
					    @Override
					    public void run() {
					    	player.teleport(new RandomTeleport().getRandomLocation(player.getWorld()));

					    }
					});
					return true;
							
				case "spawn":
					player.teleport(player.getWorld().getSpawnLocation());
					return true;
	
				case "top":
					for (int i = 0; i<256; i++) {
						Location location = player.getLocation().add(0,i,0);
						if (location.getBlock().getType() != Material.AIR && location.add(0,1,0).getBlock().getType() == Material.AIR) {
							player.teleport(location);
							break;
						}
					}
					player.sendMessage(ChatColor.LIGHT_PURPLE+"Yoo-hoo");
					return true;

				case "hat":
					if (player.getInventory().getItemInMainHand().getType() != Material.AIR) {
						if (player.getInventory().getHelmet() != null) {
							player.getInventory().addItem(player.getInventory().getHelmet());
						} 
						player.getInventory().setHelmet(player.getInventory().getItemInMainHand());
						player.getInventory().setItemInMainHand(null);
					} else { player.sendMessage("archiquest.youcanthatair"); }
					
					return true;
					
				case "enchant": 
					player.openEnchanting(null, true);
					return true;
				
				case "workbench": 
					player.openWorkbench(null, true);
					return true;

				case "bag": 
					if (args.length > 0 && player.hasPermission("archiquest.bag.admin")) {
						Player target = Bukkit.getPlayer(args[0]);
							if (target != null) {
								player.openInventory(target.getEnderChest());
					         } else {
					        	  player.sendMessage("archiquest.player-is-offline");
					         }
					} else {
						player.openInventory(player.getEnderChest());
					}					
					return true;
			
					
				
				case "commands":
					
					for(Plugin plugin : Bukkit.getPluginManager().getPlugins()){
		                if(plugin.getName().equals("archiquestcore")){
		                    List<Command> commandList = PluginCommandYamlParser.parse(plugin);
		                    for(int i = 0; i < commandList.size(); i++){
		                        sender.sendMessage(commandList.get(i).toString().replace("org.bukkit.command.PluginCommand(", "").replace(",", "").replace("archiquestcore", "").replace("v1", "").replace(")", ""));
		                    }
		                }
					}
					
					return true;
					
				case "heal":
					player.setHealth(20);
					player.sendMessage("archiquest.heal");
					return true;

				case "feed":
					player.setFoodLevel(20);
					player.sendMessage("archiquest.feed");
					return true;
						
					
				case "near":
					List<String> players = new ArrayList<String>();
					Bukkit.getOnlinePlayers().stream().filter(p -> p.getWorld() == player.getWorld() && p.getLocation().distance(player.getLocation()) <= 500).forEach(p -> players.add(p.getDisplayName()));
					player.sendMessage("archiquest.playersnearyou: "+String.join(", ", players));
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
