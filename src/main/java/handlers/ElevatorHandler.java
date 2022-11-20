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

public class ElevatorHandler  implements Listener {
	
  private BrainSpigot spigot;
  
  public ElevatorHandler(BrainSpigot spigot) {
	  this.spigot = spigot;
  }

  public void register() {
	spigot.getServer().getPluginManager().registerEvents(this, spigot);
  }
   
  List<Material> elevator = Arrays.asList(Material.QUARTZ_BLOCK, Material.CHISELED_QUARTZ_BLOCK , Material.QUARTZ_PILLAR, Material.IRON_BLOCK);
    
  private Boolean check(Material mat) {
	  
	  if (elevator.contains(mat) || mat.name().contains("SLAB") || mat.name().contains("CARPET")) {
		  return true;
	  } 
	  return false;
  }
  
  @EventHandler
  public void liftUp(PlayerMoveEvent e) {
	Player player = e.getPlayer();
	BreadMaker bread = spigot.getBread(player.getName());
    if (e.getTo().getY() > e.getFrom().getY()) {
      Location loc = new Location(player.getWorld(), player.getLocation().getBlockX(), (player.getLocation().getBlockY() - 0.1), player.getLocation().getBlockZ());
      if (check(loc.getBlock().getType())) {
        loc = new Location(player.getWorld(), player.getLocation().getBlockX(), (player.getLocation().getBlockY() - 1.5), player.getLocation().getBlockZ());
        if (loc.getBlock().getType() == Material.REDSTONE_BLOCK) {
          for (int i = player.getLocation().getBlockY(); i < 256; i++) {
            loc = new Location(player.getWorld(), player.getLocation().getBlockX(), i, player.getLocation().getBlockZ());
            
            if (loc.getBlock().getType() == Material.REDSTONE_BLOCK) {
              loc = new Location(player.getWorld(), player.getLocation().getBlockX(), (i + 1), player.getLocation().getBlockZ());
              if (check(loc.getBlock().getType())) {
            	player.teleport(new Location(player.getWorld(), player.getLocation().getX(), (i + 2), player.getLocation().getZ(), player.getLocation().getYaw(), player.getLocation().getPitch()));
                bread.sendTitle("", "&aUP", 1);
                break;
              } 
            } 
          } 
        }
      } 
    } 
  }


  @EventHandler
  public void LiftDown(final PlayerToggleSneakEvent e) {
	Player player = e.getPlayer();
	BreadMaker bread = spigot.getBread(player.getName());
	
	
    if (bread.getData("elevator").getAsBoolean() == false) {
      Location loc = new Location(player.getWorld(), player.getLocation().getBlockX(), (player.getLocation().getY() - 0.1), player.getLocation().getBlockZ());
      if (check(loc.getBlock().getType())) {
        loc = new Location(player.getWorld(), player.getLocation().getBlockX(), (player.getLocation().getY() - 1.5), player.getLocation().getBlockZ());
        if (loc.getBlock().getType() == Material.REDSTONE_BLOCK) {
          for (int i = player.getLocation().getBlockY() - 2; i > 0; i--) {
            loc = new Location(player.getWorld(), player.getLocation().getBlockX(), i, player.getLocation().getBlockZ());
            
            if (check(loc.getBlock().getType())) {
              loc = new Location(player.getWorld(), player.getLocation().getBlockX(), (i - 1), player.getLocation().getBlockZ());
              if (loc.getBlock().getType() == Material.REDSTONE_BLOCK) {
            	player.teleport(new Location(player.getWorld(), player.getLocation().getX(), (i + 1.2), player.getLocation().getZ(), player.getLocation().getYaw(), player.getLocation().getPitch()));
                bread.sendTitle("", "&cDOWN", 1);
                bread.setData("elevator", "true");
                break;
              } 
            } 
          } 
          
          spigot.getServer().getScheduler().scheduleSyncDelayedTask(this.spigot, new Runnable() {
              public void run() {
            	  bread.setData("elevator", null);
              }
            },  5);
          
        }
      } 
      
    } 
  }
}
