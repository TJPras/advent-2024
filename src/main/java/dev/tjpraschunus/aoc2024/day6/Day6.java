package dev.tjpraschunus.aoc2024.day6;

import dev.tjpraschunus.aoc2024.Day;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static java.util.Map.entry;

public class Day6 extends Day {

    private final List<List<String>> maze;
    private int startingPosRow;
    private int startingPosCol;
    private List<List<String>> traversedMaze;

    public Day6() {
        super(2024, 6);
        maze = new ArrayList<>();
    }

    @Override
    public String partOne() {
        BufferedReader reader;
        reader = readInput("6");
        int rowCount = 0;
        try {
            for (String line; (line = reader.readLine()) != null; ) {
                if (line.contains("^")) {
                    startingPosRow = rowCount;
                    startingPosCol = line.indexOf("^");
                }
                maze.add(Arrays.asList(line.split("")));
                rowCount++;
            }
        } catch (IOException exc) {
            System.out.println("Could not read from input file.");
        }
        List<List<String>> guardsMaze = new ArrayList<>();
        for (List<String> row : maze) {
            guardsMaze.add(new ArrayList<>(List.copyOf(row)));
        }
        Guard mazeGuard = new Guard("^", guardsMaze, startingPosRow, startingPosCol);
        mazeGuard.patrol();
        int visited = 0;
        traversedMaze = mazeGuard.getMaze();
        for (List<String> row : mazeGuard.getMaze()) {
            for (String col : row) {
                if (col.equals("X")) {
                    visited++;
                }
            }
        }
        return Integer.toString(visited);
    }

    @Override
    public String partTwo() {
        int numLocations = 0;
        for (int i=0; i < maze.size(); i++) {
            for (int j=0; j < maze.getFirst().size(); j++) {
                if ((i == startingPosRow) & (j == startingPosCol)) {
                    continue;
                }
                if (maze.get(i).get(j).equals("#")) {
                    continue;
                }
                if (traversedMaze.get(i).get(j).equals("X")) {
                    List<List<String>> modifiedMaze = new ArrayList<>(List.of());
                    for (List<String> row : maze) {
                        modifiedMaze.add(new ArrayList<>(List.copyOf(row)));
                    }
                    modifiedMaze.get(i).set(j, "#");
                    Guard mazeGuard = new Guard("^", modifiedMaze, startingPosRow, startingPosCol);
                    if (mazeGuard.loopingPatrol()) {
                        numLocations++;
                    }
                }
            }
        }
        return Integer.toString(numLocations);
    }

    private class Guard {

        private String direction;
        private List<List<String>> maze;
        private int currentRow;
        private int currentCol;
        private HashMap<String, HashMap<Integer, Set<Integer>>> hitObstacles;

        public Guard(String direction, List<List<String>> maze, int startingRow, int startingCol) {
            this.direction = direction;
            currentRow = startingRow;
            currentCol = startingCol;
            this.maze = maze;
            hitObstacles = new HashMap<>(Map.ofEntries(
                    entry("^", new HashMap<>()),
                    entry(">", new HashMap<>()),
                    entry("v", new HashMap<>()),
                    entry("<", new HashMap<>())
            ));
        }

        public List<List<String>> getMaze() {
            return this.maze;
        }

        public boolean loopingPatrol() {
            while (true) {
                this.move();
                if (!this.inbounds()) {
                    return false;
                }
                if (this.checkLoop()) {
                    return true;
                }
            }
        }

        private boolean checkLoop() {
            switch (direction) {
                case "^":
                    if (currentRow == 0) {
                        return false;
                    }
                    if (this.maze.get(currentRow - 1).get(currentCol).equals("#") & hitObstacles.get(direction).containsKey(currentRow - 1)) {
                        return hitObstacles.get(direction).get(currentRow - 1).contains(currentCol);
                    }
                    return false;
                case ">":
                    if (currentCol == this.maze.getFirst().size()-1) {
                        return false;
                    } else if (this.maze.get(currentRow).get(currentCol+1).equals("#") & hitObstacles.get(direction).containsKey(currentRow)){
                        return hitObstacles.get(direction).get(currentRow).contains(currentCol+1);
                    } else {
                        return false;
                    }
                case "v":
                    if (currentRow == this.maze.size()-1) {
                        return false;
                    } else if (this.maze.get(currentRow+1).get(currentCol).equals("#") & hitObstacles.get(direction).containsKey(currentRow+1)) {
                        return hitObstacles.get(direction).get(currentRow+1).contains(currentCol);
                    } else {
                        return false;
                    }
                default: // "<"
                    if (currentCol == 0) {
                        return false;
                    } else if (this.maze.get(currentRow).get(currentCol-1).equals("#") & hitObstacles.get(direction).containsKey(currentRow)) {
                        return hitObstacles.get(direction).get(currentRow).contains(currentCol-1);
                    } else {
                        return false;
                    }
            }
        }

        public void patrol() {
            while (true) {
                this.move();
                if (!this.inbounds()) {
                    return;
                }
            }
        }

        private void move() {
            while (true) {
                if (this.checkDirection()) {
                    switch (direction){
                        case "^":
                            currentRow--;
                            break;
                        case ">":
                            currentCol++;
                            break;
                        case "v":
                            currentRow++;
                            break;
                        default: // "<"
                            currentCol--;
                            break;
                    }
                    break;
                } else {
                    this.turn();
                }
            }
            this.mark();
        }

        private boolean checkDirection() {
            return switch (direction) {
                case "^" -> {
                    if (currentRow == 0) {
                        yield true;
                    } else if (this.maze.get(currentRow - 1).get(currentCol).equals("#")) {
                        this.addHit(currentRow-1, currentCol);
                        yield false;
                    }
                    yield true;
                }
                case ">" -> {
                    if (currentCol == this.maze.getFirst().size() - 1) {
                        yield true;
                    } else if (this.maze.get(currentRow).get(currentCol + 1).equals("#")) {
                        this.addHit(currentRow, currentCol+1);
                        yield false;
                    }
                    yield true;
                }
                case "v" -> {
                    if (currentRow == this.maze.size() - 1) {
                        yield true;
                    } else if (this.maze.get(currentRow + 1).get(currentCol).equals("#")) {
                        this.addHit(currentRow+1, currentCol);
                        yield false;
                    }
                    yield true;
                }
                default -> { // "<"
                    if (currentCol == 0) {
                        yield true;
                    } else if (this.maze.get(currentRow).get(currentCol - 1).equals("#")) {
                        this.addHit(currentRow, currentCol-1);
                        yield false;
                    }
                    yield true;
                }
            };
        }

        private void mark() {
            switch (direction){
                case "^":
                    this.maze.get(currentRow+1).set(currentCol, "X");
                    break;
                case ">":
                    this.maze.get(currentRow).set(currentCol-1, "X");
                    break;
                case "v":
                    this.maze.get(currentRow-1).set(currentCol, "X");
                    break;
                default: // "<"
                    this.maze.get(currentRow).set(currentCol+1, "X");
                    break;
            }
        }

        private void turn() {
            switch (direction){
                case "^":
                    direction = ">";
                    break;
                case ">":
                    direction = "v";
                    break;
                case "v":
                    direction = "<";
                    break;
                default: // "<"
                    direction = "^";
                    break;
            }
        }

        private void addHit(int row, int col) {
            if (hitObstacles.get(direction).containsKey(row)) {
                hitObstacles.get(direction).get(row).add(col);
            } else {
                hitObstacles.get(direction).put(row, new HashSet<>());
                hitObstacles.get(direction).get(row).add(col);
            }
        }

        private boolean inbounds() {
            if ((currentRow == this.maze.size()) | (currentRow < 0)) {
                return false;
            }
            return !((currentCol == this.maze.getFirst().size()) | (currentCol < 0));
        }

        private void printMaze() {
            try {
                PrintWriter writer = new PrintWriter("day6_output.txt", StandardCharsets.UTF_8);
                for(List<String> row : this.maze) {
                    writer.println(String.join("", row));
                }
                writer.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
