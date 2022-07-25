package fun;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
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
    	if (e.getPlayer().hasPermission("archiquest.doublejump") && plugin.getBread(e.getPlayer().getName()).getData("doublejump").getAsBoolean()) {
    		e.getPlayer().setAllowFlight(true);
        	e.getPlayer().setFlying(true);
    	}   
	
	}
    
	@EventHandler
	public void onMove(PlayerMoveEvent event) {
		String player = event.getPlayer().getName();
		Player p = event.getPlayer();
		if (plugin.doublejump.contains(player)) {
			if (event.getFrom().getY() == event.getTo().getY() && p.getLocation().add(0, -1, 0).getBlock().getType() != Material.AIR) {
	            plugin.doublejump.remove(player);
	    		p.setAllowFlight(true);
			}
		}
	}
    
    @EventHandler
    public void nofalldamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player && plugin.doublejump.contains(e.getEntity().getName())) {
            e.setCancelled(true);
        }
    }
    
    @EventHandler
    public void setVelocity(PlayerToggleFlightEvent e) {
    	
    	Player p = e.getPlayer();
    	
    	if (p.hasPermission("archiquest.doublejump")) {

            if (plugin.getBread(p.getName()).getData("doublejump").getAsBoolean() == false || p.getGameMode() == GameMode.CREATIVE || p.getGameMode() == GameMode.SPECTATOR || plugin.doublejump.contains(p.getName())) {
             
                return;
             
            } else {
            	
                if (!plugin.doublejump.contains(p.getName())) {

	                plugin.doublejump.add(p.getName());
	             
	                e.setCancelled(true);
	             
	                p.setAllowFlight(false);
	                p.setFlying(false);
	             
	                p.setVelocity(e.getPlayer().getLocation().getDirection().multiply(1.2).setY(1));
	                p.playSound(p.getLocation(), Sound.ENTITY_BAT_TAKEOFF, 1.0f, -5.0f);
	             
	                p.setFallDistance(1);
	                
                }
            }
    	} 
    }
 

    @EventHandler
    public void removePlayer(PlayerQuitEvent e) {
     
        if (plugin.doublejump.contains(e.getPlayer().getName()))
        	plugin.doublejump.remove(e.getPlayer().getName());
     
    }
     

}
