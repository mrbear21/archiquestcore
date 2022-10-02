package fun;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;

import com.BrainSpigot;

public class DoubleJump implements Listener {

	private BrainSpigot plugin;
 
    public DoubleJump(BrainSpigot plugin) {
        this.plugin = plugin;
    }
        
    @EventHandler
    public void onGMChange(PlayerGameModeChangeEvent e) {
    	if (e.getPlayer().hasPermission("archiquest.doublejump") && plugin.getBread(e.getPlayer().getName()).getData("doublejump").getAsBoolean()) {
    		e.getPlayer().setAllowFlight(true);
    		e.getPlayer().setFlying(true);
    	}   
    }
    
    @EventHandler
    public void setFly(PlayerJoinEvent e) {
    	if (e.getPlayer().getGameMode() != GameMode.CREATIVE || e.getPlayer().getGameMode() != GameMode.SPECTATOR) {
        	e.getPlayer().setFlying(false);
    	}
    	if (e.getPlayer().hasPermission("archiquest.doublejump") && plugin.getBread(e.getPlayer().getName()).getData("doublejump").getAsBoolean()) {
    		e.getPlayer().setAllowFlight(true);
    	}   
	}
    
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		if (plugin.doublejump.containsKey(player)) {
			if (player.isOnGround() || event.getFrom().getY() == event.getTo().getY() && player.getLocation().add(0, -1, 0).getBlock().getType() != Material.AIR) {
		        player.setFallDistance((plugin.doublejump.get(player).getY()-player.getLocation().getY())-5 > 5 && player.getInventory().getChestplate().getType() != Material.ELYTRA && player.getLocation().getBlock().getType() != Material.WATER ? 4 : 1);
	            plugin.doublejump.remove(player);
	        	plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() { public void run() {	
	        		player.setAllowFlight(true);
	        	} }, 5);
			}
		}
	}

    @EventHandler
    public void setVelocity(PlayerToggleFlightEvent e) {
    	
    	Player p = e.getPlayer();
    	
    	if (p.hasPermission("archiquest.doublejump")) {

            if (plugin.getBread(p.getName()).getData("doublejump").getAsBoolean() == false || p.getGameMode() == GameMode.CREATIVE || p.getGameMode() == GameMode.SPECTATOR || plugin.doublejump.containsKey(p)) {
             
                return;
             
            } else {
            	
                if (!plugin.doublejump.containsKey(p)) {

	                plugin.doublejump.put(p, p.getLocation());
	             
	                e.setCancelled(true);
	             
	                p.setAllowFlight(false);
	                p.setFlying(false);
	             
	                p.setVelocity(e.getPlayer().getLocation().getDirection().multiply(1.2).setY(1));
	                p.playSound(p.getLocation(), Sound.ENTITY_BAT_TAKEOFF, 1.0f, -10.0f);

                }
            }
    	} 
    }
 

    @EventHandler
    public void removePlayer(PlayerQuitEvent e) {
     
	    if (plugin.doublejump.containsKey(e.getPlayer()))
	        plugin.doublejump.remove(e.getPlayer());
	    	e.getPlayer().setAllowFlight(true);
	}
     

}
