package ca.mobnetwork.core;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import ca.mobnetwork.core.data.DataBase;
import ca.mobnetwork.core.sessions.Session;
import ca.mobnetwork.core.sessions.SessionManager;

public class GroupManager 
{

	private static GroupManager groupManager = new GroupManager();
	private Connection dataBase = DataBase.getInstance().getConnection("main");
	private SessionManager sessionManager = SessionManager.getInstance();
	
	public static GroupManager getInstance()
	{
		return groupManager;
	}
	
	private GroupManager(){}
	
	public void setup()
	{
		for(Player player : Bukkit.getOnlinePlayers())
		{
			String uuid = player.getUniqueId().toString();
			try
			{
				PreparedStatement request = this.dataBase.prepareStatement("SELECT * FROM `userdata` WHERE uuid = ?");
				request.setString(1, uuid);
				ResultSet result = request.executeQuery();
				if(result.getString("uuid") != null)
				{
					player.kickPlayer(ChatColor.RED + "Please register on our website. http://server.benliam12.com");
				}
				else
				{
					Session session = this.sessionManager.getSession(player.getName());
					session.putData("srv-group", result.getString("rank"));
				}
				request.execute();
				request.close();
			} 
			catch (Exception ex)
			{
				ex.printStackTrace();
			}
		}
	}
	
	
}
