package ca.mobnetwork.core.group;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import ca.mobnetwork.core.Core;
import ca.mobnetwork.core.data.DataBase;
import ca.mobnetwork.core.events.ChangeGroupEvent;
import ca.mobnetwork.core.sessions.Session;
import ca.mobnetwork.core.sessions.SessionManager;

/**
 * Group Manager class
 * @author Benliam12
 * @version 1.0
 */
public class GroupManager 
{

	private static GroupManager groupManager = new GroupManager();
	private ArrayList<Rank> ranks = new ArrayList<>();
	private DataBase dataBase = DataBase.getInstance();
	private GroupChecker groupChecker;
	
	public static GroupManager getInstance()
	{
		return groupManager;
	}
	
	private GroupManager(){}
	
	public void setup()
	{
		this.groupChecker = new GroupChecker();
		Thread thread = new Thread(this.groupChecker);
		thread.start();
		this.reload();
	}
	
	public void end()
	{
		this.groupChecker.setRunning(false);
	}
	
	public void reload()
	{
		Core.log.info("Cleaning ranks...");
		this.ranks.clear();
		try
		{
			String sql = "SELECT * FROM `rank`";
			PreparedStatement request = this.dataBase.getConnection("main").prepareStatement(sql);
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
				Core.log.info("Add rank : " + name);
			}
		}
		
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	public String deleteRank(String name) throws RankException
	{
		Rank rank = this.getRank(name);
		if(rank != null)
		{
			return this.deleteRank(rank);
		}
		throw new RankException("Rank not found !");
	}
	
	public String deleteRank(int id) throws RankException
	{
		Rank rank = this.getRank(id);
		if(rank != null)
		{
			return this.deleteRank(rank);
		}
		throw new RankException("Rank not found !");
	}
	
	public String deleteRank(Rank rank) throws RankException
	{
		try
		{
			rank.delete();
			return rank.getName();
		}
		catch (SQLException ex)
		{
			throw new RankException("Couldn't delete Rank : " + rank.getName() + " (ERR : "+ ex.getMessage() + ")", rank);
		}
	}
	
	public void createGroup(String name) throws RankException, SQLException
	{
		if(this.getRank(name) == null)
		{
			String sql = "INSERT INTO `rank` VALUE(0,?,null,null,null)";
			PreparedStatement request = this.dataBase.getConnection("main").prepareStatement(sql);
			request.setString(1, name);
			request.executeUpdate();
		}
		else
		{
			throw new RankException("This group already exists !", this.getRank(name));
		}
	}
	
	public void checkUpUser(String param, boolean isUUID)
	{
		String uuid = param;
		if(!isUUID)
		{
			@SuppressWarnings("deprecation")
			OfflinePlayer player = Bukkit.getOfflinePlayer(param);
			uuid = player.getUniqueId().toString();
		}
		try
		{
			String sql = "SELECT * FROM `users` WHERE uuid = ?";
			PreparedStatement request = this.dataBase.getConnection("main").prepareStatement(sql);
			request.setString(1, uuid);
			ResultSet result = request.executeQuery();
			boolean isResult = result.next();
			if(!isResult)
			{
				String req = "INSERT INTO `users` VALUES(0,?,0,0,0,0,0,0)";
				PreparedStatement insert = this.dataBase.getConnection("main").prepareStatement(req);
				insert.setString(1, uuid);
				insert.executeUpdate();
			}	
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	public void setUserGroup(String uuid, String groupName) throws RankException
	{
		if(this.getRank(groupName) == null)
		{
			throw new RankException("Group not found !");
		}
		else
		{
			this.setUserGroup(uuid, this.getRank(groupName).getID());
		}
	}
	
	public void setUserGroup(String uuid, int groupId) throws RankException
	{
		if(this.getRank(groupId) == null)
		{
			throw new RankException("Group not found !");
		}
		else
		{
			ChangeGroupEvent changeGroupEvent = new ChangeGroupEvent(uuid);
			Bukkit.getPluginManager().callEvent(changeGroupEvent);
			if(!changeGroupEvent.isCancelled())
			{
				try
				{
					String sql = "UPDATE users SET `rank` = ? WHERE uuid = ?";
					PreparedStatement request = this.dataBase.getConnection("main").prepareStatement(sql);
					request.setString(1, Integer.toString(groupId));
					request.setString(2, uuid);
					request.executeUpdate();
				}
				catch (Exception ex)
				{
					ex.printStackTrace();
				}
				Session session = SessionManager.getInstance().getSession(uuid,true);
				if(session != null)
				{
					session.updateData("rank", this.getRank(groupId));
				}
			
			}
		}
	}
	
	public ArrayList<Rank> getRanks()
	{
		return this.ranks;
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
}
