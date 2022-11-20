package integrations;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import brain.BrainSpigot;
import objects.MenuBuilder;

public class PlotSquaredAPI implements CommandExecutor {

	private BrainSpigot spigot;
	
	public PlotSquaredAPI(BrainSpigot spigot) {
		this.spigot = spigot;
	}
	
	int i;
	
	public void register() {
		if (spigot.getServer().getPluginManager().isPluginEnabled("PlotSquared")) {
			/*
			 * треба назбирати 15 європейських грошей на придбання PlotSquared v6
			 * 
			spigot.getCommand("home").setExecutor(this);
			*/
		}
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		Player player = (Player) sender;
		
		MenuBuilder menu = new MenuBuilder(spigot, player, "PLOTS");
		
		i = 0;
/*
		PlotSquared.get().getPlots("plotworld", player.getName()).stream().forEach(plot -> {
			menu.setOption(!plot.getAlias().isEmpty() ? plot.getAlias() : "#"+i, i, "plot v "+plot.getId().toString(), Material.GRASS_BLOCK,
					new String[] {
						ChatColor.WHITE+plot.getId().toString(), ChatColor.WHITE+plot.getMembers().stream().map(Object::toString).collect(Collectors.joining(","))
					});
			i++;
		});
		*/
		menu.build();
		
		return true;
	}
	
	
	
}
