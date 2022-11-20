package brain;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

public class Utils {

	public String locToString(Location loc) {
		return loc.getWorld().getName() +","+ loc.getX()+","+ loc.getY()+","+ loc.getZ()+","+ loc.getYaw()+","+ loc.getPitch();
		
	}
	public Location stringToLoc(String loc) {
		return stringToLoc(loc.split(","));
	}
	
	public Location stringToLoc(String[] loc) {
		double X = Double.valueOf(loc[1]);
		double Y = Double.valueOf(loc[2]);
		double Z = Double.valueOf(loc[3]);
		float Yaw = Float.valueOf(loc[4]);
		float Pitch = Float.valueOf(loc[5]);
		return new Location(Bukkit.getServer().getWorld(loc[0]), X, Y, Z, Yaw, Pitch);
	}
	
	
	public String longToTime(Long time) {
		Long seconds = time / 1000;
		int day = (int) TimeUnit.SECONDS.toDays(seconds);
		long hours = TimeUnit.SECONDS.toHours(seconds) - (day * 24);
		long minute = TimeUnit.SECONDS.toMinutes(seconds) - (TimeUnit.SECONDS.toHours(seconds) * 60);
		return day + " d " + hours + " h " + minute + " m " + new SimpleDateFormat("ss").format(new Date(time)) + " s";
	}
	public String translateSmiles(String string) {
		
		string = string.replace("<3", "�?�");
		string = string.replace(":flip:", "(╯°益°）╯︵ ┻�?┻");
		string = string.replace(":v:", "✔");
		string = string.replace(":x:", "✘");
		string = string.replace("(c)", "©");
		string = string.replace(":hi:", "(´• ω •`)ﾉ");
		string = string.replace(":love:", "╰(�?�ω�?�)╯");
		string = string.replace(":cry:", "o(╥�?╥)o");
		string = string.replace("(r)", "®");
		string = string.replace(":hugs:", "⊂(￣▽￣)⊃");
		string = string.replace(":hid:", "┬┴┤･ω･)ﾉ");
		string = string.replace(":scry:", "＼(〇_�?)�?");
		string = string.replace(":star:", "✮");
		string = string.replace(":spider:", "/\\╭(ఠఠ益ఠఠ)╮/\\");
		string = string.replace(":kstringx:", "(ノ°益°)ノ");
		string = string.replace(":friend:", "ヽ(≧◡≦)八(o^◡^o)ノ");
		string = string.replace(":sold:", "(ﾒ` ﾛ ´)︻デ�?一");
		string = string.replace(":toxic:", "☢");
		string = string.replace(":magic:", "╰(` ﾛ ´)�?�──☆*:・ﾟ");
		string = string.replace(":)", "☺");
		string = string.replace(":bear:", "ʕᵔᴥᵔʔ");
		string = string.replace(":fku:", "(ಠ益ಠ)凸");
		string = string.replace(":music:", "♬♪♫");
		string = string.replace(":meh:", "¯\\_(ツ)_/¯");
		string = string.replace(":cat:", "(^ↀᴥↀ^)");
		string = string.replace(":che:", "⊙�?⊙");
		string = string.replace(":hah:", "◉◡◉");
		string = string.replace(":hey:", "ಠ▃ಠ");
		string = string.replace(":nah:", "(�?�◡�?◡)�?�");
		string = string.replace(":hmm:", "(¬‿¬)");
		string = string.replace(":heh:", "(◕‿◕)");
		string = string.replace(":kiya:", "┌( ಠ‿ಠ)┘");
		string = string.replace(":fart:", "ε=ε=┌( >_<)┘");
		string = string.replace(":nuu:", "(ಠ╭╮ಠ)");
		string = string.replace(":magic2:", "(ﾉ◕ヮ◕)ﾉ*:･ﾟ✧");
		string = string.replace(":yay:", "(�?�｡◕‿‿◕｡)�?�");
		string = string.replace(":4pok:", "(�?�￣ ³￣)�?�");
		string = string.replace(":takblet:", "໒( ಠ ヮ ಠ )७");
		string = string.replace(":sword:", "(ಠ o ಠ)¤=}=====>");
		string = string.replace(":fcplm:", "(/_-)z");

		return string;
	}
	
	public String getLangWritingSystem(String lang) {
		switch (lang) {
			case "ua": return "cyrillic";
			case "by": return "cyrillic";
			case "ru": return "cyrillic";
			case "lv": return "latin";
			case "en": return "latin";
		}
		return "latin";
	}
	
	private String latin = "qwertyuiopasdfghjklzxcvbnm", cyrillic = "йцукенгшщзхїфівапролджє�?ч�?митьбю";
	
	public String checkAlphabet(String message) {
		int l = 0, c = 0;
		for (String s : message.split("")) {
			if (latin.contains(s)) { l++; }
			if (cyrillic.contains(s)) { c++; }
		}
		return l > c ? "latin" : "cyrillic";
	}
	
	public Entity[] getNearbyEntities(Location l, int radius) {
		int chunkRadius = radius < 16 ? 1 : (radius - (radius % 16)) / 16;
		HashSet<Entity> radiusEntities = new HashSet<Entity>();
		for (int chX = 0 - chunkRadius; chX <= chunkRadius; chX++) {
			for (int chZ = 0 - chunkRadius; chZ <= chunkRadius; chZ++) {
				int x = (int) l.getX(), y = (int) l.getY(), z = (int) l.getZ();
				for (Entity e : new Location(l.getWorld(), x + (chX * 16), y, z + (chZ * 16)).getChunk()
						.getEntities()) {
					if (e.getLocation().distance(l) <= radius && e.getLocation().getBlock() != l.getBlock())
						radiusEntities.add(e);
				}
			}
		}
		return radiusEntities.toArray(new Entity[radiusEntities.size()]);
	}


}
