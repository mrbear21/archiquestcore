package commands;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import brain.BrainSpigot;
import net.md_5.bungee.api.ChatColor;

public class GradientSpecialCommand implements CommandExecutor{
	
	BrainSpigot plugin;
	
	public GradientSpecialCommand(BrainSpigot plugin){
		this.plugin = plugin;
	}

	public void register() {
		plugin.getCommand("sgradient").setExecutor(this);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String arg, String[] args) {
		Player p = Bukkit.getPlayer(sender.getName());
		if(p.hasPermission("archiquest.gradient")) {
		Inventory inv =	Bukkit.createInventory(null, 9, "Special Gradients");
		ItemStack item = new ItemStack(Material.RED_CONCRETE);
		ItemMeta im = item.getItemMeta();
		im.setDisplayName("Red Gradient");
		item.setItemMeta(im);
		inv.setItem(0, item);
		item = new ItemStack(Material.LIME_CONCRETE);
		im = item.getItemMeta();
		im.setDisplayName("Green Gradient");
		item.setItemMeta(im);
		inv.setItem(1, item);
		item = new ItemStack(Material.LIGHT_BLUE_CONCRETE);
		im = item.getItemMeta();
		im.setDisplayName("Blue Gradient");
		item.setItemMeta(im);
		inv.setItem(2, item);
		//�������
		item = new ItemStack(Material.PINK_CONCRETE);
		im = item.getItemMeta();
		im.setDisplayName("Pink Gradient");
		item.setItemMeta(im);
		inv.setItem(3, item);
		item = new ItemStack(Material.BROWN_CONCRETE);
		im = item.getItemMeta();
		im.setDisplayName("Brown Gradient");
		item.setItemMeta(im);
		inv.setItem(4, item);
		item = new ItemStack(Material.GRAY_CONCRETE);
		im = item.getItemMeta();
		im.setDisplayName("Gray Gradient");
		item.setItemMeta(im);
		inv.setItem(5, item);
		item = new ItemStack(Material.VINE);
		im = item.getItemMeta();
		im.setDisplayName("Loza Gradient");
		item.setItemMeta(im);
		inv.setItem(6, item);
		//���
		item = new ItemStack(Material.SNOW_BLOCK);
		im = item.getItemMeta();
		im.setDisplayName("Snow Gradient");
		item.setItemMeta(im);
		inv.setItem(7, item);
		//
		p.openInventory(inv);
		return true;
		}else {
			p.sendMessage(ChatColor.RED+"Required permission: archiquest.gradient");
			return true;
		}
	}

}
