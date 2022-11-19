package listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class HintListener implements Listener {

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
