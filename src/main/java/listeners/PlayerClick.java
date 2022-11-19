package listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import com.BrainSpigot;

import objects.Cooldown;

public class PlayerClick implements Listener {

	private BrainSpigot spigot;
	
	public PlayerClick(BrainSpigot spigot) {
		this.spigot = spigot;
	}
		
	public void register() {
		Bukkit.getPluginManager().registerEvents(this, spigot);
	}
	
    @EventHandler
    public void onPlayerClick(PlayerInteractEntityEvent event) {
    	
        Player player = event.getPlayer();
        Entity entity = event.getRightClicked();
        
		if (entity instanceof Player) {
			
			Cooldown cooldown = new Cooldown(spigot, player.getName());
	 		if (cooldown.hasCooldown("playerckick")) {
	 			return;
			}
	 		cooldown.setCooldown("playerckick", 1);
	 		
			if (player.getInventory().getItemInMainHand().getAmount() == 0) {
				player.chat("/bread "+entity.getName());
			}
			
			
		}
  
	}
	
	
}
