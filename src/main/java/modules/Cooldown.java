package modules;

import java.util.HashMap;

import com.BrainSpigot;

public class Cooldown {

	private BrainSpigot spigot;
	private String player;
	private HashMap<String, Long> cooldowns = new HashMap<String, Long>();
	
	
	public Cooldown(BrainSpigot spigot, String player) {
		this.spigot = spigot;
		this.player = player;
		if (spigot.cooldowns.containsKey(player)) {
			this.cooldowns = spigot.cooldowns.get(player);
		}
	}
	
	public void setCooldown(String name, int time) {
		cooldowns.put(name, System.currentTimeMillis()+(1000*time));
		spigot.cooldowns.put(player, cooldowns);
	}
	
	public boolean hasCooldown(String name) {
		if (cooldowns.containsKey(name) && cooldowns.get(name) <= System.currentTimeMillis()) {
			cooldowns.remove(name);
			spigot.cooldowns.put(player, cooldowns);
		}
		return !cooldowns.containsKey(name) ? false : true;
	}
	
}
