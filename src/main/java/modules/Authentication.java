package modules;

import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Random;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import com.BrainBungee;

import objects.BreadMaker;



public class Authentication extends Command {
	/** default time-step which is part of the spec, 30 seconds is default */
	public static final int DEFAULT_TIME_STEP_SECONDS = 30;
	/** set to the number of digits to control 0 prefix, set to 0 for no prefix */
	private static int NUM_DIGITS_OUTPUT = 6;

	private static final String blockOfZeros;

	static {
		char[] chars = new char[NUM_DIGITS_OUTPUT];
		for (int i = 0; i < chars.length; i++) {
			chars[i] = '0';
		}
		blockOfZeros = new String(chars);
	}

	private BrainBungee plugin;

	public Authentication(BrainBungee plugin) { 
		super("2fa");
		this.plugin = plugin;
	}
	
	
	
	
	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {

		if (args.length > 0) {

		//	plugin.sendMessage(locale.getLocale("2fa-disabled", player), player);
			
			
			BreadMaker bread = new BreadMaker(plugin).getBread(sender.getName());
		
			String code = null;
			
	    	if (bread.getData("2Fa") != null) {
	    		code = bread.getData("2Fa");
	    	}

	    	try {
				if (code != null && generateCurrentNumberString(code).equals(args[0])) {

			   //	  plugin.sendBungeeMessage(" "+locale.getLocale("auth-confirmed", player), player.getName());
			
			   //	  bread.setData("Authentication", "true");
								
			   //	  bread.setData("lastIP", bread.getIp()).write();
							    	  
							} else {
			   //	  plugin.sendBungeeMessage(" &c"+locale.getLocale("invalid-code", player), player.getName());
								
			     }
			} catch (GeneralSecurityException e) {
				e.printStackTrace();
			}
		}
		
		
		return false;
	}
	
	public static String generateBase32Secret() {
		return generateBase32Secret(16);
	}

	public static String generateBase32Secret(int length) {
		StringBuilder sb = new StringBuilder(length);
		Random random = new SecureRandom();
		for (int i = 0; i < length; i++) {
			int val = random.nextInt(32);
			if (val < 26) {
				sb.append((char) ('A' + val));
			} else {
				sb.append((char) ('2' + (val - 26)));
			}
		}
		return sb.toString();
	}

	public static boolean validateCurrentNumber(String base32Secret, int authNumber, int windowMillis)
			throws GeneralSecurityException {
		return validateCurrentNumber(base32Secret, authNumber, windowMillis, System.currentTimeMillis(),
				DEFAULT_TIME_STEP_SECONDS);
	}

	public static boolean validateCurrentNumber(String base32Secret, int authNumber, int windowMillis, long timeMillis,
			int timeStepSeconds) throws GeneralSecurityException {
		long fromTimeMillis = timeMillis;
		long toTimeMillis = timeMillis;
		if (windowMillis > 0) {
			fromTimeMillis -= windowMillis;
			toTimeMillis += windowMillis;
		}
		long timeStepMillis = timeStepSeconds * 1000;
		for (long millis = fromTimeMillis; millis <= toTimeMillis; millis += timeStepMillis) {
			int generatedNumber = generateNumber(base32Secret, millis, timeStepSeconds);
			if (generatedNumber == authNumber) {
				return true;
			}
		}
		return false;
	}

	public static String generateCurrentNumberString(String base32Secret) throws GeneralSecurityException {
		return generateNumberString(base32Secret, System.currentTimeMillis(), DEFAULT_TIME_STEP_SECONDS);
	}

	public static String generateNumberString(String base32Secret, long timeMillis, int timeStepSeconds)
			throws GeneralSecurityException {
		int number = generateNumber(base32Secret, timeMillis, timeStepSeconds);
		return zeroPrepend(number, NUM_DIGITS_OUTPUT);
	}

	public static int generateCurrentNumber(String base32Secret) throws GeneralSecurityException {
		return generateNumber(base32Secret, System.currentTimeMillis(), DEFAULT_TIME_STEP_SECONDS);
	}

	public static int generateNumber(String base32Secret, long timeMillis, int timeStepSeconds) throws GeneralSecurityException {
		byte[] key = decodeBase32(base32Secret);
		byte[] data = new byte[8];
		long value = timeMillis / 1000 / timeStepSeconds;
		for (int i = 7; value > 0; i--) {
			data[i] = (byte) (value & 0xFF);
			value >>= 8;
		}
		SecretKeySpec signKey = new SecretKeySpec(key, "HmacSHA1");
		Mac mac = Mac.getInstance("HmacSHA1");
		mac.init(signKey);
		byte[] hash = mac.doFinal(data);
		int offset = hash[hash.length - 1] & 0xF;
		long truncatedHash = 0;
		for (int i = offset; i < offset + 4; ++i) {
			truncatedHash <<= 8;
			truncatedHash |= (hash[i] & 0xFF);
		}
		truncatedHash &= 0x7FFFFFFF;

		truncatedHash %= 1000000;
		return (int) truncatedHash;
	}

	public static String qrImageUrl(String keyId, String secret) {
		StringBuilder sb = new StringBuilder(128);
		sb.append("https://chart.googleapis.com/chart?chs=500x500&cht=qr&chl=500x500&chld=M|0&cht=qr&chl=");
		addOtpAuthPart(keyId, secret, sb);
		return sb.toString();
	}
	
	public static String generateOtpAuthUrl(String keyId, String secret) {
		StringBuilder sb = new StringBuilder(64);
		addOtpAuthPart(keyId, secret, sb);
		return sb.toString();
	}

	private static void addOtpAuthPart(String keyId, String secret, StringBuilder sb) {
		sb.append("otpauth://totp/").append(keyId).append("%3Fsecret%3D").append(secret);
	}

	static String zeroPrepend(int num, int digits) {
		String numStr = Integer.toString(num);
		if (numStr.length() >= digits) {
			return numStr;
		} else {
			StringBuilder sb = new StringBuilder(digits);
			int zeroCount = digits - numStr.length();
			sb.append(blockOfZeros, 0, zeroCount);
			sb.append(numStr);
			return sb.toString();
		}
	}


	static byte[] decodeBase32(String str) {
		int numBytes = ((str.length() * 5) + 7) / 8;
		byte[] result = new byte[numBytes];
		int resultIndex = 0;
		int which = 0;
		int working = 0;
		for (int i = 0; i < str.length(); i++) {
			char ch = str.charAt(i);
			int val;
			if (ch >= 'a' && ch <= 'z') {
				val = ch - 'a';
			} else if (ch >= 'A' && ch <= 'Z') {
				val = ch - 'A';
			} else if (ch >= '2' && ch <= '7') {
				val = 26 + (ch - '2');
			} else if (ch == '=') {
				// special case
				which = 0;
				break;
			} else {
				throw new IllegalArgumentException("Invalid base-32 character: " + ch);
			}

			switch (which) {
				case 0:
					// all 5 bits is top 5 bits
					working = (val & 0x1F) << 3;
					which = 1;
					break;
				case 1:
					// top 3 bits is lower 3 bits
					working |= (val & 0x1C) >> 2;
					result[resultIndex++] = (byte) working;
					// lower 2 bits is upper 2 bits
					working = (val & 0x03) << 6;
					which = 2;
					break;
				case 2:
					// all 5 bits is mid 5 bits
					working |= (val & 0x1F) << 1;
					which = 3;
					break;
				case 3:
					// top 1 bit is lowest 1 bit
					working |= (val & 0x10) >> 4;
					result[resultIndex++] = (byte) working;
					// lower 4 bits is top 4 bits
					working = (val & 0x0F) << 4;
					which = 4;
					break;
				case 4:
					// top 4 bits is lowest 4 bits
					working |= (val & 0x1E) >> 1;
					result[resultIndex++] = (byte) working;
					// lower 1 bit is top 1 bit
					working = (val & 0x01) << 7;
					which = 5;
					break;
				case 5:
					// all 5 bits is mid 5 bits
					working |= (val & 0x1F) << 2;
					which = 6;
					break;
				case 6:
					// top 2 bits is lowest 2 bits
					working |= (val & 0x18) >> 3;
					result[resultIndex++] = (byte) working;
					// lower 3 bits of byte 6 is top 3 bits
					working = (val & 0x07) << 5;
					which = 7;
					break;
				case 7:
					// all 5 bits is lower 5 bits
					working |= (val & 0x1F);
					result[resultIndex++] = (byte) working;
					which = 0;
					break;
			}
		}
		if (which != 0) {
			result[resultIndex++] = (byte) working;
		}
		if (resultIndex != result.length) {
			result = Arrays.copyOf(result, resultIndex);
		}
		return result;
	}


}
