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
	private int groupChecker;
	
	public static GroupManager getInstance()
	{
		return groupManager;
	}
	
	private GroupManager(){}
	
	public void setup()
	{
		this.groupChecker = Bukkit.getScheduler().scheduleSyncRepeatingTask(Core.getInstance(), new GroupChecker(), 0, 20 * 300);
		this.reload();
	}
	
	public void end()
	{
		Bukkit.getScheduler().cancelTask(this.groupChecker);
	}
	
	/**
	 * Method run when reloading groups
	 */
	public void reload()
	{
		Core.log.info("Cleaning ranks...");
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
				String[] perms = result.getString("perms").split(",");
				if(this.getRank(id) != null)
				{
					Rank rank = this.getRank(id);
					rank.setName(name)
					.setFormat(format)
					.setPrefix(prefix)
					.addPermission(perms);
				}
				else
				{
					Rank rank = new Rank(id,name,color,prefix,format);
					rank.addPermission(perms);
					this.ranks.add(rank);
					Core.log.info("Add rank : " + name);
				}
			}
		}
		
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	/**
	 * Permanatly delete a rank.
	 * 
	 * @param name Rank name
	 * @return Group name
	 * @throws RankException If something when wrong
	 */
	public String deleteRank(String name) throws RankException
	{
		Rank rank = this.getRank(name);
		if(rank != null)
		{
			return this.deleteRank(rank);
		}
		throw new RankException("Rank not found !");
	}

	/**
	 * Permanatly delete a rank.
	 * 
	 * @param id Rank ID
	 * @return Group name
	 * @throws RankException If something when wrong
	 */
	public String deleteRank(int id) throws RankException
	{
		Rank rank = this.getRank(id);
		if(rank != null)
		{
			return this.deleteRank(rank);
		}
		throw new RankException("Rank not found !");
	}
	
	/**
	 * Permanatly delete a rank.
	 * 
	 * @param rank Rank object
	 * @return Group name
	 * @throws RankException If something when wrong
	 */
	public String deleteRank(Rank rank) throws RankException
	{
		try
		{
			rank.delete();
			this.removeRank(rank);
			return rank.getName();
		}
		catch (SQLException ex)
		{
			throw new RankException("Couldn't delete Rank : " + rank.getName() + " (ERR : "+ ex.getMessage() + ")", rank);
		}
	}
	
	/**
	 * Add a rank to rank list
	 * 
	 * @param rank
	 */
	public void addRank(Rank rank)
	{
		if(this.getRank(rank.getName()) == null)
		{
			this.ranks.add(rank);
		}
	}
	
	/**
	 * Remove a rank from the list
	 * 
	 * @param rank
	 */
	public void removeRank(Rank rank)
	{
		if(this.getRank(rank.getName()) != null)
		{
			this.ranks.remove(rank);
		}
	}
	
	/**
	 * Creating a new group
	 * 
	 * @param name Group name
	 * @throws RankException If something relative to rank went wrong
	 * @throws SQLException If something relative do DataBase went wrong
	 */
	public void createGroup(String name) throws RankException, SQLException
	{
		if(this.getRank(name) == null)
		{
			String sql = "INSERT INTO `rank` VALUE(0,?,?,?,?,?,?,?)";
			PreparedStatement request = this.dataBase.getConnection("main").prepareStatement(sql);
			request.setString(1, name);
			request.setString(2, "7");
			request.setString(3, "%player% : %message%");
			request.setString(4, "&7");
			request.setString(5, "");;
			request.setString(6, "0");
			request.setString(7, Integer.toString(Math.round(System.currentTimeMillis() / 1000)));
			request.executeUpdate();
			
			this.addRank(new Rank(name));
		}
		else
		{
			throw new RankException("This group already exists !", this.getRank(name));
		}
	}
	
	/**
	 * Implementing essentials data to specefic player
	 * 
	 * @param param 
	 * @param isUUID If the param is a UUID (true) or a player name (false)
	 */
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
				String req = "INSERT INTO `users` VALUES(0,?,0,0,0,0,0,?,0)";
				PreparedStatement insert = this.dataBase.getConnection("main").prepareStatement(req);
				insert.setString(1, uuid);
				insert.setString(2, "");
				insert.executeUpdate();
			}	
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	/**
	 * Set a player grroup
	 * 
	 * @param uuid Player uuid 
	 * @param groupName The name of the targed group
	 * @throws RankException If something went wrong
	 */
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
	
	/**
	 * Set a player grroup
	 * 
	 * @param uuid Player uuid 
	 * @param groupId The id of the targed group
	 * @throws RankException If something went wrong
	 */
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
	
	/**
	 * Get the rank list
	 * 
	 * @return List of rank
	 */
	public ArrayList<Rank> getRanks()
	{
		return this.ranks;
	}
	
	/**
	 * Get a specific rank
	 * 
	 * @param name Rank name
	 * @return Asked rank (null if not exist)
	 */
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

	/**
	 * Get a specific rank
	 * 
	 * @param id Rank id
	 * @return Asked rank (null if not exist)
	 */
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
