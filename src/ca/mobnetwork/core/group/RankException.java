package ca.mobnetwork.core.group;

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
	
	public Rank getRank()
	{
		return this.rank;
	}
	
	public String getMessage()
	{
		return this.message;
	}
	
}
