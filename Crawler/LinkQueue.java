package webcrawler;
import java.util.*;

/**
 * Created by zhengyangqiao on 9/9/16.
 */
public class LinkQueue {
    private static Set visitedUrl = new HashSet();
    private static Queue unvisitedUrl = new PriorityQueue();
    private static Stack<String> unvisitedUrlStack = new Stack<>();

    public static void addUnvisitedStack (String url) {
        if (url != null && !url.trim().equals("") && !visitedUrl.contains(url) && (unvisitedUrlStack.search(url) == -1))
            unvisitedUrlStack.push(url);
    }

    public static Object unVisitedUrlPop () {
        return unvisitedUrlStack.pop();
    }

    public static Queue getUnvisitedUrl() {
        return unvisitedUrl;
    }

    public static boolean unVisitedStacksEmpty() {
        return unvisitedUrlStack.empty();
    }

    public static int getUnVisitedStackSize() {
        return unvisitedUrlStack.size();
    }

    public static void addVisitedUrl (String url) {
        visitedUrl.add(url);
    }

    public static void removeVisitedUrl (String url) {
        visitedUrl.remove(url);
    }

    public static Object unVisitedUrlDequeue() {
        return unvisitedUrl.poll();
    }

    public static void addUnvisitedUrl(String url) {
        if (url != null && !url.trim().equals("") && !visitedUrl.contains(url) && !unvisitedUrl.contains(url))
            unvisitedUrl.add(url);
    }

    public static int getVisitedUrlNum() {
        return visitedUrl.size();
    }

    public static int getUnvisitedUrlNum() {
        return unvisitedUrl.size();
    }

    public static boolean unVisitedUrlsEmpty() {
        return unvisitedUrl.isEmpty();
    }
}
