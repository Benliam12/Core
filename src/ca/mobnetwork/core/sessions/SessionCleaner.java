package ca.mobnetwork.core.sessions;

import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.Bukkit;

import ca.mobnetwork.core.Core;
import ca.mobnetwork.core.events.RemoveSessionEvent;

/**
 * Session cleaner
 * @author Benliam12
 * @version 1.0
 */
public class SessionCleaner implements Runnable
{
	private SessionManager sessionManager = SessionManager.getInstance();
	
	public void run()
	{
		@SuppressWarnings("unchecked")
		HashMap<String, Session> tempSession = (HashMap<String, Session>) this.sessionManager.getSessions().clone();
		Core.log.info("Starting Cleaning sessions...");
		for(Entry<String, Session> tmpSession : tempSession.entrySet())
		{
			Session session = tmpSession.getValue();
			if(!session.isOnline())
			{
				try {
					RemoveSessionEvent removeSessionEvent = new RemoveSessionEvent(session);
					Bukkit.getPluginManager().callEvent(removeSessionEvent);
					if(!removeSessionEvent.isCancelled())
					{
						this.sessionManager.removeSession(session.getName());
						Core.log.info("Session with name : "+ session.getName() + " was removed !");
					}
				} catch (SessionException e) {
					Core.log.info("Could remove Session with name " + session.getName());
				}
			}
		}
		Core.log.info("Session cleaned !");	
	}
}
