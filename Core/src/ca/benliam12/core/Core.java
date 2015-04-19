package ca.benliam12.core;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import ca.benliam12.core.commands.Commands;
import ca.benliam12.core.data.SettingManager;
import ca.benliam12.core.listeners.PlayerListener;
import ca.benliam12.core.scoreboard.CoreScoreBoardManager;
import ca.benliam12.core.sessions.SessionManager;



public class Core extends JavaPlugin
{	
	private SessionManager sessionManager = SessionManager.getInstance();
	private SettingManager settingManager = SettingManager.getInstance();
	private CoreScoreBoardManager coreScoreBoardManager = CoreScoreBoardManager.getInstance();

	public static Logger log = Logger.getLogger("minecraft");
	
	public void onEnable()
	{
		Bukkit.getServer().getPluginManager().registerEvents(new PlayerListener(), this);
		this.settingManager.setup();
		this.sessionManager.setup();
		this.coreScoreBoardManager.setup();
		getCommand("setgroup").setExecutor(new Commands());
	}
	
	public void onDisable()
	{

	}
	
	/*
	 * Getters
	 */
	public SettingManager getSettingManager()
	{
		return this.settingManager;
	}
	
	public SessionManager getSessionManager()
	{
		return this.sessionManager;
	}
}
