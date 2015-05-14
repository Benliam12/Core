package ca.mobnetwork.core.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import ca.mobnetwork.core.sessions.Session;

public class AddSessionEvent extends Event
{
	private static final HandlerList handlers = new HandlerList();
	private Session session;
	private boolean isCancelled = false;
	
	public AddSessionEvent(Session session)
	{
		this.session = session;
	}
	
	public HandlerList getHandlers() {
	    return handlers;
	}
	 
	public static HandlerList getHandlerList() {
	    return handlers;
	}
	
	public void setCancelled(boolean cancelled)
	{
		this.isCancelled = cancelled;
	}
	
	public Session getSession()
	{
		return this.session;
	}
	
	public boolean isCancelled()
	{
		return this.isCancelled;
	}
}
