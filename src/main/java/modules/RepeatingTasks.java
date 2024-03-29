package modules;

import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;

import brain.BrainBungee;
import brain.BrainSpigot;
import brain.Utils;
import objects.BreadMaker;

public class RepeatingTasks {
	
	private BrainSpigot spigot;
	private BrainBungee bungee;
	private String servertype;
	
	public RepeatingTasks(BrainSpigot spigot) {
		this.spigot = spigot;
		servertype = "client";
	}
	
	public RepeatingTasks(BrainBungee bungee) {
		this.bungee = bungee;
		servertype = "proxy";
	}
	
	public int automesage_id = 0;
	
	 public void stop() {
    	if (servertype.equals("proxy")) {
    		if (bungee.repeatingtask != null)
    		bungee.repeatingtask.cancel();
    		bungee.log("repeating task successfully stopped!");
    	}

    	if (servertype.equals("client")) {
    		if (spigot.runnableTasks.containsKey("repeatingtask"))
    		Bukkit.getServer().getScheduler().cancelTask(spigot.runnableTasks.get("repeatingtask"));
    		spigot.log("repeating task successfully stopped!");
    	}
	 }
	
    public RepeatingTasks start() {
    	
    	if (servertype.equals("proxy")) {
    		bungee.repeatingtask = bungee.getProxy().getScheduler().schedule(bungee, new Runnable() {
	            @Override
	            public void run() {  	
	            	if (bungee.getConfig().getBoolean("mysql.use")) {
		            	try {
							new Mysql(bungee).getConnection().prepareStatement("/* ping */ SELECT 1").executeQuery();
						} catch (SQLException e) {
							e.printStackTrace();
						}
	            	}
	            }
	        }, 1, 60, TimeUnit.MINUTES);
    		bungee.log("repeating task successfully started!");
    	}
    	
    	if (servertype.equals("client")) {
    		spigot.runnableTasks.put("repeatingtask", Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(spigot, new Runnable() {
	    		public void run() {

	    			Bukkit.getOnlinePlayers().stream().forEach(player -> {
	    				
	    				final BreadMaker bread = spigot.getBread(player.getName());

	    				if (bread.getData("yaw").isNotNull() && bread.getData("yaw").getAsString().equals(new Utils().locToString(player.getLocation())) && bread.getData("autoafk").getAsBoolean()) {
	    					bread.setData("afk", "auto");
	    				}
	    				
	    				bread.setData("yaw", new Utils().locToString(player.getLocation()));
	    				
	    				bread.updateDisplayName();
	    				

	    				if (!bread.getLocales().getLocalesMap().containsKey("archiquest.automessage_"+automesage_id)) {
	    					automesage_id = 0;
	    				}
	    				
	    				final String text = bread.getLocales().getLocalesMap().get("archiquest.automessage_"+automesage_id);
	    				bread.sendBossbar(new Localizations(spigot).translateString(text, bread.getLanguage()), 30);
    				
	    			});
	    			
	    			automesage_id++;
	    			
			    }
			}, 0, 20*60)); // 60 sec
    		
    		spigot.runnableTasks.put("money-for-play", Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(spigot, new Runnable() {
	    		public void run() {

	    			Bukkit.getOnlinePlayers().stream().forEach(player -> {
	    				final BreadMaker bread = spigot.getBread(player.getName());
	
	    				if (!bread.getData("yaw").isNotNull() && !bread.getData("yaw").getAsString().equals(new Utils().locToString(player.getLocation()))) {
		    				Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "eco give "+player.getName()+" 5");
		    				player.sendMessage("archiquest.money-for-play");
	    				}
	    				
	    			});
	    			
			    }
			}, 0, 20*60*10)); // 10 min
    		
    		spigot.log("repeating task successfully started!");
	    }
    	
    	return this;
    }
}
