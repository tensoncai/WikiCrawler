package pa1Revised;

import java.util.ArrayList;

public class PageLink
{
	private String link = null;
	private ArrayList<String> children;
	private String htmlDoc;
	
	PageLink(String parentUrl, ArrayList<String> childrenLinksToBeAdded)
	{
		link = parentUrl;
		children = new ArrayList<String>();
		addChildrenLinks(childrenLinksToBeAdded);
	}
	
	private void addChildrenLinks(ArrayList<String> childrenLinksToBeAdded)
	{	
		for (int i = 0; i < childrenLinksToBeAdded.size(); i++)
		{
			boolean found = false;
			String childLink = childrenLinksToBeAdded.get(i);
			
			if (link.equals(childLink) == false)
			{
				for (int j = 0; j < children.size(); j++)
				{
					if (childLink.equals(children.get(j)))
					{
						found = true;
					}
				}
				
				if (found == false)
				{
					children.add(childLink);
				}
			}
		}
	}
	
	public boolean findSubStringInString(String link)
	{
		boolean containsLink = false;
		if (htmlDoc.contains(link))
		{
			containsLink = true;
		}
		
		return containsLink;
	}
	
	public String getHtmlDoc()
	{
		return htmlDoc;
	}
	
	public String getLink()
	{
		return link;
	}
	
	public int getChildSize()
	{
		return children.size();
	}
	
	public String getChildLink(int i)
	{
		return children.get(i);
	}
	
	public void addChildLink(String url)
	{
		children.add(url);
	}
	
	public void printPageLink()
	{
		System.out.println("link: " + link);
		//System.out.println("children size = " + children.size());
		
		for (int i = 0; i < children.size(); i++)
		{
			System.out.println("child: " + children.get(i));
		}
		
		System.out.println();
	}
}
