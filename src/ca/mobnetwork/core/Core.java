package ca.mobnetwork.core;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import ca.mobnetwork.core.commands.Commands;
import ca.mobnetwork.core.data.DataBase;
import ca.mobnetwork.core.group.GroupManager;
import ca.mobnetwork.core.listeners.PlayerListener;
import ca.mobnetwork.core.messaging.MessageListener;
import ca.mobnetwork.core.permissions.PermissionManager;
import ca.mobnetwork.core.sessions.SessionManager;


/**
 * Main class
 * @author Benliam12
 * @version 1.0
 */
public class Core extends JavaPlugin
{	
	private SessionManager sessionManager;
	private SettingManager settingManager;
	private DataBase dataBase;
	private GroupManager groupManager;
	private PermissionManager permissionManager;
	private static Core core;

	public static Logger log = Logger.getLogger("minecraft");
	
	public void onEnable()
	{
		core = this;
		this.sessionManager = SessionManager.getInstance();
		this.settingManager = SettingManager.getInstance();
		this.dataBase = DataBase.getInstance();
		this.groupManager = GroupManager.getInstance();
		this.permissionManager = PermissionManager.getInstance();
		
		Bukkit.getServer().getPluginManager().registerEvents(new PlayerListener(), this);
		this.settingManager.setup();
		this.dataBase.setup();
		
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
		
		Commands commands = new Commands();
		getCommand("setgroup").setExecutor(commands);
		getCommand("creategroup").setExecutor(commands);
		getCommand("deletegroup").setExecutor(commands);
		getCommand("checkperm").setExecutor(commands);
		
		this.groupManager.setup();
		this.sessionManager.setup();
		this.permissionManager.setup();
		//this.permissionManager.setup();
	}
	
	public void onDisable()
	{
		SessionManager.getInstance().end();
		DataBase.getInstance().end();
		GroupManager.getInstance().end();
	}
	
	public static Core getInstance()
	{
		return core;
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
	
	public GroupManager getGroupManager()
	{
		return this.groupManager;
	}
	
	public PermissionManager getPermissionManager()
	{
		return this.permissionManager;
	}
}
