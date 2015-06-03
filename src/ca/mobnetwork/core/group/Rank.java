package ca.mobnetwork.core.group;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.bukkit.ChatColor;

import ca.mobnetwork.core.data.DataBase;

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
	
	public Rank setName(String name)
	{
		this.name = name;
		return this;
	}
	
	public Rank setPrefix(String prefix)
	{
		this.prefix = prefix;
		return this;
	}
	
	public Rank setFormat(String format)
	{
		this.format = format;
		return this;
	}
	
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
	
	public void delete() throws SQLException
	{
		Connection connection = DataBase.getInstance().getConnection("main");
		String sql = "DELETE FROM `rank` WHERE id = ?";
		PreparedStatement request = connection.prepareStatement(sql);
		request.setString(1, Integer.toString(this.id));
		request.executeUpdate();
		request.close();
	}
	
	public String getRawFormat()
	{
		return this.format;
	}
	
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
	
	public String getName()
	{
		return this.name;
	}
	
	public String getRawPrefix()
	{
		return this.prefix;
	}
	
	public String getPrefix()
	{
		return ChatColor.translateAlternateColorCodes('&', this.prefix);
	}
	
	public String getCharColor()
	{
		return this.color;
	}
	
	public ChatColor getColor()
	{	
		return ChatColor.getByChar(this.color);
	}
	
	public String getColorByString()
	{
		return this.color;
	}
		
	public int getID()
	{
		return this.id;
	}
	
}
