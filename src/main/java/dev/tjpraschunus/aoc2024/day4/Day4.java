package dev.tjpraschunus.aoc2024.day4;

import dev.tjpraschunus.aoc2024.Day;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day4 extends Day {

    private final List<List<String>> wordSearch;

    public Day4() {
        super(2024, 4);
        wordSearch = new ArrayList<>();
    }

    @Override
    public String partOne() {
        BufferedReader reader;
        reader = readInput("4");
        try {
            for (String line; (line = reader.readLine()) != null; ) {
                List<String> splitLine = Arrays.asList(line.split(""));
                wordSearch.add(splitLine);
            }
        } catch (IOException exc) {
            System.out.println("Could not read from input file.");
        }
        int numXmas = 0;
        for(int i = 0; i < wordSearch.size(); i++) {
            for(int j = 0; j < wordSearch.get(i).size(); j++) {
                if (wordSearch.get(i).get(j).equals("X")) {
                    numXmas += findMAS(wordSearch, i, j);
                }
            }
        }
        return Integer.toString(numXmas);
    }

    @Override
    public String partTwo() {
        int numX_Mas = 0;
        for(int i = 1; i < wordSearch.size()-1; i++) {
            for(int j = 1; j < wordSearch.get(i).size()-1; j++) {
                if (wordSearch.get(i).get(j).equals("A")) {
                    numX_Mas += findMS(wordSearch, i, j);
                }
            }
        }
        return Integer.toString(numX_Mas);
    }

    private int findMAS(List<List<String>> wordSearch, int i, int j) {
        int xmasFound = 0;
        if (i > 2) {
            // Look N
            if (wordSearch.get(i-1).get(j).equals("M")) {
                xmasFound += findDirectedAS(wordSearch, i-2, j, "N");
            }
            // Look NE
            if (j < wordSearch.getFirst().size()-3) {
                if (wordSearch.get(i-1).get(j+1).equals("M")) {
                    xmasFound += findDirectedAS(wordSearch, i-2, j+2, "NE");
                }
            }
            // Look NW
            if (j > 2) {
                if (wordSearch.get(i-1).get(j-1).equals("M")) {
                    xmasFound += findDirectedAS(wordSearch, i-2, j-2, "NW");
                }
            }
        }
        // Look E
        if (j < wordSearch.getFirst().size()-3) {
            if (wordSearch.get(i).get(j+1).equals("M")) {
                xmasFound += findDirectedAS(wordSearch, i, j+2, "E");
            }
        }
        // Look S
        if (i < wordSearch.size()-3) {
            if (wordSearch.get(i+1).get(j).equals("M")) {
                xmasFound += findDirectedAS(wordSearch, i+2, j, "S");
            }
            // Look SE
            if (j < wordSearch.getFirst().size()-3) {
                if (wordSearch.get(i+1).get(j+1).equals("M")) {
                    xmasFound += findDirectedAS(wordSearch, i+2, j+2, "SE");
                }
            }
            // Look SW
            if (j > 2) {
                if (wordSearch.get(i+1).get(j-1).equals("M")) {
                    xmasFound += findDirectedAS(wordSearch, i+2, j-2, "SW");
                }
            }
        }
        // Look W
        if (j > 2) {
            if (wordSearch.get(i).get(j-1).equals("M")) {
                xmasFound += findDirectedAS(wordSearch, i, j-2, "W");
            }
        }
        return xmasFound;
    }

    private int findDirectedAS(List<List<String>> wordSearch, int i, int j, String direction) {
        if (wordSearch.get(i).get(j).equals("A")) {
            switch (direction) {
                case "N":
                    return findS(wordSearch, i-1, j);
                case "NE":
                    return findS(wordSearch, i-1, j+1);
                case "E":
                    return findS(wordSearch, i, j+1);
                case "SE":
                    return findS(wordSearch, i+1, j+1);
                case "S":
                    return findS(wordSearch, i+1, j);
                case "SW":
                    return findS(wordSearch, i+1, j-1);
                case "W":
                    return findS(wordSearch, i, j-1);
                case "NW":
                    return findS(wordSearch, i-1, j-1);
            }
        }
        return 0;
    }

    private int findMS(List<List<String>> wordSearch, int i, int j) {
        int numX_MAS = 0;

        // Look NE
        if (wordSearch.get(i-1).get(j+1).equals("M")) {
            numX_MAS += findS(wordSearch, i+1, j-1);
        } else if (wordSearch.get(i+1).get(j-1).equals("M")) {
            // Look SW
            numX_MAS += findS(wordSearch, i-1, j+1);
        } else {
            return 0;
        }

        // Look NW
        if (wordSearch.get(i-1).get(j-1).equals("M")) {
            numX_MAS += findS(wordSearch, i+1, j+1);
        } else if (wordSearch.get(i+1).get(j+1).equals("M")) {
            // Look SE
            numX_MAS += findS(wordSearch, i-1, j-1);
        } else {
            return 0;
        }

        if (numX_MAS == 2) {
            return 1;
        }
        return 0;
    }

    private int findS(List<List<String>> wordSearch, int i, int j) {
        if (wordSearch.get(i).get(j).equals("S")) {
            return 1;
        }
        return 0;
    }
}
