package listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import com.BrainSpigot;
import com.SystemMessage;


public class SpigotListeners implements Listener {

	private BrainSpigot spigot;
	
	public SpigotListeners(BrainSpigot plugin) {
		this.spigot = plugin;
	}
	
    @EventHandler
    public void onPlayerJoin(PlayerLoginEvent event) {

    	if (spigot.locales.size() == 0) {
    		Bukkit.getScheduler ().runTaskLater (spigot, () -> new SystemMessage(spigot).newMessage("locale", new String[] {"get"}, event.getPlayer()), 20);
    	}

    }

	
}
