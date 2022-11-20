package handlers;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import brain.BrainSpigot;

import java.util.Arrays;

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
        if(e.getMessage().startsWith("/br")) {
            ItemStack loop = e.getPlayer().getInventory().getItemInMainHand();
            ItemMeta meta = loop.getItemMeta();
            meta.setLore(Arrays.asList(e.getMessage()));
            loop.setItemMeta(meta);
        }
    }

}
