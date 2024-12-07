package dev.tjpraschunus.aoc2024.day5;

import dev.tjpraschunus.aoc2024.Day;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;

public class Day5 extends Day {

    private final List<List<Integer>> invalidPages;
    private final HashMap<Integer, Set<Integer>> rules;

    public Day5() {
        super(2024, 5);
        invalidPages = new ArrayList<>();
        rules = new HashMap<>();
    }

    @Override
    public String partOne() {
        BufferedReader reader;
        reader = readInput("5");
        List<List<Integer>> manualUpdates = new ArrayList<>();
        try {
            for (String line; (line = reader.readLine()) != null; ) {
                if (line.isEmpty()) {
                    break;
                }
                List<String> splitLine = Arrays.asList(line.split("\\|"));
                Integer newKey = Integer.valueOf(splitLine.getFirst());
                int newValue = Integer.parseInt(splitLine.getLast());
                if (rules.containsKey(newKey)) {
                    rules.get(newKey).add(newValue);
                } else {
                    rules.put(newKey, new HashSet<>());
                    rules.get(newKey).add(newValue);
                }
            }
            for (String line; (line = reader.readLine()) != null; ) {
                List<Integer> pages = Arrays.stream(line.split(","))    // stream of String
                        .map(Integer::valueOf) // stream of Integer
                        .toList();
                manualUpdates.add(pages);
            }
        } catch (IOException exc) {
            System.out.println("Could not read from input file.");
        }
        int middlePageSum = 0;
        for (List<Integer> pages : manualUpdates) {
            boolean pagesValid = true;
            for (int i=pages.size()-1; i >= 0; i--) {
                for (int j=0; j < i; j++) {
                    if (rules.get(pages.get(i)).contains(pages.get(j))) {
                        // Rule states page earlier in print list
                        // should be after page later in list
                        pagesValid = false;
                        break;
                    }
                }
                if (!pagesValid) {
                    break;
                }
            }
            if (pagesValid) {
                middlePageSum += pages.get((pages.size()-1) / 2);
            } else {
                invalidPages.add(pages);
            }
        }
        return Integer.toString(middlePageSum);
    }

    @Override
    public String partTwo() {
        int middlePageSum = 0;
        for (List<Integer> pages : invalidPages) {
            List<Integer> sortedPages = new ArrayList<>(List.copyOf(pages));
            sortedPages.sort(new PageComparator());
            middlePageSum += sortedPages.get((pages.size()-1) / 2);
        }
        return Integer.toString(middlePageSum);
    }

    public class PageComparator implements Comparator<Integer>
    {
        public int compare(Integer page1, Integer page2) {
            if (rules.get(page1).contains(page2)) {
                return -1;
            } else if (rules.get(page2).contains(page1)) {
                return 1;
            }
            return 0;
        }
    }

}
