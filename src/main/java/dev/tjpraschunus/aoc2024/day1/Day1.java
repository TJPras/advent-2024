package dev.tjpraschunus.aoc2024.day1;

import dev.tjpraschunus.aoc2024.Day;
import dev.tjpraschunus.aoc2024.util.Sorting;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Day1 extends Day {

    private List<Integer> sortedOne;
    private List<Integer> sortedTwo;

    public Day1() {
        super(2024, 1);
    }

    @Override
    public String partOne() {
        // java.io.InputStream
        InputStream inputStream = Day1.class.getResourceAsStream("/day1.txt");
        InputStreamReader streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        BufferedReader reader = new BufferedReader(streamReader);
        List<Integer> listOne = new ArrayList<>();
        List<Integer> listTwo = new ArrayList<>();
        try {
            for (String line; (line = reader.readLine()) != null; ) {
                String[] splitLine = line.split("\\s+");
                listOne.add(Integer.valueOf(splitLine[0]));
                listTwo.add(Integer.valueOf(splitLine[1]));
            }
        } catch (IOException exc) {
            System.out.println("Could not read from input file.");
        }
        Sorting.quickSort(listOne, 0, listOne.size()-1);
        Sorting.quickSort(listTwo, 0, listTwo.size()-1);
        sortedOne = List.copyOf(listOne);
        sortedTwo = List.copyOf(listTwo);
        Integer diffSum = 0;
        for(int i=0; i<listOne.size(); i++) {
            diffSum += Math.abs(listOne.get(i) - listTwo.get(i));
        }
        return diffSum.toString();
    }

    @Override
    public String partTwo() {
        Integer diffScore = 0;
        int lastScore = 0;
        int j = 0;
        for(int i=0; i<sortedOne.size(); i++) {
            int oneVal = sortedOne.get(i);
            int twoVal = sortedTwo.get(j);
            while(oneVal > twoVal & j < sortedTwo.size() - 1) {
                twoVal = sortedTwo.get(j+1);
                j++;
            }
            if (oneVal == twoVal) {
                if (oneVal == sortedOne.get(i-1)) {
                    diffScore += lastScore;
                    continue;
                }
                int k = j;
                do {
                    j++;
                } while (twoVal == sortedTwo.get(j) & j < sortedTwo.size());
                lastScore = oneVal * (j - k);
                diffScore += lastScore;
            } else {
                // twoVal is greater
                lastScore = 0;
            }
        }
        return diffScore.toString();
    }

}