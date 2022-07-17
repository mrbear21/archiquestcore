package commands;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

import com.BrainSpigot;
import com.Utils;

import objects.BreadMaker;

public class BetterTeleportCommands implements CommandExecutor, Listener {

	private BrainSpigot spigot;


	public BetterTeleportCommands(BrainSpigot spigot) {
		this.spigot = spigot;
	}
	
	public void register() {
		spigot.getServer().getPluginManager().registerEvents(this, spigot);
		spigot.getCommand("tp").setExecutor(this);
		spigot.getCommand("back").setExecutor(this);
	}

	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		Player player = (Player) sender;
		BreadMaker bread = spigot.getBread(sender.getName());
		
		if (Arrays.asList("teleport", "tpa", "tp", "call").contains(label.toLowerCase())) {
			if (args.length == 0) { return false; }
			if (player.hasPermission("archiquest.tp")) {
				if (Bukkit.getPlayer(args[0]) != null) {
					player.teleport(Bukkit.getPlayer(args[0]));
					return true;
				}
				player.sendMessage("archiquest.player.is.offline");
				return true;
			}
			player.sendMessage(sendTpRequest(player, Bukkit.getPlayer(args[0])));
			return true;
		}
		
		if (Arrays.asList("tpahere", "s").contains(label.toLowerCase())) {
			if (args.length == 0) { return false; }
			if (player.hasPermission("archiquest.tphere")) {
				if (Bukkit.getPlayer(args[0]) != null) {
					Bukkit.getPlayer(args[0]).teleport(player);
					return true;
				}
				player.sendMessage("archiquest.player.is.offline");
				return true;
			}
			player.sendMessage(sendTphereRequest(player, Bukkit.getPlayer(args[0])));
			return true;
		}
		
		if (Arrays.asList("tpyes", "tpaccept", "tpallow").contains(label.toLowerCase())) {
			
			Player otherPlayer = args.length > 0 ? Bukkit.getPlayer(args[0]) : bread.getData("lasttprequest").getAsPlayer();
			if (otherPlayer == null) { player.sendMessage("archiquest.player.is.offline archiquest.notprequest"); return true; }
			
			String[] request = spigot.getBread(otherPlayer.getName()).getData("tprequest").getAsString().split(":");
			
			if (request[1].equals(player.getName())) {
				if (request[0].equals("tp")) {
					otherPlayer.teleport(player);
					spigot.getBread(otherPlayer.getName()).setData("tprequest", null);
					spigot.getBread(player.getName()).setData("lasttprequest", null);
				}
				if (request[0].equals("tphere")) {
					player.teleport(otherPlayer);
					spigot.getBread(player.getName()).setData("tprequest", null);
					spigot.getBread(otherPlayer.getName()).setData("lasttprequest", null);
				}
				otherPlayer.sendMessage(player.getDisplayName() + " archiquest.acceptedtprequest");
				return true;
			}
			return true;
		}
		
		if (Arrays.asList("back").contains(label.toLowerCase())) {
			
			if (!player.hasPermission("archiquest.back")) { player.sendMessage("archiquest.no_permission"); return true; }
			
			if (bread.getData("back").getAsLocation() != null) {
				player.teleport(bread.getData("back").getAsLocation());
				player.sendMessage("archiquest.tptoprevposition");
			} else {
				player.sendMessage("archiquest.noprevposition");
			}
			
			return true;
		}
		
		return false;
	}

	
	private String sendTphereRequest(Player player, Player otherPlayer) {
		if (otherPlayer != null) {
			if (spigot.getBread(otherPlayer.getName()).getData("tptoggle").getAsBoolean()) {
				return "archiquest.playertptoggle";
			}
			spigot.getBread(otherPlayer.getName()).setData("lasttprequest", player.getName());
			spigot.getBread(player.getName()).setData("tprequest", "tphere:"+otherPlayer.getName());
			otherPlayer.sendMessage(player.getName()+" archiquest.tphererequest");
			return "archiquest.tprequest-sent";
		}
		return "archiquest.player.is.offline";
	}
	
	private String sendTpRequest(Player player, Player otherPlayer) {
		if (otherPlayer != null) {
			if (spigot.getBread(otherPlayer.getName()).getData("tptoggle").getAsBoolean()) {
				return "archiquest.playertptoggle";
			}
			spigot.getBread(otherPlayer.getName()).setData("lasttprequest", player.getName());
			spigot.getBread(player.getName()).setData("tprequest", "tp:"+otherPlayer.getName());
			otherPlayer.sendMessage(player.getName()+" archiquest.tprequest");
			return "archiquest.tprequest-sent";
		}
		return "archiquest.player.is.offline";
	}
	
	@EventHandler
	public void onTp(PlayerTeleportEvent event) {
		if (event.getPlayer().hasPermission("architectcore.doublejump") && Boolean.valueOf(spigot.getBread(event.getPlayer().getName()).getData("doublejump").getAsBoolean())) {
			spigot.getServer().getScheduler().scheduleSyncDelayedTask(spigot, new Runnable() { public void run() {	
				event.getPlayer().setAllowFlight(true);
			} }, 20);
		}
		if (event.getTo().getWorld() != event.getFrom().getWorld() || event.getTo().distance(event.getFrom()) > 10) {
			spigot.getBread(event.getPlayer().getName()).setData("back", new Utils().locToString(event.getPlayer().getLocation()));
		}
	}
	
}
