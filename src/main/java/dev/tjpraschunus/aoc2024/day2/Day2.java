package dev.tjpraschunus.aoc2024.day2;

import dev.tjpraschunus.aoc2024.Day;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

public class Day2 extends Day {

    private List<List<Integer>> allReports;

    public Day2() {
        super(2024, 2);
    }

    @Override
    public String partOne() {
        // java.io.InputStream
        InputStream inputStream = Day2.class.getResourceAsStream("/day2.txt");
        InputStreamReader streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        BufferedReader reader = new BufferedReader(streamReader);
        List<List<Integer>> reports = new ArrayList<>();
        try {
            for (String line; (line = reader.readLine()) != null; ) {
                String[] splitLine = line.split("\\s+");
                List<Integer> report = new ArrayList<>();
                for (String s : splitLine) {
                    report.add(Integer.valueOf(s));
                }
                reports.add(report);
            }
        } catch (IOException exc) {
            System.out.println("Could not read from input file.");
        }
        allReports = reports;
        List<List<Integer>> validReports = new ArrayList<>();
        for (List<Integer> report : reports) {
            BiFunction<Integer, Integer, Boolean> comparator;
            if (report.get(0) < report.get(1)) {
                comparator = Day2::lessThan;
            } else if (report.get(0) > report.get(1)) {
                comparator = Day2::greaterThan;
            }
            else {
                continue;
            }
            int diff = Math.abs(report.get(0) - report.get(1));
            if ((diff < 1) | (diff > 3)) {
                continue;
            }
            int j = 2;
            boolean reportValid = true;
            for(int i=1; i < report.size() -1; i++) {
                if (!comparator.apply(report.get(i), report.get(j))) {
                    reportValid = false;
                    break;
                }
                diff = Math.abs(report.get(i) - report.get(j));
                if ((diff < 1) | (diff > 3)) {
                    reportValid = false;
                    break;
                }
                j++;
            }
            if (reportValid) {
                validReports.add(report);
            }
        }
        return Integer.toString(validReports.size());
    }

    public static boolean greaterThan(int a, int b) {
        return a > b;
    }

    public static boolean lessThan(int a, int b) {
        return a < b;
    }

    @Override
    public String partTwo() {
        List<List<Integer>> validReports = new ArrayList<>();
        for (List<Integer> report : allReports) {
            if (validateReport(report)) {
                validReports.add(report);
            }
        }

        return Integer.toString(validReports.size());
    }

    private boolean validateReport(List<Integer> report) {
        if (validateSubReport(report)) {
            return true;
        }
        for (int i=0; i < report.size(); i++) {
            Integer removed = report.remove(i);
            if (validateSubReport(report)) {
                return true;
            }
            report.add(i, removed);
        }
        return false;
    }

    private boolean validateSubReport(List<Integer> report) {
        BiFunction<Integer, Integer, Boolean> comparator;
        if (report.get(0) < report.get(1)) {
            comparator = Day2::lessThan;
        } else if (report.get(0) > report.get(1)) {
            comparator = Day2::greaterThan;
        }
        else {
            return false;
        }
        int diff = Math.abs(report.get(0) - report.get(1));
        if ((diff < 1) | (diff > 3)) {
            return false;
        }
        int j = 2;
        for(int i=1; i < report.size() -1; i++) {
            if (!comparator.apply(report.get(i), report.get(j))) {
                return false;
            }
            diff = Math.abs(report.get(i) - report.get(j));
            if ((diff < 1) | (diff > 3)) {
                return false;
            }
            j++;
        }
        return true;
    }
}
