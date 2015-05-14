package ca.mobnetwork.core.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import ca.mobnetwork.core.sessions.SessionException;
import ca.mobnetwork.core.sessions.SessionManager;

public class PlayerListener implements Listener
{
	private SessionManager sessionManager = SessionManager.getInstance();
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e)
	{
		try
		{
			this.sessionManager.addSession(e.getPlayer().getName());
		}
		catch(SessionException sessionException)
		{
			
		}
	}
	
}
