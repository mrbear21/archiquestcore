package commands;

import com.BrainSpigot;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class EnderchestCommand implements CommandExecutor {

    public EnderchestCommand(BrainSpigot brainSpigot) {
    }

    public void openEc(Player i, Player t){
        if(Bukkit.getOnlinePlayers().contains(i)){
            if(Bukkit.getOnlinePlayers().contains(t)){
                i.openInventory(t.getEnderChest());
            }
        }
    }

    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(!(sender instanceof ConsoleCommandSender)){
            Player p = Bukkit.getPlayer(sender.getName());
            if(args.length > 0){
                if(p.hasPermission("archiquestcore.enderchest.others")){
                    if(Bukkit.getOnlinePlayers().contains(Bukkit.getPlayer(args[0]))){
                        openEc(p,Bukkit.getPlayer(args[0]));
                    } else {
                        p.sendMessage("Сообщение что игрок офлайн");
                    }
                } else {
                    if(p.hasPermission("archiquestcode.enderchest")){
                        openEc(p,p);
                    }
                }
            } else {
                if(p.hasPermission("archiquestcode.enderchest")){
                    openEc(p,p);
                }
            }
        }
        return true;
    }
}
