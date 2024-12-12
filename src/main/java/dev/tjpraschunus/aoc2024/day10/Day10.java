package dev.tjpraschunus.aoc2024.day10;

import dev.tjpraschunus.aoc2024.Day;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;

public class Day10 extends Day {

    private final List<List<Integer>> topographicMap;
    private final Map<coord, Map<coord, Integer>> trailHeads;

    public Day10() {
        super(2024, 10);
        topographicMap = new ArrayList<>();
        trailHeads = new HashMap<>();
    }

    record coord(int x, int y) {};

    @Override
    public String partOne() {
        BufferedReader reader;
        reader = readInput("10");
        try {
            int rowCount = 0;
            for (String line; (line = reader.readLine()) != null; ) {
                String[] cols = line.split("");
                List<Integer> mapRow = new ArrayList<>();
                for(int col=0; col < cols.length; col++) {
                    if (cols[col].equals("0")) {
                        trailHeads.put(new coord(rowCount, col), new HashMap<>());
                    }
                    mapRow.add(Integer.parseInt(cols[col]));
                }
                topographicMap.add(mapRow);
                rowCount++;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        int scores = 0;
        for (coord trail : trailHeads.keySet()) {
            bfs(trail, trail.x(), trail.y());
            scores += trailHeads.get(trail).size();
        }
        return Integer.toString(scores);
    }

    @Override
    public String partTwo() {
        int ratings = 0;
        for (Map.Entry<coord, Map<coord, Integer>> trailHead : trailHeads.entrySet()) {
            for (Map.Entry<coord, Integer> trail : trailHead.getValue().entrySet()) {
                ratings += trail.getValue();
            }
        }
        return Integer.toString(ratings);
    }

    private void bfs(coord trailHead, int x, int y) {
        if (topographicMap.get(x).get(y) == 9) {
            coord peak = new coord(x, y);
            if (trailHeads.get(trailHead).containsKey(peak)) {
                trailHeads.get(trailHead).put(peak, trailHeads.get(trailHead).get(peak)+1);
            } else {
                trailHeads.get(trailHead).put(peak, 1);
            }
            return;
        }
        int current = topographicMap.get(x).get(y);
        if (checkDirection(x+1, y, current)) {
            bfs(trailHead, x+1, y);
        }
        if (checkDirection(x-1, y, current)) {
            bfs(trailHead, x-1, y);
        }
        if (checkDirection(x, y+1, current)) {
            bfs(trailHead, x, y+1);
        }
        if (checkDirection(x, y-1, current)) {
            bfs(trailHead, x, y-1);
        }
    }

    private boolean checkDirection(int x, int y, int current) {
        if (x < 0 | x >= topographicMap.size()) {
            return false;
        }
        if (y < 0 | y >= topographicMap.getFirst().size()) {
            return false;
        }
        return topographicMap.get(x).get(y) == current+1;
    }
}
