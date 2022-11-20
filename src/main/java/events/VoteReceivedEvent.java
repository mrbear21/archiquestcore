package events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class VoteReceivedEvent extends Event {
	
    private static final HandlerList handlers = new HandlerList();
    private String site;
    private String player;

    public VoteReceivedEvent(String player, String site) {
        this.site = site;
        this.player = player;
    }

    public String getSite() {
        return site;
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
