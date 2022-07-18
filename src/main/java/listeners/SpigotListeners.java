package listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.BrainSpigot;
import com.SystemMessage;

import objects.BreadMaker;


public class SpigotListeners implements Listener {

	private BrainSpigot spigot;
	
	public SpigotListeners(BrainSpigot plugin) {
		this.spigot = plugin;
	}
	
	
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
    	
    	Player player = event.getPlayer();
    	player.setPlayerListName(spigot.getBread(player.getName()).getPrefix() + player.getName());
    	
    }
    
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
    	BreadMaker bread = spigot.getBread(event.getPlayer().getName());
		bread.setData("loggedin", "true");
    }
	
    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) {

    	if (spigot.locales.size() == 0) {
    		Bukkit.getScheduler ().runTaskLater (spigot, () -> new SystemMessage(spigot).newMessage("locale", new String[] {"get"}, event.getPlayer()), 20);
    	}
    	
    }

    @EventHandler(priority= EventPriority.HIGH, ignoreCancelled=true)
    public void onSignChange(SignChangeEvent e) {
        Player player = e.getPlayer();
        if (player.hasPermission("archiquest.chat.color")) {
	        String[] lineArray = e.getLines();
	        int lineArrayLength = lineArray.length;
	        for(int i = 0; i < lineArrayLength; i++) {
	            String oldLine = lineArray[i];
	            String newLine = ChatColor.translateAlternateColorCodes('&', oldLine);
	            e.setLine(i, newLine);
	        }
        }
    }
    
    
}
