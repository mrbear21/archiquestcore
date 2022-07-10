package commands;

import com.BrainSpigot;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class TeleportCommands implements CommandExecutor {
	
    HashMap<Player, Player> q = new HashMap<Player, Player>();
	private BrainSpigot spigot;

    public TeleportCommands(BrainSpigot spigot) {
    	this.spigot = spigot;
    }

	public void register() {
		spigot.getCommand("tp").setExecutor(this);
	}
	
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof ConsoleCommandSender)){
            Player p = Bukkit.getPlayer(sender.getName());
            if(label.equals("tp")){
                if(args.length > 0){
                    if(p.hasPermission("archiquestcore.tp")){
                        if(Bukkit.getOnlinePlayers().contains(Bukkit.getPlayer(args[0]))){
                            p.teleport(Bukkit.getPlayer(args[0]));
                            p.sendMessage("Ð¡Ð¾Ð¾Ð±Ñ‰ÐµÐ½Ð¸Ðµ Ñ‡Ñ‚Ð¾ Ð´ÐµÐ»Ð¾ Ñ�Ð´ÐµÐ»Ð°Ð½Ð¾");
                            return true;
                        } else {
                            p.sendMessage("Ð¡Ð¾Ð¾Ð±Ñ‰ÐµÐ½Ð¸Ðµ Ñ‡Ñ‚Ð¾ Ñ†ÐµÐ»ÑŒ Ð¾Ñ„Ð»Ð°Ð¹Ð½");
                            return true;
                        }
                    } else {
                        if(Bukkit.getOnlinePlayers().contains(Bukkit.getPlayer(args[0]))){
                            Player target = Bukkit.getPlayer(args[0]);
                            if(q.containsKey(p)){
                                if(q.get(p).equals(target)){
                                    p.sendMessage("Ð’Ñ‹ ÑƒÐ¶Ðµ Ð¾Ñ‚Ð¿Ñ€Ð°Ð²Ð¸Ð»Ð¸Ð· Ð°Ð¿Ñ€Ð¾Ñ� Ð¸Ð³Ñ€Ð¾ÐºÑƒ Ð¿Ð¸ÑˆÐ¸Ñ‚Ðµ /tpcancel Ð°ÑƒÐµ");
                                    return true;
                                } else {
                                    q.remove(p);
                                }
                            }
                            q.put(p,Bukkit.getPlayer(args[0]));
                            if(q.containsKey(target) && q.get(target).equals(p)) {
                                p.teleport(target);
                                q.remove(p);
                                q.remove(target);
                                p.sendMessage("Ð¡Ð¾Ð¾Ð±Ñ‰ÐµÐ½Ð¸Ðµ Ñ‡Ñ‚Ð¾ Ð´ÐµÐ»Ð¾ Ñ�Ð´ÐµÐ»Ð°Ð½Ð¾");
                                return true;
                            } else {
                                p.sendMessage("Ð—Ð°Ð¿Ñ€Ð¾Ñ� Ð¾Ñ‚ Ð¿Ñ€Ð°Ð²Ð» ÐµÐ½ Ð°ÑƒÐµ");
                                target.sendMessage("Ð’Ñ‹ Ð¿Ð¾Ð»ÑƒÑ‡Ð¸Ð»Ð¸ Ð·Ð°Ð¿Ñ€Ð¾Ñ� Ð½Ð° Ñ‚Ð¿ÑˆÐºÑƒ Ð¾Ñ‚ Ð¸Ð³Ñ€Ð¾ÐºÐ° "+p.getDisplayName()+" Ð°ÑƒÐµ");
                                return true;
                            }
                        } else {
                            p.sendMessage("Ð¡Ð¾Ð¾Ð±Ñ‰ÐµÐ½Ð¸Ðµ Ñ‡Ñ‚Ð¾ Ñ†ÐµÐ»ÑŒ Ð¾Ñ„Ð»Ð°Ð¹Ð½");
                            return true;
                        }
                    }
                } else {
                    p.sendMessage("Ð¡Ð¾Ð±Ñ‰ÐµÐ½Ð¸Ðµ Ñ‡Ñ‚Ð¾ Ð½Ð°Ð´Ð¾ /"+command+" [Ð½Ð¸Ðº] Ð¸Ð»Ð¸ Ñ‚Ð¸Ð¿Ð° Ñ‚Ð°Ð²Ð¾");
                    return true;
                }
            } else if(label.equals("tpdeny") || label.equals("tpno")){
                if(q.containsValue(p)){
                    for(Map.Entry<Player, Player> entry : q.entrySet()){
                        Player key = entry.getKey();
                        Player value = entry.getValue();
                        if(value.equals(p)){
                            q.remove(key);
                        }
                    }
                    p.sendMessage("Ð¡Ð¾Ð¾Ð±Ñ‰ÐµÐ½Ð¸Ðµ Ñ‡Ñ‚Ð¾ ÑƒÑ�Ð¿ÐµÑˆÐ½Ð¾ Ð²Ñ�Ðµ Ð¾Ñ‚Ð¼ÐµÐ½ÐµÐ½Ð¾ Ð°ÑƒÐµ");
                    return true;
                } else {
                    p.sendMessage("Ð¡Ð¾Ð¾Ð±Ñ‰ÐµÐ½Ð¸Ðµ Ñ‡Ñ‚Ð¾ Ð·Ð°Ð¿Ñ€Ð¾Ñ�Ð¾Ð² Ð½ÐµÑ‚");
                    return true;
                }
            } else if(label.equals("tpallow") || label.equals("tpaccept") || label.equals("tpyes")){
                if(q.containsValue(p)){
                    for(Map.Entry<Player, Player> entry : q.entrySet()){
                        Player key = entry.getKey();
                        Player value = entry.getValue();
                        if(value.equals(p)){
                            if(Bukkit.getOnlinePlayers().contains(key)){
                                p.teleport(key);
                            }
                            q.remove(key);
                        }
                    }
                    p.sendMessage("Ð¡Ð¾Ð¾Ð±Ñ‰ÐµÐ½Ð¸Ðµ Ñ‡Ñ‚Ð¾ ÑƒÑ�Ð¿ÐµÑˆÐ½Ð¾ Ñ‚ÐµÐ»ÐµÐ¿Ð¾Ñ€Ñ‚ Ð°ÑƒÐµ");
                    return true;
                } else {
                    p.sendMessage("Ð¡Ð¾Ð¾Ð±Ñ‰ÐµÐ½Ð¸Ðµ Ñ‡Ñ‚Ð¾ Ð·Ð°Ð¿Ñ€Ð¾Ñ�Ð¾Ð² Ð½ÐµÑ‚");
                    return true;
                }
            } else if(label.equals("tpcancel")){
                if(q.containsKey(p)){
                    q.remove(p);
                    p.sendMessage("Ð¡Ð¾Ð¾Ð±Ñ‰ÐµÐ½Ð¸Ðµ Ñ‡Ñ‚Ð¾ ÑƒÑ�Ð¿ÐµÑˆÐ½Ð¾ Ð¾Ñ‚Ð¼ÐµÐ½Ð°");
                    return true;
                } else {
                    p.sendMessage("Ð¡Ð¾Ð¾Ð±Ñ‰ÐµÐ½Ð¸Ðµ Ñ‡Ñ‚Ð¾ Ð¸Ñ�Ñ…Ð¾Ð´Ñ�Ñ‰Ð¸Ñ… Ð·Ð°Ð¿Ñ€Ð¾Ñ�Ð¾Ð² Ð½ÐµÑ‚");
                    return true;
                }
            }
            return true;
        } else {
            return true;
        }
    }

}
