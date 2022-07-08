package modules;

import java.util.Arrays;

import javax.security.auth.login.LoginException;

import com.BrainBungee;
import com.SystemMessage;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

public class Discord extends ListenerAdapter {

	private BrainBungee plugin;
	
	public Discord(BrainBungee plugin) {
		this.plugin = plugin;
	}
	
	private final static GatewayIntent[] INTENTS = {GatewayIntent.DIRECT_MESSAGES, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_MESSAGE_REACTIONS, GatewayIntent.GUILD_VOICE_STATES};
    
	public void login() throws LoginException {
		String token = plugin.getConfig().getString("discord.token");
		if (token == null || token.equals("token")) {
			return;
		}
        try {
        	plugin.jda = JDABuilder.create(token, Arrays.asList(INTENTS))
                    .enableCache(CacheFlag.MEMBER_OVERRIDES, CacheFlag.VOICE_STATE)
                    .disableCache(CacheFlag.ACTIVITY, CacheFlag.CLIENT_STATUS, CacheFlag.EMOJI, CacheFlag.ONLINE_STATUS, CacheFlag.STICKER)
                    .setStatus(OnlineStatus.ONLINE)
                    .addEventListeners(new Discord(plugin), new Discord(plugin))
                    .setBulkDeleteSplittingEnabled(true)
                    .build();
        } catch (LoginException e) {
            plugin.getLogger().severe(e.getMessage());
        } 
	
	    CommandListUpdateAction commands = plugin.jda.updateCommands();

	    commands.addCommands(
		        Commands.slash("погроза", "Кинути користувачу погрозу в приватні повідомлення.")
		            .addOptions(new OptionData(OptionType.USER, "user", "жертва")
		                .setRequired(true))
		            .setGuildOnly(true)
		            .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.BAN_MEMBERS))
		);
	    
	    commands.addCommands(
		        Commands.slash("disable", "Вимкнути бота")
		            .setGuildOnly(true)
		            .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR))
		);
	    
	    commands.addCommands(
		        Commands.slash("enable", "Увімкнути бота")
		            .setGuildOnly(true)
		            .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR))
		);
	    
	    commands.addCommands(
	        Commands.slash("say", "Бот напише те, що ви йому скажете").addOption(OptionType.STRING, "content", "Що бот повинен сказати", true)
	    );

	    commands.addCommands(
	        Commands.slash("prune", "Очистити повідомлення у цьому каналі")
	            .addOption(OptionType.INTEGER, "amount", "Скільки повідомлень очистити (за замовчуванням 100)")
	            .setGuildOnly(true)
	            .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.MESSAGE_MANAGE))
	    );
	
	    commands.queue();
	}
	
	
	@Override
	public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
	    if (event.getGuild() == null)
	        return;
	    switch (event.getName())
	    {
	    case "погроза":
	        User user = event.getOption("user").getAsUser();
			event.reply("погрозу відіслано!");
			user.openPrivateChannel().complete().sendMessage("https://cdn.discordapp.com/attachments/994920082927013989/994920564953198655/download.jpg").queue();
	        break;
	    case "say":
	    	event.reply(event.getOption("content").getAsString()).queue();
	        break;
	    case "disable":
	    	plugin.botActivation = false;
	    	event.reply("Бота вимкнено").queue();
	        break;
	    case "enable":
	    	plugin.botActivation = true;
	    	event.reply("Бота увімкнено").queue();
	        break;
	    case "prune":
	        prune(event);
	        break;
	    default:
	        event.reply("I can't handle that command right now :(").setEphemeral(true).queue();
	    }
	}

	@Override
	public void onButtonInteraction(ButtonInteractionEvent event) {
	    String[] id = event.getComponentId().split(":");
	    String authorId = id[0];
	    String type = id[1];
	    if (!authorId.equals(event.getUser().getId()))
	        return;
	    event.deferEdit().queue();
	
	    MessageChannel channel = event.getChannel();
	    switch (type)
	    {
	        case "prune":
	            int amount = Integer.parseInt(id[2]);
	            event.getChannel().getIterableHistory()
	                .skipTo(event.getMessageIdLong())
	                .takeAsync(amount)
	                .thenAccept(channel::purgeMessages);
	        case "delete":
	            event.getHook().deleteOriginal().queue();
	    }
	}
	
	public void prune(SlashCommandInteractionEvent event) {
	    OptionMapping amountOption = event.getOption("amount");
	    int amount = amountOption == null ? 100 : (int) Math.min(200, Math.max(2, amountOption.getAsLong()));
	    String userId = event.getUser().getId();
	    event.reply("Це видалить " + amount + " повідомлень.\nВи впевнені?")
	        .addActionRow(
	            Button.secondary(userId + ":delete", "Ні!"),
	            Button.danger(userId + ":prune:" + amount, "Так!"))
	        .queue();
	}
		
	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		Message message = event.getMessage();
		
		plugin.log(message.getContentDisplay());
		
		if (!event.getAuthor().isBot()) {
			
			if (event.getChannel().getId().equals("993476444883796019")) {
				
				new SystemMessage(plugin).newMessage("chat", new String[] {"admin", event.getMember().getUser().getAsTag(), message.getContentDisplay()});
				
			}
			
			return;
		}
	}


	public JDA getJda() {
		return plugin.botActivation == false ? null : plugin.jda;
		
	}
	
}