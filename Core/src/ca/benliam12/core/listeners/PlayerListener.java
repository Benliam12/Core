package ca.benliam12.core.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import ca.benliam12.core.exeptions.SessionException;
import ca.benliam12.core.scoreboard.CoreScoreBoardManager;
import ca.benliam12.core.sessions.SessionManager;

public class PlayerListener implements Listener
{
	private SessionManager sessionManager = SessionManager.getInstance();
	private CoreScoreBoardManager csbManager = CoreScoreBoardManager.getInstance();
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e)
	{
		try
		{
			this.sessionManager.addSession(e.getPlayer().getName());
		}
		catch(SessionException se)
		{
			
		}
		this.csbManager.updateBoard(e.getPlayer());
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e)
	{
		try
		{
			this.sessionManager.removeSession(e.getPlayer().getName());
		}
		catch(SessionException se)
		{
			
		}
		this.csbManager.removeBoard(e.getPlayer());
	}
	
}
