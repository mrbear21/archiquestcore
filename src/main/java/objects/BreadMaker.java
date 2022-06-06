package objects;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import com.BrainBungee;
import com.Mysql;
import com.Utils;

public class BreadMaker {

	private Utils utils;
	private String name;
	private BrainBungee plugin;
	
	public BreadMaker(BrainBungee plugin, String name) {
		this.plugin = plugin;
		this.utils = new Utils();
		this.name = name;
	}

	public void setData(String player, String option, String value) {

		String[] data = new String[utils.options.length];

		if (utils.playerdata.containsKey(player)) {
			data = utils.playerdata.get(player);
		}
		
		if (utils.getOption(option) != utils.getOption("null")) {
			data[utils.getOption(option)] = value;
			utils.playerdata.put(player, data);
		}
	}
	
	
	public String getData(String option) {
		if (!utils.playerdata.containsKey(name)) {
			loadPlayer();
		}
		if (utils.playerdata.containsKey(name)) {
			if (utils.playerdata.get(name)[utils.getOption(option)] != null && utils.playerdata.get(name)[utils.getOption(option)].length() > 0) {
				return utils.playerdata.get(name)[utils.getOption(option)];
			} 
		}
		return null;
	}
	
	public void loadPlayer() {
		try {
			Mysql mysql = new Mysql(plugin);
			PreparedStatement statement = mysql.getConnection().prepareStatement("SELECT * FROM " + mysql.getTable() + " WHERE username=?");
			statement.setString(1, name.toLowerCase());
			ResultSet results = statement.executeQuery();
			ResultSetMetaData md = results.getMetaData();
			int columns = md.getColumnCount();
			while (results.next()) {
				for (int i = 1; i <= columns; ++i) {
					if (results.getObject(i) != null) {
						setData(name, md.getColumnName(i), results.getObject(i).toString());
					}
				}
			}
			results.close();
		} catch (SQLException c) { c.printStackTrace(); }
	}
	
}
