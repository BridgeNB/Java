package webcrawler;

import java.util.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created by zhengyangqiao on 9/9/16.
 */
public class MyCrawler {

    private void initCrawlerWithSeeds(LinkQueue linkQueue, String[] seeds) {

        for (int i = 0; i < seeds.length; i++) {
            linkQueue.addUnvisitedUrl(seeds[i]);
        }
    }

    private void GetTokens(Document doc, Map tokens) {
        try {
            String[] words = doc.body().text().replaceAll("[^A-Za-z]", " ").replace("\\s[A-Za-z0-9]\\s", " ").split("[\\p{Punct}\\s]+");

            for (String word : words) {
                if (tokens.containsKey(word)) {
                    Integer val = (Integer) tokens.get(word);
                    tokens.put(word, val + 1);
                } else {
                    tokens.put(word, 1);
                }
            }
        } catch (NullPointerException e) {
            System.err.println("Caught NullPointerException: " + e.getMessage());
        }
    }

    private List SortTokens(Map tokens) {
        List list=new ArrayList(tokens.entrySet());
        Collections.sort(list,new Comparator(){
            public int compare(Object obj1, Object obj2){
                return ((Comparable)((Map.Entry)(obj2)).getValue()).compareTo(((Map.Entry)(obj1)).getValue());
            }
        });

        return list;
    }

    private List RemoveStops(Map stops, Map tokens) {
//        Map tokensWithoutStops = new HashMap<String, Integer>();
        Iterator stopIT = stops.entrySet().iterator();
        while (stopIT.hasNext()) {
            Map.Entry pair = (Map.Entry)stopIT.next();
            if (tokens.containsKey(pair.getKey())) {
                tokens.remove(pair.getKey());
            }
            stopIT.remove();
        }
        // Sort Tokens without stops again
        List sortedTokensWithoutStops = SortTokens(tokens);
        return sortedTokensWithoutStops;
    }

    private Map<String, Integer> BfsCrawling(LinkQueue linkQueue, String[] seeds) {
        // Figure out duplicated urls
        int totalUrls = 0;
        int maxdepth = 0;

        Map tokens = new HashMap<String, Integer>();

        initCrawlerWithSeeds(linkQueue, seeds);

        while (!linkQueue.unVisitedUrlsEmpty() && linkQueue.getVisitedUrlNum() <= 250) {

            String visitedUrl = (String) linkQueue.unVisitedUrlDequeue();
            if (visitedUrl == null) continue;
            PageCollector pageCollector = new PageCollector();
            // Add visited url to queue
            linkQueue.addVisitedUrl(visitedUrl);

            Document doc = pageCollector.returnPageInfo(visitedUrl);
            if (doc == null) continue;
            // Get Tokens
            GetTokens(doc, tokens);

            Elements urlElements = doc.select("a[href]");
            AbstractList<String> urls = pageCollector.returnUrls(urlElements);

            // Queue unvisited Urls
            for (String url : urls) {
                linkQueue.addUnvisitedUrl(url);
                totalUrls++;
            }

        }
        List list = SortTokens(tokens);
        for (int i = 0; i < 100; i++) {
            System.out.println(list.get(i));
        }
        System.out.println("The number of unique tokens: " + list.size());
        System.out.println("Average number of unique tokens per page: " + list.size()/linkQueue.getVisitedUrlNum());
        System.out.print("Total BFS duplicated webpages: ");
        System.out.println(totalUrls - linkQueue.getVisitedUrlNum() - linkQueue.getUnvisitedUrlNum());
        System.out.println("Total BFS unvisited url queue size: " + linkQueue.getUnvisitedUrlNum());
        System.out.println("Maximum depth is: " + maxdepth);
        // return token value
        return tokens;
    }

    private void DfsCrawling(LinkQueue linkStack, String[] seeds) {
        int totalUrls = 0;
        int maxdepth = 0;

        linkStack.addUnvisitedStack(seeds[0]);

        while (!linkStack.unVisitedStacksEmpty() && linkStack.getVisitedUrlNum() <= 1000) {
            String visitedUrl = (String) linkStack.unVisitedUrlPop();
            if (visitedUrl == null) continue;
            PageCollector pageCollector = new PageCollector();
            // Add visited url to visited url set
            linkStack.addVisitedUrl(visitedUrl);
            // obtain all urls
            Document doc = pageCollector.returnPageInfo(visitedUrl);
            if (doc == null) continue;
            Elements urlElements = doc.select("a[href]");
            AbstractList<String> urls = pageCollector.returnUrls(urlElements);

            for (String url : urls) {
                linkStack.addUnvisitedStack(url);
                totalUrls++;
            }
        }
        System.out.print("Total DFS duplicated webpages: ");
        System.out.println(totalUrls - linkStack.getVisitedUrlNum() - linkStack.getUnVisitedStackSize());
        System.out.println("Total DFS unvisited url stack size: " + linkStack.getUnVisitedStackSize());
        System.out.println("Maximum depth is: " + maxdepth);
    }

    public static void main(String[] args) {
        LinkQueue bfslinkQueue = new LinkQueue();
        MyCrawler crawler = new MyCrawler();
        String[] startSeed = {"http://www.cs.purdue.edu"};
        Map tokens = crawler.BfsCrawling(bfslinkQueue, startSeed);

//        // Stop words
//        Map stops = new HashMap<String, Integer>();
//        StopWords sw = new StopWords();
//        sw.GetStops("Stop_words.txt", stops);
//        List sortedTokensWithoutStops = crawler.RemoveStops(stops, tokens);

//        LinkQueue dfslinkStack = new LinkQueue();
//        crawler.DfsCrawling(dfslinkStack, startSeed);
    }

}
