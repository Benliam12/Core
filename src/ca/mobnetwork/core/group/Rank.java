package ca.mobnetwork.core.group;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

import ca.mobnetwork.core.data.DataBase;
import ca.mobnetwork.core.permissions.PermissionManager;

/**
 * Rank object
 * @author Benliam12
 * @version 1.0
 */
public class Rank 
{

	private String format;
	private String name;
	private String color;
	private String prefix;
	private int id;
	private ArrayList<String> permissions = new ArrayList<>();
	private PermissionManager permissionManager = PermissionManager.getInstance();

	public Rank(String name)
	{
		try
		{
			String sql = "SELECT * FROM `rank` WHERE name = ?";
			PreparedStatement request = DataBase.getInstance().getConnection("main").prepareStatement(sql);
			request.setString(1, name);	
			ResultSet resultSet = request.executeQuery();
			if(resultSet.next())
			{
				this.id = resultSet.getInt("id"); 
				this.name = name;
				this.color = resultSet.getString("color"); 
				this.prefix = resultSet.getString("prefix");
				this.format = resultSet.getString("format");
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	public Rank(int id, String name, String color, String prefix, String format)
	{
		this.id = id;
		this.name = name;
		this.color = color;
		this.prefix = prefix;
		this.format = format;
	}
	
	public Rank addPermission(String permission)
	{
		if(!this.hasPermission(permission))
		{
			this.permissions.add(permission);
		}
		return this;
	}
	
	public void addPermission(String[] permissions)
	{
		for(String perm : permissions)
		{
			this.addPermission(perm);
		}
	}
	
	public Rank removePermission(String permission)
	{
		for(String perm : this.permissions)
		{
			if(perm.equalsIgnoreCase(permission))
			{
				this.permissions.remove(perm);
				return this;
			}
		}
		return this;
	}
	
	/**
	 * Inject the player permissions
	 * 
	 * @param player Player object
	 */
	public void injectPlayer(Player player)
	{
		PermissionAttachment permissionAttachment = this.permissionManager.getPlayerPermissionAttachment(player);
		for(String permission : this.permissions)
		{
			permissionAttachment.setPermission(permission, true);
		}
	}
	
	/**
	 * Setting the rank name. Rank not save to database
	 * 
	 * @see Method save()
	 * @param name New rank name
	 * @return The target object
	 */
	public Rank setName(String name)
	{
		this.name = name;
		return this;
	}
	
	/**
	 * Setting the rank prefix. Rank not save to databse
	 * 
	 * @see Method save()
	 * @param prefix New rank prefix. Use the symbol "&" for colors
	 * @return The target object
	 */
	public Rank setPrefix(String prefix)
	{
		this.prefix = prefix;
		return this;
	}

	/**
	 * Setting the rank format. Rank not save to databse
	 * 
	 * @see Method save()
	 * @param prefix New rank format. Use the symbol "&" for colors
	 * @return The target object
	 */
	public Rank setFormat(String format)
	{
		this.format = format;
		return this;
	}
	
	/**
	 * Save the datas that could have been modified
	 * 
	 * @see setFormat()
	 * @see setName()
	 * @see setPrefix()
	 */
	public void save()
	{
		Connection connection = DataBase.getInstance().getConnection("main");
		String lPerm = "";
		for(String perm : this.permissions)
		{
			if(lPerm.length() > 0)
			{
				lPerm = lPerm + "," + perm;
			}
			else
			{
				lPerm = perm;
			}
		}
		try
		{
			String sql = "UPDATE `rank` SET name = ?, prefix = ?, format = ?, color = ?, perms = ? WHERE id = ?";
			PreparedStatement request = connection.prepareStatement(sql);
			request.setString(1, this.name);
			request.setString(2, this.prefix);
			request.setString(3, this.format);
			request.setString(4, this.color);
			request.setString(5, lPerm);
			request.setString(6, Integer.toString(this.id));
			request.executeUpdate();
			request.close();
			
			// Saving perms that could have been added or removed
		
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	/**
	 * Delete the current rank
	 * 
	 * @throws SQLException
	 */
	public void delete() throws SQLException
	{
		Connection connection = DataBase.getInstance().getConnection("main");
		String sql = "DELETE FROM `rank` WHERE id = ?";
		PreparedStatement request = connection.prepareStatement(sql);
		request.setString(1, Integer.toString(this.id));
		request.executeUpdate();
		request.close();
	}
	
	/**
	 * Get the format without the colors
	 * 
	 * @return Format at raw form
	 */
	public String getRawFormat()
	{
		return this.format;
	}
	
	/**
	 * Get the format to apply in chat
	 * 
	 * @param playerName 
	 * @param message
	 * @return The format
	 */
	public String getChatFormat(String playerName, String message)
	{
		String format = ChatColor.translateAlternateColorCodes('&', this.format);
		if(this.id == 3)
		{
			message = ChatColor.translateAlternateColorCodes('&', message);
		}
		
		format = format.replaceAll("%message%", message);
		format = format.replaceAll("%player%", this.getColor() + playerName);
		format = format.replaceAll("%prefix%", this.getPrefix());
		
		return format;
	}
	
	/**
	 * Get the name of the rank
	 * 
	 * @return Rank name
	 */
	public String getName()
	{
		return this.name;
	}
	
	/**
	 * Get the prefix without the colors
	 * 
	 * @return Prefix at raw form
	 */
	public String getRawPrefix()
	{
		return this.prefix;
	}
	
	/**
	 * ¸Get the rank prefix
	 * 
	 * @return Rank prefix
	 */
	public String getPrefix()
	{
		return ChatColor.translateAlternateColorCodes('&', this.prefix);
	}
	
	/**
	 * Get the char of the rank color
	 * 
	 * @return
	 * @see getColor()
	 */
	public String getCharColor()
	{
		return this.color;
	}
	
	/**
	 * Get the ChatColor of the rank
	 * 
	 * @return ChatColor object
	 */
	public ChatColor getColor()
	{	
		return ChatColor.getByChar(this.color);
	}
	
	/**
	 * Get the char of the rank color
	 * 
	 * @return
	 * @see getColor()
	 */
	public String getColorByString()
	{
		return this.color;
	}
	
	/**
	 * Get the rank id
	 * 
	 * @return Rank ID
	 */
	public int getID()
	{
		return this.id;
	}
	
	public String getPermission(String permission)
	{
		for(String perm : this.permissions)
		{
			if(perm.equalsIgnoreCase(permission))
			{
				return perm;
			}
		}
		return null;
	}
	
	/**
	 * Get if the rank has a specific permission
	 * 
	 * @return boolean
	 */
	public boolean hasPermission(String permission)
	{
		for(String perm : this.permissions)
		{
			if(perm.equalsIgnoreCase(permission))
			{
				return true;
			}
		}
		return false;
	}
	
}
