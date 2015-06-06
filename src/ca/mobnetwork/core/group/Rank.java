package ca.mobnetwork.core.group;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.bukkit.ChatColor;

import ca.mobnetwork.core.data.DataBase;

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

	
	public Rank(int id, String name, String color, String prefix, String format)
	{
		this.id = id;
		this.name = name;
		this.color = color;
		this.prefix = prefix;
		this.format = format;
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
		try
		{
			String sql = "UPDATE `rank` SET name = ?, prefix = ?, format = ?, color = ? WHERE id = ?";
			PreparedStatement request = connection.prepareStatement(sql);
			request.setString(1, this.name);
			request.setString(2, this.prefix);
			request.setString(3, this.format);
			request.setString(4, this.color);
			request.setString(4, Integer.toString(this.id));
			request.executeUpdate();
			request.close();
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
	
}
