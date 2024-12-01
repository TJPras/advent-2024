package dev.tjpraschunus.aoc2024.util;

import java.util.List;

public class Sorting {

    public static void quickSort(List<Integer> array, Integer low, Integer high) {
        if (low < high) {
            int part = quickSortPartition(array, low, high);
            quickSort(array, low, part - 1);
            quickSort(array, part + 1, high);
        }
    }

    public static int quickSortPartition(List<Integer> array, Integer low, Integer high) {
        Integer pivot = array.get(high);
        int i = low - 1;
        for(int j=low; j<high; j++) {
            if (array.get(j) < pivot) {
                i++;
                Integer temp = array.get(i);
                array.set(i, array.get(j));
                array.set(j, temp);
            }
        }
        Integer temp = array.get(i + 1);
        array.set(i+1, array.get(high));
        array.set(high, temp);
        return i + 1;
    }
}
