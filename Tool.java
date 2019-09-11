package pa1Revised;

import java.util.ArrayList;

public class Tool 
{
	// return the number of occurrence of findStr in str
	public static int getOccurrences(String str, String findStr)
	{
		int lastIndex = 0;
		int count = 0;
	
		while (lastIndex != -1) 
		{
		    lastIndex = str.indexOf(findStr, lastIndex);

		    if (lastIndex != -1) 
		    {
		        count++;
		        lastIndex = lastIndex + findStr.length();
		    }
		}
		
		return count;
	}
		
	// return true if a string is found in an array of strings
	public static boolean contains(String[] ss, String s)
	{
		boolean found = false;
		
		int i = 0;
		while (i<ss.length && !found)
		{
			if (ss[i] != null)
			{
				if (ss[i].equals(s))
				{
					found = true;
				}
			}
			
			i++;
		}
		
		return found;
	}
	
	public static boolean contains(ArrayList<String> ss, String s)
	{
		boolean found = false;
		int i = 0;
		
		while (i < ss.size() && !found)
		{

			if (ss.get(i).equals(s))
			{
				found = true;
			}
			
			i++;
		}
		
		return found;
	}

}
