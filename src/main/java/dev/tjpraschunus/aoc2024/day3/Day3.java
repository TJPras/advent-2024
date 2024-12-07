package dev.tjpraschunus.aoc2024.day3;

import dev.tjpraschunus.aoc2024.Day;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day3 extends Day{

    public Day3() {
        super(2024, 3);
    }

    @Override
    public String partOne() {
        BufferedReader reader;
        reader = readInput("3");
        Pattern instructionPattern = Pattern.compile("mul\\([0-9]{1,3},[0-9]{1,3}\\)");
        Pattern numberPattern = Pattern.compile("[0-9]{1,3}");
        int instructionTotal = 0;
        try {
            for (String line; (line = reader.readLine()) != null; ) {
                Matcher instructionMatcher = instructionPattern.matcher(line);
                while(instructionMatcher.find()) {
                    Matcher numberMatcher = numberPattern.matcher(instructionMatcher.group());
                    numberMatcher.find();
                    int operandOne = Integer.parseInt(numberMatcher.group());
                    numberMatcher.find();
                    int operandTwo = Integer.parseInt(numberMatcher.group());
                    instructionTotal+= operandOne * operandTwo;
                }
            }
        } catch (IOException exc) {
            System.out.println("Could not read from input file.");
        }
        return Integer.toString(instructionTotal);
    }

    @Override
    public String partTwo() {
        BufferedReader reader;
        reader = readInput("3");
        Pattern instructionPattern = Pattern.compile("(do\\(\\))|(don't\\(\\))|(mul\\([0-9]{1,3},[0-9]{1,3}\\))");
        Pattern numberPattern = Pattern.compile("[0-9]{1,3}");
        int instructionTotal = 0;
        boolean instructionsEnabled = true;
        String enableString = "do()";
        String disableString = "don't()";
        try {
            for (String line; (line = reader.readLine()) != null; ) {
                Matcher instructionMatcher = instructionPattern.matcher(line);
                while(instructionMatcher.find()) {
                    if (instructionMatcher.group().equals(enableString)) {
                        instructionsEnabled = true;
                        continue;
                    } else if (instructionMatcher.group().equals(disableString)) {
                        instructionsEnabled = false;
                        continue;
                    }
                    if (instructionsEnabled) {
                        Matcher numberMatcher = numberPattern.matcher(instructionMatcher.group());
                        numberMatcher.find();
                        int operandOne = Integer.parseInt(numberMatcher.group());
                        numberMatcher.find();
                        int operandTwo = Integer.parseInt(numberMatcher.group());
                        instructionTotal += operandOne * operandTwo;
                    }
                }
            }
        } catch (IOException exc) {
            System.out.println("Could not read from input file.");
        }
        return Integer.toString(instructionTotal);
    }
}
