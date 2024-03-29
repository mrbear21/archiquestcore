package handlers;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Cat;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;

import brain.BrainSpigot;
import modules.SystemMessages;
import objects.BreadMaker;
import objects.Cooldown;


public class SpigotListeners implements Listener {

	private BrainSpigot spigot;
	
	public SpigotListeners(BrainSpigot plugin) {
		this.spigot = plugin;
	}
	
	
    
    private static final int MAX_MOVE_DISTANCE = 10; // maximum distance a player can move in one tick
    private static final int TELEPORT_DELAY = 2; // delay in ticks before teleporting player
    private static final int TELEPORT_DISTANCE = 5; // distance to teleport the player after lag

    
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location from = event.getFrom();
        Location to = event.getTo();
        
        // Check if player moved too quickly
        if (to.distance(from) > MAX_MOVE_DISTANCE) {
            
            // Schedule teleport after a short delay
            new BukkitRunnable() {
                @Override
                public void run() {
                    // Check if player is still online
                    if (player.isOnline()) {
                        // Teleport player to a new location
                        Location newLocation = from.clone().add(to.clone().subtract(from).multiply(TELEPORT_DISTANCE));
                        player.teleport(newLocation);
                    }
                }
            }.runTaskLater(spigot, TELEPORT_DELAY);
        }
    }
	
	
    @SuppressWarnings("deprecation")
	@EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
    	
    	Player player = event.getPlayer();
    	player.setPlayerListName(spigot.getBread(player.getName()).getPrefix() + player.getName());

		BreadMaker bread = spigot.getBread(event.getPlayer().getName());
		
		bread.loadData();
		
		if (!bread.getData("language").isNotNull()) {	
			//	new LanguageCommand(spigot).langSelector(player);		
			String locale = event.getPlayer().getLocale().split("_")[0];
			bread.setData("language", locale.equals("uk") ? "ua" : locale);
		}

		if (System.currentTimeMillis() - bread.getData("lastLogin").getAsLong() < 30000) {
			player.teleport(player.getWorld().getSpawnLocation());
		}

    	bread.setData("lastLogin", String.valueOf(System.currentTimeMillis())).save();
    	
        for (Player p : Bukkit.getOnlinePlayers()) {
        	if (spigot.getBread(p.getName()).getData("vanish").getAsBoolean()) {
        		player.hidePlayer(p);
        	}
        }
        
		
    }
    
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
    	BreadMaker bread = spigot.getBread(event.getPlayer().getName());
    	bread.setData("lastLogin", String.valueOf(System.currentTimeMillis())).save();
	//	bread.clearData();
    }
	
    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) {

    	if (spigot.locales.size() == 0) {
    		Bukkit.getScheduler ().runTaskLater (spigot, () -> new SystemMessages(spigot).newMessage("locale", new String[] {"get"}, event.getPlayer()), 20);
    	}
    	
    }
	
    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
    	if (event.getEntity() instanceof Player) { 
	    	BreadMaker bread = spigot.getBread(event.getEntity().getName());
	    	if (bread.getData("afk").isNotNull()) {
	    		event.setCancelled(true);
	    	}
    	}
    }
    
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
    	event.setDeathMessage("["+ ChatColor.YELLOW+"-"+ ChatColor.WHITE+"] " + ChatColor.YELLOW + event.getDeathMessage());
    }
    
    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
		Entity damagee = event.getEntity();
		Entity damager = event.getDamager();
    	if (damagee instanceof Wolf || damagee instanceof Cat) { 
    		if(damager instanceof Player) {
				if (((Wolf) damagee).isTamed() && ((Wolf) damagee).getOwner() == damager) {
					event.setCancelled(true);			
				}
    		}
    		if(damager instanceof Projectile && ((Projectile) damager).getShooter() instanceof Player) {
    			if (((Wolf) damagee).isTamed() && ((Wolf) damagee).getOwner() == ((Projectile) damager).getShooter()) {
    				event.setCancelled(true);
    			}
    		}
    	}
    }
	

	@EventHandler(priority = EventPriority.MONITOR)
	public void onInventoryClick(InventoryClickEvent event) {
		Player player = ((Player) event.getWhoClicked());
		if (event.getView().getTitle().contains("ASE")) {
			if (event.getCursor() != null) {
				player.closeInventory();
			}
		}
	}
    
    @EventHandler(priority= EventPriority.HIGH, ignoreCancelled=true)
    public void onSignChange(SignChangeEvent e) {
        Player player = e.getPlayer();
        if (player.hasPermission("archiquest.chat.color")) {
	        String[] lineArray = e.getLines();
	        int lineArrayLength = lineArray.length;
	        for(int i = 0; i < lineArrayLength; i++) {
	            String oldLine = lineArray[i];
	            String newLine = ChatColor.translateAlternateColorCodes('&', oldLine);
	            e.setLine(i, newLine);
	        }
        }
    }
    
	@EventHandler
	public void place(BlockPlaceEvent e) {

		Player p = e.getPlayer();
		PlayerInventory inventory = p.getInventory();
		ItemStack item = inventory.getItemInMainHand().clone();

		if (item.getAmount() > 0) {
			spigot.getServer().getScheduler().scheduleSyncDelayedTask(spigot, new Runnable() { public void run() {				
				if (inventory.getItemInMainHand().getAmount() == 0) {
					for (int i=0; i<inventory.getSize(); i++) {
						if (inventory.getItem(i) != null && item.getType() == inventory.getItem(i).getType()) {
							inventory.setItemInMainHand(inventory.getItem(i));
							inventory.setItem(i, null);
							return;
						}
					}
				}
			} }, 1);
		}
	}
    
    
	@EventHandler
	public void interact(PlayerInteractEvent e) {
		if (e.getAction() == Action.PHYSICAL || e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			
			Player p = e.getPlayer();
			PlayerInventory inventory = p.getInventory();
			ItemStack item = inventory.getItemInMainHand();
			Cooldown cooldown = new Cooldown(spigot, p.getName());
			
			if (item != null) {
				
				if (cooldown.hasCooldown("item")) {
					e.setCancelled(true);
					return;
				}

				if (item.getType().name().contains("CHESTPLATE") || item.getType().name().contains("ELYTRA")) {
					ItemStack i = inventory.getChestplate().clone();
					inventory.setChestplate(item);
					inventory.setItemInMainHand(i);
					cooldown.setCooldown("item", 1);
					e.setCancelled(true);
				}
				
				if (item.getType().name().contains("HELMET")) {
					ItemStack i = inventory.getHelmet().clone();
					inventory.setHelmet(item);
					inventory.setItemInMainHand(i);
					cooldown.setCooldown("item", 1);
					e.setCancelled(true);
				}
				
				if (item.getType().name().contains("LEGGINGS")) {
					ItemStack i = inventory.getLeggings().clone();
					inventory.setLeggings(item);
					inventory.setItemInMainHand(i);
					cooldown.setCooldown("item", 1);
					e.setCancelled(true);
				}
				
				if (item.getType().name().contains("BOOTS")) {
					ItemStack i = inventory.getBoots().clone();
					inventory.setBoots(item);
					inventory.setItemInMainHand(i);
					cooldown.setCooldown("item", 1);
					e.setCancelled(true);
				}
				
				if (item.getType() == Material.COMPASS) {
					e.setCancelled(true);
					p.performCommand("menu");
					cooldown.setCooldown("item", 1);
					e.setCancelled(true);
				}
			
			}
		}
	}
	

	@EventHandler
	public void removeAfk(PlayerCommandPreprocessEvent cmd) {
		Player player = cmd.getPlayer();
		BreadMaker bread = spigot.getBread(player.getName());
		if (bread.getData("afk").isNotNull()) {
			if (!bread.getData("afk").getAsString().equals("auto")) {
				Bukkit.getOnlinePlayers().stream().forEach(p -> p.sendMessage(player.getDisplayName()+" archiquest.cameback"));
			}
			bread.setData("afk", null);
			bread.updateDisplayName();
		}
	}
	
	@EventHandler
	public void removeAfk(AsyncPlayerChatEvent msg) {
		Player player = msg.getPlayer();
		BreadMaker bread = spigot.getBread(player.getName());
		if (bread.getData("afk").isNotNull()) {
			if (!bread.getData("afk").getAsString().equals("auto")) {
				Bukkit.getOnlinePlayers().stream().forEach(p -> p.sendMessage(player.getDisplayName()+" archiquest.cameback"));
			}
			bread.setData("afk", null);
			bread.updateDisplayName();
		}
	}
	
	@EventHandler
	public void removeAfk(PlayerMoveEvent e) {
		if (e.getFrom().getBlockX() != e.getTo().getBlockX() || e.getFrom().getBlockZ() != e.getTo().getBlockZ()) {
			Player player = e.getPlayer();
			BreadMaker bread = spigot.getBread(player.getName());
			if (bread.getData("afk").isNotNull()) {
				if (e.getFrom().getYaw() != e.getTo().getYaw()) {
					if (!bread.getData("afk").getAsString().equals("auto")) {
						if (!player.isSneaking()) {
							Bukkit.getOnlinePlayers().stream().forEach(p -> p.sendMessage(e.getPlayer().getDisplayName()+" archiquest.cameback"));
						} else {
							player.sendMessage("archiquest.silentunafk");
						}
					}
					bread.setData("afk", null);
					bread.updateDisplayName();
				}
			}
		}
	}
	

}
