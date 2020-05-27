package bearmaps.proj2c;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyTrie {
    /** Clears all items out of Trie */


    private class Node {
        private HashMap<Character, Node> map;
        private boolean isKey;
        private char ch;
        Node(char c, boolean isKey) {
            this.isKey = false;
            map = new HashMap<>();
            ch = c;
        }
    }

    private Node root;

    public MyTrie() {
        root = new Node('0', false);

    }

    public void clear() {
        root = new Node('0', false);
    }


    /** Returns true if the Trie contains KEY, false otherwise */

    public boolean contains(String key) {
        if (key == null || key.length() < 1) {
            return false;
        }
        Node curr = root;
        for (int i = 0, n = key.length(); i < n; i++) {
            char c = key.charAt(i);
            if (!curr.map.containsKey(c)) {
                return false;
            }
            curr = curr.map.get(c);
        }
        if (curr.isKey) {
            return true;
        }
        return false;
    }



    /** Inserts string KEY into Trie */

    public void add(String key) {
        if (key == null || key.length() < 1) {
            return;
        }
        Node curr = root;
        for (int i = 0, n = key.length(); i < n; i++) {
            char c = key.charAt(i);
            if (!curr.map.containsKey(c)) {
                curr.map.put(c, new Node(c, false));
            }
            curr = curr.map.get(c);
        }
        curr.isKey = true;
    }

    private List<String> colHelp(String s, List<String> x, Node n) {
        if (n.isKey) {
            x.add(s);
        }
        for (char c : n.map.keySet()) {
            x = colHelp(s + c, x, n.map.get(c));
        }
        return x;
    }

    /** Returns a list of all words that start with PREFIX */
    public List<String> keysWithPrefix(String prefix) {
        List<String> ret = new ArrayList<>();
        if (prefix == null || prefix.length() < 1) {
            return ret;
        }
        Node curr = root;
        for (int i = 0, n = prefix.length(); i < n; i++) {
            char c = prefix.charAt(i);
            if (!curr.map.containsKey(c)) {
                return ret;
            }
            curr = curr.map.get(c);
        }
//        for (char c : curr.map.keySet()) {
//            colHelp(prefix + c, ret, curr.map.get(c));
//        }
        ret = colHelp(prefix, ret, curr);
        return ret;

    }

    /** Returns the longest prefix of KEY that exists in the Trie
     * Not required for Lab 9. If you don't implement this, throw an
     * UnsupportedOperationException.
     */
    public String longestPrefixOf(String key) {
        throw new UnsupportedOperationException();
    }


}

