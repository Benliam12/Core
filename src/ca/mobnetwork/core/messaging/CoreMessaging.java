package ca.mobnetwork.core.messaging;


public class CoreMessaging 
{
	private static CoreMessaging instance = new CoreMessaging();
	
	public static CoreMessaging getInstance()
	{
		return instance;
	}
	
	private CoreMessaging(){}
	
	
}
