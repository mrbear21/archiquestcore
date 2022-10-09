package listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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

import com.BrainSpigot;
import com.SystemMessage;

import modules.Cooldown;
import objects.BreadMaker;


public class SpigotListeners implements Listener {

	private BrainSpigot spigot;
	
	public SpigotListeners(BrainSpigot plugin) {
		this.spigot = plugin;
	}
	
	
    @SuppressWarnings("deprecation")
	@EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
    	
    	Player player = event.getPlayer();
    	player.setPlayerListName(spigot.getBread(player.getName()).getPrefix() + player.getName());

		BreadMaker bread = spigot.getBread(event.getPlayer().getName());
		if (!bread.getData("language").isNotNull()) {	
			//	new LanguageCommand(spigot).langSelector(player);
				
			String locale = event.getPlayer().getLocale().split("_")[0];
			bread.setData("language", locale.equals("uk") ? "ua" : locale);
		}

        for (Player p : Bukkit.getOnlinePlayers()) {
        	if (spigot.getBread(p.getName()).getData("vanish").getAsBoolean()) {
        		player.hidePlayer(p);
        	}
        }
        
		
    }
    
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
    	BreadMaker bread = spigot.getBread(event.getPlayer().getName());
		bread.clearData();
    }
	
    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) {

    	if (spigot.locales.size() == 0) {
    		Bukkit.getScheduler ().runTaskLater (spigot, () -> new SystemMessage(spigot).newMessage("locale", new String[] {"get"}, event.getPlayer()), 20);
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
	
		if (item != null) {
			if (item.getAmount() == 1) {
				spigot.getServer().getScheduler().scheduleSyncDelayedTask(spigot, new Runnable() { public void run() {	
					for (ItemStack i : inventory) {
						if (i != null) {
							ItemStack ii = i.clone();
							if (item.getType() == ii.getType()) {
								inventory.remove(ii);
								inventory.setItemInMainHand(ii);
								break;
							}
						}
					}
				} }, 1);
			}
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
