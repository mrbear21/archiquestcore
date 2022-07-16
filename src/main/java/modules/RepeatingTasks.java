package modules;

import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;

import com.BrainBungee;
import com.BrainSpigot;

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
    		spigot.log("repeating task successfully stopped!");
    	}

    	if (servertype.equals("client")) {
    		if (spigot.repeatingtask != 0)
    		Bukkit.getServer().getScheduler().cancelTask(spigot.repeatingtask);
    		spigot.log("repeating task successfully stopped!");
    	}
	 }
	
    public RepeatingTasks start() {
    	
    	if (servertype.equals("proxy")) {
    		bungee.repeatingtask = bungee.getProxy().getScheduler().schedule(bungee, new Runnable() {
	            @Override
	            public void run() {  	
	            	try {
						new Mysql(bungee).getConnection().prepareStatement("/* ping */ SELECT 1").executeQuery();
					} catch (SQLException e) {
						e.printStackTrace();
					}        	
	            }
	        }, 1, 60, TimeUnit.MINUTES);
    		bungee.log("repeating task successfully started!");
    	}
    	
    	if (servertype.equals("client")) {
    		spigot.repeatingtask = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(spigot, new Runnable() {
	    		public void run() {

	    			Bukkit.getOnlinePlayers().stream().forEach(player -> {
	    				player.setPlayerListName(spigot.getBread(player.getName()).getPrefix() + player.getName());
	    				
	    				final BreadMaker bread = spigot.getBread(player.getName());
	    				if (bread.getLocales().getLocalesMap().containsKey("archiquest.automessage_"+automesage_id)) {
		    				final String text = bread.getLocales().getLocalesMap().get("archiquest.automessage_"+automesage_id);
		    				bread.sendBossbar(new Locales(spigot).translateString(text, bread.getLanguage()), 30);
	    				}
	    				
	    			});
	    			
	    			if (automesage_id++ > 3) { automesage_id = 0; }
	    			
			    }
			}, 0, 20*60); // 60 sec
    		spigot.log("repeating task successfully started!");
	    }
    	
    	return this;
    }
}
