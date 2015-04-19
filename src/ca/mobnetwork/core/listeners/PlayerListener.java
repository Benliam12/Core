package ca.mobnetwork.core.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

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
		catch(SessionException exception)
		{
			
		}
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e)
	{
		try
		{
			this.sessionManager.removeSession(e.getPlayer().getName());
		}
		catch(SessionException exception)
		{
			
		}
	}
	
}
