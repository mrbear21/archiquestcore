package modules;

import java.util.Arrays;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.BrainSpigot;

import objects.BreadMaker;

public class MenuDrawer implements Listener {

	private BrainSpigot plugin;

	public HashMap<Player, ItemStack[]> inventorySaves = new HashMap<Player, ItemStack[]>();
	public HashMap<Player, ItemStack[]> ArmorSaves = new HashMap<Player, ItemStack[]>();
	public HashMap<Player, Boolean> menucooldown = new HashMap<Player, Boolean>();
	public HashMap<Player, String> name = new HashMap<Player, String>();
	public HashMap<Player, String[]> optionNames = new HashMap<Player, String[]>();
	public HashMap<Player, ItemStack[]> optionIcons = new HashMap<Player, ItemStack[]>();
	public HashMap<Player, HashMap<Integer, String>> optionCommands = new HashMap<Player, HashMap<Integer, String>>();
	public HashMap<Player, HashMap<Integer, Boolean>> optionWillClose = new HashMap<Player, HashMap<Integer, Boolean>>();
	public HashMap<Player, Boolean> optionIsClosed = new HashMap<Player, Boolean>();
	
	private Player player;
	
	public MenuDrawer(BrainSpigot plugin, Player player) {
		this.player = player;
		this.plugin = plugin;
		
		optionNames.put(player, new String[54]);
		optionIcons.put(player, new ItemStack[54]);
		optionCommands.put(player, new HashMap<Integer, String>());
		optionWillClose.put(player, new HashMap<Integer, Boolean>());
		
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	public MenuDrawer setOption(String name, int position, String command, ItemStack icon, String[] info, Boolean willClose) {
		position = position + 9;
		optionNames.get(player)[position] = name;
		optionIcons.get(player)[position] = setItemNameAndLore(icon, name, info);
		optionCommands.get(player).put(position, command);
		optionWillClose.get(player).put(position, willClose);
		return this;
	}

	public void openMenu(String name) {
		
		if (!inventorySaves.containsKey(player)) {
			inventorySaves.put(player, player.getInventory().getContents());
			ArmorSaves.put(player, player.getEquipment().getArmorContents());
		}

	
		if (this.name.containsKey(player)) {
			optionIsClosed.put(player, false);
			player.closeInventory();
		} 
		
		Inventory inventory = Bukkit.createInventory(player, 9, name);
		this.name.put(player, name);
		player.openInventory(inventory);

		optionIsClosed.put(player, true);
		
		player.getInventory().clear();
		
		BreadMaker bread = plugin.getBread(player.getName());
		
		Locales locale = new Locales(plugin);
		
		String lang = bread.getLanguage();

		
		String[] lore = { ChatColor.translateAlternateColorCodes('&', "&f" + locale.translateString("click-to-open", lang)) };
		
		if (!this.name.get(player).contains("MENU")) {
			
			inventory.setItem(0, setItemNameAndLore(new ItemStack(Material.ARROW, 1), ChatColor.translateAlternateColorCodes('&', "&e" + locale.translateString("back", lang)), lore));
		
		}


		String[] logout = {ChatColor.translateAlternateColorCodes('&', "&f" + locale.translateString("click-to-close", lang)) };
		inventory.setItem(8, setItemNameAndLore(new ItemStack(Material.RED_STAINED_GLASS_PANE, 1),
				ChatColor.translateAlternateColorCodes('&', "&e" + locale.translateString("close", lang)), logout));

				
		player.getInventory().setItem(3, setItemNameAndLore(new ItemStack(Material.DIAMOND_HELMET, 1),
				ChatColor.translateAlternateColorCodes('&', "&9" + locale.translateString("discord", lang)), lore));

		player.getInventory().setItem(5, setItemNameAndLore(new ItemStack(Material.PAPER, 1),
				ChatColor.translateAlternateColorCodes('&', "&c" + locale.translateString("rules", lang)), lore));

		for (int i = 0; i < optionIcons.get(player).length; i++) {
			if (optionIcons.get(player)[i] != null) {
				player.getInventory().setItem(i, optionIcons.get(player)[i]);
			}
		}
	/*
		if (plugin.menucooldown.containsKey(player)) {
			return;
		}
			
		plugin.menucooldown.put(player, true);
		
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() { public void run() {	
			
			plugin.menucooldown.remove(player);
			
		} }, 50);
	*/
		
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onDrop(PlayerDropItemEvent event) {
		if (inventorySaves.containsKey(event.getPlayer())) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void updateWorth(InventoryCloseEvent event) {
		if ((event.getPlayer() instanceof Player)) {

			if (optionIsClosed.get(event.getPlayer())) {
				
				destroy((Player) event.getPlayer());
				returnInventory();
			}
				
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerJoin(PlayerJoinEvent event) {
		if ((event.getPlayer() instanceof Player)) {
			returnInventory();
		}
	}

	public void destroy(Player player) {
		
		name.remove(player);
		optionNames.remove(player);
		optionIcons.remove(player);
		optionCommands.remove(player);
		optionWillClose.remove(player);
		optionIsClosed.remove(player);

	}

	@EventHandler(priority = EventPriority.MONITOR)
	void onInventoryClick(InventoryClickEvent event) {
		Player player = ((Player) event.getWhoClicked());
		
		if (event.getView().getTitle().equals(name.get((Player) event.getWhoClicked()))) {
			
			event.setCancelled(true);
			int slot = event.getRawSlot();

			if (optionCommands.get(player).containsKey(slot)) {
				player.chat("/"+optionCommands.get(player).get(slot));
				if (optionWillClose.get(player).containsKey(slot)) {
					if (optionWillClose.get(player).get(slot)) {
						player.closeInventory();
						destroy(player);
					} else {
						optionIsClosed.put(player, false);
					}
				}
				return;
			}
			
			if (slot == 0) {
				player.chat("/menu");
				return;
			}
			if (slot == 8) {
				player.closeInventory();
				destroy(player);
				return;
			}
			if (slot == 3) {
				player.closeInventory();
				destroy(player);
				player.chat("/discord");
				return;
			}
			if (slot == 5) {
				player.closeInventory();
				destroy(player);
				player.chat("/rules");
				return;
			}
		}
	}


	public void returnInventory() {
		
		if (inventorySaves.containsKey(player)) {

			player.getInventory().clear();
			player.getInventory().setContents(inventorySaves.get(player));
			player.getInventory().setArmorContents(ArmorSaves.get(player));

			inventorySaves.remove(player);
			ArmorSaves.remove(player);
		}
	}
	


	private ItemStack setItemNameAndLore(ItemStack item, String name, String[] lore) {
		ItemMeta im = item.getItemMeta();
		im.setDisplayName(name);
		im.setLore(Arrays.asList(lore));
		item.setItemMeta(im);
		return item;
	}

}
