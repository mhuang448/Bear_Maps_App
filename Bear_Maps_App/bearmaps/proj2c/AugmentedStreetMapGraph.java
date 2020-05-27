package bearmaps.proj2c;

import bearmaps.hw4.streetmap.Node;
import bearmaps.hw4.streetmap.StreetMapGraph;
import bearmaps.proj2ab.Point;
import bearmaps.proj2ab.WeirdPointSet;
import edu.princeton.cs.algs4.TrieST;

import java.util.*;

import static bearmaps.proj2c.utils.Constants.OSM_DB_PATH;
import static java.lang.Character.toLowerCase;

/**
 * An augmented graph that is more powerful that a standard StreetMapGraph.
 * Specifically, it supports the following additional operations:
 *
 *
 * @author Alan Yao, Josh Hug, ________
 */
public class AugmentedStreetMapGraph extends StreetMapGraph {
    private WeirdPointSet weirdPointSet;
    private Map<Point, Node> pointMap;
    private Map<String, List<Node>> locationsWithCleanName;
//    private TrieST<String> names;
    private MyTrie names;
    private Map<String, String> cleanToNorm;

    public AugmentedStreetMapGraph(String dbPath) {
        super(dbPath);
        // You might find it helpful to uncomment the line below:
         List<Node> nodes = this.getNodes();
         pointMap = new HashMap<>();
         List<Point> points = new ArrayList<>();
        locationsWithCleanName = new HashMap<>();
//        names = new TrieST<>();
        names = new MyTrie();
        cleanToNorm = new HashMap<>();

         for (Node n: nodes) {
            if (neighbors(n.id()).size() > 0) { //dealing with points on the road
                Point validPoint = new Point(n.lon(), n.lat());
                pointMap.put(validPoint, n);
                points.add(validPoint);
            }
//            else { //dealing with locations only
//                String name = n.name();
//                String cleanName = cleanName(name);
//                if (!locationsWithCleanName.containsKey(cleanName)) {
//                    List<Node> locations = new ArrayList<>();
//                    locations.add(n);
//                    locationsWithCleanName.put(cleanName, locations);
//                } else {
//                    List<Node> locations = locationsWithCleanName.get(cleanName);
//                    locations.add(n);
//                }
//                names.add(cleanName);
//                cleanToNorm.put(cleanName, name);
//            }
             if (n.name() != null) {
                 String name = n.name();
                 String cleanName = cleanName(name);
                 if (!locationsWithCleanName.containsKey(cleanName)) {
                     List<Node> locations = new ArrayList<>();
                     locations.add(n);
                     locationsWithCleanName.put(cleanName, locations);
                 } else {
                     List<Node> locations = locationsWithCleanName.get(cleanName);
                     locations.add(n);
                 }
                 names.add(cleanName);
                 cleanToNorm.put(cleanName, name);
             }


         }
         weirdPointSet = new WeirdPointSet(points);
    }

    private String cleanName(String name) {
        String ret = "";
        for (int i = 0; i < name.length(); i++) {
            char c = name.charAt(i);
            if ((int) c <= 'Z' && (int) c >= 'A') {
                c = toLowerCase(c);
            }
            if (((int) c <= 'z' && (int) c >= 'a') || (int) c == ' ') {
                String add = Character.toString(c);
                ret = ret + add;
            }


        }
        return ret;
    }


    /**
     * For Project Part II
     * Returns the vertex closest to the given longitude and latitude.
     * @param lon The target longitude.
     * @param lat The target latitude.
     * @return The id of the node in the graph closest to the target.
     */
    public long closest(double lon, double lat) {
        Point nearest = weirdPointSet.nearest(lon, lat);
        Node closest = pointMap.get(nearest);
        return closest.id();
    }


    /**
     * For Project Part III (gold points)
     * In linear time, collect all the names of OSM locations that prefix-match the query string.
     * @param prefix Prefix string to be searched for. Could be any case, with our without
     *               punctuation.
     * @return A <code>List</code> of the full names of locations whose cleaned name matches the
     * cleaned <code>prefix</code>.
     */
    public List<String> getLocationsByPrefix(String prefix) {
//        System.out.println("HEY");
        String cleanPrefix = cleanName(prefix);
//        Iterable<String> iter = names.keysWithPrefix(cleanPrefix);

        //MyTrie
        List<String> keysWithPrefix = names.keysWithPrefix(cleanPrefix);
//        System.out.println(iter.size());

        List<String> ret = new ArrayList<>();
        for (String cleanName: keysWithPrefix) {
            List<Node> sameName = locationsWithCleanName.get(cleanName);
            for (Node n: sameName) {
                ret.add(n.name());
            }

        }
        return ret;
    }

    /**
     * For Project Part III (gold points)
     * Collect all locations that match a cleaned <code>locationName</code>, and return
     * information about each node that matches.
     * @param locationName A full name of a location searched for.
     * @return A list of locations whose cleaned name matches the
     * cleaned <code>locationName</code>, and each location is a map of parameters for the Json
     * response as specified: <br>
     * "lat" -> Number, The latitude of the node. <br>
     * "lon" -> Number, The longitude of the node. <br>
     * "name" -> String, The actual name of the node. <br>
     * "id" -> Number, The id of the node. <br>
     */
    public List<Map<String, Object>> getLocations(String locationName) {
        String cleanLocationName = cleanName(locationName);
        List<Node> locations = locationsWithCleanName.get(cleanLocationName);
        List<Map<String, Object>> ret = new ArrayList<>();
        for (Node n: locations) {
            Map<String, Object> node = new HashMap<>();
            node.put("lat", n.lat());
            node.put("lon", n.lon());
            node.put("name", n.name());
            node.put("id", n.id());
            ret.add(node);
        }
        return ret;
    }


    /**
     * Useful for Part III. Do not modify.
     * Helper to process strings into their "cleaned" form, ignoring punctuation and capitalization.
     * @param s Input string.
     * @return Cleaned string.
     */
    private static String cleanString(String s) {
        return s.replaceAll("[^a-zA-Z ]", "").toLowerCase();
    }

    public static void main(String[] args) {
        System.out.println(cleanString("SUPERMAN"));
        System.out.println(cleanString("SUPERMAN's"));
        System.out.println(cleanString("SUPER MAN"));
        System.out.println(cleanString("-SUPERM4209348203948230942++AN"));
        System.out.println(cleanString("e"));
        System.out.println(cleanString("99 Ranch"));
        AugmentedStreetMapGraph graph = new AugmentedStreetMapGraph(OSM_DB_PATH);
        List<String> prefix = graph.getLocationsByPrefix(" ");
        System.out.println("Prefix length for space: " + prefix.size());
        List<Map<String, Object>> topDogs = graph.getLocations("Top Dog");

        System.out.println(topDogs.size());

    }

}
