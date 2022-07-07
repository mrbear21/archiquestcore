package com;

import java.util.Arrays;

public class Utils {

	public String[] options = {

			 "username", "level", "experience", "hashtag", "guild", "isAfk", "likes", "dislikes", "2faAuth", "language", "rainbowprefix", "inVanish", "plotchat", "marry", "isLogged",
			 "playtime", "settings", "guildtag", "guildinvite", "currentPlot", "lastPM", "joinmessage", "rpname", "voiceChat", "job", "keys",  "rpprefix", "firstPlay",  "lastIP",
			 "donatereward", "holidayBonus", "2Fa", "nickname", "lastLogin", "invitedBy", "isMuted", "isBanned", "discord", "prefix", "ignore", "votes", "achievements", "group", "youtube",
			 "email", "prefixColor", "kills", "deaths", "god", "back", "tempPrefix", "yaw", "isJailed", "oldIP", "qualityfactor",  "rank", "lastEmptyPlot", "emptyPlotsTravelled", "chestUse",
			 "lastChestUse", "squad", "slave", "squadJoinTime", "ramson", "joinTime", "faction", "tabprefix", "shadowMute", "inMinigame"
			
			};
			
	public int getOption(String option) {
		
		return Arrays.asList(options).indexOf(option);
		
	}
}
