package dev.tjpraschunus.aoc2024;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class AdventOfCode2024 {

    public static void main(String[] args) {
        new AdventOfCode2024().run(args[0]);
    }

    public void run(String dayNumber) {

        try {
            Class<?> dayClass = Class.forName("dev.tjpraschunus.aoc2024.day" + dayNumber + ".Day" + dayNumber);
            Constructor<?> constructor = dayClass.getConstructor();

            Day day = (Day) constructor.newInstance();
            day.run();
        } catch (ClassNotFoundException | IllegalAccessException | InvocationTargetException | NoSuchMethodException |
                 InstantiationException e) {
            System.out.printf("Could not instantiate class Day%s%n", dayNumber);
        }
    }
}