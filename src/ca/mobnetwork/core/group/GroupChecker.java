package ca.mobnetwork.core.group;

/**
 * Group checker class
 * @author Benliam12
 * @version 1.0
 */
public class GroupChecker extends Thread
{

	private boolean running = true;
	
	public void setRunning(boolean running)
	{
		this.running = running;
	}
	
	public void run()
	{
		try
		{
			Thread.sleep(2000);
		}
		catch(Exception ex)
		{
			this.running = false;
		}
		
		while(this.running)
		{
			try
			{
				Thread.sleep(600000);
			}
			catch(Exception ex)
			{
				this.running = false;
			}
			GroupManager.getInstance().reload();
		}
	}
	
}
