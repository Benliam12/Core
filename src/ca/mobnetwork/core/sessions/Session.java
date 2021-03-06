package ca.mobnetwork.core.sessions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import ca.mobnetwork.core.SettingManager;
import ca.mobnetwork.core.data.DataBase;
import ca.mobnetwork.core.group.GroupManager;
import ca.mobnetwork.core.group.Rank;
import ca.mobnetwork.core.permissions.PermissionManager;

/**
 * Session object
 * @author Benliam12
 * @version 1.0
 */
public class Session
{
	
	private String name;
	private String uuid;
	private HashMap<String,Object> data = new HashMap<String,Object>();
	private SettingManager settingManager = SettingManager.getInstance();
	private GroupManager groupManager = GroupManager.getInstance();
	
	public Session(Player player)
	{
		this.name = player.getName();
		this.uuid = player.getUniqueId().toString();
		this.settingManager.addConfig(player.getUniqueId().toString(), "plugins/Core/data/players");
		this.checkUp();
	}
	
	/**
	 * Checking up for essentials informations
	 */
	public void checkUp()
	{
		Player player = Bukkit.getPlayer(this.name);
		Connection dataBase = DataBase.getInstance().getConnection("main");
		try
		{
			PreparedStatement request = dataBase.prepareStatement("SELECT * FROM `users` WHERE uuid = ?");
			request.setString(1, player.getUniqueId().toString());
			ResultSet result = request.executeQuery();
			boolean exist = result.next();
			if(!exist)
			{
				try
				{
					String req = "INSERT INTO `users` VALUES(0,?,0,0,0,0,0,?,0)";
					PreparedStatement insert = dataBase.prepareStatement(req);
					insert.setString(1, player.getUniqueId().toString());
					insert.setString(2, "core.member");
					insert.executeUpdate();
					this.putData("rank", this.groupManager.getRank(0));
					this.putData("permissionsArray", "");
					this.putData("golds", 0);
					this.putData("tokens", 0);
					insert.close();
				}
				catch (SessionException sessionException)
				{
					
				}
				catch (Exception ex)
				{
					player.kickPlayer(ChatColor.RED + "Error has occured !");
					ex.printStackTrace();
				}
			
			}
			else
			{
				try
				{
					int rankId = Integer.parseInt(result.getString("rank"));
					Rank rank = this.groupManager.getRank(rankId);
					if(rank != null)
					{
						this.putData("rank", this.groupManager.getRank(rankId));
					}
					else
					{
						this.putData("rank", this.groupManager.getRank(0));
					}
					this.putData("golds", result.getInt("golds"));
					this.putData("tokens", result.getInt("tokens"));
					String permissionList = result.getString("perms");
					if(permissionList != null) this.putData("permissionsArray", result.getString("perms"));
					
				} 
				catch (SessionException sessionException)
				{
					
				}
				catch (Exception ex)
				{
					player.kickPlayer(ChatColor.RED + "Error has occured !");
					ex.printStackTrace();
				}
				
			}
			request.close();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	/**
	 * Method to run when closing the session
	 */
	public void closeSession()
	{
		if(this.uuid == null) 
		{
			return;
		}
		this.settingManager.removeConfig(this.uuid);
		PermissionManager.getInstance().removePlayer(this.uuid);
	}
	
	/**
	 * Adding data to the session
	 * 
	 * @param datakey Name of the data
	 * @param data Data it self
	 * @throws SessionException if the name of the data is already registed
	 */
	public void putData(String datakey, Object data) throws SessionException
	{
		if(!this.isData(datakey))
		{
			this.data.put(datakey,data);
		}
		else
		{
			throw new SessionException("This data was already registed !",this);
		}
	}
	
	/**
	 * Updating already registed data
	 * 
	 * @param key Data name
	 * @param data new Data
	 */
	public void updateData(String key, Object data)
	{
		if(this.isData(key))
		{
			this.data.put(key,data);
		}
		else
		{
			try 
			{
				this.putData(key, data);
			} 
			catch (SessionException se) 
			{
				
			}
		}
	}

	/**
	 * If the data is registed
	 * 
	 * @param datakey Data name
	 * @return
	 */
	public boolean isData(String datakey)
	{
		return this.data.containsKey(datakey);
	}
	
	/**
	 * If the session's owner is online
	 * 
	 * @return
	 */
	public boolean isOnline()
	{
		Player player = Bukkit.getPlayer(this.name);
		if(player != null)
		{
			return true;
		}
		return false;
	}
	
	/**
	 * Getting a specific data
	 * 
	 * @param datakey Data name
	 * @return null if not data found
	 */
	public Object getData(String datakey)
	{
		return this.data.get(datakey);
	}
	
	/**
	 * Getting the name of the session
	 * 
	 * @return
	 */
	public String getName()
	{
		return this.name;
	}
	
	/**
	 * Getting the uuid of the session's owner
	 * 
	 * @return
	 */
	public String getUUID()
	{
		return this.uuid;
	}
	
}
