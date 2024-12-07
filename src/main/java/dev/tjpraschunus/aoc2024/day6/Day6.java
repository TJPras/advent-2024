package dev.tjpraschunus.aoc2024.day6;

import dev.tjpraschunus.aoc2024.Day;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;

public class Day6 extends Day {

    private final List<List<String>> maze;
    private int startingPosRow;
    private int startingPosCol;

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
        Guard mazeGuard = new Guard("^", List.copyOf(maze), startingPosRow, startingPosCol);
        mazeGuard.patrol();
        int visited = 0;
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
                List<List<String>> modifiedMaze = List.copyOf(maze);
                modifiedMaze.get(i).set(j, "#");
                Guard mazeGuard = new Guard("^", modifiedMaze, startingPosRow, startingPosCol);
                if (mazeGuard.loopingPatrol()) {
                    numLocations++;
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
        private HashMap<String, HashMap<Integer, Integer>> hitObstacles;

        public Guard(String direction, List<List<String>> maze, int startingRow, int startingCol) {
            this.direction = direction;
            currentRow = startingRow;
            currentCol = startingCol;
            this.maze = maze;
        }

        public List<List<String>> getMaze() {
            return maze;
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
                    } else {
                        return maze.get(currentRow-1).get(currentCol).equals("O");
                    }
                case ">":
                    if (currentCol == maze.getFirst().size()-1) {
                        return false;
                    } else {
                        return maze.get(currentRow).get(currentCol+1).equals("O");
                    }
                case "v":
                    if (currentRow == maze.size()-1) {
                        return false;
                    } else {
                        return maze.get(currentRow+1).get(currentCol).equals("O");
                    }
                default: // "<"
                    if (currentCol == 0) {
                        return true;
                    } else {
                        return maze.get(currentRow).get(currentCol-1).equals("O");
                    }
            }
        }

        public boolean pathTraveled() {
            int row = currentRow;
            int col = currentCol;
            switch (direction) {
                case "^":
                    while (true) {
                        if (row <= 0) {
                            return false;
                        } else if (maze.get(row).get(currentCol).equals("#")) {
                            return true;
                        } else if (maze.get(row).get(currentCol).equals("X")) {
                            row--;
                        } else {
                            return false;
                        }
                    }
                case ">":
                    while (true) {
                        if (col >= maze.getFirst().size()-1) {
                            return false;
                        } else if (maze.get(currentRow).get(col).equals("#")) {
                            return true;
                        } else if (maze.get(currentRow).get(col).equals("X")) {
                            col++;
                        } else {
                            return false;
                        }
                    }
                case "v":
                    while (true) {
                        if (row >= maze.size()-1) {
                            return false;
                        } else if (maze.get(row).get(currentCol).equals("#")) {
                            return true;
                        } else if (maze.get(row).get(currentCol).equals("X")) {
                            row++;
                        } else {
                            return false;
                        }
                    }
                default: // "<"
                    while (true) {
                        if (col <= 0) {
                            return false;
                        } else if (maze.get(currentRow).get(col).equals("#")) {
                            return true;
                        } else if (maze.get(currentRow).get(col).equals("X")) {
                            col--;
                        } else {
                            return false;
                        }
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
                    } else if (maze.get(currentRow - 1).get(currentCol).equals("#") | maze.get(currentRow - 1).get(currentCol).equals("O")) {
                        maze.get(currentRow - 1).set(currentCol, "O");
                        yield false;
                    }
                    yield true;
                }
                case ">" -> {
                    if (currentCol == maze.getFirst().size() - 1) {
                        yield true;
                    } else if (maze.get(currentRow).get(currentCol + 1).equals("#") | maze.get(currentRow).get(currentCol + 1).equals("O")) {
                        maze.get(currentRow).set(currentCol + 1, "O");
                        yield false;
                    }
                    yield true;
                }
                case "v" -> {
                    if (currentRow == maze.size() - 1) {
                        yield true;
                    } else if (maze.get(currentRow + 1).get(currentCol).equals("#") | maze.get(currentRow + 1).get(currentCol).equals("O")) {
                        maze.get(currentRow + 1).set(currentCol, "O");
                        yield false;
                    }
                    yield true;
                }
                default -> {
                    if (currentCol == 0) {
                        yield true;
                    } else if (maze.get(currentRow).get(currentCol - 1).equals("#") | maze.get(currentRow).get(currentCol - 1).equals("O")) {
                        maze.get(currentRow).set(currentCol - 1, "O");
                        yield false;
                    }
                    yield true;
                }
            };
        }

        private void mark() {
            switch (direction){
                case "^":
                    maze.get(currentRow+1).set(currentCol, "X");
                    break;
                case ">":
                    maze.get(currentRow).set(currentCol-1, "X");
                    break;
                case "v":
                    maze.get(currentRow-1).set(currentCol, "X");
                    break;
                default: // "<"
                    maze.get(currentRow).set(currentCol+1, "X");
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

        private boolean inbounds() {
            if ((currentRow == maze.size()) | (currentRow < 0)) {
                return false;
            }
            return !((currentCol == maze.getFirst().size()) | (currentCol < 0));
        }
    }
}
