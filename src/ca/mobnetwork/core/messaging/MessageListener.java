package ca.mobnetwork.core.messaging;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

public class MessageListener implements PluginMessageListener
{

	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] data) 
	{
		if(channel.equalsIgnoreCase("Mobnetwork"))
		{
	        DataInputStream in = new DataInputStream(new ByteArrayInputStream(data));
	        String subchannel = null;   
			try {
				subchannel = in.readUTF();
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
			
			if(subchannel != null)
			{
				
			}
		}
	}

}
