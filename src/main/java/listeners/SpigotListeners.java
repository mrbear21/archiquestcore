package listeners;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.BrainSpigot;
import com.SystemMessage;

import objects.BreadMaker;


public class SpigotListeners implements Listener {

	private BrainSpigot spigot;
	
	public SpigotListeners(BrainSpigot plugin) {
		this.spigot = plugin;
	}
	
	
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
    	
    	Player player = event.getPlayer();
    	player.setPlayerListName(spigot.getBread(player.getName()).getPrefix() + player.getName());

		BreadMaker bread = spigot.getBread(event.getPlayer().getName());
		if (!bread.getData("language").isNotNull()) {
			String locale = event.getPlayer().getLocale().split("_")[0];
			bread.setData("language", locale.equals("uk") ? "ua" : locale);
		}
		
		if (!player.hasPlayedBefore()) {
			ItemStack item = new ItemStack(Material.COMPASS);
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(bread.getLocales().translateString("archiquest.menu"));
			meta.setLore(Arrays.asList(bread.getLocales().translateString("archiquest.click-to-open")));
			item.setItemMeta(meta);
			player.getInventory().setItem(0, item);
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
	public void interact(PlayerInteractEvent e) {
		if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			
			Player p = e.getPlayer();
			ItemStack item = p.getInventory().getItemInMainHand();

			if (item != null) {

				if (item.getType() == Material.COMPASS) {
	
					e.setCancelled(true);
					p.performCommand("menu");
	
				}
				
			}
		}
	}
    
    
}
