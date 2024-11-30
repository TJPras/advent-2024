package dev.tjpraschunus.aoc2024;

public abstract class Day {

    private final int year;
    private final int day;

    public Day(int year, int day) {
        this.year = year;
        this.day = day;
    }

    public abstract String partOne();

    public abstract String partTwo();

    public final void run() {
        long start = System.currentTimeMillis();
        System.out.printf("Year %d, Day %d%n", year, day);
        System.out.printf("| Part 1: %s ", partOne());
        long part1Elapsed = System.currentTimeMillis() - start;
        System.out.printf("(%dms)%n", part1Elapsed);
        System.out.printf("| Part 2: %s ", partTwo());
        long part2Elapsed = System.currentTimeMillis() - start - part1Elapsed;
        System.out.printf("(%dms)%n", part2Elapsed);
        System.out.printf("| Total time: %dms%n", part1Elapsed + part2Elapsed);
    }
}