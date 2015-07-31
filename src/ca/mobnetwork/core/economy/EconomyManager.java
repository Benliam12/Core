package ca.mobnetwork.core.economy;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import ca.mobnetwork.core.data.DataBase;
import ca.mobnetwork.core.sessions.Session;
import ca.mobnetwork.core.sessions.SessionManager;

public class EconomyManager 
{
	private static EconomyManager instance = new EconomyManager();
	
	public static EconomyManager getInstance()
	{
		return instance;
	}
	
	public int getMoney(String playername, String device)
	{
		@SuppressWarnings("deprecation")
		OfflinePlayer player = Bukkit.getOfflinePlayer(playername);
		if(player.isOnline())
		{
			return this.getMoney(player.getPlayer(), device);
		}
		
		return this.getMoney(player.getUniqueId(), device);
	}
	
	public int getMoney(Player player, String device)
	{
		Session session = SessionManager.getInstance().getSession(player.getName());
		if(session != null)
		{
			return (int) session.getData(device);
		}
		return this.getMoney(player.getUniqueId(), device);
	}
	
	public int getMoney(UUID uuid, String device)
	{
		Connection connection = DataBase.getInstance().getConnection("main");
		try
		{
			String sql = "SELECT * FROM `users` WHERE `uuid` = ?";
			PreparedStatement request = connection.prepareStatement(sql);
			request.setString(1, uuid.toString());
			ResultSet result = request.executeQuery();
			if(result.next())
			{
				return result.getInt(device);
			}
			return 0;
		}
		catch(Exception ex)
		{
			return 0;
		}
	}
}
