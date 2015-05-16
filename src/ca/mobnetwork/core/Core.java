package ca.mobnetwork.core;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import ca.mobnetwork.core.commands.Commands;
import ca.mobnetwork.core.data.DataBase;
import ca.mobnetwork.core.listeners.PlayerListener;
import ca.mobnetwork.core.messaging.MessageListener;
import ca.mobnetwork.core.sessions.SessionManager;



public class Core extends JavaPlugin
{	
	private SessionManager sessionManager = SessionManager.getInstance();
	private SettingManager settingManager = SettingManager.getInstance();
	private DataBase dataBase = DataBase.getInstance();


	public static Logger log = Logger.getLogger("minecraft");
	
	public void onEnable()
	{
		Bukkit.getServer().getPluginManager().registerEvents(new PlayerListener(), this);
		this.settingManager.setup();
		this.dataBase.setup();
		
		if(!this.dataBase.isConnect("main"))
		{
			log.info("Couldn't connect to main database !");
			Bukkit.getServer().getPluginManager().disablePlugin(this);
		}
		Bukkit.getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new MessageListener());
		Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
		Bukkit.getMessenger().registerIncomingPluginChannel(this, "MobNetwork", new MessageListener());
		Bukkit.getMessenger().registerOutgoingPluginChannel(this, "MobNetwork");
		this.sessionManager.setup();
		getCommand("setgroup").setExecutor(new Commands());
	}
	
	public void onDisable()
	{

	}
	
	public SettingManager getSettingManager()
	{
		return this.settingManager;
	}
	
	public SessionManager getSessionManager()
	{
		return this.sessionManager;
	}
	
	public DataBase getDataBaseManager()
	{
		return this.dataBase;
	}
}
