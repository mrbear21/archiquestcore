package modules;

import static net.dv8tion.jda.api.interactions.commands.OptionType.INTEGER;
import static net.dv8tion.jda.api.interactions.commands.OptionType.STRING;
import static net.dv8tion.jda.api.interactions.commands.OptionType.USER;

import java.util.Arrays;
import java.util.EnumSet;

import javax.security.auth.login.LoginException;

import com.BrainBungee;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import objects.SlashCommands;

public class Discord extends ListenerAdapter {

	public static JDA jda;
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
            jda = JDABuilder.create(token, Arrays.asList(INTENTS))
                    .enableCache(CacheFlag.MEMBER_OVERRIDES, CacheFlag.VOICE_STATE)
                    .disableCache(CacheFlag.ACTIVITY, CacheFlag.CLIENT_STATUS, CacheFlag.EMOTE, CacheFlag.ONLINE_STATUS)
                    .setStatus(OnlineStatus.OFFLINE)
                    .addEventListeners(new Discord(plugin), new SlashCommands())
                    .setBulkDeleteSplittingEnabled(true)
                    .build();
        } catch (LoginException e) {
            plugin.getLogger().severe(e.getMessage());
        }
        
	    JDA jda = JDABuilder.createLight(token, EnumSet.noneOf(GatewayIntent.class))
	                .addEventListeners(new SlashCommands()).build();
        CommandListUpdateAction commands = jda.updateCommands();

        commands.addCommands(
            new CommandData("ban", "Ban a user from this server. Requires permission to ban users.")
                .addOptions(new OptionData(USER, "user", "The user to ban")
                    .setRequired(true))
                .addOptions(new OptionData(INTEGER, "del_days", "Delete messages from the past days."))
        );

        commands.addCommands(
            new CommandData("say", "Makes the bot say what you tell it to")
                .addOptions(new OptionData(STRING, "content", "What the bot should say")
                    .setRequired(true))
        );

        commands.addCommands(
            new CommandData("prune", "Prune messages from this channel")
                .addOptions(new OptionData(INTEGER, "amount", "How many messages to prune (Default 100)"))
        );

        commands.queue();

	}

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		Message message = event.getMessage();
		if (event.getAuthor().isBot()) {
			if (!message.getEmbeds().isEmpty()) {
				@SuppressWarnings("unused")
				String content = message.getEmbeds().get(0).getDescription()+message.getEmbeds().get(0).getTitle()+message.getEmbeds().get(0).getFields();
			}
			return;
		}
		if (Arrays.asList("893952100285968396", "600051084408520750", "781880474875985960").contains(event.getChannel().getId())) {
			//threads as comments
		}
	}
	
}
