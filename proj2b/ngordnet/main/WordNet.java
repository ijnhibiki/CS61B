package ngordnet.main;
import edu.princeton.cs.algs4.In;
import ngordnet.ngrams.NGramMap;
import ngordnet.ngrams.TimeSeries;
import java.util.Map.Entry;

import java.util.*;

public class WordNet {
    private final HashMap<Integer, List<String>> indexWordList;
    private final HashMap<String, List<Integer>> wordIndexList;
    //wrapper for a graph
    private final Graph graph;

    public WordNet(String synsetsFilename, String hyponymsFilename) {
        indexWordList = new HashMap<>();
        wordIndexList = new HashMap<>();
        In synsets = new In(synsetsFilename);
        In hyponyms = new In(hyponymsFilename);
        this.graph = new Graph();
        while (synsets.hasNextChar()) {
            String next = synsets.readLine();
            String[] new_next = next.split(",");
            int index = Integer.parseInt(new_next[0]);
            graph.addNode(index);
            String[] definitions = new_next[1].split(" ");
            LinkedList<String> reference = new LinkedList<>();
            for (String definition : definitions) {
                reference.add(definition);
                if (!wordIndexList.containsKey(definition)) {
                    LinkedList<Integer> index_reference = new LinkedList<>();
                    index_reference.add(index);
                    wordIndexList.put(definition, index_reference);
                } else {
                    wordIndexList.get(definition).add(index);
                }
            }
            indexWordList.put(index, reference);
        }
        while (hyponyms.hasNextChar()) {
            String next = hyponyms.readLine();
            String[] new_next = next.split(",");
            int index = Integer.parseInt(new_next[0]);
            for (int counter = 1; counter < new_next.length; counter++) {
                graph.addEdge(index, Integer.parseInt(new_next[counter]));
            }
        }
    }

    public Set<String> hyponyms(String input) {
        List<Integer> index;
        Set<Integer> reference;
        Set<String> result = new LinkedHashSet<>();
        if (wordIndexList.containsKey(input)) {
            index = wordIndexList.get(input);
            for (Integer i : index) {
                reference = graph.depthFirstTraversal(graph, i);
                for (Integer j : reference) {
                    result.addAll(indexWordList.get(j));
                }
            }
            return result;
        }
        return null;
    }

    public Set<String> hyponyms(Collection<String> words) {
        Set<String> result = new LinkedHashSet<>();
        Set<String> to_remove = new LinkedHashSet<>();
        for (String input : words) {
            Set<String> reference = new LinkedHashSet<>(hyponyms(input));
            if (!result.isEmpty()) {
                for (String index: result) {
                    if (!result.contains(index) || !reference.contains(index)) {
                        to_remove.add(index);
                    }
                }
            } else {
                result.addAll(reference);
            }
        }
        for (String remove_element: to_remove) {
            result.remove(remove_element);
        }
        return result;
    }

    public ArrayList<String> khyponyms(Collection<String> words, int startYear, int endYear, int k, NGramMap ngm) {
        Set<String> result = new LinkedHashSet<>();
        Map<String, Integer> sortedMap = new LinkedHashMap<>();
        ArrayList<Integer> list = new ArrayList<>();
        int counter = 0;
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
