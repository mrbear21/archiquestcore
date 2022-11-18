package commands;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.BrainSpigot;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;

public class GradientCommand implements CommandExecutor{
	
	BrainSpigot plugin;
	Inventory brownGradient, grayGradient, pinkGradient,greenGradient, redGradient, blueGradient;
	Material[] associatedBrown, associatedGray, associatedPink, associatedGreen, associatedRed, associatedBlue;
	
	public GradientCommand(BrainSpigot plugin) {
		this.plugin = plugin;
		initializateInventories();
		initializateArrays();
	}

	public void register() {
		plugin.getCommand("gradient").setExecutor(this);
		plugin.getCommand("sgradient").setExecutor(this);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String arg, String[] args) {
		Player p = Bukkit.getPlayer(sender.getName());
		if(p.hasPermission("archiquest.gradient")) {
		ItemStack hand = new ItemStack(p.getInventory().getItemInMainHand().getType());
		for(ItemStack is : brownGradient) {
			if(is.getType()==hand.getType()) {
				p.openInventory(brownGradient);
				return true;
			}
		}
		for(ItemStack is : grayGradient) {
			if(is.getType()==hand.getType()) {
				p.openInventory(grayGradient);
				return true;
			}
		}
		for(ItemStack is : pinkGradient) {
			if(is.getType()==hand.getType()) {
				p.openInventory(pinkGradient);
				return true;
			}
		}
		for(ItemStack is : greenGradient) {
			if(is.getType()==hand.getType()) {
				p.openInventory(greenGradient);
				return true;
			}
		}
		for(ItemStack is : redGradient) {
			if(is.getType()==hand.getType()) {
				p.openInventory(redGradient);
				return true;
			}
		}
		for(ItemStack is : blueGradient) {
			if(is.getType()==hand.getType()) {
				p.openInventory(blueGradient);
				return true;
			}
		}
		for(Material m : associatedGray) {
			if(m == hand.getType()) {
				p.openInventory(grayGradient);
				return true;
			}
		}
		for(Material m : associatedBrown) {
			if(m == hand.getType()) {
				p.openInventory(brownGradient);
				return true;
			}
		}
		for(Material m : associatedGreen) {
			if(m == hand.getType()) {
				p.openInventory(greenGradient);
				return true;
			}
		}
		for(Material m : associatedRed) {
			if(m == hand.getType()) {
				p.openInventory(redGradient);
				return true;
			}
		}
		for(Material m : associatedPink) {
			if(m == hand.getType()) {
				p.openInventory(pinkGradient);
				return true;
			}
		}
		for(Material m : associatedBlue) {
			if(m == hand.getType()) {
				p.openInventory(blueGradient);
				return true;
			}
		}
		//TODO вивід в дс пліазе
		TextComponent discord = new TextComponent("Извините, градиент не найден. Напишите нам об этом в /discord. Материал: "+ hand.getType());
		discord.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Нажмите, что бы получить приглашение")));
		discord.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/discord"));
		discord.setColor(ChatColor.RED);
		p.spigot().sendMessage(discord);
		return true;
		}else { // Купи права
			p.sendMessage(ChatColor.RED+"Required permission: archiquest.gradient");
			return true;
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
	}

	private void initializateArrays() {
		associatedGray = new Material[]{Material.BEDROCK, Material.GRAVEL, Material.COAL_ORE, Material.BIRCH_LOG, Material.BIRCH_WOOD,
				Material.ACACIA_WOOD, Material.WHITE_WOOL, Material.LIGHT_GRAY_WOOL, Material.BIRCH_SLAB, Material.STONE_SLAB,
				Material.SMOOTH_STONE_SLAB, Material.COBBLESTONE_SLAB, Material.STONE_BRICK_SLAB, Material.QUARTZ_SLAB, Material.COBBLESTONE_STAIRS,
				Material.BASALT, Material.CRACKED_STONE_BRICKS, Material.CHISELED_STONE_BRICKS, Material.STONE_BRICK_STAIRS};
		associatedBrown = new Material[] {Material.IRON_ORE, Material.OAK_LOG, Material.SPRUCE_LOG, Material.JUNGLE_LOG, Material.ACACIA_LOG,
				Material.DARK_OAK_LOG, Material.STRIPPED_OAK_LOG, Material.STRIPPED_SPRUCE_LOG, Material.STRIPPED_JUNGLE_LOG,
				Material.STRIPPED_DARK_OAK_LOG, Material.JUNGLE_SLAB, Material.OAK_SLAB, Material.SPRUCE_SLAB, Material.DARK_OAK_SLAB,
				Material.PETRIFIED_OAK_SLAB, Material.BRICK_SLAB, Material.NETHER_BRICK_SLAB, Material.BOOKSHELF, Material.OAK_STAIRS,
				Material.SOUL_SAND, Material.BRICK_STAIRS, Material.NETHER_BRICK_STAIRS, Material.CHISELED_NETHER_BRICKS, Material.CRACKED_NETHER_BRICKS,
				Material.NETHER_BRICKS};
		associatedPink = new Material[] {Material.CRIMSON_SLAB, Material.CRIMSON_STEM, Material.STRIPPED_CRIMSON_STEM, Material.PURPUR_SLAB, 
				Material.PURPUR_PILLAR, Material.PURPUR_STAIRS, Material.MYCELIUM};
		associatedBlue = new Material[] {Material.WARPED_NYLIUM, Material.WARPED_STEM, Material.STRIPPED_WARPED_STEM, Material.LAPIS_ORE, 
				Material.WARPED_SLAB, Material.PRISMARINE_BRICK_SLAB, Material.PRISMARINE_SLAB, Material.DARK_PRISMARINE_SLAB, Material.DIAMOND_ORE,
				Material.ICE};
		associatedRed = new Material[] {Material.SAND, Material.RED_SAND, Material.GOLD_ORE, Material.NETHER_GOLD_ORE, Material.SANDSTONE_SLAB,
				Material.CRIMSON_HYPHAE, Material.STRIPPED_ACACIA_LOG, Material.CUT_SANDSTONE_SLAB, Material.SPONGE, Material.WET_SPONGE,
				Material.SANDSTONE, Material.CHISELED_SANDSTONE, Material.CUT_SANDSTONE, Material.ACACIA_SLAB, Material.SANDSTONE_STAIRS,
				Material.CUT_RED_SANDSTONE_SLAB, Material.REDSTONE_ORE, Material.CARVED_PUMPKIN, Material.NETHERRACK, Material.GLOWSTONE,
				Material.JACK_O_LANTERN};
		associatedGreen = new Material[] {Material.GRASS_BLOCK, Material.MOSSY_COBBLESTONE, Material.MOSSY_STONE_BRICKS, Material.EMERALD_ORE};
	}
}
