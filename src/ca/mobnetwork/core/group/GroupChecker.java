package ca.mobnetwork.core.group;

/**
 * Group checker class
 * @author Benliam12
 * @version 1.0
 */
public class GroupChecker implements Runnable
{	
	public void run()
	{
		GroupManager.getInstance().reload();
	}	
}
