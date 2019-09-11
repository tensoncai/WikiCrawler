package pa1Revised;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

//import pa1.Tools;


public class WikiCrawler
{
	private static final String BASE_URL = "https://en.wikipedia.org";
	private String seed;
	private int max;
	private String[] topics;
	private String output;
	private boolean focused;
	
	private ArrayList<String> visited;
	private ArrayList<String> queue;
	private ArrayList<PageLink> pageLinks;
	
	WikiCrawler(String s, int m, String[] t, String o, boolean f)
	{
		seed = s;
		max = m;
		topics = t;
		output = o;
		focused = f;
	}
	
//	WikiCrawler()
//	{
//		queue = new ArrayList<String>();
//		visited = new ArrayList<String>();
//		pageLinks = new ArrayList<PageLink>();
//		max = 100;
//		seed = "/wiki/Complexity_theory";
//		output = "Revised";
//	}
	
	public void crawl(boolean focused) throws IOException, InterruptedException
	{
		if (focused == false)
		{
			crawlNoPriority();
		}
		
		if (focused == true)
		{
			crawlWithPriority();
		}
	}
	
	public void crawlNoPriority() throws IOException, InterruptedException
	{
		queue = new ArrayList<String>();
		visited = new ArrayList<String>();
		pageLinks = new ArrayList<PageLink>();
		
		if (pageContainsAllKeywords(seed) == true)
		{
			queue.add(seed);
			visited.add(seed);
			
			while (queue.size() != 0)
			{
				String url = queue.remove(0);
				
				String htmlDocument = openURL(url);
				ArrayList<String> links = extractLinks(htmlDocument);

				// find all links not visited yet
				for (int i = 0; i < links.size(); i++)
				{
					if (findNewLink(links.get(i)) == true && visited.size() < max)
					{
						visited.add(links.get(i));
						queue.add(links.get(i));
					}
				}
				
				PageLink p = new PageLink(url, links);
				pageLinks.add(p);
			}
		}
		
		outputFile(pageLinks);
	}
	
	public void crawlWithPriority() throws IOException, InterruptedException
	{
		visited = new ArrayList<String>();
		pageLinks = new ArrayList<PageLink>();
		
		PriorityQ priorityQueue = new PriorityQ();
		
		if (pageContainsAllKeywords(seed) == true)
		{
			int relevance = computeRelevance(seed);
			priorityQueue.add(seed, relevance);
			visited.add(seed);
			
			while (priorityQueue.getCounter() > 1)
			{
				String url = priorityQueue.extractMax();
				
				String htmlDocument = openURL(url);
				ArrayList<String> links = extractLinks(htmlDocument);
				
				// find all links not visited yet
				for (int i = 0; i < links.size(); i++)
				{
					if (visited.size() < max && findNewLink(links.get(i)) == true)
					{
						int r = computeRelevance(links.get(i));
						
						visited.add(links.get(i));
						priorityQueue.add(links.get(i), r);
					}
				}
				
				PageLink p = new PageLink(url, links);
				pageLinks.add(p);
			}
		}
		
		outputFile(pageLinks);
	}
	
	private int computeRelevance(String link) throws IOException
	{
		int relevance = 0;
		if (topics.length > 0)
		{
			String htmlDoc = openURL(link);
			for (int i = 0; i < topics.length; i++)
			{
				relevance = relevance + Tool.getOccurrences(htmlDoc, topics[i]);
			}
		}
		
		return relevance;
	}
	
	public String openURL(String link) throws IOException
	{
		String url = BASE_URL + link;
		
		URL u = new URL(url);
		URLConnection urlConnection = u.openConnection();
		
		BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

		String document = "";
		
		// read line by line
		String line = br.readLine();
	    while (line != null)
	    {
	    		document = document + line;
	    		line = br.readLine();
	    }
	    
	    br.close();
	    
	    return document;
	}
	
	public ArrayList<String> extractLinks(String document) throws IOException, InterruptedException
	{
		String htmlDoc = remainingHtmlDocAfterFirstPTag(document);
		ArrayList<String> links = findValidWikiLinks(htmlDoc);
		
		return links;
	}

	private String remainingHtmlDocAfterFirstPTag(String document)
	{
		for (int i = 0; i < document.length() - 2; i++)
		{
			String p = document.substring(i, i + 3);
			if (p.equals("<p>"))
			{
				return document.substring(i, document.length());
			}
		}
		
		return null;
	}
	
	private ArrayList<String> findValidWikiLinks(String remainingDocument) throws IOException, InterruptedException
	{
		ArrayList<String> wikiLinks = new ArrayList<String>();
		
		String wikiLink = "";
		for (int i = 0; i < remainingDocument.length() - 15; i++)
		{
			if (remainingDocument.substring(i, i + 15).equals("<a href=\"/wiki/"))
			{	
				wikiLink = extractCurrentLink(i + 9, remainingDocument);
				
				// Check that the wiki link does not contain # or :
				if (wikiWebLinkDoesNotContainSymbols(wikiLink) == true &&
					pageContainsAllKeywords(wikiLink) == true)
				{
					wikiLinks.add(wikiLink);
				}
			}
		}
		
		return wikiLinks;
	}
	
	private String extractCurrentLink(int index, String document)
	{
		// index is the starting index of the /wiki/
		// document is the html document
		String link = "";
				
		while (document.charAt(index) != '"')
		{
			link = link + document.charAt(index);
			index++;

			if (index == document.length())
			{
				System.out.println("SOMETHING WRONG!");
			}
		}
				
		return link;
	}
	
	private boolean wikiWebLinkDoesNotContainSymbols(String link)
	{
		// the web link cannot contain characters # or :
		for (int i = 0; i < link.length(); i++)
		{
			if (link.charAt(i) == '#' || link.charAt(i) == ':')
			{
				return false;
			}
		}

		return true;
	}
	
	public boolean findNewLink(String link)
	{
		boolean linkIsNew = true;
		
		for (int  i = 0; i < visited.size(); i++)
		{
			if (link.equals(visited.get(i)))
			{
				linkIsNew = false;
			}
		}
		
		return linkIsNew;
	}
	
	private boolean pageContainsAllKeywords(String link) throws IOException, InterruptedException
	{
		// if there is at least one topic, scan the links for the topic. Else, return the links queue
		if (topics.length == 0) 
		{
			return true;
		}

		String htmlDoc = openURL(link);
		String doc = remainingHtmlDocAfterFirstPTag(htmlDoc);
		
		// check if htmlDoc contains all keywords
		boolean containsAll = true;
		
		for (int i = 0; i < topics.length && containsAll; i++)
		{
			if (!doc.contains(topics[i]))
			{
				containsAll = false;
			}
		}

		return containsAll;
	}
	
	public void outputFile(ArrayList<PageLink> p) throws FileNotFoundException
	{
		PrintWriter outputFile = new PrintWriter(output);
		outputFile.println(max);
	
		// print each link and its children links
		for (int i = 0; i < p.size(); i++)
		{
			PageLink node = p.get(i);
			int size = node.getChildSize();
		
			for (int j = 0; j < size; j++)
			{
				String curChild = node.getChildLink(j);
				
				if (Tool.contains(visited, curChild))
				{
					outputFile.println(node.getLink() + " " + curChild);
					System.out.println(node.getLink() + " " + curChild);
				}
			}
		}
		
		outputFile.close();
	}
	
	public void printVisited()
	{
		for (int i = 0; i < visited.size(); i++)
		{
			System.out.println("visited link: " + visited.get(i));
		}
	}
}
