package ca.benliam12.core.scoreboard;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import ca.benliam12.core.Core;
import ca.benliam12.core.exeptions.SessionException;
import ca.benliam12.core.sessions.Session;
import ca.benliam12.core.sessions.SessionManager;

public class CoreScoreBoardManager
{
	private static CoreScoreBoardManager coreScoreBoardManager = new CoreScoreBoardManager();
	private SessionManager sessionManager = SessionManager.getInstance();
	
	public static CoreScoreBoardManager getInstance()
	{
		return coreScoreBoardManager;
	}
	
	public void addScoreboard(String name) throws SessionException
	{
		Scoreboard sb = Bukkit.getScoreboardManager().getNewScoreboard();
		
		this.sessionManager.getSession(name).putData("core-scoreboard", new CoreScoreBoard(sb));
	}
	
	public void setup()
	{
		for(Player player : Bukkit.getOnlinePlayers())
		{
			this.updateBoard(player);
		}
	}
	
	public void removeBoard(Player player)
	{
		try
		{
			this.sessionManager.getSession(player.getName()).getData("core-scoreboard");
		}
		catch (Exception ex)
		{
			
		}
		
	}
	
	public void updateBoard(Player player)
	{
		Session s = this.sessionManager.getSession(player.getName());
		if(s.isData("core-scoreboard"))
		{
			Scoreboard sb =  ((CoreScoreBoard) s.getData("core-scoreboard")).getScoreboard();
			String name = ChatColor.translateAlternateColorCodes('&', "&a&lMob&e&lNetwork");
			Objective obj = sb.registerNewObjective("main", "dummy");
			obj.setDisplaySlot(DisplaySlot.SIDEBAR);
			obj.setDisplayName(name);
			obj.getScore("").setScore(1);
			obj.getScore("Level : " + ChatColor.GREEN + player.getLevel()).setScore(0);
			player.setScoreboard(sb);
		}
		else
		{
			try {
				this.addScoreboard(player.getName());
				this.updateBoard(player);
			} catch (SessionException e) {
				Core.log.info("Cannot add scoreboard to player : " + player.getName());
			}
		}
	}
}
