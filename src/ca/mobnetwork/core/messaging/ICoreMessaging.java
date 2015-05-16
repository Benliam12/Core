package ca.mobnetwork.core.messaging;

import org.bukkit.entity.Player;

public interface ICoreMessaging 
{
	public void onPluginMessageReceived(String channel, Player player, byte[] data);
}
