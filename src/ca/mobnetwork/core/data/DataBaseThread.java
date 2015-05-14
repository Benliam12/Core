package ca.mobnetwork.core.data;

import org.bukkit.Bukkit;

import ca.mobnetwork.core.Core;


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
		while(this.running)
		{
			Core.log.info("-----------------------------------");
			Core.log.info("[Stats] Deconnexion to DataBases...");
			this.dataBase.closeConnexions();
			Core.log.info("[Stats] Connexion to DataBases...");
			this.dataBase.load();
			Core.log.info("[Stats] DataBases connected !");
			if(!this.dataBase.isConnect("main"))
			{
				Core.log.info("[Stats] Cannot connect to main DataBase ! Plugin is now disabling");
				Bukkit.getPluginManager().disablePlugin(Bukkit.getPluginManager().getPlugin("Core"));
			}
			Core.log.info("-----------------------------------");
			try
			{
				Thread.sleep(60000);
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
				this.running = false;
			}
		}
	}
	
}
