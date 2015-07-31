package ca.mobnetwork.core.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import ca.mobnetwork.core.permissions.PermissionManager;
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
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerJoin(PlayerJoinEvent e)
	{
		// Adding Player session
		try
		{
			this.sessionManager.addSession(e.getPlayer().getName());
		}
		catch(SessionException sessionException)
		{
			// Session has not expired
			if(sessionException.getSession() != null)
			{
				sessionException.getSession().checkUp();
			}
		}
		
		// Adding player Permissions
		try {
			PermissionManager.getInstance().injectPlayer(e.getPlayer());
		} 
		catch (SessionException sessionException) 
		{
			sessionException.printStackTrace();
		}
	}
	
}
