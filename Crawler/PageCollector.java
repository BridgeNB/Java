package webcrawler;


/**
 * Created by zhengyangqiao on 9/8/16.
 */
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.lang.Integer;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class PageCollector {

    public Document returnPageInfo (String currentUrl) {
        try {
            Document doc = Jsoup.connect(currentUrl).ignoreContentType(true).get();
//            org.jsoup.select.Elements links = doc.select("a[href]");
            return doc;
        } catch (IOException ex) {
            Logger.getLogger(PageCollector.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

//    public Elements abtainUrls (String currentUrl) {
//        try {
//            Document doc = Jsoup.connect(currentUrl).ignoreContentType(true).get();
//            org.jsoup.select.Elements links = doc.select("a[href]");
//            return links;
//        } catch (IOException ex) {
//            Logger.getLogger(PageCollector.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//        return null;
//    }

    public ArrayList<String> returnUrls (Elements links) {
        ArrayList urls = new ArrayList();
        for (Element e: links) {
            if (e != null) {
                urls.add(e.attr("abs:href"));
            }
        }
        return urls;
    }


    public static void main(String[] args) {
        try {
            Map tokens = new HashMap<String, Integer>();

            Document doc = Jsoup.connect("http://www.purdue.edu/").get();
//            org.jsoup.select.Elements links = doc.select("a[href]");
            String text = doc.body().text();
            String[] words = text.split("[\\p{Punct}\\s]+");

            for (String word: words) {
                if (tokens.containsKey(word)) {
                    Integer val = (Integer)tokens.get(word);
                    tokens.put(word, val + 1);
                } else {
                    tokens.put(word, 1);
                }
            }

            List list=new ArrayList(tokens.entrySet());

            Collections.sort(list,new Comparator(){
                public int compare(Object obj1, Object obj2){
                    return ((Comparable)((Map.Entry)(obj2)).getValue()).compareTo(((Map.Entry)(obj1)).getValue());
                }
            });

            // Print works without stop words
            for (int i = 0; i < 10; i++) {
                System.out.println(list.get(i));
            }


        } catch (IOException ex) {
            Logger.getLogger(PageCollector.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
