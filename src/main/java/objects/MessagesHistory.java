package objects;

import java.util.ArrayList;
import java.util.List;

import com.BrainSpigot;

public class MessagesHistory {

	private BrainSpigot spigot;
	
	public MessagesHistory(BrainSpigot spigot) {
		this.spigot = spigot;
	}
	
	public void add(String player, ChatMessage message) {
		if (message.getMessage().equals("{\"text\":\"\"}")) { return; }
		List<String[]> history = spigot.chathistory.containsKey(player) ? spigot.chathistory.get(player) : new ArrayList<String[]>();
		history.add(new String[] {message.getChat(), message.getPlayer(), message.getMessage(), message.getStatus(), message.getId()});
		spigot.chathistory.put(player, history);
	}
	
	public void clear(String player) {
		spigot.chathistory.remove(player);
	}
	
}
