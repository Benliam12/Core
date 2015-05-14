package ca.mobnetwork.core.sessions;

import java.util.HashMap;
import java.util.Map.Entry;

import ca.mobnetwork.core.Core;

public class SessionCleaner extends Thread
{

	private boolean running = true;
	private SessionManager sessionManager = SessionManager.getInstance();
	
	public void end()
	{
		this.running = false;
	}
	
	public void run()
	{
		while(this.running)
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
						this.sessionManager.removeSession(session.getName());
						Core.log.info("Session with name : "+ session.getName() + " was removed !");
					} catch (SessionException e) {
						Core.log.info("Could remove Session with name " + session.getName());
					}
				}
			}
			Core.log.info("Session cleaned !");
			
			try
			{
				Thread.sleep(30000);
			}
			catch (Exception ex)
			{
				this.running = false;
			}
		}
	}
	
}
