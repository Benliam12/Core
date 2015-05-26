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
	private SessionManager sessionManager;
	private SettingManager settingManager;
	private DataBase dataBase = DataBase.getInstance();


	public static Logger log = Logger.getLogger("minecraft");
	
	public void onEnable()
	{
		this.sessionManager = SessionManager.getInstance();
		this.settingManager = SettingManager.getInstance();
		this.dataBase = DataBase.getInstance();
		
		Bukkit.getServer().getPluginManager().registerEvents(new PlayerListener(), this);
		this.settingManager.setup();
		this.dataBase.setup();
		this.sessionManager.setup();
		
		if(!this.dataBase.isConnect("main"))
		{
			log.info("Couldn't connect to main database !");
			Bukkit.getServer().getPluginManager().disablePlugin(this);
			return;
		}
		Bukkit.getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new MessageListener());
		Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
		Bukkit.getMessenger().registerIncomingPluginChannel(this, "MobNetwork", new MessageListener());
		Bukkit.getMessenger().registerOutgoingPluginChannel(this, "MobNetwork");
		getCommand("setgroup").setExecutor(new Commands());
	}
	
	public void onDisable()
	{
		SessionManager.getInstance().end();
		DataBase.getInstance().end();
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
