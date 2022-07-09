package fun;

import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;

import com.BrainSpigot;

public class DoubleJump implements Listener {

	@SuppressWarnings("unused")
	private BrainSpigot plugin;
 
    public DoubleJump(BrainSpigot plugin) {
        this.plugin = plugin;
    }
        
    @EventHandler
    public void onGMChange(PlayerGameModeChangeEvent e) {
    	if (e.getPlayer().hasPermission("archiquest.doublejump")) {
    		e.getPlayer().setAllowFlight(true);
    		e.getPlayer().setFlying(true);
    	}   
    }
    
    @EventHandler
    public void setFly(PlayerJoinEvent e) {
    	if (e.getPlayer().hasPermission("archiquest.doublejump")) {
    		e.getPlayer().setAllowFlight(true);
    	}  
    }
 
    @EventHandler
    public void nofalldamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player && e.getEntity().hasPermission("archiquest.doublejump") && e.getCause() == DamageCause.FALL) {
            e.setCancelled(true);
        }
    }
    
    @EventHandler
    public void setVelocity(PlayerToggleFlightEvent e) {
    	Player p = e.getPlayer();
    	if (p.hasPermission("architectcore.doublejump")) {
            if (p.getGameMode() == GameMode.CREATIVE || p.getGameMode() == GameMode.SPECTATOR) {
                return;
            } else {
             
                e.setCancelled(true);
             
                p.setAllowFlight(false);
                p.setFlying(false);
             
                p.setVelocity(e.getPlayer().getLocation().getDirection().multiply(1.5).setY(1));
                p.playSound(p.getLocation(), Sound.ENTITY_BAT_TAKEOFF, 1.0f, -5.0f);
             
                p.setFallDistance(1);
            }
    	}
    }
     

}
