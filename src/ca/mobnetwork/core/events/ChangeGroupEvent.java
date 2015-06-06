package ca.mobnetwork.core.events;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * ChangeGroup event
 * @author Benliam12
 * @version 1.0
 */
public class ChangeGroupEvent extends Event
{
	private static final HandlerList handlers = new HandlerList();
	private String uuid;
	private boolean isCancelled = false;
	
	public ChangeGroupEvent(String uuid)
	{
		this.uuid = uuid;
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
	
	public OfflinePlayer getPlayer()
	{
		return Bukkit.getOfflinePlayer(UUID.fromString(this.uuid));
	}
	
	public boolean isCancelled()
	{
		return this.isCancelled;
	}
}
