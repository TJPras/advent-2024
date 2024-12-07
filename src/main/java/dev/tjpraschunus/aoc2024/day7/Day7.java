package dev.tjpraschunus.aoc2024.day7;

import dev.tjpraschunus.aoc2024.Day;
import dev.tjpraschunus.aoc2024.util.Permutations;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day7 extends Day {

    private List<Long> allTotals;
    private List<Long> incorrectTotals;
    private List<Long> correctTotals;

    private List<List<Long>> allOperands;
    private List<List<Long>> incorrectOperands;

    public Day7() {
        super(2024, 7);
        allTotals = new ArrayList<>();
        incorrectTotals = new ArrayList<>();
        correctTotals = new ArrayList<>();
        allOperands = new ArrayList<>();
        incorrectOperands = new ArrayList<>();
    }

    @Override
    public String partOne() {
        BufferedReader reader;
        reader = readInput("7");
        try {
            for (String line; (line = reader.readLine()) != null; ) {
                String[] totalAndOperands = line.split(": ");
                allTotals.add(Long.valueOf(totalAndOperands[0]));
                List<Long> nextOperands = new ArrayList<>();
                for (String operand : totalAndOperands[1].split(" ")) {
                    nextOperands.add(Long.valueOf(operand));
                }
                allOperands.add(nextOperands);
            }
        } catch (IOException exc) {
            System.out.println("Could not read from input file.");
        }
        for (int i=0; i < allTotals.size(); i++) {
            List<String> operatorPermutations = Permutations.permutation("+*", allOperands.get(i).size()-1);
            if (this.solve(allTotals.get(i), allOperands.get(i), operatorPermutations)) {
                correctTotals.add(allTotals.get(i));
            } else {
                incorrectTotals.add(allTotals.get(i));
                incorrectOperands.add(allOperands.get(i));
            }
        }
        return Long.toString(correctTotals.stream().mapToLong(Long::longValue).sum());
    }

    @Override
    public String partTwo() {
        for (int i=0; i < incorrectTotals.size(); i++) {
            List<String> operatorPermutations = Permutations.permutation("+*|", incorrectOperands.get(i).size()-1);
            if (this.solve(incorrectTotals.get(i), incorrectOperands.get(i), operatorPermutations)) {
                correctTotals.add(incorrectTotals.get(i));
            }
        }
        return Long.toString(correctTotals.stream().mapToLong(Long::longValue).sum());
    }

    private boolean solve(Long total, List<Long> operands, List<String> operators) {
        for (String operatorSet : operators) {
            if (solveRecursive(operands, List.of(operatorSet.split(""))) == total) {
                return true;
            }
        }
        return false;
    }

    private long solveRecursive(List<Long> operands, List<String> operators) {
        if (operands.size() == 1) {
            return operands.getFirst();
        }
        long result = compute(operands.get(0), operands.get(1), operators.getFirst());
        List<Long> recursiveOperands = new ArrayList<>(operands.subList(2, operands.size()));
        recursiveOperands.addFirst(result);
        return solveRecursive(recursiveOperands, operators.subList(1, operators.size()));
    }

    private long compute(long operand1, long operand2, String operator) {
        return switch (operator) {
            case "+" -> operand1 + operand2;
            case "|" -> Long.parseLong(Long.toString(operand1) + Long.toString(operand2));
            default -> // "*"
                    operand1 * operand2;
        };
    }
}
