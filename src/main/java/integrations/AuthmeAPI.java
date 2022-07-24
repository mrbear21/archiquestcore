package integrations;

import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.BrainSpigot;

import fr.xephi.authme.api.v3.AuthMeApi;
import fr.xephi.authme.events.LoginEvent;
import fr.xephi.authme.events.LogoutEvent;
import fr.xephi.authme.events.RegisterEvent;
import objects.BreadMaker;

public class AuthmeAPI implements Listener {
	
	private BrainSpigot spigot;
	
	public AuthmeAPI(BrainSpigot spigot) {
		this.spigot = spigot;
		
	}
	
	public void initialize() {
		if (spigot.getServer().getPluginManager().isPluginEnabled("AuthMe")) {
			spigot.getServer().getPluginManager().registerEvents(this, spigot);
			initializeAuthMeHook();
			spigot.log("Authme initialized!");
		}
	}
	
	@EventHandler
	public void onLogin(LoginEvent event) {
		BreadMaker bread = spigot.getBread(event.getPlayer().getName());
		Player player = event.getPlayer();
		bread.setData("lastLogin", String.valueOf(System.currentTimeMillis())).save();
		bread.setData("loggedin", "true");	
		
		
		String[] emoji = { "(´• ω •`)ﾉ", "╰(❤ω❤)╯", "⊂(￣▽￣)⊃" };

		String em = emoji[new Random().nextInt(emoji.length)];
			player.sendTitle("§l" + em, "§e§l " + bread.getLocales().translateString("archiquest.hello")+" " + player.getDisplayName(), 20, 50, 20);
		spigot.getServer().getScheduler().scheduleSyncDelayedTask(spigot, new Runnable() { public void run() {
			player.sendTitle("§l" + em, "§e§l " + bread.getLocales().translateString("archiquest.welcome"), 20, 50, 20);
		} }, 120);
		

	}
	
	@EventHandler
	public void onRegister(RegisterEvent event) {
		
		BreadMaker bread = spigot.getBread(event.getPlayer().getName());

		bread.setData("loggedin", "true");
		
		String locale = event.getPlayer().getLocale().split("_")[0];
		bread.setData("language", locale.equals("uk") ? "ua" : locale).save();
		bread.setData("lastLogin", String.valueOf(System.currentTimeMillis())).save();
		bread.setData("nickname", event.getPlayer().getName()).save();
		bread.setData("firstPlay", String.valueOf(event.getPlayer().getFirstPlayed())).save();
		bread.setData("level", "1").save();
		bread.setData("experience", "0").save();
		bread.setData("lastIp", String.valueOf(event.getPlayer().getAddress()).substring(1).split(":")[0]).save();

	}
	
	
	@EventHandler
	public void onLogout(LogoutEvent event) {
		BreadMaker bread = spigot.getBread(event.getPlayer().getName());
		bread.kick(ChatColor.translateAlternateColorCodes('&', "&e&lBye bye! \n &fHave a good day!"));
		bread.clearData();
	}

    public AuthMeApi getInstance() {
        return spigot.authMeApi;
    }
	
    public void initializeAuthMeHook() {
        spigot.authMeApi = AuthMeApi.getInstance();
    }

    public void removeAuthMeHook() {
    	spigot.authMeApi = null;
    }

    public boolean isHookActive() {
        return spigot.authMeApi != null;
    }

    public boolean isNameRegistered(String name) {
        return spigot.authMeApi != null && spigot.authMeApi.isRegistered(name);
    }
	
    public boolean isLoggedIn(Player player) {
        return spigot.authMeApi != null && spigot.authMeApi.isAuthenticated(player);
    }
}
