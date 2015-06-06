package ca.mobnetwork.core.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import ca.mobnetwork.core.sessions.SessionException;
import ca.mobnetwork.core.sessions.SessionManager;

/**
 * Player main listener
 * @author Benliam12
 * @version 1.0
 */
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
			if(sessionException.getSession() != null)
			{
				sessionException.getSession().checkUp();
			}
			// Session has not expired
		}
	}
	
}
