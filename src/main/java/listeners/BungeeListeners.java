package listeners;

import com.BrainBungee;

import modules.Locales;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.ServerKickEvent;
import net.md_5.bungee.api.event.TabCompleteEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class BungeeListeners implements Listener {

	private BrainBungee bungee;
	
	public BungeeListeners(BrainBungee bungee) {
		this.bungee = bungee;
	}
	
	@EventHandler(priority = 64)
	public void onServerKickEvent(ServerKickEvent e) {
		
		ServerInfo fallback = bungee.getProxy().getServerInfo("hub");
		
		if (fallback == null) {
			bungee.getLogger().severe("Unable to find the specified fallback server!!");
		} else {
			e.setCancelServer(fallback);
			e.setCancelled(true);
			  
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append(ChatColor.GREEN);
			stringBuilder.append("Ваше соединиение с ");
			stringBuilder.append(e.getKickedFrom().getName());
			stringBuilder.append(" было прервано. Вы были перемещены в Хаб. Причина: ");
			stringBuilder.append(e.getKickReasonComponent());
			e.getPlayer().sendMessage((new ComponentBuilder(stringBuilder.toString())).color(ChatColor.GREEN).create());
		} 
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
    	
    	bungee.getBread(event.getPlayer().getName()).loadData();

    }

    @EventHandler
    public void onTab(TabCompleteEvent e){
        if (e.getCursor().startsWith("/language")) {
        	for (String l : new Locales(bungee).languages) {
        		e.getSuggestions().add(l);
        	}
        }
    }
    
}
