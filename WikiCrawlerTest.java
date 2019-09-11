package pa1Revised;

import java.io.IOException;
import java.util.ArrayList;

public class WikiCrawlerTest 
{
	public static void main(String[] args) throws IOException, InterruptedException
	{
		String seed = "/wiki/Complexity_theory";
		String[] topics = {};
		WikiCrawler w = new WikiCrawler(seed, 3, topics, "falseOutput", false);
		
//		w.start();
		
		
//		w.openURL(seed);
		
		// crawl with no priority
		w.crawl(false);
		
		// crawl with priority
//		w.crawl(true);
//		w.crawlNoPriority();

		
//		String htmlDoc = w.openURL("https://en.wikipedia.org/wiki/Complexity_theory");
//		ArrayList<String> wikiLinks = w.extractLinks(htmlDoc);
//		//System.out.println(document);
//	
//		for (int i = 0; i < wikiLinks.size(); i++)
//		{
//			System.out.println("Link: " + wikiLinks.get(i));
//		}
		
		//w.printVisited();

	}
}
