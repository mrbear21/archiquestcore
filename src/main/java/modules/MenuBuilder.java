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
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.BrainSpigot;

import objects.BreadMaker;

public class MenuBuilder implements Listener {

	private BrainSpigot plugin;

	public HashMap<Player, String> name = new HashMap<Player, String>();
	public HashMap<Player, String[]> optionNames = new HashMap<Player, String[]>();
	public HashMap<Player, ItemStack[]> optionIcons = new HashMap<Player, ItemStack[]>();
	public HashMap<Player, HashMap<Integer, String[]>> optionCommands = new HashMap<Player, HashMap<Integer, String[]>>();
	
	private Player player;
	private BreadMaker bread;
	
	public MenuBuilder(BrainSpigot plugin, Player player, String name) {
		this.player = player;
		this.plugin = plugin;
		this.name.put(player, "➜ "+name);
		this.optionNames.put(player, new String[54]);
		this.optionIcons.put(player, new ItemStack[54]);
		this.optionCommands.put(player, new HashMap<Integer, String[]>());
		this.bread = plugin.getBread(player.getName());
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	public MenuBuilder setOption(String name, int position, String command, Material icon, String[] info) {
		return setOption(name, position, new String[] {command}, icon, info);
	}
	
	public MenuBuilder setOption(String name, int position, String[] command, Material icon, String[] info) {
		position = position + 9;
		optionNames.get(player)[position] = name;
		optionIcons.get(player)[position] = setItemNameAndLore(new ItemStack(icon), name, info);
		optionCommands.get(player).put(position, command);
		return this;
	}

	public void build() {
		
		BreadMaker bread = plugin.getBread(player.getName());
		Locales locale = new Locales(plugin);
		String lang = bread.getLanguage();

		saveInventory();
		player.getInventory().clear();
		player.getInventory().setArmorContents(plugin.ArmorSaves.get(player));
		Inventory inventory = Bukkit.createInventory(player, 9, name.get(player));

		if (!this.name.get(player).contains("MENU")) {
			inventory.setItem(0, setItemNameAndLore(new ItemStack(Material.ARROW, 1), ChatColor.translateAlternateColorCodes('&', "&e" + locale.translateString("menu.back", lang)), new String[] {}));
		}
		inventory.setItem(8, setItemNameAndLore(new ItemStack(Material.RED_STAINED_GLASS_PANE, 1),
				ChatColor.translateAlternateColorCodes('&', "&e" + locale.translateString("menu.close", lang)), new String[] {}));	
		for (int i = 0; i < optionIcons.get(player).length; i++) {
			if (optionIcons.get(player)[i] != null) {
				player.getInventory().setItem(i, optionIcons.get(player)[i]);
			}
		}
		
		player.openInventory(inventory);

	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onDrop(PlayerDropItemEvent event) {
		if (name.containsKey(player)) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onInventoryClose(InventoryCloseEvent event) {
		if ((event.getPlayer() instanceof Player)) {
			if (name.containsKey(player)) {
				returnInventory();
				destroy((Player) event.getPlayer());
			}
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerJoin(PlayerQuitEvent event) {
		if ((event.getPlayer() instanceof Player)) {
			returnInventory();
			destroy(event.getPlayer());
		}
	}

	private void destroy(Player player) {
		name.remove(player);
		optionNames.remove(player);
		optionIcons.remove(player);
		optionCommands.remove(player);
		plugin.inventorySaves.remove(player);
		plugin.ArmorSaves.remove(player);
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onInventoryClick(InventoryClickEvent event) {
		Player player = ((Player) event.getWhoClicked());
		if (event.getView().getTitle().equals(name.get((Player) event.getWhoClicked()))) {
			event.setCancelled(true);
			
			Cooldown cooldown = new Cooldown(plugin, player.getName());
			
			int slot = event.getRawSlot();
			if (optionCommands.get(player).containsKey(slot)) {
				
				if (cooldown.hasCooldown("menu")) {
					return;
				} else {
					cooldown.setCooldown("menu", 1);
				}
				
				String[] commands = optionCommands.get(player).get(slot);

				returnInventory();
				
				plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() { public void run() {	
					
					player.closeInventory();
					
					for (String command : commands) {
						player.chat("/"+command);
					}
					
				} }, 1);
				
	
				return;
			}
			if (slot == 0) {
				player.closeInventory();
				player.chat("/menu");
				return;
			}
			if (slot == 8) {
				player.closeInventory();
				return;
			}
		}
	}


	private void saveInventory() {
		if (!plugin.inventorySaves.containsKey(player)) {
			plugin.inventorySaves.put(player, player.getInventory().getContents());
			plugin.ArmorSaves.put(player, player.getEquipment().getArmorContents());
		}
	}
	
	private void returnInventory() {
		if (plugin.inventorySaves.containsKey(player)) {
			player.getInventory().clear();
			player.getInventory().setContents(plugin.inventorySaves.get(player));
			player.getInventory().setArmorContents(plugin.ArmorSaves.get(player));
		}
	}
	
	private ItemStack setItemNameAndLore(ItemStack item, String name, String[] lore) {
		ItemMeta im = item.getItemMeta();
		im.setDisplayName(ChatColor.GOLD+bread.getLocales().translateString(name));
		for (int i = 0; i<lore.length; i++) {
			lore[i] = ChatColor.GRAY+bread.getLocales().translateString(lore[i]);
		}
		im.setLore(Arrays.asList(lore));
		im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		item.setItemMeta(im);
		return item;
	}

}
