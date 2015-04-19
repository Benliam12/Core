package ca.benliam12.core.sessions;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import ca.benliam12.core.exeptions.SessionException;

public class SessionManager {

	private static SessionManager instance = new SessionManager();
	
	private HashMap<String,Session> sessions = new HashMap<String,Session>();
	
	public static SessionManager getInstance()
	{
		return instance;
	}
	
	public void setup()
	{
		for(Player player : Bukkit.getOnlinePlayers())
		{
			try
			{
				this.addSession(player.getName());
			}
			catch(SessionException se)
			{
				
			}
		}
	}
	
	public void addSession(String name) throws SessionException
	{
		if(!this.isSession(name))
		{
			this.sessions.put(name,new Session(Bukkit.getPlayer(name)));
		}
		else
		{
			throw new SessionException("The session already exists !");
		}
	}
	
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
	
	public boolean isSession(String name)
	{
		return this.sessions.containsKey(name);
	}
	
	/**
	 * Get a specific session
	 * @param sessionkey Usename of the player is use as the sessionkey
	 * @return The session asked or null if not exits
	 */
	public Session getSession(String sessionkey)
	{
		return this.sessions.get(sessionkey);
	}
	
	/**
	 * Get all the sessions
	 * @return all the sessions
	 */
	public HashMap<String,Session> getSessions()
	{
		return this.sessions;
	}
	
}
