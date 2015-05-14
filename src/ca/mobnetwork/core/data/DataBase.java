package ca.mobnetwork.core.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.configuration.file.FileConfiguration;

import ca.mobnetwork.core.Core;
import ca.mobnetwork.core.SettingManager;



public class DataBase 
{
	private static DataBase db = new DataBase();
	private DataBaseThread dataBase;
	private Thread dataBaseThread;
	
	public static DataBase getInstance()
	{
		return db;
	}

	private HashMap<String,Connection> connections = new HashMap<String, Connection>();
	
	public void setup()
	{
		this.dataBase = new DataBaseThread();
		this.dataBaseThread = new Thread(this.dataBase);
		this.dataBaseThread.start();
	}
	
	public void end()
	{
		this.dataBase.end();
	}
	
	public synchronized void load()
	{
		FileConfiguration config = SettingManager.getInstance().getConfig("config");
		for(String name : config.getConfigurationSection("").getKeys(false))
		{
			String host = config.getString(name + ".host");
			String db = config.getString(name + ".db");
			String user = config.getString(name + ".user");
			String password = config.getString(name + ".password");
			int port = config.getInt(name + ".port");

			String url = "jdbc:mysql://"+host+":"+port+"/" + db;
			try 
			{
				Connection c  = DriverManager.getConnection( url, user, password);
				this.connections.put(name, c);
				return;
			}
			catch ( SQLException e )
			{
			    Core.log.info(e.getMessage());
			    return;
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}
		}
	}
	
	public synchronized Connection getConnection(String name)
	{
		return this.connections.get(name);
	}
	
	public synchronized void close(String name)
	{
		try
		{
			this.connections.get(name).close();
		}
		catch(SQLException ex)
		{
			ex.printStackTrace();
		}
	}
	
	public synchronized void closeConnexions()
	{
		for(Entry<String, Connection> c : this.connections.entrySet())
		{
			try {
				c.getValue().close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public boolean isConnect(String name)
	{
		return this.connections.containsKey(name);
	}
}
