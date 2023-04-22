package modules;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import brain.BrainSpigot;
import objects.BreadMaker;

public class MarrigeManager implements Listener, CommandExecutor {
	
	private BrainSpigot spigot;
	
	public MarrigeManager(BrainSpigot plugin) {
		this.spigot = plugin;
	}
	
	
	public void register() {
		spigot.getCommand("marry").setExecutor(this);
		spigot.getCommand("marriage").setExecutor(this);
		spigot.getCommand("priestpoint").setExecutor(this);
		spigot.getCommand("guestpoint").setExecutor(this);
		spigot.getCommand("bridepoint").setExecutor(this);
		spigot.getCommand("fiancepoint").setExecutor(this);
		spigot.getCommand("divorce").setExecutor(this);
		spigot.getCommand("pressf").setExecutor(this);
		Bukkit.getPluginManager().registerEvents(this, spigot);
        for (String s : spigot.getConfig().getConfigurationSection("marriage").getKeys(false)) {
        	points().put(s, spigot.getConfig().getLocation("marriage."+s));
        }
	}
	
	private HashMap<String, Location> points() {
		return spigot.marrigePoints;
	}
	
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
	//	BreadMaker fiance = spigot.getBread(event.getPlayer().getName());
	//	BreadMaker bride = spigot.getBread(fiance.getData("marriagePropossal").getAsString());

    }
	
	
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
	//	BreadMaker fiance = spigot.getBread(event.getEntity().getName());
	//	BreadMaker bride = spigot.getBread(fiance.getData("marriagePropossal").getAsString());
    	
    }


	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		BreadMaker fiance = spigot.getBread(event.getPlayer().getName());
		BreadMaker bride = spigot.getBread(fiance.getData("marriagePropossal").getAsString());
		bride.getLocation().distance(fiance.getLocation());
	}
	
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onChat(AsyncPlayerChatEvent event) {
		
		BreadMaker fiance = spigot.getBread(event.getPlayer().getName());
		BreadMaker bride = spigot.getBread(fiance.getData("marriagePropossal").getAsString());
		
		//обидва сказали да
		fiance.setData("marry", bride.getName());
		bride.setData("marry", fiance.getName());
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		BreadMaker bread = spigot.getBread(sender.getName());
		
		switch(label.toLowerCase()) {
		
			case "marry":
				
				if (args.length == 0) {
					return false;
				}
				
				BreadMaker fiance = spigot.getBread(sender.getName());
				
				if (fiance.getData("marriagePropossal").isNotNull() && fiance.getData("marriagePropossal").getAsString().equalsIgnoreCase(args[0])) {
					fiance.sendMessage("ви вже зробили пропозицію цьому гравцеві");
					return false;
				}
				
				BreadMaker bride = spigot.getBread(args[0]);
	
				if (!bride.isOnline()) {
					fiance.sendMessage("гравець офлайн");
					return false;
				}
				
				if (bride.getData("marriagePropossal").isNotNull() && bride.getData("marriagePropossal").getAsString().equalsIgnoreCase(sender.getName())) {
					
					fiance.teleport(points().get("fiancepoint"));
					bride.teleport(points().get("bridepoint"));
					bride.setData("marriagePropossal", fiance.getName());
					
				} else {
					fiance.setData("marriagePropossal", bride.getName());
					fiance.sendMessage("ви зробили пропозицію");
					bride.sendMessage("вам зробили пропозицію");
				}
				


				
			
			case "pressf":
				//привітати в чат
				//та телепортувати гостя на весілля
				
			case "divorce":
				
				fiance = spigot.getBread(sender.getName());
				bride = spigot.getBread(fiance.getData("marry").getAsString());
				
				fiance.sendMessage("ви розвелись");
				bride.sendMessage("в загсі на вікнах одні розводи");
				

				fiance.setData("marry", null);
				bride.setData("marry", null);
				
			case "marriage":
		
				if (args.length == 0) {
					return false;
				}

				switch (args[0].toLowerCase()) {
				
					case "priestpoint":
					case "guesttpoint":
					case "bridepoint":
					case "fiancepoint":
						points().put(args[0].toLowerCase(), bread.getLocation());
						spigot.getConfig().set("marrige."+args[0].toLowerCase(), bread.getLocation());
						return true;
					case "reset":
						points().clear();
						spigot.getConfig().set("marrige", null);
						return true;
				
				
				}
		
		}
		
		
		
		

		
		
		
		
		return true;
		
	}

	
}
