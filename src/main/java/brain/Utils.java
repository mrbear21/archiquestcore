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
		String m = string;
		m = m.replace("<3", "❤");
		m = m.replace(":flip:", "(╯°益°）╯︵ ┻━┻");
		m = m.replace(":v:", "✔");
		m = m.replace(":x:", "✘");
		m = m.replace("(c)", "©");
		m = m.replace(":hi:", "(´• ω •`)ﾉ");
		m = m.replace(":love:", "╰(❤ω❤)╯");
		m = m.replace(":cry:", "o(╥﹏╥)o");
		m = m.replace("(r)", "®");
		m = m.replace(":hugs:", "⊂(￣▽￣)⊃");
		m = m.replace(":hid:", "┬┴┤･ω･)ﾉ");
		m = m.replace(":scry:", "＼(〇_ｏ)／");
		m = m.replace(":star:", "✮");
		m = m.replace(":spider:", "/\\╭(ఠఠ益ఠఠ)╮/\\");
		m = m.replace(":kmx:", "(ノ°益°)ノ");
		m = m.replace(":friend:", "ヽ(≧◡≦)八(o^◡^o)ノ");
		m = m.replace(":sold:", "(ﾒ` ﾛ ´)︻デ═一");
		m = m.replace(":toxic:", "☢");
		m = m.replace(":magic:", "╰(` ﾛ ´)つ──☆*:・ﾟ");
		m = m.replace(":)", "☺");
		m = m.replace(":bear:", "ʕᵔᴥᵔʔ");
		m = m.replace(":fku:", "(ಠ益ಠ)凸");
		m = m.replace(":music:", "♬♪♫");
		m = m.replace(":meh:", "¯\\_(ツ)_/¯");
		m = m.replace(":cat:", "(^ↀᴥↀ^)");
		m = m.replace(":che:", "⊙﹏⊙");
		m = m.replace(":hah:", "◉◡◉");
		m = m.replace(":hey:", "ಠ▃ಠ");
		m = m.replace(":nah:", "(づ◡﹏◡)づ");
		m = m.replace(":hmm:", "(¬‿¬)");
		m = m.replace(":heh:", "(◕‿◕)");
		m = m.replace(":kiya:", "┌( ಠ‿ಠ)┘");
		m = m.replace(":fart:", "ε=ε=┌( >_<)┘");
		m = m.replace(":nuu:", "(ಠ╭╮ಠ)");
		m = m.replace(":magic2:", "(ﾉ◕ヮ◕)ﾉ*:･ﾟ✧");
		m = m.replace(":yay:", "(づ｡◕‿‿◕｡)づ");
		m = m.replace(":4pok:", "(づ￣ ³￣)づ");
		m = m.replace(":takblet:", "໒( ಠ ヮ ಠ )७");
		m = m.replace(":sword:", "(ಠ o ಠ)¤=}=====>");
		m = m.replace(":fcplm:", "(/_-)z");

		return m;
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
