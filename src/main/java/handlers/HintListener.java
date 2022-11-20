package handlers;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import brain.BrainSpigot;

import java.util.Arrays;
import java.util.List;

public class HintListener implements Listener {

    BrainSpigot spigot;

    public HintListener(BrainSpigot spigot){
        this.spigot = spigot;
    }
    public void register() {
        Bukkit.getPluginManager().registerEvents(this, spigot);
    }
    @EventHandler
    public void onCommandSend(PlayerCommandPreprocessEvent e) {
        if(e.getMessage().startsWith("/br") || e.getMessage().startsWith("/mask")) {
            ItemStack loop = e.getPlayer().getInventory().getItemInMainHand();
            ItemMeta meta = loop.getItemMeta();
            List<String> lore = meta.getLore();
            if(e.getMessage().startsWith("/br"))
                lore.set(0, e.getMessage());
            else
                lore.set(1, e.getMessage());
            meta.setLore(lore);
            loop.setItemMeta(meta);
        }
    }

}
