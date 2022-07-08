package events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class LanguageChangedEvent extends Event {
	
    private static final HandlerList handlers = new HandlerList();
    private String language;
    private String player;

    public LanguageChangedEvent(String player, String language) {
        this.language = language;
        this.player = player;
    }

    public String getLanguage() {
        return language;
    }
    
    public String getPlayer() {
        return player;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
	
	
}
