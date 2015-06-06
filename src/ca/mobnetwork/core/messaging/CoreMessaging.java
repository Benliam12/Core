package ca.mobnetwork.core.messaging;

/**
 * Cross messaging manager
 * @author Benliam12
 * @version 1.0
 */
public class CoreMessaging 
{
	private static CoreMessaging instance = new CoreMessaging();
	
	public static CoreMessaging getInstance()
	{
		return instance;
	}
	
	private CoreMessaging(){}
	
	
}
