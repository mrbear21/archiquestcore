package listeners;

import java.io.IOException;

import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.BrainSpigot;
import com.SystemMessage;

import net.md_5.bungee.event.EventHandler;
import objects.BreadMaker;

public class SpigotListeners implements Listener {

	private BrainSpigot plugin;
	
	public SpigotListeners(BrainSpigot plugin) {
		this.plugin = plugin;
	}
	
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) throws IOException {
    	
    	if (new BreadMaker(plugin).getBread(event.getPlayer().getName()).getLocales() == null) {
    		new SystemMessage(plugin).getLocales();
    	}

    }

	
}
