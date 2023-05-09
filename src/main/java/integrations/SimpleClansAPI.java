package integrations;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import brain.BrainSpigot;
import events.LanguageChangedEvent;

public class SimpleClansAPI implements Listener {

	  private BrainSpigot spigot;
	  
	  public SimpleClansAPI(BrainSpigot spigot) {
		  this.spigot = spigot;
	  }

	  public void register() {
		spigot.getServer().getPluginManager().registerEvents(this, spigot);
	  }
	   
	@EventHandler
	public void LanguageChangedEvent(LanguageChangedEvent event) {

	//	spigot.getSCPlugin();
		
	}
	
}
