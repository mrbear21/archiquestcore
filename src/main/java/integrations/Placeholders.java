package integrations;

import java.util.HashMap;

import org.bukkit.entity.Player;

import brain.BrainSpigot;
import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import modules.Localizations;
import net.md_5.bungee.api.ChatColor;
import objects.BreadMaker;

public class Placeholders extends PlaceholderExpansion {

	private BrainSpigot spigot;

	public Placeholders(BrainSpigot spigot) {
		this.spigot = spigot;
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
		
		if (p != null) {
			BreadMaker bread = spigot.getBread(p.getName());
			if (bread.getOption(identifier) != -1) {
				return bread.getData(identifier).getAsString();
			}
		}
		
		HashMap<String, String> locales = p != null ? spigot.getBread(p.getName()).getLocales().getLocalesMap(spigot.getBread(p.getName()).getLanguage()) : new Localizations(spigot).getLocalesMap("en");
		if (locales != null && locales.containsKey(identifier)) {
			return locales.get(identifier);
		}
		return identifier;
	}

}
