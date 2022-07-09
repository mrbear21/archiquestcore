package listeners;

import java.io.IOException;

import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.BrainSpigot;
import com.SystemMessage;

import modules.Locales;
import net.md_5.bungee.event.EventHandler;

public class SpigotListeners implements Listener {

	private BrainSpigot spigot;
	
	public SpigotListeners(BrainSpigot plugin) {
		this.spigot = plugin;
	}
	
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) throws IOException {
    	
    	if (new Locales(spigot).getLocales("en").size() == 0) {
    		new SystemMessage(spigot).newMessage("locale", new String[] {"get"});
    	}

    }

	
}
