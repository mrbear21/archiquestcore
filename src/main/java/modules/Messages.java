package modules;

import com.BrainBungee;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

public class Messages {
    private BrainBungee plugin;

    public Messages(BrainBungee plugin){
        this.plugin = plugin;
    }

    HashMap<String, String> messages = new HashMap<String, String>();

    File storage = new File(plugin.getDataFolder()+"/"+"messages.yml");
    FileConfiguration storage_configuration = YamlConfiguration.loadConfiguration(storage);

    public void Setup(){
        if(!messages.isEmpty()){
            messages.clear();
        }
        for(String l : storage_configuration.getConfigurationSection("").getKeys(false)){
            for(String m : storage_configuration.getConfigurationSection(l).getKeys(false)){
                messages.put(l+"."+m, storage_configuration.getString(l+"."+m));
            }
        }
    }
    public String getMessage(String language, String message){ return messages.get(language+"."+message); }

    public void setMessage(String language, String message, String value){
        if(message.contains(language+"."+message)){
            messages.remove(language+"."+message);
        }
        messages.put(language+"."+message, value);
    }

    public void Stop(){
        if(!messages.isEmpty()){
            for(Map.Entry<String,String> entry : messages.entrySet()){
                String key = entry.getKey();
                String value = entry.getValue();
                storage_configuration.set(key, value);
            }
            try {
                storage_configuration.save(storage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
