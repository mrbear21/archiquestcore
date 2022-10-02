package listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import com.BrainSpigot;

import modules.Cooldown;

public class NPCCommands implements CommandExecutor, Listener {

	private BrainSpigot spigot;
	
	public NPCCommands(BrainSpigot plugin) {
		this.spigot = plugin;
	}
	
	public void register() {
		spigot.getCommand("npccmd").setExecutor(this);
		Bukkit.getPluginManager().registerEvents(this, spigot);
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEntityEvent event) {
		Player player = event.getPlayer();
		Entity entity = event.getRightClicked();
		String command = ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', entity.getName()));
		if (spigot.npccommands.get(command) != null) {
			Cooldown cooldown = new Cooldown(spigot, player.getName());
			if (Bukkit.getPlayer(event.getRightClicked().getName()) == null) {

				if (!cooldown.hasCooldown(command)) {
					player.performCommand(spigot.npccommands.get(command));
					cooldown.setCooldown(command, 1);
				}
	
			}
		}
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender.hasPermission("archiquest.npc")) {
			if (args.length == 0) {
				return false;
			}
			spigot.getConfig().set("npc-commands."+args[0], String.join(" ", args).substring(args[0].length()+1, String.join(" ", args).length()));
			spigot.npccommands.put(args[0], String.join(" ", args).substring(args[0].length()+1, String.join(" ", args).length()));
			sender.sendMessage("Added command "+args[0]+": "+String.join(" ", args).substring(args[0].length()+1, String.join(" ", args).length()));
		}
		return true;
	}
	
	
	
	
}
