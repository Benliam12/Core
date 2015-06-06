package ca.mobnetwork.core.data;

import org.bukkit.Bukkit;

import ca.mobnetwork.core.Core;

/**
 * DataBase thread class
 * @author Benliam12
 * @version 1.0
 */
public class DataBaseThread extends Thread
{

	private boolean running = true;
	private DataBase dataBase = DataBase.getInstance();
	
	public DataBaseThread()
	{
		this.dataBase.load();
	}
	
	public void end()
	{
		this.running = false;
	}
	
	public void run()
	{
		try
		{
			Thread.sleep(2000);
		}
		catch (Exception ex)
		{
			this.running = false;
		}
		while(this.running)
		{
			Core.log.info("-----------------------------------");
			Core.log.info("[Core] Deconnexion to DataBases...");
			this.dataBase.closeConnexions();
			Core.log.info("[Core] Connexion to DataBases...");
			this.dataBase.load();
			Core.log.info("[Core] DataBases connected !");
			if(!this.dataBase.isConnect("main"))
			{
				Core.log.info("[Core] Cannot connect to main DataBase ! Plugin is now disabling");
				this.running = false;
				Bukkit.getPluginManager().disablePlugin(Bukkit.getPluginManager().getPlugin("Core"));
			}
			Core.log.info("-----------------------------------");
			try
			{
				Thread.sleep(30000);
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
				this.running = false;
			}
		}
	}
	
}
