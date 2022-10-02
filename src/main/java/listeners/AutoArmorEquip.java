package listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import com.BrainSpigot;

public class AutoArmorEquip implements Listener {

	private BrainSpigot spigot;
	
	public AutoArmorEquip(BrainSpigot spigot) {
		this.spigot = spigot;
	}

	public void register() {
		Bukkit.getPluginManager().registerEvents(this, spigot);
	}
	
	@EventHandler
	public void onPickupItem(EntityPickupItemEvent e) {
	    LivingEntity livingEntity = e.getEntity();
	    if (livingEntity instanceof Player) {
	      Player p = (Player)livingEntity;
	      Material m = e.getItem().getItemStack().getType();
	      if (m.name().endsWith("BOOTS")) {
	        if (p.getInventory().getBoots() == null) {
	          ItemStack n = new ItemStack(e.getItem().getItemStack().clone());
	          p.getInventory().setBoots(n);
	          e.setCancelled(true);
	          e.getItem().remove();
	          p.updateInventory();
	        } 
	    } else if (m.name().endsWith("LEGGINGS")) {
	        if (p.getInventory().getLeggings() == null) {
	          ItemStack n2 = new ItemStack(e.getItem().getItemStack().clone());
	          p.getInventory().setLeggings(n2);
	          e.setCancelled(true);
	          e.getItem().remove();
	          p.updateInventory();
	        } 
	    } else if (m.name().endsWith("CHESTPLATE")) {
	        if (p.getInventory().getChestplate() == null) {
	          ItemStack n3 = new ItemStack(e.getItem().getItemStack().clone());
	          p.getInventory().setChestplate(n3);
	          e.setCancelled(true);
	          e.getItem().remove();
	          p.updateInventory();
	        } 
	    } else if (m.name().endsWith("HELMET")) {
	        if (p.getInventory().getHelmet() == null) {
	          ItemStack n4 = new ItemStack(e.getItem().getItemStack().clone());
	          p.getInventory().setHelmet(n4);
	          e.setCancelled(true);
	          e.getItem().remove();
	          p.updateInventory();
	        } 
	      } 
	    } 
	}
}
