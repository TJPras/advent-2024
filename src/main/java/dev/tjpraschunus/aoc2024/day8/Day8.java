package dev.tjpraschunus.aoc2024.day8;

import dev.tjpraschunus.aoc2024.Day;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static java.util.Map.entry;

public class Day8 extends Day {

    // Create HashMap of each antenna
    // Frequency is the key (a-zA-Z0-9)
    // Set of records (coordinates) is the value
    // Calculate distance between each antenna for each frequency
    // If distance still within bounds in other direction, mark antinode

    private final Map<Character, Set<Coord>> antennas;
    private final List<List<String>> grid;
    private final List<List<String>> oneGrid;
    private final List<List<String>> twoGrid;

    public Day8() {
        super(2024, 8);
        this.antennas = new HashMap<>();
        this.grid = new ArrayList<>();
        this.oneGrid = new ArrayList<>();
        this.twoGrid = new ArrayList<>();
    }

    public record Coord(int row, int col) {};

    @Override
    public String partOne() {
        BufferedReader reader;
        reader = readInput("8");
        int rowCount = 0;
        try {
            for (String line; (line = reader.readLine()) != null; ) {
                grid.add(Arrays.asList(line.split("")));
                for (int i=0; i < line.length(); i++) {
                    char frequency = line.charAt(i);
                    Coord newAntenna = new Coord(rowCount, i);
                    if ('.' != frequency) {
                        if (antennas.containsKey(frequency)) {
                            antennas.get(frequency).add(newAntenna);
                        } else {
                            antennas.put(frequency, new HashSet<>(Collections.singleton(newAntenna)));
                        }
                    }
                }
                rowCount++;
            }
        } catch (IOException exc) {
            System.out.println("Could not read from input file.");
        }

        for (List<String> row : grid) {
            oneGrid.add(new ArrayList<>(List.copyOf(row)));
            twoGrid.add(new ArrayList<>(List.copyOf(row)));
        }
        for (Map.Entry<Character, Set<Coord>> entry : antennas.entrySet()) {
            List<Coord> antennas = new ArrayList<>(entry.getValue());
            for (int i=0; i < entry.getValue().size(); i++) {
                Coord antenna = antennas.get(i);
                for (int j=i+1; j < entry.getValue().size(); j++) {
                    Coord otherAntenna = antennas.get(j);
                    Coord distance = calcDistance(antenna, otherAntenna);
                    Coord inverseDistance = new Coord(-distance.row(), -distance.col());
                    if (inbounds(antenna, distance)) {
                        mark(antenna, distance, oneGrid);
                    }
                    if (inbounds(otherAntenna, inverseDistance)) {
                        mark(otherAntenna, inverseDistance, oneGrid);
                    }
                }
            }
        }
        int numAntinodes = 0;
        for (List<String> row : oneGrid) {
            for (String col : row) {
                if (col.equals("#")) {
                    numAntinodes++;
                }
            }
        }
        // printGrid(oneGrid);
        return Integer.toString(numAntinodes);
    }

    @Override
    public String partTwo() {
        Coord zeroDistance = new Coord(0, 0);
        for (Map.Entry<Character, Set<Coord>> entry : antennas.entrySet()) {
            List<Coord> antennas = new ArrayList<>(entry.getValue());
            for (int i=0; i < entry.getValue().size(); i++) {
                Coord antenna = antennas.get(i);
                for (int j=i+1; j < entry.getValue().size(); j++) {
                    Coord otherAntenna = antennas.get(j);
                    Coord distance = calcDistance(antenna, otherAntenna);
                    Coord inverseDistance = new Coord(-distance.row(), -distance.col());
                    mark(antenna, zeroDistance, twoGrid);
                    mark(otherAntenna, zeroDistance, twoGrid);
                    markRecursive(antenna, distance, distance, twoGrid);
                    markRecursive(antenna, inverseDistance, inverseDistance, twoGrid);
                }
            }
        }
        int numAntinodes = 0;
        for (List<String> row : twoGrid) {
            for (String col : row) {
                if (col.equals("#")) {
                    numAntinodes++;
                }
            }
        }
        // printGrid(twoGrid);
        return Integer.toString(numAntinodes);
    }

    private Coord calcDistance(Coord c1, Coord c2) {
        return new Coord(c1.row()-c2.row(), c1.col()-c2.col());
    }

    private boolean inbounds(Coord antenna, Coord distance) {
        Coord result = new Coord(antenna.row()+distance.row(), antenna.col()+distance.col());
        if (result.row() < 0 | result.row() > oneGrid.size()-1) {
            return false;
        }
        return !(result.col() < 0 | result.col() > oneGrid.getFirst().size() - 1);
    }

    private void mark(Coord antenna, Coord distance, List<List<String>> grid) {
        Coord antinode = new Coord(antenna.row()+distance.row(), antenna.col()+distance.col());
        grid.get(antinode.row()).set(antinode.col(), "#");
    }

    private void markRecursive(Coord antenna, Coord originalDistance, Coord distance, List<List<String>> grid) {
        if (inbounds(antenna, distance)) {
            mark(antenna, distance, grid);
            markRecursive(antenna,
                    originalDistance,
                    new Coord(distance.row()+originalDistance.row(),
                            distance.col()+originalDistance.col()),
                    grid);
        }
    }

    private void printGrid(List<List<String>> grid) {
        try {
            PrintWriter writer = new PrintWriter("day8_output.txt", StandardCharsets.UTF_8);
            for(List<String> row : grid) {
                writer.println(String.join("", row));
            }
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
