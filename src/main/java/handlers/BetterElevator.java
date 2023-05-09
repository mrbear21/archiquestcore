package handlers;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import brain.BrainSpigot;
import objects.BreadMaker;

public class BetterElevator implements Listener {

	  private BrainSpigot spigot;
	  
	  public BetterElevator(BrainSpigot spigot) {
		  this.spigot = spigot;
	  }

	  public void register() {
		spigot.getServer().getPluginManager().registerEvents(this, spigot);
	  }
	   
	@SuppressWarnings("deprecation")
	private Boolean checkTop(Material mat) {
		if (BrainSpigot.version > 12) {
			List<Material> elevator = Arrays.asList(Material.QUARTZ_BLOCK, Material.CHISELED_QUARTZ_BLOCK , Material.QUARTZ_PILLAR, Material.IRON_BLOCK, Material.QUARTZ_SLAB);
			if (elevator.contains(mat)) {
				return true;
			}
		} else  {
			List<Integer> elevator = Arrays.asList(155, 156, 155, 42, 126);	
			if (elevator.contains(mat.getId())) {
				return true;
			}
		}
	    return false;
	}
	
	@SuppressWarnings("deprecation")
	private boolean checkBottom(Material mat) {
		if (BrainSpigot.version > 12) {
			List<Material> elevator = Arrays.asList(Material.REDSTONE_BLOCK);
			if (elevator.contains(mat)) {
				return true;
			}
		} else {
			List<Integer> elevator = Arrays.asList(152);
			if (elevator.contains(mat.getId())) {
				return true;
			}
		}
		return false;
	}

	@EventHandler
	public void liftUp(PlayerMoveEvent e) {
	    Player player = e.getPlayer();
	    Location from = e.getFrom();
	    Location to = e.getTo();

	    if (to.getY() <= from.getY()) {
	        return;
	    }

	    Location loc = new Location(player.getWorld(), from.getBlockX(), from.getBlockY() - 0.1, from.getBlockZ());

	    if (checkTop(loc.getBlock().getType())) {
	        loc.setY(from.getBlockY() - 1.5);
	        if (checkBottom(loc.getBlock().getType())) {
	            for (int i = from.getBlockY(); i < 256; i++) {
	                loc.setY(i);
	                if (checkBottom(loc.getBlock().getType())) {
	                    loc.setY(i + 1);
	                    if (checkTop(loc.getBlock().getType())) {
	                        player.teleport(new Location(player.getWorld(), from.getX(), i + 2, from.getZ(), from.getYaw(), from.getPitch()));
	                        spigot.getBread(player.getName()).sendTitle("", "&aUP", 1);
	                        break;
	                    }
	                }
	            }
	        }
	    }
	}
	
	@EventHandler
	public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
	    Player player = event.getPlayer();
	    BreadMaker bread = spigot.getBread(player.getName());
	    if (bread.getData("elevator").getAsBoolean()) {
	        return;
	    }
	    Location loc = player.getLocation().clone().subtract(0, 0.1, 0);
	    if (!checkTop(loc.getBlock().getType())) {
	        return;
	    }
	    loc.subtract(0, 1.5, 0);
	    if (!checkBottom(loc.getBlock().getType())) {
	        return;
	    }
	    for (int i = player.getLocation().getBlockY() - 2; i > 0; i--) {
	        loc.setY(i);
	        if (checkTop(loc.getBlock().getType())) {
	            loc.add(0, -1, 0);
	            if (checkBottom(loc.getBlock().getType())) {
	                player.teleport(new Location(player.getWorld(), player.getLocation().getX(), i + 1.2, player.getLocation().getZ(), player.getLocation().getYaw(), player.getLocation().getPitch()));
	                bread.sendTitle("", "&cDOWN", 1);
	                bread.setData("elevator", true);
	                spigot.getServer().getScheduler().scheduleSyncDelayedTask(this.spigot, () -> {
	                    bread.setData("elevator", null);
	                }, 10L);
	                break;
	            }
	        }
	    }
	}
	
}
