package listeners;

import com.BrainBungee;

import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import objects.BreadMaker;

public class BungeeListeners implements Listener {

	private BrainBungee plugin;
	
	public BungeeListeners(BrainBungee plugin) {
		this.plugin = plugin;
	}
	
    @EventHandler
    public void onPostLogin(PostLoginEvent event) {
    	
    	new BreadMaker(plugin).getBread(event.getPlayer().getName()).loadData();

    }

	
}
