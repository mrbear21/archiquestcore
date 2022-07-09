package com;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class Utils {

	public String locToString(Location loc) {
		return loc.getWorld().getName() +","+ loc.getX()+","+ loc.getY()+","+ loc.getZ()+","+ loc.getYaw()+","+ loc.getPitch();
		
	}
	public Location stringToLoc(String loc) {
		return stringToLoc(loc.split(","));
	}
	
	public Location stringToLoc(String[] loc) {
		double spawnX = Double.valueOf(loc[1]);
		double spawnY = Double.valueOf(loc[2]);
		double spawnZ = Double.valueOf(loc[3]);
		float spawnYaw = Float.valueOf(loc[4]);
		float spawnPitch = Float.valueOf(loc[5]);
		return new Location(Bukkit.getServer().getWorld(loc[0]), spawnX, spawnY, spawnZ, spawnYaw, spawnPitch);
	}
	
	
	public String longToTime(Long time) {
		Long seconds = time / 1000;
		int day = (int) TimeUnit.SECONDS.toDays(seconds);
		long hours = TimeUnit.SECONDS.toHours(seconds) - (day * 24);
		long minute = TimeUnit.SECONDS.toMinutes(seconds) - (TimeUnit.SECONDS.toHours(seconds) * 60);
		return day + " d " + hours + " h " + minute + " m " + new SimpleDateFormat("ss").format(new Date(time)) + " s";
	}


}
