package ca.mobnetwork.core.sessions;

@SuppressWarnings("serial")
public class SessionException extends Exception
{
	private String message;
	private Session session;
	
	/**
	 * Contructor
	 * @param message Message of the exception
	 */
	public SessionException(String message)
	{
		this.message = message;
	}
	
	/**
	 * Contructor
	 * @param message Message of the exception
	 * @param session Session linked to the exception
	 */
	public SessionException(String message,Session session)
	{
		this.message = message;
		this.session = session;
	}
	
	/**
	 * Get the exception message
	 * @return Message
	 */
	public String getMessage()
	{
		return this.message;
	}
	
	/**
	 * Get the linked session
	 * @return the session, null if not defined
	 */
	public Session getSession()
	{
		return this.session;
	}
}
