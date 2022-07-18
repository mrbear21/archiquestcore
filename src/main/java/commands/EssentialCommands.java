package commands;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommandYamlParser;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.TabCompleteEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.Plugin;

import com.BrainSpigot;
import com.Utils;

import modules.Cooldown;
import modules.MenuBuilder;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import objects.BreadMaker;
import objects.RandomTeleport;

public class EssentialCommands implements CommandExecutor, Listener {

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
	//	spigot.getCommand("rtp").setExecutor(this);
		spigot.getCommand("spawn").setExecutor(this);
		spigot.getCommand("top").setExecutor(this);
		spigot.getCommand("hat").setExecutor(this);
		spigot.getCommand("bag").setExecutor(this);
		spigot.getCommand("commands").setExecutor(this);
		spigot.getCommand("heal").setExecutor(this);
		spigot.getCommand("feed").setExecutor(this);
		spigot.getCommand("near").setExecutor(this);
		spigot.getCommand("tptoggle").setExecutor(this);
		spigot.getCommand("doublejump").setExecutor(this);
		spigot.getCommand("jump").setExecutor(this);
		spigot.getCommand("ext").setExecutor(this);
		spigot.getCommand("discord").setExecutor(this);
		spigot.getCommand("rules").setExecutor(this);
		spigot.getCommand("head").setExecutor(this);
		Bukkit.getPluginManager().registerEvents(this, spigot);
	}
	
	@EventHandler
	public void onTabComplete(TabCompleteEvent event) {
		String buffer = event.getBuffer();
		List<String> completions = event.getCompletions();
		if (buffer.startsWith("/time")) {
			completions.clear();
			completions.addAll(Arrays.asList("day", "night"));
		}
		if (buffer.startsWith("/weather")) {
			completions.clear();
			completions.addAll(Arrays.asList("sun", "rain"));
		}
	}
	
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if (sender.hasPermission("archiquest."+label)) {
			
			Cooldown cooldown = new Cooldown(spigot, sender.getName());
			
	 		if (cooldown.hasCooldown(label)) {
	 			sender.sendMessage("archiquest.waitcooldown "+((cooldown.getTimeLeft(label)/ 1000) % 60) +" sec");
	 			return true;
			}
			
			
		
		/*
		 * General commands
		 */
		
			switch(label) {
			
			case "seen":
				
				if (args.length == 0) { return false;}

				if (spigot.runnableTasks.containsKey("seen:"+sender.getName())) { sender.sendMessage("archiquest.pleasewait"); return true; }

				if (Bukkit.getPlayer(args[0]) == null && spigot.getBread(args[0]).getData("username") == null) {
					spigot.getBread(args[0]).loadData();
				}
				
				spigot.runnableTasks.put("seen:"+sender.getName(), Bukkit.getScheduler().scheduleSyncRepeatingTask(spigot, new Runnable() {
    				int i = 0;
				    @Override
				    public void run() {
                        if (spigot.getBread(args[0]).getData("username") != null) {
                        	
	                        BreadMaker bread = spigot.getBread(args[0]);
	                        sender.sendMessage("");
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
	        				sender.sendMessage("§aDiscord: §f" + (bread.getData("discord") == null ? "none" : "connected"));
	        				sender.sendMessage("§earchiquest.2fa: §f" + (bread.getData("2Fa") == null ? "none" : "activated"));
	        				sender.sendMessage("§earchiquest.muted: §f");
	        				sender.sendMessage("§earchiquest.banned: §f");
	                        sender.sendMessage("");
	                        
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
			BreadMaker bread = spigot.getBread(player.getName());
		
			switch (label) {
			
				case "fly":
					if (player.getAllowFlight()) {
						player.setAllowFlight(false);
						player.setFlying(false);
						player.sendMessage("archiquest.fly archiquest.disabled");
					} else {
						player.setAllowFlight(true);
						player.setFlying(true);
						player.sendMessage("archiquest.fly archiquest.enabled");
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
					cooldown.setCooldown(label, 60);
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
					cooldown.setCooldown(label, 60);
					return true;	

				case "repair":
					if (player.getInventory().getItemInMainHand().getType() != Material.AIR) {
						try {
							if (player.getInventory().getItemInMainHand().getDurability() == 0) {
								player.sendMessage("archiquest.item-durability-is-full");
								return true;
							} 
							player.getInventory().getItemInMainHand().setDurability((short)0);
							player.sendMessage("archiquest.item-repaired");
							cooldown.setCooldown(label, 60*60);
							return true;
						} catch (Exception c) {
							player.sendMessage("archiquest.item-cant-be-repaired");
							return true;
						} 
					} else {
						player.sendMessage("archiquest.air-cant-be-repaired");
					}
					return true;
						
				case "menu":
					
					MenuBuilder menu = new MenuBuilder(spigot, player, "MENU");
					menu.setOption("settings", 0, "settings", Material.COMPARATOR, new String [] {});
					menu.setOption("discord", 1, "discord", Material.DIAMOND_HELMET, new String [] {});
					menu.setOption("rules", 2, "rules", Material.BOOK, new String [] {});
					menu.setOption("links", 3, "links", Material.PAPER, new String [] {});
					menu.build();
					return true;
					
				case "settings":
					
					menu = new MenuBuilder(spigot, player, "SETTINGS");
					menu.setOption("tptoggle", 0, new String[] {"tptoggle", "settings"}, Material.BAMBOO, new String [] {""+bread.getData("tptoggle").getAsBoolean()});
					menu.setOption("doublejump", 1, new String[] {"doublejump", "settings"}, Material.ELYTRA, new String [] {""+bread.getData("doublejump").getAsBoolean()});
					menu.setOption("joinmessage", 2, new String[] {"joinmessage", "settings"}, Material.OAK_SIGN, new String [] {""+bread.getData("joinmessage").getAsString()});
					menu.build();
					return true;
					
				case "bread":
					
					if (args.length == 0) { return false;}
					menu = new MenuBuilder(spigot, player, args[0].toUpperCase());
					menu.setOption("", 0, new String[] {}, Material.GOLD_INGOT, new String [] {});
					menu.build();
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
					        	  player.sendMessage("archiquest.player.is.offline");
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
					player.setHealth(player.getMaxHealth());
					player.sendMessage("archiquest.heal");
					cooldown.setCooldown(label, 60);
					return true;

				case "feed":
					player.setFoodLevel(20);
					player.sendMessage("archiquest.feed");
					cooldown.setCooldown(label, 60);
					return true;
							
				case "near":
					List<String> players = new ArrayList<String>();
					Bukkit.getOnlinePlayers().stream().filter(p -> p.getWorld() == player.getWorld() && p.getLocation().distance(player.getLocation()) <= 500).forEach(p -> players.add(p.getDisplayName()));
					player.sendMessage("archiquest.playersnearyou: "+String.join(", ", players));
					return true;
					
				case "clear":
					player.getInventory().clear();
					player.sendMessage("archiquest.clear");
					return true;

				case "tptoggle":
					if (bread.getData("tptoggle") == null || !bread.getData("tptoggle").getAsBoolean()) {
						bread.setData("tptoggle", "true");
						player.sendMessage("archiquest.tptoggle archiquest.enabled");
					} else {
						bread.setData("tptoggle", "false");
						player.sendMessage("archiquest.tptoggle archiquest.disabled");
					}
					return true;
					
				case "doublejump":
					if (bread.getData("doublejump") == null || !bread.getData("doublejump").getAsBoolean()) {
						bread.setData("doublejump", "true");
						player.sendMessage("archiquest.doublejump archiquest.enabled");
					} else {
						bread.setData("doublejump", "false");
						player.sendMessage("archiquest.doublejump archiquest.disabled");
					}
					return true;
					
				case "ext":
					player.setFireTicks(0);
					player.sendMessage("archiquest.extcommand");
					cooldown.setCooldown(label, 60);
					return true;		

				case "jump":
					for (int i = 0; i<256; i++) {
						Location location = player.getTargetBlock(null, 100).getLocation().add(0.5,i,0.5);
						if (location.getBlock().getType() == Material.AIR) {
							player.sendMessage("archiquest.cantjumpintoair");
							return true;
						}
						if (location.getBlock().getType() != Material.AIR && location.add(0.5,1,0.5).getBlock().getType() == Material.AIR) {
							player.teleport(location.setDirection(player.getLocation().getDirection()));
							player.sendMessage(ChatColor.LIGHT_PURPLE+"Yoo-hoo");
							return true;
						}
					}
					return true;
					
					
				case "discord":
					player.sendMessage("");
					TextComponent text = new TextComponent(bread.getLocales().translateString("archiquest.discord"));
					TextComponent discord = new TextComponent("["+bread.getLocales().translateString("archiquest.open")+"]");
					discord.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, bread.getLocales().translateString("archiquest.discordinvite")));
					discord.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(bread.getLocales().translateString("archiquest.open")).create()));
					text.addExtra(discord);
					player.spigot().sendMessage(text);
					player.sendMessage("");
					return true;

					
				case "head":
					String owner = sender.getName();
					if (args.length > 0) {

						owner = args[0];
					}
					ItemStack skull = new ItemStack(Material.PLAYER_HEAD);

					SkullMeta sm = (SkullMeta) skull.getItemMeta();
					sm.setOwningPlayer(Bukkit.getOfflinePlayer(owner));
					sm.setDisplayName(ChatColor.AQUA + owner);
					skull.setItemMeta(sm);

					player.sendMessage("archiquest.skull " + owner);

					((Player) sender).getInventory().addItem(skull);
					
					
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
