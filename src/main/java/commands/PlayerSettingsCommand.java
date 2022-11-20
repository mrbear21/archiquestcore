package commands;

import java.util.stream.Collectors;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import brain.BrainSpigot;
import objects.BreadMaker;
import objects.MenuBuilder;

public class PlayerSettingsCommand implements CommandExecutor{

	private BrainSpigot spigot;
	
	public PlayerSettingsCommand(BrainSpigot spigot) {
		this.spigot = spigot;
	}
	
	public void register() {
		spigot.getCommand("settings").setExecutor(this);
	}

	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if (sender.hasPermission("archiquest."+command.getName())) {
			
			Player player = (Player) sender;
			BreadMaker bread = spigot.getBread(player.getName());
			
	 		MenuBuilder menu = new MenuBuilder(spigot, player, "SETTINGS"); int l = 10;
	 		
			menu.setOption("archiquest.fly", l++, new String[] {"fly", "settings"}, Material.FEATHER, new String [] {
					!player.hasPermission("archiquest.fly") ? "archiquest.donate-feature" : player.getAllowFlight() ? "archiquest.enabled" : "archiquest.disabled" });
			menu.setOption("archiquest.tptoggle", l++, new String[] {"tptoggle", "settings"}, Material.ENDER_PEARL, new String [] {
					!player.hasPermission("archiquest.tptoggle") ? "archiquest.donate-feature" : bread.getData("tptoggle").isNotNull() == null ? "archiquest.disabled" : bread.getData("tptoggle").getAsBoolean() ? "archiquest.enabled" : "archiquest.disabled" });
			menu.setOption("archiquest.doublejump", l++, new String[] {"doublejump", "settings"}, Material.ELYTRA, new String [] { !player.getAllowFlight() ? "archiquest.disabled" :
					!player.hasPermission("archiquest.doublejump") ? "archiquest.donate-feature" : bread.getData("doublejump").isNotNull() == null ? "archiquest.disabled" : bread.getData("doublejump").getAsBoolean() ? "archiquest.enabled" : "archiquest.disabled" });
			menu.setOption("menu.joinmessage", l++, "joinmessage", Material.OAK_SIGN, new String [] {
					!player.hasPermission("archiquest.joinmessage") ? "archiquest.donate-feature" : bread.getData("joinmessage").isNotNull() ? bread.getData("joinmessage").getAsString().equals("false") ? "archiquest.disabled" : "archiquest.enabled" : "archiquest.disabled"});
			menu.setOption("archiquest.player-autoafk", l++, new String[] {"afk auto", "settings"}, Material.RED_BED, new String [] {!player.hasPermission("archiquest.afk.auto") ? "archiquest.donate-feature" : (bread.getData("autoafk").getAsBoolean() ? "archiquest.enabled" : "archiquest.disabled")});
			if (spigot.getServer().getPluginManager().isPluginEnabled("Builders-Utilities")) {
				menu.setOption("archiquest.noclip", l++, new String[] {"noclip", "settings"}, Material.PHANTOM_MEMBRANE, new String [] {"archiquest.click-to-select"});
				menu.setOption("archiquest.nightvision", l++, new String[] {"nv", "settings"}, Material.ENDER_EYE, new String [] {player.getActivePotionEffects().stream().map(PotionEffect::getType).collect(Collectors.toList()).contains(PotionEffectType.NIGHT_VISION) ? "archiquest.enabled" : "archiquest.disabled"});
			}
			//	menu.setOption("archiquest.2fa", 15, "2fa", Material.SHIELD, new String [] {""+(bread.getData("2Fa").isNotNull() ? "archiquest.enabled" : "archiquest.disabled")});
			menu.build();
		}
		return true;
	}
	
}
