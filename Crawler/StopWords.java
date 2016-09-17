package webcrawler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Created by zhengyangqiao on 9/13/16.
 */
public class StopWords {
    public void GetStops (String filename, Map stops) {
        try {
            Scanner input = new Scanner (new File (filename));
            while (input.hasNextLine()) {
                String line = input.nextLine();
                if (stops.containsKey(line)) {
                    Integer val = (Integer) stops.get(line);
                    stops.put(line, val + 1);
                } else {
                    stops.put(line, 1);
                }
            }
        } catch (Exception e) {
            System.err.println("Caught Exceptions from GetStops: " + e.getMessage());
        }
    }

    public static void main (String[] args) {

        Map stops = new HashMap<String, Integer>();
        StopWords sw = new StopWords();
        sw.GetStops("Stop_words.txt", stops);
        List stopwordsList = new ArrayList(stops.entrySet());
        Collections.sort(stopwordsList, new Comparator() {
            public int compare(Object obj1, Object obj2) {
                return ((Comparable) ((Map.Entry) (obj2)).getValue()).compareTo(((Map.Entry) (obj1)).getValue());
            }
        });
        for (int i = 0; i < 10; i++) {
            System.out.println(stopwordsList.get(i));
        }

    }
}
