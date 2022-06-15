package modules;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.BrainBungee;

public class TeleportManager {
	
	FileConfiguration backs;
	File file;
	
	public TeleportManager(BrainBungee plugin) {
		file = new File(plugin.getDataFolder()+"/backs.yml");
		backs = YamlConfiguration.loadConfiguration(file);
	}
	
	public Location getLastPoint(String player) {
		String[] pD = backs.getString(player).split("%%");
		return new Location(Bukkit.getWorld(pD[0]), Double.valueOf(pD[1]),Double.valueOf(pD[2]),Double.valueOf(pD[3]),
				Float.valueOf(pD[4]),Float.valueOf(pD[5]));
	}
	
	public void setLastPoint(String player, Location l) {
		backs.set(player, l.getWorld().getName()+"%%"+l.getX()+"%%"+l.getY()+"%%"+l.getZ()+"%%"+l.getYaw()+"%%"+l.getPitch());
		try {
			backs.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void teleport(String player, Location l) {
		setLastPoint(player, l);
		Bukkit.getPlayer(player).teleport(l);
	}
}
