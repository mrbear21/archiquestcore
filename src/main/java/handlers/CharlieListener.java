package handlers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import brain.BrainBungee;
import modules.Mysql;

public class CharlieListener {

	private BrainBungee bungee;
	
	public CharlieListener(BrainBungee bungee) {
		this.bungee = bungee;
	}
	
	
	public void register() {
		
		if (bungee.getConfig().getBoolean("mysql.use")) {
			
			Mysql mysql = new Mysql(bungee);
			try {
				PreparedStatement statement = mysql.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS `"+bungee.database+"`.`charliebot` ( `id` INT NOT NULL AUTO_INCREMENT , `pattern` VARCHAR(250) CHARACTER SET utf8 COLLATE utf8_general_ci NULL , `answer` VARCHAR(250) CHARACTER SET utf8 COLLATE utf8_general_ci NULL , `language` VARCHAR(250) CHARACTER SET utf8 COLLATE utf8_general_ci NULL , PRIMARY KEY (`id`)) ENGINE = InnoDB;");
				statement.executeUpdate();
				statement = mysql.getConnection().prepareStatement("SELECT * FROM `"+bungee.database+"`.`charliebot`");
				ResultSet results = statement.executeQuery();
				ResultSetMetaData md = results.getMetaData();
				int columns = md.getColumnCount();
				while (results.next()) {
					for (int i = 2; i <= columns; ++i) {
						if (results.getObject(i) != null) {
							
				            String pattern = results.getObject(2).toString();
				            String answer = results.getObject(3).toString();
				            String language = results.getObject(4).toString();
				            
				            if (bungee.charliepatterns.containsKey(pattern)) {
				            	List<String> answers = new ArrayList<String>();
				            	answers.add(language);
				            	answers.addAll(bungee.charliepatterns.get(pattern));
				            	answers.add(answer);
				            	bungee.charliepatterns.put(pattern, answers);
				            } else {
				            	bungee.charliepatterns.put(pattern, Arrays.asList(answer));
				            }

						}
					}
				}
				results.close();
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	public void checkPhrase(String phrase) {
		
		String[] p = phrase.split(" ");
		
		if (p.length >= 2 && p.length <= 10) {
			
			for (String w : p) {
				
				if (Arrays.asList("�?к", "що", "шо", "коли", "де").contains(w)) {
					bungee.possiblePattern = new String[2];
					bungee.possiblePattern[0] = phrase;
					bungee.possiblePattern[1] = "ua";
					bungee.log("Можливе запитанн�?: "+phrase);
					return;
				}
				
				if (Arrays.asList("как", "что", "што", "когда", "где").contains(w)) {
					bungee.possiblePattern = new String[2];
					bungee.possiblePattern[0] = phrase;
					bungee.possiblePattern[1] = "ru";
					bungee.log("Можливе запитанн�?: "+phrase);
					return;
				}	
				
			}
			
			addAnswer(phrase);
			
		}
		
	}
	
	
	
	public void addAnswer(String phrase) {
		
		if (bungee.possiblePattern != null) {
			
			bungee.possibleAnswers.add(phrase);
			
			bungee.log("Можлива відповідь: "+phrase);
			
			if (bungee.getConfig().getBoolean("mysql.use")) {
				Mysql mysql = new Mysql(bungee);
				try {
					PreparedStatement statement = mysql.getConnection().prepareStatement("INSERT INTO `"+bungee.database+"`.`charliebot` (`id`, `pattern`, `answer`, `language`) VALUES (NULL, '"+bungee.possiblePattern[0]+"', '"+phrase+"', '"+bungee.possiblePattern[1]+"')");
					statement.executeUpdate();
				} catch (SQLException e) { e.printStackTrace(); }
			}
			
			if (bungee.possibleAnswers.size() > 2) {
				
				bungee.possiblePattern = null;
				
			}
			
		}
		
	}
	
	
	
}
