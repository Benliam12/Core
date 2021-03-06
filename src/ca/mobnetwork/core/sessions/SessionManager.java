package ca.mobnetwork.core.sessions;

import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import ca.mobnetwork.core.Core;
import ca.mobnetwork.core.events.AddSessionEvent;

/**
 * Manager of sessions
 * @author Benliam12
 * @version 1.0
 */
public class SessionManager {

	private static SessionManager instance = new SessionManager();
	
	private HashMap<String,Session> sessions = new HashMap<String,Session>();
	private int sessionCleaner;
	
	public static SessionManager getInstance()
	{
		return instance;
	}
	
	/**
	 * Method to run when server enabling
	 */
	public void setup()
	{
		for(Player player : Bukkit.getOnlinePlayers())
		{
			try
			{
				this.addSession(player.getName()).checkUp();
			}
			catch(SessionException se)
			{
				
			}
		}
		this.sessionCleaner = Bukkit.getScheduler().scheduleSyncRepeatingTask(Core.getInstance(), new SessionCleaner(), 0, 20 * 120);
	}
	
	/**
	 * Method to run when server disabling
	 */
	public void end()
	{
		Bukkit.getScheduler().cancelTask(this.sessionCleaner);
	}
	
	/**
	 * Add a new session 
	 * 
	 * @param name Player's name
	 * @throws SessionException if session already exists
	 */
	public Session addSession(String name) throws SessionException
	{
		if(!this.isSession(name))
		{
			Session session = new Session(Bukkit.getPlayer(name));
			AddSessionEvent addSessionEvent = new AddSessionEvent(session);
			Bukkit.getPluginManager().callEvent(addSessionEvent);
			if(!addSessionEvent.isCancelled())
			{
				this.sessions.put(name,session);
				return session;
			}
			return null;
		}
		else
		{
			throw new SessionException("The session already exists !", this.getSession(name));
		}
	}
	
	/**
	 * Remove a session from the current server
	 * 
	 * @param name Player's name
	 * @throws SessionException if session doesn't exists 
	 */
	public void removeSession(String name) throws SessionException
	{
		if(this.isSession(name))
		{
			this.sessions.get(name).closeSession();
			this.sessions.remove(name);
		}
		else
		{
			throw new SessionException("The session doesn't exists !");
		}
	}
	
	/**
	 * Check if user has a session
	 * 
	 * @param name Session name
	 * @return True / Fase if session exist or not
	 */
	public boolean isSession(String name)
	{
		return this.sessions.containsKey(name);
	}
	
	/**
	 * Get a specific session
	 * 
	 * @param sessionkey Username of the player is use as the sessionkey
	 * @return The session asked or null if not exist
	 */
	public Session getSession(String sessionkey)
	{
		return this.sessions.get(sessionkey);
	}
	
	/**
	 * Get a specifc session
	 * 
	 * @param param UUID
	 * @param useUUID Confirm it's a uuid. If false please use getSession(String sessionkey).
	 * @return The session asked of null if not exist
	 */
	public Session getSession(String param, boolean useUUID)
	{
		if(!useUUID)
		{
			return this.getSession(param);
		}
		for(Entry<String, Session> session : this.sessions.entrySet())
		{
			if(session.getValue().getUUID().equalsIgnoreCase(param))
			{
				return session.getValue();
			}
		}
		return null;
	}
	
	/**
	 * Get all the sessions
	 * 
	 * @return all the sessions
	 */
	public HashMap<String,Session> getSessions()
	{
		return this.sessions;
	}
}
