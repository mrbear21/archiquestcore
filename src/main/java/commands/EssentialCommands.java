package commands;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.WeatherType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommandYamlParser;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.TabCompleteEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.BrainSpigot;
import com.Utils;

import integrations.AuthmeAPI;
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
		spigot.getCommand("ptime").setExecutor(this);
		spigot.getCommand("pweather").setExecutor(this);
		spigot.getCommand("repair").setExecutor(this);
		spigot.getCommand("help").setExecutor(this);
		spigot.getCommand("links").setExecutor(this);
		spigot.getCommand("menu").setExecutor(this);
		spigot.getCommand("settings").setExecutor(this);
		spigot.getCommand("bread").setExecutor(this);
		spigot.getCommand("spawn").setExecutor(this);
		spigot.getCommand("top").setExecutor(this);
		spigot.getCommand("hat").setExecutor(this);
		spigot.getCommand("bag").setExecutor(this);
		spigot.getCommand("workbench").setExecutor(this);
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
		spigot.getCommand("gamemode").setExecutor(this);
		spigot.getCommand("patreon").setExecutor(this);
		spigot.getCommand("sudo").setExecutor(this);
		spigot.getCommand("warps").setExecutor(this);
		spigot.getCommand("youtube").setExecutor(this);
		spigot.getCommand("vote").setExecutor(this);
		spigot.getCommand("start").setExecutor(this);
		spigot.getCommand("joinmessage").setExecutor(this);
		spigot.getCommand("afk").setExecutor(this);
		spigot.getCommand("vanish").setExecutor(this);
		spigot.getCommand("msg").setExecutor(this);
		spigot.getCommand("invsee").setExecutor(this);
		spigot.getCommand("home").setExecutor(this);
		spigot.getCommand("idea").setExecutor(this);
		spigot.getCommand("openworld").setExecutor(this);
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
		
		if (sender.hasPermission("archiquest."+command.getName())) {
			
			Cooldown cooldown = new Cooldown(spigot, sender.getName());
			
	 		if (cooldown.hasCooldown(command.getName())) {
	 			sender.sendMessage("archiquest.waitcooldown "+((cooldown.getTimeLeft(command.getName())/ 1000)) +" sec");
	 			return true;
			}
			
			
		
		/*
		 * General commands
		 */
		
			switch(command.getName()) {
			
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
		
			switch (command.getName()) {
			
				case "fly":
					if (player.getAllowFlight()) {
						player.setAllowFlight(false);
						player.setFlying(false);
						player.sendMessage("archiquest.fly archiquest.disabled");
					} else {
						player.setAllowFlight(true);
						player.sendMessage("archiquest.fly archiquest.enabled");
					}
					return true;
					
				case "help":
					player.sendMessage("archiquest.help");
					return true;
					
				case "links":
					
					String[] links = bread.getLocales().getLocalesMap().get("archiquest.links").split(",");

					player.sendMessage("");
					for (String l : links) {
					
						TextComponent text = new TextComponent(l.split("#")[1]+" » "+ChatColor.GOLD+""+ChatColor.UNDERLINE);
						TextComponent link = new TextComponent("["+bread.getLocales().translateString("archiquest.open")+"]");
						link.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, l.split(",")[0]));
						link.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(bread.getLocales().translateString("archiquest.open")).create()));
						text.addExtra(link);
						player.spigot().sendMessage(text);
						
					}
					player.sendMessage("");
					
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
					cooldown.setCooldown(command.getName(), 60);
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
					cooldown.setCooldown(command.getName(), 60);
					return true;	

				case "ptime":
					if (args.length == 0) { return false; }
					long worldTime = player.getWorld().getTime();
					
					switch(args[0]) {
						case "night": 
					    	  player.setPlayerTime(worldTime+Math.abs(15000-worldTime), true);
					    	  break;
						case "day": 
							  player.setPlayerTime(worldTime+Math.abs(6000-worldTime), true);
					    	  break;
						case "reset": 
							  player.setPlayerTime(worldTime+Math.abs(worldTime-worldTime), true);
					    	  break;
					    default:
					    	return false;
					}
					player.sendMessage("archiquest.time-set "+player.getWorld().getTime());
					cooldown.setCooldown(command.getName(), 60);
					return true;
					
				case "pweather":
					if (args.length == 0) { return false; }
					switch(args[0]) {
						case "sun": 
					    	  player.setPlayerWeather(WeatherType.CLEAR);
					    	  break;
						case "rain": 
					    	  player.setPlayerWeather(WeatherType.DOWNFALL);
					    	  break;
					    default:
					    	return false;
					}
					player.sendMessage("archiquest.weather-set "+args[0].toLowerCase());
					cooldown.setCooldown(command.getName(), 60);
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
							cooldown.setCooldown(command.getName(), 60*60);
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
					
					MenuBuilder menu = new MenuBuilder(spigot, player, "MENU"); int l = 0;
					menu.setOption("menu.homes", l++, "homes", Material.GRASS_BLOCK, new String [] {});
					menu.setOption("menu.warps", l++, "warps", Material.ENDER_EYE, new String [] {});
					menu.setOption("archiquest.gadgets", l++, "gmenu main", Material.PISTON, new String [] {});
					menu.setOption("menu.discord", l++, "discord", Material.DIAMOND_HELMET, new String [] {});
					menu.setOption("menu.links", l++, "links", Material.ACACIA_SIGN, new String [] {});
					menu.setOption("menu.patreon", l++, "patreon", Material.EMERALD, new String [] {});
					menu.setOption("menu.rules", l++, "rules", Material.BOOK, new String [] {});
					menu.setOption("menu.vote", l++, "vote", Material.SUNFLOWER, new String [] {});
					if (spigot.getServer().getPluginManager().isPluginEnabled("PlotSquared")) {
						menu.setOption("menu.banners", l++, "bm", Material.BLUE_BANNER, new String [] {});
						menu.setOption("menu.color", l++, "color", Material.LEATHER_CHESTPLATE, new String [] {});
						ItemStack icon = new ItemStack(Material.PLAYER_HEAD); SkullMeta meta = (SkullMeta) icon.getItemMeta();
						meta.setOwningPlayer(Bukkit.getOfflinePlayer("books")); icon.setItemMeta(meta);
						menu.setOption("menu.heads", l++, "hdb", icon, new String [] {});
					}
					menu.setOption("menu.settings", 25, "settings", Material.COMPARATOR, new String [] {});
					menu.setOption("archiquest.language.selector", 26, "lang", spigot.getConfig().get("languages."+bread.getLanguage()+".icon") != null ?
							spigot.getConfig().getItemStack("languages."+bread.getLanguage()+".icon") : spigot.getConfig().getItemStack("languages.ua.icon"), new String [] { "archiquest.click-to-browse" });
					menu.build();
					return true;
					
				case "settings":
					
					menu = new MenuBuilder(spigot, player, "SETTINGS"); l = 10;
					menu.setOption("archiquest.fly", l++, new String[] {"fly", "settings"}, Material.FEATHER, new String [] {
							!player.hasPermission("archiquest.fly") ? "archiquest.donate-feature" : player.getAllowFlight() ? "archiquest.enabled" : "archiquest.disabled" });
					menu.setOption("archiquest.tptoggle", l++, new String[] {"tptoggle", "settings"}, Material.ENDER_PEARL, new String [] {
							!player.hasPermission("archiquest.tptoggle") ? "archiquest.donate-feature" : bread.getData("tptoggle").isNotNull() == null ? "archiquest.disabled" : bread.getData("tptoggle").getAsBoolean() ? "archiquest.enabled" : "archiquest.disabled" });
					menu.setOption("archiquest.doublejump", l++, new String[] {"doublejump", "settings"}, Material.ELYTRA, new String [] { !player.getAllowFlight() ? "archiquest.disabled" :
							!player.hasPermission("archiquest.doublejump") ? "archiquest.donate-feature" : bread.getData("doublejump").isNotNull() == null ? "archiquest.disabled" : bread.getData("doublejump").getAsBoolean() ? "archiquest.enabled" : "archiquest.disabled" });
					menu.setOption("menu.joinmessage", l++, "joinmessage", Material.OAK_SIGN, new String [] {
							!player.hasPermission("archiquest.joinmessage") ? "archiquest.donate-feature" : bread.getData("joinmessage").isNotNull() ? bread.getData("joinmessage").getAsString().equals("false") ? "archiquest.disabled" : "archiquest.enabled" : "archiquest.disabled"});
					menu.setOption("archiquest.player-autoafk", l++, new String[] {"afk auto", "settings"}, Material.RED_BED, new String [] {!player.hasPermission("archiquest.afk.auto") ? "archiquest.donate-feature" : (bread.getData("autoafk").getAsBoolean() ? "archiquest.enabled" : "archiquest.disabled")});
					if (spigot.getServer().getPluginManager().isPluginEnabled("PlotSquared")) {
						menu.setOption("archiquest.noclip", l++, new String[] {"noclip", "settings"}, Material.PHANTOM_MEMBRANE, new String [] {"archiquest.click-to-select"});
						menu.setOption("archiquest.nightvision", l++, new String[] {"nv", "settings"}, Material.ENDER_EYE, new String [] {player.getActivePotionEffects().stream().map(PotionEffect::getType).collect(Collectors.toList()).contains(PotionEffectType.NIGHT_VISION) ? "archiquest.enabled" : "archiquest.disabled"});
					}
					//	menu.setOption("archiquest.2fa", 15, "2fa", Material.SHIELD, new String [] {""+(bread.getData("2Fa").isNotNull() ? "archiquest.enabled" : "archiquest.disabled")});
					menu.build();
					return true;
					
				case "bread":
					
					if (args.length == 0) { return false;}
					menu = new MenuBuilder(spigot, player, args[0].toUpperCase());
					menu.setOption("", 0, new String[] {}, Material.GOLD_INGOT, new String [] {});
					menu.build();
					return true;
					
				case "joinmessage":
					
					if (args.length == 0) {
						player.sendMessage("");
						player.sendMessage("archiquest.joinmessage "+(bread.getData("joinmessage").isNotNull() ? bread.getData("joinmessage").getAsString().equals("false") ? 
								"archiquest.disabled" : bread.getData("joinmessage").getAsString().equals("true") ? "archiquest.enabled" : bread.getData("joinmessage").getAsString() : "archiquest.disabled"));
						player.sendMessage("");
						player.sendMessage("archiquest.joinmessage-cmd");
						player.sendMessage("");
					} else {
						switch (args[0]) {
							case "reset" :
								bread.setData("joinmessage", "true").save();
								player.sendMessage("archiquest.joinmessage-reset");
								return true;
							case "off" :
								bread.setData("joinmessage", "false").save();
								player.sendMessage("archiquest.joinmessage-off");
								return true;
						}
						if (player.hasPermission("archiquest.joinmessage.custom")) {
							bread.setData("joinmessage", String.join(" ", args)).save();
							player.sendMessage("archiquest.joinmessage-set "+String.join(" ", args));
						} else {
							player.sendMessage("archiquest.donate-feature");
						}
					}
					
					return true;
					
				case "afk":
					
					 if (args.length > 0 && args[0].equalsIgnoreCase("auto") && player.hasPermission("archiquest.afk.auto")) {
							if (bread.getData("autoafk").getAsBoolean()) {
								bread.setData("autoafk", "false").save();
							} else {
								bread.setData("autoafk", "true").save();
							}
							Bukkit.getOnlinePlayers().stream().forEach(p -> p.sendMessage("archiquest.player-autoafk "+(bread.getData("autoafk").getAsBoolean() ? "archiquest.enabled" : "archiquest.disabled")));
							return true;
					} else if (args.length > 0 && player.hasPermission("archiquest.afk.custom")) {
						bread.setData("afk", "true");
						bread.updateDisplayName();
						Bukkit.getOnlinePlayers().stream().forEach(p -> p.sendMessage(bread.getDisplayName()+ " archiquest.player-afk "+String.join(" ", args)));
						cooldown.setCooldown(command.getName(), 60);
						return true;
					} else if (args.length == 0) {
						bread.setData("afk", "true");
						bread.updateDisplayName();
						int i = new Random().nextInt(5);
						Bukkit.getOnlinePlayers().stream().forEach(p -> p.sendMessage(bread.getDisplayName() + " archiquest.player-afk archiquest.player-afk-msg-"+i));
						cooldown.setCooldown(command.getName(), 60);
						return true;
					} else {
						player.sendMessage("archiquest.donate-feature");
					}

					return true;
					
				case "start":
					
					if (spigot.getServer().getPluginManager().isPluginEnabled("PlotSquared")) {
						player.performCommand("p auto");
					} else {
						player.performCommand("rtp");
					}
					return true;
					
					
				case "warps":
					
					File file = new File(spigot.getDataFolder()+"/warps.yml");
					FileConfiguration pwarps = YamlConfiguration.loadConfiguration(file);
					List<String> list = pwarps.getStringList(args.length > 0 ? args[0] : sender.getName());
					
					if (list.size() > 0) {
						menu = new MenuBuilder(spigot, player, "WARPS");
						int j = 0;
						for (String w : list) {
							menu.setOption(w.split("%")[0], j++, "w "+w.split("%")[0], Material.ENDER_EYE, new String [] {"archiquset.click-to-tp"});
						}
						menu.build();
					} else {
						player.sendMessage("pw-no-points");
					}
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
					/*
					AuthmeAPI authme = new AuthmeAPI(spigot);
					if (authme.isHookActive() && spawnLoader.getSpawn() != null) {
						player.teleport(spawnLoader.getSpawn());
					} else {
						player.teleport(player.getWorld().getSpawnLocation());
					}*/
					player.performCommand("authme spawn");
					return true;
	
				case "home":
					if (player.getBedSpawnLocation() != null) {
						player.teleport(player.getBedSpawnLocation());
					} else {
						player.sendMessage("archiquest.nobedlocation");
					}
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
						String[] plugins = new String[] {"archiquestcore"};
						List<String> commands = new ArrayList<String>();
		                if (Arrays.asList(plugins).contains(plugin.getName().toLowerCase())) {
		                    List<Command> commandList = PluginCommandYamlParser.parse(plugin);
		                    for (int i = 0; i < commandList.size(); i++) {
		                    	String cmd = commandList.get(i).toString().replace("org.bukkit.command.PluginCommand(", "").replace(",", "").replace("archiquestcore", "").replace("v1", "").replace(")", "");
		                    	commands.add((sender.hasPermission("archiquest."+cmd.split(" ")[0]) ? ChatColor.GREEN : ChatColor.RED) + cmd.split(" ")[0]);
		                    }
			                sender.sendMessage(String.join(", ", commands));
		                }
					}
					
					return true;
					
				case "heal":
					player.setHealth(player.getMaxHealth());
					player.sendMessage("archiquest.heal");
					cooldown.setCooldown(command.getName(), 60);
					return true;

				case "feed":
					player.setFoodLevel(20);
					player.sendMessage("archiquest.feed");
					cooldown.setCooldown(command.getName(), 60);
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
						bread.setData("tptoggle", "true").save();
						player.sendMessage("archiquest.tptoggle archiquest.enabled");
					} else {
						bread.setData("tptoggle", "false").save();
						player.sendMessage("archiquest.tptoggle archiquest.disabled");
					}
					return true;
					
				case "doublejump":
					if (bread.getData("doublejump") == null || !bread.getData("doublejump").getAsBoolean() || player.getAllowFlight() == false) {
						bread.setData("doublejump", "true").save();
						player.setAllowFlight(true);
						player.sendMessage("archiquest.doublejump archiquest.enabled");
					} else {
						bread.setData("doublejump", "false").save();
						if (player.getGameMode() != GameMode.CREATIVE && player.getGameMode() != GameMode.SPECTATOR) {
							player.setAllowFlight(false);
							player.setFlying(false);
						}
						player.sendMessage("archiquest.doublejump archiquest.disabled");
					}
					return true;
					
				case "ext":
					player.setFireTicks(0);
					player.sendMessage("archiquest.extcommand");
					cooldown.setCooldown(command.getName(), 60);
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

					
				case "youtube":
					player.sendMessage("");
					text = new TextComponent(bread.getLocales().translateString("archiquest.youtube"));
					TextComponent youtube = new TextComponent("["+bread.getLocales().translateString("archiquest.open")+"]");
					youtube.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, bread.getLocales().translateString("archiquest.ytplaylist")));
					youtube.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(bread.getLocales().translateString("archiquest.open")).create()));
					text.addExtra(youtube);
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
					
					
				case "gamemode":
					if (args.length == 0) {
						if (player.getGameMode() != GameMode.CREATIVE) {
							player.setGameMode(GameMode.CREATIVE);
						} else {
							player.setGameMode(GameMode.SURVIVAL);
						}
					} else {
						switch (args[0]) {
							case "0": player.setGameMode(GameMode.SURVIVAL); break;
							case "1": player.setGameMode(GameMode.CREATIVE); break;
							case "2": player.setGameMode(GameMode.ADVENTURE); break;
							case "3": player.setGameMode(GameMode.SPECTATOR); break;
							default: return false;
						}
					}
					player.sendMessage("archiquest.gamemode-set §a"+player.getGameMode().name().toLowerCase());
					return true;
					
				case "patreon":

					if (new AuthmeAPI(spigot).getInstance().getPlayerInfo(player.getName()).get().getEmail().isPresent()) {	
						player.sendMessage("");
						text = new TextComponent(bread.getLocales().translateString("archiquest.patreon"));
						TextComponent patreon = new TextComponent("["+bread.getLocales().translateString("archiquest.open")+"]");
						patreon.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://www.patreon.com/archiquest"));
						patreon.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(bread.getLocales().translateString("archiquest.open")).create()));
						text.addExtra(patreon);
						player.spigot().sendMessage(text);
						player.sendMessage("");
					} else {
						player.sendMessage("archiquest.email.required");
					}
					
					return true;
					
				case "rules":
					player.sendMessage("");
					text = new TextComponent(bread.getLocales().translateString("archiquest.rules"));
					TextComponent patreon = new TextComponent("["+bread.getLocales().translateString("archiquest.open")+"]");
					patreon.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, bread.getLocales().translateString("archiquest.ruleslink")));
					patreon.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(bread.getLocales().translateString("archiquest.open")).create()));
					text.addExtra(patreon);
					player.spigot().sendMessage(text);
					player.sendMessage("");
					return true;
					
				case "sudo":
					if (args.length == 0) { return false; }
					Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), String.join(" ", args));
					spigot.log(ChatColor.GRAY+""+ChatColor.ITALIC+"[SUDO] "+ sender.getName() +": "+String.join(" ", args));
					return true;

				case "vote":
					player.sendMessage("Soon");
					return true;
					
					
				case "invsee":
					if (args.length > 0) {
						Player targetPlayer = Bukkit.getServer().getPlayer(args[0]);
						if (Bukkit.getPlayer(args[0]) == null) {
							player.sendMessage( "archiquest.player.is.offline");
						} else if (sender.hasPermission("archiquest.invsee.admin")) {
							player.openInventory(targetPlayer.getInventory());
						} else {
				            Inventory inv = Bukkit.createInventory(player, 54, "");
				            inv.setContents(targetPlayer.getInventory().getContents());
				            player.openInventory(inv);
						} 
						return true;
					} else {
						return false;
					} 
					
				case "vanish":
					
		            if (bread.getData("vanish").getAsBoolean()) {
		                for(Player p : Bukkit.getOnlinePlayers() ) {
		                    p.showPlayer(player);
		                }
		                bread.setData("vanish", "false");
		                player.sendMessage("archiquest.youre-unvanished");
		            } else {
		                for(Player p : Bukkit.getOnlinePlayers()) {
		                    p.hidePlayer(player);
		                }
		                bread.setData("vanish", "true");
		                player.sendMessage("archiquest.youre-vanished");
		            }
					
				case "msg":
					
					if (args.length == 0) {return false;}
					
					if (Bukkit.getPlayer(args[0]) == null) {
						player.sendMessage("archiquest.player.is.offline");
						return true;
					} else if (args.length > 1) {
						
						Bukkit.getPlayer(args[0]).sendMessage(bread.getDisplayName()+" §e-> archiquest.msg-you§f: "+String.join(" ", Arrays.copyOfRange(args, 1, args.length)));
						player.sendMessage("§earchiquest.msg-you -> "+spigot.getBread(args[0]).getDisplayName()+"§f: "+String.join(" ", Arrays.copyOfRange(args, 1, args.length)));
						
						return true;
						
					} else {
						return false;
					}

				case "openworld":
					if (Bukkit.getWorld("openworld") == null) {
						player.sendMessage("archiquest.worlddoestnexist");
						return true;
					}
					player.teleport(Bukkit.getWorld("openworld").getSpawnLocation());
					return true;
					
					
				case "idea":
					String[] job = { "В'язниця", "Лабіринт", "Лабораторія", "Залізничний вокзал", "Фонтан",
							"Електростанція", "Хмарочос", "Лікарня", "Маяк", "Ратуша", "Кінотеатр", "Театр",
							"Бібліотека", "Церква", "Замок", "Зоопарк", "Ферма", "Ковзанка", "Ресторан", "Пляж",
							"Готель", "Супермаркет", "Концертний зал", "Футбольний стадіон", "Смуга перешкод",
							"Аеропорт", "Тенісний корт", "Spa-салон", "Плавальний басейн", "Підводне місто",
							"Місто на палях", "Плавуче місто", "Завод", "Колізей", "Шоколадна фабрика",
							"Аквапарк", "Кемпінг", "Концетабір", "Камера тортур", "Лижний спуск",
							"Лимонадний кіоск", "Піцерія", "Снігове містечко", "Шахта", "Склад",
							"Крижані скульптури", "Динозавр", "Гарний сад", "Комп'ютер",
							"Подіум для показів моди", "Занедбане місто", "Перукарня", "Болото", "Школа",
							"McDonalds", "Вертоліт", "Стоматологія", "Лісопильня", "Водяне колесо",
							"Дерева з божевільним листям", "Кузня", "Парламент", "Майданчик для скайдайвінгу",
							"Боулінг-клуб", "Баскетбольний майданчик", "Док", "Круїзне судно", "Галеон",
							"Підводний човен", "Торгівельні намети", "Котедж", "Гігантська ялинка!",
							"Вітряна турбіна", "Космічний корабель", "Планета на небі", "Купол",
							"Гігантська настільна гра", "Гігантський кратер", "Олімпійський парк",
							"Підроблені хмари", "Метеорити, що падають!", "Самітна вілла", "Будинок вашої мрії",
							"Гігантська рука", "Гори", "Будиночок на дереві", "Псих-лікарня (дурня)" };

					cooldown.setCooldown(command.getName(), 60*30);
					player.sendMessage("archiquest.idea "+job[new Random().nextInt(job.length)]);
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
