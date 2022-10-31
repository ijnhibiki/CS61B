package ngordnet.main;
import edu.princeton.cs.algs4.In;
import ngordnet.ngrams.NGramMap;
import ngordnet.ngrams.TimeSeries;
import java.util.Map.Entry;

import java.util.*;

public class WordNet {
    private HashMap<Integer, List<String>> index_wordlist;
    private HashMap<String, List<Integer>> word_indexlist;
    //wrapper for a graph
    private Graph graph;

    public WordNet(String synsetFilename, String hyponymsFilename) {
        index_wordlist = new HashMap<>();
        word_indexlist = new HashMap<>();
        In synsets = new In(synsetFilename);
        In hyponyms = new In(hyponymsFilename);
        this.graph = new Graph();
        while (synsets.hasNextChar()) {
            String next = synsets.readLine();
            String[] newnext = next.split(",");
            int index = Integer.parseInt(newnext[0]);
            graph.addNode(index);
            String[] definitions = newnext[1].split(" ");
            LinkedList<String> reference = new LinkedList<>();
            for (String definition : definitions) {
                reference.add(definition);
                if (!word_indexlist.containsKey(definition)) {
                    LinkedList<Integer> index_reference = new LinkedList<>();
                    index_reference.add(index);
                    word_indexlist.put(definition, index_reference);
                } else {
                    word_indexlist.get(definition).add(index);
                }
            }
            index_wordlist.put(index, reference);
        }
        while (hyponyms.hasNextChar()) {
            String next = hyponyms.readLine();
            String[] newnext = next.split(",");
            int index = Integer.parseInt(newnext[0]);
            for (int counter = 1; counter < newnext.length; counter++) {
                graph.addEdge(index, Integer.parseInt(newnext[counter]));
            }
        }
    }

    public Set<String> hyponyms(String input) {
        List<Integer> index;
        Set<Integer> reference;
        Set<String> result = new LinkedHashSet<>();
        if (word_indexlist.containsKey(input)) {
            index = word_indexlist.get(input);
            for (Integer i : index) {
                reference = graph.depthFirstTraversal(graph, i);
                for (Integer j : reference) {
                    result.addAll(index_wordlist.get(j));
                }
            }
            return result;
        }
        return null;
    }

    public Set<String> hyponyms(Collection<String> words) {
        Set<String> result = new LinkedHashSet<>();
        Set<String> toremove = new LinkedHashSet<>();
        for (String input : words) {
            Set<String> reference = new LinkedHashSet<>();

            reference.addAll(hyponyms(input));
            if (!result.isEmpty()) {
                for (String index: result) {
                    if (!result.contains(index) || !reference.contains(index)) {
                        toremove.add(index);
                    }
                }
            } else {
                result.addAll(reference);
            }
        }
        for (String remove: toremove) {
            result.remove(remove);
        }
        return result;
    }

    public ArrayList<String> khyponyms(Collection<String> words, int startYear, int endYear, int k, NGramMap ngm) {
        Set<String> result = new LinkedHashSet<>();
        LinkedHashMap<String, Integer> sortedMap = new LinkedHashMap<>();
        ArrayList<Integer> list = new ArrayList<>();
        if (k == 0) {
            ArrayList<String> array = new ArrayList<>(hyponyms(words));
            Collections.sort(array);
            return array;
        } else {
            Map<String, Integer> reference = new HashMap<>();
            Set<String> result_hyponyms = hyponyms(words);
            for (String i : result_hyponyms) {
                int popularity = popularity(ngm.countHistory(i, startYear, endYear));
                if (popularity != 0) {
                    reference.put(i, popularity);
                }
            }
            for (Map.Entry<String, Integer> entry : reference.entrySet()) {
                list.add(entry.getValue());
            }
            list.sort(Collections.reverseOrder());
            for (int num : list) {
                for (Entry<String, Integer> entry : reference.entrySet()) {
                    if (entry.getValue().equals(num)) {
                        sortedMap.put(entry.getKey(), num);
                    }
                }
            }
            int counter = 0;
            for (String i : sortedMap.keySet()) {
                if (counter < k) {
                    result.add(i);
                    counter += 1;
                } else {
                    break;
                }
            }
            ArrayList<String> array = new ArrayList<>(result);
            Collections.sort(array);
            return array;
        }
    }

    public int popularity(TimeSeries input) {
        int popularity = 0;
        for (Integer year :input.years()) {
            popularity += input.get(year);
        }
        return popularity;
    }
}
