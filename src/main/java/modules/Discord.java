package modules;

import java.awt.Color;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.security.auth.login.LoginException;

import com.BrainBungee;
import com.SystemMessage;

import fun.CharliesComeback;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.IMentionable;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
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

	private BrainBungee bungee;
	
	public Discord(BrainBungee bungee) {
		this.bungee = bungee;
	}
	
	private final static GatewayIntent[] INTENTS = {GatewayIntent.DIRECT_MESSAGES, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_MESSAGE_REACTIONS, GatewayIntent.GUILD_VOICE_STATES};
    
	public void login() throws LoginException {
		String token = bungee.getConfig().getString("discord.token");
		if (token == null || token.equals("token")) {
			return;
		}
        try {
        	bungee.jda = JDABuilder.create(token, Arrays.asList(INTENTS))
                    .enableCache(CacheFlag.MEMBER_OVERRIDES, CacheFlag.VOICE_STATE)
                    .disableCache(CacheFlag.ACTIVITY, CacheFlag.CLIENT_STATUS, CacheFlag.EMOJI, CacheFlag.ONLINE_STATUS, CacheFlag.STICKER)
                    .setStatus(OnlineStatus.ONLINE)
                    .addEventListeners(new Discord(bungee))
                    .setBulkDeleteSplittingEnabled(true)
                    .build();
        } catch (LoginException e) {
            bungee.getLogger().severe(e.getMessage());
        } 
	
	    CommandListUpdateAction commands = bungee.jda.updateCommands();

	    commands.addCommands(
		        Commands.slash("??????????????", "???????????? ?????????????????????? ?????????????? ?? ???????????????? ????????????????????????.")
		            .addOptions(new OptionData(OptionType.USER, "user", "????????????")
		                .setRequired(true))
		            .setGuildOnly(true)
		            .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.BAN_MEMBERS))
		);
	    
	    
	    commands.addCommands(
		        Commands.slash("post", "???????????????? ?????????? ????????.")
		        	.addOptions(new OptionData(OptionType.STRING, "title", "??????????????????").setRequired(true))
		            .addOptions(new OptionData(OptionType.STRING, "text", "??????????").setRequired(true))
	        		.addOptions(new OptionData(OptionType.STRING, "author", "??????????"))
		            .addOptions(new OptionData(OptionType.CHANNEL, "channel", "??????????"))
		            .addOptions(new OptionData(OptionType.STRING, "image", "url ???? ????????????????"))
		            .addOptions(new OptionData(OptionType.STRING, "color", "#??????????"))
		            .addOptions(new OptionData(OptionType.STRING, "thumbnail", "url ???? ????????????????"))
		            .addOptions(new OptionData(OptionType.MENTIONABLE, "mention", "????????????"))
		            .addOptions(new OptionData(OptionType.STRING, "edit", "???????? ???? ???????????????????????? (???????????????????? ?????? ?????????????????????? ????????????????????????)"))
		            .setGuildOnly(true)
		            .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.BAN_MEMBERS))
		);
	    
	    
	    commands.addCommands(
		        Commands.slash("disable", "???????????????? ????????")
		            .setGuildOnly(true)
		            .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR))
		);
	    
	    commands.addCommands(
		        Commands.slash("list", "???????????? ???????????????????? ????????????")
		            .setGuildOnly(true)
		);
	    
	    commands.addCommands(
		        Commands.slash("enable", "?????????????????? ????????")
		            .setGuildOnly(true)
		            .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR))
		);
	    
	    commands.addCommands(
	        Commands.slash("say", "?????? ???????????? ????, ???? ???? ???????? ??????????????").addOption(OptionType.STRING, "content", "???? ?????? ?????????????? ??????????????", true)
	    );

	    commands.addCommands(
	        Commands.slash("prune", "???????????????? ???????????????????????? ?? ?????????? ????????????")
	            .addOption(OptionType.INTEGER, "amount", "?????????????? ?????????????????????? ???????????????? (???? ?????????????????????????? 100)")
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
	    case "??????????????":
	        User user = event.getOption("user").getAsUser();
			event.reply("?????????????? ??????????????????!");
			??????????????(user);
			break;
	    case "say":
	    	event.reply(event.getOption("content").getAsString()).queue();
	        break;
	    case "disable":
	    	bungee.botActivation = false;
	    	event.reply("???????? ????????????????").queue();
	        break;
	    case "enable":
	    	bungee.botActivation = true;
	    	event.reply("???????? ??????????????????").queue();
	        break;
	    case "prune":
	        prune(event);
	        break;
	    case "post":
	        post(event);
	        break;
	    case "list":
	        list(event);
	        break;
	    default:
	        event.reply("I can't handle that command right now :(").setEphemeral(true).queue();
	    }
	}
	
	String list;
	
	private void list(SlashCommandInteractionEvent event) {
		
		EmbedBuilder builder = new EmbedBuilder();
		builder.setTitle("?????????????????? ????????????:");
		list = "";
		bungee.getProxy().getServers().entrySet().stream().forEach(s -> {
			list = list+"\n**"+s.getKey()+": **";
			if (s.getValue().getPlayers().size() > 0) {
				List<String> players = new ArrayList<String>();
				s.getValue().getPlayers().stream().forEach(p -> players.add(p.getName()));
				list = list + String.join(", ", players);
			} else { list = list + "???? ????????"; }
		} );
		builder.setDescription(list);
		builder.setColor(Color.decode("#a29bfe"));
		MessageBuilder message = new MessageBuilder();
		message.setEmbeds(builder.build());
		event.reply(message.build()).queue();
	}

	private void post(SlashCommandInteractionEvent event) {
		
		String title = event.getOption("title").getAsString();
		String text = event.getOption("text").getAsString().replace("\\n", System.lineSeparator());
		String author = event.getOption("author") != null ? event.getOption("author").getAsString() : null;
		TextChannel channel = event.getOption("channel") != null ? event.getOption("channel").getAsTextChannel() : event.getTextChannel();
		String image = event.getOption("image") != null ? event.getOption("image").getAsString() : null;
		String thumbnail = event.getOption("thumbnail") != null ? event.getOption("thumbnail").getAsString() : null;
		String color = event.getOption("color") != null ? event.getOption("color").getAsString() : "#a29bfe";
		String edit = event.getOption("edit") != null ? event.getOption("edit").getAsString() : null;
		IMentionable mention = event.getOption("mention") != null ? event.getOption("mention").getAsMentionable() : null;
		
		EmbedBuilder builder = new EmbedBuilder();
			builder.setAuthor(author, null, "https://minotar.net/helm/"+author);
			builder.setTitle(title);
			builder.setDescription(text);
			builder.setThumbnail(thumbnail);
			builder.setColor(Color.decode(color));
			builder.setImage(image);
		MessageBuilder message = new MessageBuilder();
		
		if (mention != null) {
			message.append(mention).setEmbeds(builder.build());
		} else {
			message.setEmbeds(builder.build());
		}
		
		if (edit == null) {
			channel.sendMessage(message.build()).queue();
		} else {
			channel.retrieveMessageById(edit).complete().editMessage(message.build()).queue();
		}
		
		if (channel.getId().equals("993474060740743189")) {
			Mysql mysql = new Mysql(bungee);
			try {
				PreparedStatement statement = mysql.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS `"+bungee.database+"`.`news` ( `id` INT NOT NULL AUTO_INCREMENT , `date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP , `author` VARCHAR(100) NOT NULL , `title` VARCHAR(256) NOT NULL , `text` TEXT NOT NULL , `image` VARCHAR(256) NULL , `language` VARCHAR(10) NULL , `likes` INT NOT NULL DEFAULT '0' , PRIMARY KEY (`id`)) ENGINE = InnoDB;");
				statement.executeUpdate();
				
				image = image != null ? "'"+image+"'" : "NULL";
				statement = mysql.getConnection().prepareStatement("INSERT INTO `"+bungee.database+"`.`news` (`author`, `title`, `text`, `image`) VALUES ('"+author+"', '"+title+"', '"+text+"', "+image+")");
				statement.executeUpdate();
				
			} catch (SQLException e) {
				event.reply("?????????????? ?????????????? ?????? ????????????????????????!").queue();
				e.printStackTrace();
				return;
			}
			
		}
    	event.reply("???????? ????????????????????????!").queue();
	}


	public void ??????????????(User user) {
		user.openPrivateChannel().complete().sendMessage("https://cdn.discordapp.com/attachments/994920082927013989/994920564953198655/download.jpg").queue();
        
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
	    event.reply("???? ???????????????? " + amount + " ??????????????????????.\n???? ?????????????????")
	        .addActionRow(
	            Button.secondary(userId + ":delete", "????!"),
	            Button.danger(userId + ":prune:" + amount, "??????!"))
	        .queue();
	}
		
	
	
	
	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		Message message = event.getMessage();

		if (!event.getAuthor().isBot()) {
			
			String chat = null;
			String channelId = event.getChannel().getId();
			String language = "";
			
			if (channelId.equals(bungee.getConfig().getString("discord.chats.server-chat"))) {
				chat = "discord";
			}
			
			if (channelId.equals(bungee.getConfig().getString("discord.chats.server-chat-ru"))) {
				chat = "discord"; language = "ru";
			}		
			
			if (channelId.equals(bungee.getConfig().getString("discord.chats.question-chat"))) {
				chat = "question";
			}
			
			if (channelId.equals(bungee.getConfig().getString("discord.chats.question-chat-ru"))) {
				chat = "question"; language = "ru";
			}	
			
			
			if (channelId.equals(bungee.getConfig().getString("discord.chats.discord_admin"))) {
				chat = "discord_admin";
			}
			
			
			if (chat != null) {
			
				new SystemMessage(bungee).newMessage("chat", new String[] {"new", chat, event.getMember().getUser().getAsTag().split("#")[0], message.getContentDisplay(), language});
			
			}
			
			new CharliesComeback(bungee).checkPhrase(message.getContentDisplay());
			
			return;
		}
		
	}


	public JDA getJda() {
		return bungee.botActivation == false ? null : bungee.jda;
		
	}
	
}