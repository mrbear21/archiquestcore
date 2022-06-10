package listeners;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

public class SystemMessageReceiver implements PluginMessageListener {
	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] message) {
		if (!channel.equals("BungeeCord")) {
		    return;
		}
		ByteArrayDataInput in = ByteStreams.newDataInput(message);
		String subChannel = in.readUTF();
		short len = in.readShort();
		byte[] msgbytes = new byte[len];
		in.readFully(msgbytes);
	
		DataInputStream msgin = new DataInputStream(new ByteArrayInputStream(msgbytes));
		try {
			String somedata = msgin.readUTF();
		} catch (IOException e) {
			e.printStackTrace();
		}
	  }
}
