package listeners;

import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

import com.BrainSpigot;
import com.google.common.collect.Iterables;

public class PressF implements Listener {

	private BrainSpigot spigot;
	
	public PressF(BrainSpigot spigot) {
		this.spigot = spigot;
	}
		
	public void register() {
		Bukkit.getPluginManager().registerEvents(this, spigot);
	}
	
	public void addAction(Player player, String type, String action) {
		HashMap<String, String> actions = spigot.pressfactions.containsKey(player) ? spigot.pressfactions.get(player) : new HashMap<String, String>();
		actions.put(type, action);
		spigot.pressfactions.put(player, actions);
	}
	

	@EventHandler
	public void onF(PlayerSwapHandItemsEvent event) {
		Player player = event.getPlayer();
		
		if (spigot.pressfactions.containsKey(player)) {
			
			Entry<String, String> f = Iterables.getFirst(spigot.pressfactions.get(player).entrySet(), null);
			
			switch(f.getKey()) {
				
				case "command":
					player.performCommand(f.getValue());
					break;

			}
			
			spigot.pressfactions.get(player).remove(f.getKey());
			
		}
		
	}
	
}
