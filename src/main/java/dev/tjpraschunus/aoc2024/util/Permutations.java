package dev.tjpraschunus.aoc2024.util;

import java.util.ArrayList;
import java.util.List;

public class Permutations {

    public static List<String> permutation(String str, int stringLength) {
        return permutation("", str, stringLength);
    }

    private static List<String> permutation(String prefix, String str, int stringLength) {
        List<String> permutations = new ArrayList<>();
        if (stringLength == 0) {
            permutations.add(prefix);
        } else {
            for (int i = 0; i < str.length(); i++) {
                permutations.addAll(permutation(prefix + str.charAt(i), str, stringLength - 1));
            }
        }
        return permutations;
    }
}
