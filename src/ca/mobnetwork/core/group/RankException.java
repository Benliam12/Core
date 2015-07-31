package ca.mobnetwork.core.group;

/**
 * Rank exception
 * @author Benliam12
 * @version 1.0
 */
@SuppressWarnings("serial")
public class RankException extends Exception
{

	private String message;
	private Rank rank;
	
	public RankException(String message)
	{
		this.message = message;
	}
	
	public RankException(String message, Rank rank)
	{
		this.message = message;
		this.rank = rank;
	}
	
	/**
	 * Getting the rank related to the exception
	 * 
	 * @return
	 */
	public Rank getRank()
	{
		return this.rank;
	}
	
	/**
	 * Getting the error message
	 */
	public String getMessage()
	{
		return this.message;
	}
	
}
