package ca.mobnetwork.core.permissions;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

import ca.mobnetwork.core.Core;
import ca.mobnetwork.core.group.Rank;
import ca.mobnetwork.core.sessions.Session;
import ca.mobnetwork.core.sessions.SessionException;
import ca.mobnetwork.core.sessions.SessionManager;

/**
 * Permission manager
 * @author Benliam12
 * @version 1.0
 */
public class PermissionManager 
{
	private static PermissionManager instance = new PermissionManager();
	
	public static PermissionManager getInstance()
	{
		return instance;
	}
	
	private PermissionManager(){}
	
	private HashMap<UUID,PermissionAttachment> permissions = new HashMap<UUID,PermissionAttachment>();

	/**
	 * Setup method only
	 */
	public void setup()
	{
		for(Player player : Bukkit.getOnlinePlayers())
		{
			try 
			{
				this.injectPlayer(player);
			} 
			catch (SessionException e) 
			{
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * End method
	 */
	public void end()
	{
		
	}
	
	/**
	 * Get the PermissionAttachment linked to a specific player
	 * 
	 * @param player Player object of the target
	 * @return The PermissionAttachment
	 */
	public PermissionAttachment getPlayerPermissionAttachment(Player player)
	{
		return this.permissions.get(player.getUniqueId());
	}
	
	/**
	 * Remove player from the PermissionAttachment list. Use it when player disconnect.
	 * 
	 * @param player Player object of the target
	 */
	public void removePlayer(Player player)
	{
		this.permissions.remove(player.getUniqueId());
	}
	
	/**
	 * Remove player from the PermissionAttachment list. Use it when player disconnect.
	 * 
	 * @param uuid uuid of player
	 */
	public void removePlayer(String uuid)
	{
		this.permissions.remove(UUID.fromString(uuid));
	}
	
	/**
	 * Add player to the PermissionAttchment list. Use it when player connect.
	 * 
	 * @param player Player object of the target.
	 * 
	 * @throws SessionException
	 */
	public void injectPlayer(Player player) throws SessionException
	{
		this.permissions.put(player.getUniqueId(), player.addAttachment(Core.getInstance()));
		Session session = SessionManager.getInstance().getSession(player.getName());
		Rank rank = (Rank) session.getData("rank");
		String permissionList = (String) session.getData("permissionArray");
	
		if(permissionList != null)
		{
			String[] permissionArray = permissionList.split(",");
			PermissionAttachment permissionAttachment = this.permissions.get(player.getUniqueId());
			for(String permission : permissionArray)
			{
				permissionAttachment.setPermission(permission, true);
			}
		}
		
		if(rank != null)
		{
			rank.injectPlayer(player);
		}
		else
		{
			throw new SessionException("Couln't inject player : " + player.getName());
		}
	}
}
