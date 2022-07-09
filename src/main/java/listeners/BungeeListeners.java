package listeners;

import com.BrainBungee;

import modules.Locales;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.TabCompleteEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class BungeeListeners implements Listener {

	private BrainBungee plugin;
	
	public BungeeListeners(BrainBungee plugin) {
		this.plugin = plugin;
	}
	
	/*
    @EventHandler
    public void onPostLogin(PlayerDataLoadedEvent event) {
    	if (event.getData("discord") != null) {
    		String userId = event.getData("discord");
    		User user = new Discord(plugin).getJda().retrieveUserById(userId).complete();

			EmbedBuilder builder = new EmbedBuilder();
			builder.setTitle("У ваш аккаунт "+event.getData("username")+" було виконано вхід!", null);
			builder.setDescription("Підтвердіть авторизацію відповідною реакцією.");
			builder.addField(":timer:", new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z")
					.format(new Date(System.currentTimeMillis())), true);
			builder.addField(":computer:", plugin.getProxy().getPlayer(event.getPlayer()).getSocketAddress().toString(), true);
			builder.setColor(Color.decode("#2E9AFE"));
			user.openPrivateChannel().complete().sendMessageEmbeds(builder.build()).complete().addReaction(Emoji.fromUnicode("🛡️")).queue();
			String message = user.openPrivateChannel().complete().getLatestMessageId();
			user.openPrivateChannel().complete().retrieveMessageById(message).complete().addReaction(Emoji.fromUnicode("⛔")).queue();
    	}
    }
	*/
    @EventHandler
    public void onPostLogin(PostLoginEvent event) {
    	
    	plugin.getBread(event.getPlayer().getName()).loadData();

    }

    @EventHandler
    public void onTab(TabCompleteEvent e){
        if (e.getCursor().startsWith("/language")) {
        	for (String l : new Locales(plugin).languages) {
        		e.getSuggestions().add(l);
        	}
        }
    }
    
}
