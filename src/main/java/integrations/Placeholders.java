package integrations;

import java.util.HashMap;

import org.bukkit.entity.Player;

import com.BrainSpigot;

import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.md_5.bungee.api.ChatColor;
import objects.BreadMaker;

public class Placeholders extends PlaceholderExpansion {

	private BrainSpigot plugin;

	public Placeholders(BrainSpigot plugin) {
		this.plugin = plugin;
	}


	@Override
	public boolean canRegister() {

		return true;
	}

	@Override
	public String getAuthor() {

		return "mrbear22";
	}


	@Override
	public String getIdentifier() {

		return "archiquest";
	}

	@Override
	public String getPlugin() {

		return null;
	}

	/**
	 * This is the version of this expansion
	 */
	@Override
	public String getVersion() {

		return "1.0.0";
	}

	public String setPlaceholders(Player player, String placeholder) {
		return ChatColor.translateAlternateColorCodes('&', PlaceholderAPI.setPlaceholders(player, placeholder));
	}
	
	@Override
	public String onPlaceholderRequest(Player p, String identifier) {
		switch (identifier) {
			case "skills": 
				return PlaceholderAPI.setPlaceholders(p, 
						"§4 ➽%aureliumskills_strength% §c❤%aureliumskills_health% §6❥%aureliumskills_regeneration% §2☘%aureliumskills_luck% §9✿%aureliumskills_wisdom% §5✦%aureliumskills_wisdom%").replace(".0", "");
		}
		HashMap<String, String> locales = new BreadMaker(plugin).getBread(p.getName()).getLocales();
		if (locales != null && locales.containsKey(identifier)) {
			return locales.get(identifier);
		}
		return identifier;
	}

}
