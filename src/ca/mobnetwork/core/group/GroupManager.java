package ca.mobnetwork.core.group;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import ca.mobnetwork.core.Core;
import ca.mobnetwork.core.data.DataBase;

public class GroupManager 
{

	private static GroupManager groupManager = new GroupManager();
	private ArrayList<Rank> ranks = new ArrayList<>();
	private Connection connection = DataBase.getInstance().getConnection("main");
	
	public static GroupManager getInstance()
	{
		return groupManager;
	}
	
	private GroupManager(){}
	
	public void setup()
	{
		try
		{
			String sql = "SELECT * FROM rank";
			PreparedStatement request = this.connection.prepareStatement(sql);
			ResultSet result = request.executeQuery();
			while(result.next())
			{
				int id = result.getInt("id");
				String name = result.getString("name");
				String format = result.getString("format");
				String color = result.getString("color");
				String prefix = result.getString("prefix");
				Rank rank = new Rank(id,name,color,prefix,format);
				this.ranks.add(rank);
			}
		}
		catch (Exception ex)
		{
			Core.log.info(ex.getMessage());
		}
	}
	
	public Rank getRank(String name)
	{
		for(Rank rank : this.ranks)
		{
			if(rank.getName().equalsIgnoreCase(name))
			{
				return rank;
			}
		}
		return null;
	}
	
	public Rank getRank(int id)
	{
		for(Rank rank : this.ranks)
		{
			if(rank.getID() == id)
			{
				return rank;
			}
		}
		return null;
	}
	public void setGroup(String uuid, String rank)
	{
		
	}
	
	public void setGroup(String uuid, int rankId)
	{
		
	}
	
	
}
