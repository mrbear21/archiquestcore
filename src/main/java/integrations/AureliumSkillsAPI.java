package integrations;

import java.util.Locale;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.BrainSpigot;
import com.archyx.aureliumskills.api.AureliumAPI;

import events.LanguageChangedEvent;
public class AureliumSkillsAPI implements Listener {
	
	private BrainSpigot spigot;
	
	public AureliumSkillsAPI(BrainSpigot spigot) {
		this.spigot = spigot;
		
	}
	
    @EventHandler
    public void onLanguageChange(LanguageChangedEvent event) {
    	setLocale(event.getPlayer(), event.getLanguage());
    }

    
	public void initialize() {
		if (spigot.getServer().getPluginManager().isPluginEnabled("AureliumSkills")) {
	    	Bukkit.getPluginManager().registerEvents(new AureliumSkillsAPI(spigot), spigot);
	    	spigot.log("AureliumSkills initialized!");
		}
	}

    
	public void setLocale(String player, String lang) {

		Player p = Bukkit.getPlayer(player);
		
		if (p != null) {
			
			Locale locale = new Locale(lang.toLowerCase(Locale.ENGLISH));

			AureliumAPI.getPlugin().getPlayerManager().getPlayerData(p).setLocale(locale);
			
			
		}
	}

	
}
