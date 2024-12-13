package dev.tjpraschunus.aoc2024.day11;

import dev.tjpraschunus.aoc2024.Day;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Day11 extends Day {

    private final Map<Long, Long> stones;
    private final Map<Long, Long> stoneCache;

    public Day11() {
        super(2024, 11);
        stones = new HashMap<>();
        stoneCache = new HashMap<>();
    }

    @Override
    public String partOne() {
        BufferedReader reader;
        reader = readInput("11");
        try {
            String line = reader.readLine();
            String[] numbers = line.split(" ");
            for (String num : numbers) {
                Long stone = Long.parseLong(num);
                if (stones.containsKey(stone)){
                    stones.put(stone, stones.get(stone)+1);
                } else {
                    stones.put(stone, 1L);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for (int i =0; i < 25; i++) {
            updateStones();
        }
        Long numStones = 0L;
        for (Map.Entry<Long, Long> entry : stones.entrySet()) {
            numStones += entry.getValue();;
        }
        return Long.toString(numStones);
    }

    @Override
    public String partTwo() {
        for (int i =0; i < 50; i++) {
            updateStones();
        }
        Long numStones = 0L;
        for (Map.Entry<Long, Long> entry : stones.entrySet()) {
            numStones += entry.getValue();;
        }
        return Long.toString(numStones);
    }

    private void updateStones() {
        Map<Long, Long> currentStones = Map.copyOf(stones);
        for (Map.Entry<Long, Long> entry : currentStones.entrySet()) {
            Long stone = entry.getKey();
            if (stone == 0) {
                // Subtract number of moved stones from old index
                stones.put(stone, Math.max(stones.get(stone) - entry.getValue(), 0));
                // Add number of moved stones to new index
                stones.put(1L, stones.getOrDefault(1L, 0L) + entry.getValue());
                continue;
            }
            if ((int)(Math.log10(stone)+1) % 2 == 0) {
                String stoneString = Long.toString(stone);
                Long firstHalf = Long.parseLong(stoneString.substring(0, stoneString.length()/2));
                Long secondHalf = Long.parseLong(stoneString.substring(stoneString.length()/2));
                // Add number of moved stones to new index
                stones.put(firstHalf, Math.max(stones.getOrDefault(firstHalf, 0L) + entry.getValue(), 0));
                // Add number of moved stones to new index
                stones.put(secondHalf, Math.max(stones.getOrDefault(secondHalf, 0L) + entry.getValue(), 0));
                // Subtract number of moved stones from old index
                stones.put(stone, Math.max(stones.get(stone) - entry.getValue(), 0));
                continue;
            }
            if (stoneCache.containsKey(stone)) {
                Long newStone = stoneCache.get(stone);
                // Subtract number of moved stones from old index
                stones.put(stone, Math.max(stones.get(stone) - entry.getValue(), 0));
                // Add number of moved stones to new index
                stones.put(newStone, stones.getOrDefault(newStone, 0L) + entry.getValue());
                continue;
            }
            Long newStone = stone*2024;
            stoneCache.put(stone, newStone);
            // Subtract number of moved stones from old index
            stones.put(stone, Math.max(stones.get(stone) - entry.getValue(), 0));
            // Add number of moved stones to new index
            stones.put(newStone, stones.getOrDefault(newStone, 0L) + entry.getValue());
        }
    }
}
