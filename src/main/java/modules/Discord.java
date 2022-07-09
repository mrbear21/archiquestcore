package modules;

import java.awt.Color;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;

import javax.security.auth.login.LoginException;

import com.BrainBungee;
import com.SystemMessage;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.Permission;
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
		        Commands.slash("погроза", "Кинути користувачу погрозу в приватні повідомлення.")
		            .addOptions(new OptionData(OptionType.USER, "user", "жертва")
		                .setRequired(true))
		            .setGuildOnly(true)
		            .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.BAN_MEMBERS))
		);
	    
	    
	    commands.addCommands(
		        Commands.slash("post", "Створити новий пост.")
		        	.addOptions(new OptionData(OptionType.STRING, "title", "заголовок").setRequired(true))
		            .addOptions(new OptionData(OptionType.STRING, "text", "текст").setRequired(true))
	        		.addOptions(new OptionData(OptionType.STRING, "author", "автор"))
		            .addOptions(new OptionData(OptionType.CHANNEL, "channel", "канал"))
		            .addOptions(new OptionData(OptionType.STRING, "image", "url на картинку"))
		            .addOptions(new OptionData(OptionType.STRING, "color", "#колір"))
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
			погроза(user);
			break;
	    case "say":
	    	event.reply(event.getOption("content").getAsString()).queue();
	        break;
	    case "disable":
	    	bungee.botActivation = false;
	    	event.reply("Бота вимкнено").queue();
	        break;
	    case "enable":
	    	bungee.botActivation = true;
	    	event.reply("Бота увімкнено").queue();
	        break;
	    case "prune":
	        prune(event);
	        break;
	    case "post":
	        post(event);
	        break;
	    default:
	        event.reply("I can't handle that command right now :(").setEphemeral(true).queue();
	    }
	}

	private void post(SlashCommandInteractionEvent event) {
		
		String title = event.getOption("title").getAsString();
		String text = event.getOption("text").getAsString();
		String author = event.getOption("author") != null ? event.getOption("author").getAsString() : null;
		TextChannel channel = event.getOption("channel") != null ? event.getOption("channel").getAsTextChannel() : event.getTextChannel();
		String image = event.getOption("image") != null ? event.getOption("image").getAsString() : null;
		String color = event.getOption("color") != null ? event.getOption("color").getAsString() : "#a29bfe";
		
		EmbedBuilder builder = new EmbedBuilder();
			builder.setAuthor(author, null, "https://minotar.net/helm/"+author);
			builder.setTitle(title);
			builder.setDescription(text);
			builder.setColor(Color.decode(color));
			builder.setImage(image);
		channel.sendMessageEmbeds(builder.build()).queue();
		
		
		if (channel.getId().equals("993474060740743189")) {
			Mysql mysql = new Mysql(bungee);
			try {
				PreparedStatement statement = mysql.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS `"+bungee.database+"`.`news` ( `id` INT NOT NULL AUTO_INCREMENT , `date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP , `author` VARCHAR(100) NOT NULL , `title` VARCHAR(256) NOT NULL , `text` TEXT NOT NULL , `image` VARCHAR(256) NULL , `language` VARCHAR(10) NULL , `likes` INT NOT NULL DEFAULT '0' , PRIMARY KEY (`id`)) ENGINE = InnoDB;");
				statement.executeUpdate();
				
				image = image != null ? "'"+image+"'" : "NULL";
				statement = mysql.getConnection().prepareStatement("INSERT INTO `"+bungee.database+"`.`news` (`author`, `title`, `text`, `image`) VALUES ('"+author+"', '"+title+"', '"+text+"', "+image+")");
				statement.executeUpdate();
				
			} catch (SQLException e) {
				event.reply("Виникла помилка при публікуванні!").queue();
				e.printStackTrace();
				return;
			}
			
		}
    	event.reply("Пост опубліковано!").queue();
	}


	public void погроза(User user) {
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
	    event.reply("Це видалить " + amount + " повідомлень.\nВи впевнені?")
	        .addActionRow(
	            Button.secondary(userId + ":delete", "Ні!"),
	            Button.danger(userId + ":prune:" + amount, "Так!"))
	        .queue();
	}
		
	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		Message message = event.getMessage();

		if (!event.getAuthor().isBot()) {
			
			if (event.getChannel().getId().equals("993474180538433676")) {
				new SystemMessage(bungee).newMessage("chat", new String[] {"discord", event.getMember().getUser().getAsTag(), message.getContentDisplay()});
			}
			if (event.getChannel().getId().equals("993476444883796019")) {
				new SystemMessage(bungee).newMessage("chat", new String[] {"discord_admin", event.getMember().getUser().getAsTag(), message.getContentDisplay()});
			}
			
			return;
		}
		
	}


	public JDA getJda() {
		return bungee.botActivation == false ? null : bungee.jda;
		
	}
	
}