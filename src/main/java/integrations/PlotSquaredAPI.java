package integrations;

import java.util.stream.Collectors;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.BrainSpigot;
import com.github.intellectualsites.plotsquared.plot.PlotSquared;

import modules.MenuBuilder;
import net.md_5.bungee.api.ChatColor;

public class PlotSquaredAPI implements CommandExecutor {

	private BrainSpigot spigot;
	
	public PlotSquaredAPI(BrainSpigot spigot) {
		this.spigot = spigot;
	}
	
	int i;
	
	public void register() {
		if (spigot.getServer().getPluginManager().isPluginEnabled("PlotSquared")) {
			spigot.getCommand("home").setExecutor(this);
		}
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		Player player = (Player) sender;
		
		MenuBuilder menu = new MenuBuilder(spigot, player, "PLOTS");
		
		i = 0;

		PlotSquared.get().getPlots("plotworld", player.getName()).stream().forEach(plot -> {
			menu.setOption(!plot.getAlias().isEmpty() ? plot.getAlias() : "#"+i, i, "plot v "+plot.getId().toString(), Material.GRASS_BLOCK,
					new String[] {
						ChatColor.WHITE+plot.getId().toString(), ChatColor.WHITE+plot.getMembers().stream().map(Object::toString).collect(Collectors.joining(","))
					});
			i++;
		});
		
		menu.build();
		
		return true;
	}
	
	
	
}
