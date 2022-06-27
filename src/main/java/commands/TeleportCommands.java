package commands;

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
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(!(sender instanceof ConsoleCommandSender)){
            Player p = Bukkit.getPlayer(sender.getName());
            if(command.equals("tp")){
                if(args.length > 0){
                    if(p.hasPermission("archiquestcore.tp")){
                        if(Bukkit.getOnlinePlayers().contains(Bukkit.getPlayer(args[0]))){
                            p.teleport(Bukkit.getPlayer(args[0]));
                            p.sendMessage("Сообщение что дело сделано");
                            return true;
                        } else {
                            p.sendMessage("Сообщение что цель офлайн");
                            return true;
                        }
                    } else {
                        if(Bukkit.getOnlinePlayers().contains(Bukkit.getPlayer(args[0]))){
                            Player target = Bukkit.getPlayer(args[0]);
                            if(q.containsKey(p)){
                                if(q.get(p).equals(target)){
                                    p.sendMessage("Вы уже отправилиз апрос игроку пишите /tpcancel ауе");
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
                                p.sendMessage("Сообщение что дело сделано");
                                return true;
                            } else {
                                p.sendMessage("Запрос от правл ен ауе");
                                target.sendMessage("Вы получили запрос на тпшку от игрока "+p.getDisplayName()+" ауе");
                                return true;
                            }
                        } else {
                            p.sendMessage("Сообщение что цель офлайн");
                            return true;
                        }
                    }
                } else {
                    p.sendMessage("Собщение что надо /"+command+" [ник] или типа таво");
                    return true;
                }
            } else if(command.equals("tpdeny")){
                if(q.containsValue(p)){
                    for(Map.Entry<Player, Player> entry : q.entrySet()){
                        Player key = entry.getKey();
                        Player value = entry.getValue();
                        if(value.equals(p)){
                            q.remove(key);
                        }
                    }
                    p.sendMessage("Сообщение что успешно все отменено ауе");
                    return true;
                } else {
                    p.sendMessage("Сообщение что запросов нет");
                    return true;
                }
            } else if(command.equals("tpallow")){
                if(q.containsValue(p)){
                    for(Map.Entry<Player, Player> entry : q.entrySet()){
                        Player key = entry.getKey();
                        Player value = entry.getValue();
                        if(value.equals(p)){
                            p.teleport(key);
                            q.remove(key);
                        }
                    }
                    p.sendMessage("Сообщение что успешно телепорт ауе");
                    return true;
                } else {
                    p.sendMessage("Сообщение что запросов нет");
                    return true;
                }
            } else if(command.equals("tpcancel")){
                if(q.containsKey(p)){
                    q.remove(p);
                    p.sendMessage("Сообщение что успешно отмена");
                    return true;
                } else {
                    p.sendMessage("Сообщение что исходящих запросов нет");
                    return true;
                }
            }
            return true;
        } else {
            return true;
        }
    }
}
