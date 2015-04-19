package ca.benliam12.core.scoreboard;

import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

public class CoreScoreBoard 
{
	private Scoreboard scoreboard;
	private Objective objective;
	
	public CoreScoreBoard(Scoreboard scoreboard)
	{
		this.scoreboard = scoreboard;
	}
	
	public Scoreboard getScoreboard()
	{
		return this.scoreboard;
	}
	
	public void setObjective(Objective obj)
	{
		this.objective = obj;
	}
	
	public Objective getObjective()
	{
		return this.objective;
	}
	
	
}
