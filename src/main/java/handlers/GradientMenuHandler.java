package handlers;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import brain.BrainSpigot;


public class GradientMenuHandler implements Listener{

	BrainSpigot plugin;
	Inventory lozaGradient, snowGradient, brownGradient, redGradient, blueGradient, grayGradient, greenGradient, pinkGradient;
	
	public GradientMenuHandler(BrainSpigot plugin) {
		this.plugin = plugin;
		initializateInventories();
	}

	public void register() {
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		String[] names = {"Brown Gradient", "Gray Gradient", "Pink Gradient", "Green Gradient", "Red Gradient", "Blue Gradient",
				"Loza Gradient", "Snow Gradient"};
		for(String s : names) {
			if(e.getView().getTitle().equals(s)) {
				if(e.getRawSlot() < e.getInventory().getSize()) {
				e.setCancelled(true);
				e.getWhoClicked().getInventory().addItem(e.getInventory().getItem(e.getSlot()));
				}
				break;
			}
		}
		if(e.getView().getTitle().equals("Special Gradients")) {
			if(e.getRawSlot() < e.getInventory().getSize()) {
			switch(e.getInventory().getItem(e.getSlot()).getItemMeta().getDisplayName()) {
			case "Loza Gradient":
				e.getWhoClicked().openInventory(lozaGradient);
				break;
			case "Snow Gradient":
				e.getWhoClicked().openInventory(snowGradient);
				break;
			case "Red Gradient":
				e.getWhoClicked().openInventory(redGradient);
				break;
			case "Blue Gradient":
				e.getWhoClicked().openInventory(blueGradient);
				break;
			case "Pink Gradient":
				e.getWhoClicked().openInventory(pinkGradient);
				break;
			case "Green Gradient":
				e.getWhoClicked().openInventory(greenGradient);
				break;
			case "Gray Gradient":
				e.getWhoClicked().openInventory(grayGradient);
				break;
			case "Brown Gradient":
				e.getWhoClicked().openInventory(brownGradient);
				break;
			}
			e.setCancelled(true);
			}
		}
	}

	public void initializateInventories() {
		brownGradient = Bukkit.createInventory(null, 27, "Brown Gradient");
		brownGradient.addItem(new ItemStack(Material.BLACK_TERRACOTTA),new ItemStack(Material.GRAY_TERRACOTTA),
				new ItemStack(Material.BROWN_TERRACOTTA), new ItemStack(Material.SOUL_SOIL), new ItemStack(Material.SPRUCE_WOOD),
				new ItemStack(Material.DARK_OAK_WOOD),new ItemStack(Material.DARK_OAK_PLANKS), new ItemStack(Material.BROWN_CONCRETE),
				new ItemStack(Material.BROWN_WOOL), new ItemStack(Material.PODZOL), new ItemStack(Material.JUNGLE_WOOD),
				new ItemStack(Material.STRIPPED_DARK_OAK_WOOD), new ItemStack(Material.OAK_WOOD), new ItemStack(Material.SPRUCE_PLANKS),
				new ItemStack(Material.STRIPPED_SPRUCE_WOOD), new ItemStack(Material.BARREL), new ItemStack(Material.OAK_PLANKS),
				new ItemStack(Material.STRIPPED_OAK_WOOD), new ItemStack(Material.STRIPPED_JUNGLE_WOOD), new ItemStack(Material.JUNGLE_PLANKS),
				new ItemStack(Material.COARSE_DIRT), new ItemStack(Material.DIRT), new ItemStack(Material.BROWN_MUSHROOM_BLOCK), 
				new ItemStack(Material.TERRACOTTA), new ItemStack(Material.POLISHED_GRANITE), new ItemStack(Material.GRANITE),
				new ItemStack(Material.BRICKS));
		grayGradient = Bukkit.createInventory(null, 27, "Gray Gradient");
		grayGradient.addItem(new ItemStack(Material.BLACK_CONCRETE), new ItemStack(Material.COAL_BLOCK), new ItemStack(Material.BLACK_WOOL),
				new ItemStack(Material.OBSIDIAN), new ItemStack(Material.BLACKSTONE), new ItemStack(Material.POLISHED_BLACKSTONE_BRICKS),
				new ItemStack(Material.POLISHED_BLACKSTONE), new ItemStack(Material.NETHERITE_BLOCK),new ItemStack(Material.GRAY_CONCRETE),
				new ItemStack(Material.GRAY_WOOL),new ItemStack(Material.CYAN_TERRACOTTA), new ItemStack(Material.POLISHED_BASALT),
				new ItemStack(Material.STONE_BRICKS), new ItemStack(Material.DEAD_BRAIN_CORAL_BLOCK),new ItemStack(Material.LIGHT_GRAY_CONCRETE),
				new ItemStack(Material.POLISHED_ANDESITE), new ItemStack(Material.COBBLESTONE),new ItemStack(Material.ANDESITE),
				new ItemStack(Material.STONE), new ItemStack(Material.SMOOTH_STONE),new ItemStack(Material.CLAY),
				new ItemStack(Material.WHITE_CONCRETE), new ItemStack(Material.POLISHED_DIORITE),new ItemStack(Material.DIORITE),
				new ItemStack(Material.IRON_BLOCK), new ItemStack(Material.SMOOTH_QUARTZ), new ItemStack(Material.SNOW_BLOCK));
		pinkGradient = Bukkit.createInventory(null, 18, "Pink Gradient");
		pinkGradient.addItem(new ItemStack(Material.PINK_GLAZED_TERRACOTTA), new ItemStack(Material.PINK_WOOL), new ItemStack(Material.PINK_SHULKER_BOX),
				new ItemStack(Material.PINK_CONCRETE), new ItemStack(Material.MAGENTA_GLAZED_TERRACOTTA),new ItemStack(Material.MAGENTA_WOOL),
				new ItemStack(Material.MAGENTA_SHULKER_BOX), new ItemStack(Material.MAGENTA_CONCRETE), new ItemStack(Material.STRIPPED_CRIMSON_HYPHAE),
				new ItemStack(Material.MAGENTA_TERRACOTTA), new ItemStack(Material.PURPLE_TERRACOTTA), new ItemStack(Material.CRIMSON_PLANKS),
				new ItemStack(Material.SHULKER_BOX), new ItemStack(Material.PURPUR_BLOCK), new ItemStack(Material.PURPLE_WOOL),
				new ItemStack(Material.PURPLE_SHULKER_BOX), new ItemStack(Material.PURPLE_CONCRETE), new ItemStack(Material.PURPLE_GLAZED_TERRACOTTA));
		greenGradient = Bukkit.createInventory(null, 9, "Green Gradient");
		greenGradient.addItem(new ItemStack(Material.GREEN_TERRACOTTA), new ItemStack(Material.GREEN_WOOL), new ItemStack(Material.LIME_TERRACOTTA),
				new ItemStack(Material.GREEN_GLAZED_TERRACOTTA), new ItemStack(Material.MELON), new ItemStack(Material.LIME_CONCRETE),
				new ItemStack(Material.LIME_WOOL), new ItemStack(Material.LIME_GLAZED_TERRACOTTA), new ItemStack(Material.EMERALD_BLOCK));
		redGradient = Bukkit.createInventory(null, 27, "Red Gradient");
		redGradient.addItem(new ItemStack(Material.RED_NETHER_BRICKS), new ItemStack(Material.NETHER_WART_BLOCK), new ItemStack(Material.CRIMSON_NYLIUM),
				new ItemStack(Material.RED_CONCRETE), new ItemStack(Material.RED_WOOL), new ItemStack(Material.RED_GLAZED_TERRACOTTA),
				new ItemStack(Material.RED_MUSHROOM_BLOCK), new ItemStack(Material.STRIPPED_ACACIA_WOOD), new ItemStack(Material.ACACIA_PLANKS),
				new ItemStack(Material.ORANGE_TERRACOTTA), new ItemStack(Material.SMOOTH_RED_SANDSTONE), new ItemStack(Material.ORANGE_CONCRETE),
				new ItemStack(Material.ORANGE_WOOL), new ItemStack(Material.PUMPKIN), new ItemStack(Material.HONEYCOMB_BLOCK),
				new ItemStack(Material.YELLOW_TERRACOTTA), new ItemStack(Material.YELLOW_CONCRETE), new ItemStack(Material.YELLOW_WOOL),
				new ItemStack(Material.GOLD_BLOCK), new ItemStack(Material.YELLOW_GLAZED_TERRACOTTA), new ItemStack(Material.BEE_NEST),
				new ItemStack(Material.BEEHIVE), new ItemStack(Material.STRIPPED_BIRCH_WOOD), new ItemStack(Material.BIRCH_PLANKS),
				new ItemStack(Material.END_STONE_BRICKS), new ItemStack(Material.END_STONE), new ItemStack(Material.SMOOTH_SANDSTONE));
		blueGradient = Bukkit.createInventory(null, 18, "Blue Gradient");
		blueGradient.addItem(new ItemStack(Material.WARPED_HYPHAE), new ItemStack(Material.BLUE_GLAZED_TERRACOTTA), new ItemStack(Material.BLUE_CONCRETE),
				new ItemStack(Material.LAPIS_BLOCK), new ItemStack(Material.BLUE_WOOL), new ItemStack(Material.WARPED_WART_BLOCK),
				new ItemStack(Material.DARK_PRISMARINE), new ItemStack(Material.WARPED_PLANKS), new ItemStack(Material.CYAN_CONCRETE),
				new ItemStack(Material.CYAN_WOOL), new ItemStack(Material.STRIPPED_WARPED_HYPHAE), new ItemStack(Material.CYAN_GLAZED_TERRACOTTA),
				new ItemStack(Material.PRISMARINE), new ItemStack(Material.PRISMARINE_BRICKS), new ItemStack(Material.LIGHT_BLUE_WOOL),
				new ItemStack(Material.DIAMOND_BLOCK), new ItemStack(Material.LIGHT_BLUE_CONCRETE), new ItemStack(Material.LIGHT_BLUE_GLAZED_TERRACOTTA));
		lozaGradient = Bukkit.createInventory(null, 9, "Loza Gradient");
		lozaGradient.addItem(new ItemStack(Material.GREEN_WOOL), new ItemStack(Material.GREEN_CONCRETE_POWDER),
				new ItemStack(Material.MOSSY_COBBLESTONE), new ItemStack(Material.COBBLESTONE),
				new ItemStack(Material.ANDESITE), new ItemStack(Material.STONE));
		snowGradient = Bukkit.createInventory(null, 9, "Snow Gradient");
		snowGradient.addItem(new ItemStack(Material.SNOW_BLOCK), new ItemStack(Material.SMOOTH_QUARTZ),
				new ItemStack(Material.DIORITE),new ItemStack(Material.ANDESITE), new ItemStack(Material.STONE));
	}
}
