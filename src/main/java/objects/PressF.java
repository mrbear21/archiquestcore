package objects;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import com.BrainSpigot;

public class PressF implements Listener {

	private BrainSpigot spigot;
	
	public PressF(BrainSpigot spigot) {
		this.spigot = spigot;
	}
	
	
	public void addAction(Player player, String type, String action) {
		List<String> actions = spigot.pressfactions.containsKey(player) ? spigot.pressfactions.get(player) : new ArrayList<String>();
		actions.add(action);
		spigot.pressfactions.put(player, actions);
	}
	
	
}
