package listeners;

import java.io.File;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import com.BrainSpigot;


public class PlayerWarpsSignHandler implements Listener {
	
	BrainSpigot plugin;
	
	public PlayerWarpsSignHandler(BrainSpigot plugin) {
		this.plugin = plugin;
	}

	public void register() {
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void onSignClicked(PlayerInteractEvent e) {
		if(e.getClickedBlock()!= null) {
			if(e.getClickedBlock().getState() instanceof Sign) {
				Sign s = (Sign) e.getClickedBlock().getState();
				if(s.getLine(0).equals("[PW]")) {
					String loop = s.getLine(1);
					String name = s.getLine(2);
					File file = new File(plugin.getDataFolder()+"/warps.yml");
					FileConfiguration pwarps = YamlConfiguration.loadConfiguration(file);
					List<String> list = pwarps.getStringList(loop);
					for(String point : list) {
						if(point.substring(0, point.indexOf("%%")).equals(name)) {
							String[] pD = point.split("%%");
							Location l = new Location(e.getPlayer().getLocation().getWorld(), Double.valueOf(pD[1]),Double.valueOf(pD[2]),Double.valueOf(pD[3]),
									Float.valueOf(pD[4]),Float.valueOf(pD[5]));
							e.getPlayer().teleport(l);
							e.getPlayer().sendMessage(ChatColor.AQUA+"Woohoo!");
							return;
						}
					}
				}
			}
		}
	}
	
}
