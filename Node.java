package pa1Revised;

public class Node 
{
	private String s;
	private int priority;
	
	Node(String str, int p)
	{
		s = str;
		priority = p;
	}
	
	public String getString()
	{
		return s;
	}
	
	public int getPriority()
	{
		return priority;
	}
	
	public void setPriority(int p)
	{
		priority = p;
	}
}
