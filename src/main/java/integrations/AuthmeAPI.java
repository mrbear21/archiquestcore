package integrations;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.BrainSpigot;

import fr.xephi.authme.api.v3.AuthMeApi;
import fr.xephi.authme.events.LoginEvent;
import fr.xephi.authme.events.LogoutEvent;
import objects.BreadMaker;

public class AuthmeAPI implements Listener {
	
	private BrainSpigot spigot;
	private AuthMeApi authMeApi;
	
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
		
		bread.setData("lastLogin", String.valueOf(System.currentTimeMillis())).save();
		bread.setData("loggedin", "true");

	}
	
	@EventHandler
	public void onLogout(LogoutEvent event) {
		BreadMaker bread = spigot.getBread(event.getPlayer().getName());
		bread.kick(ChatColor.translateAlternateColorCodes('&', "&e&lBye bye! \n &fHave a good day!"));
		bread.clearData();
	}

    public void initializeAuthMeHook() {
        authMeApi = AuthMeApi.getInstance();
    }

    public void removeAuthMeHook() {
        authMeApi = null;
    }

    public boolean isHookActive() {
        return authMeApi != null;
    }

    public boolean isNameRegistered(String name) {
        return authMeApi != null && authMeApi.isRegistered(name);
    }
	
}
