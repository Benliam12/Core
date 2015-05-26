package ca.mobnetwork.core.group;

import org.bukkit.ChatColor;

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
