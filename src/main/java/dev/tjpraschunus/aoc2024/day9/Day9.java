package dev.tjpraschunus.aoc2024.day9;

import dev.tjpraschunus.aoc2024.Day;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;

import static java.util.Collections.swap;

public class Day9 extends Day {

    int[] diskMap;
    List<List<String>> files;
    List<List<String>> freeSpaces;

    public Day9() {
        super(2024, 9);
    }

    @Override
    public String partOne() {
        BufferedReader reader;
        reader = readInput("9");
        String text;
        try {
            text = reader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        diskMap = Arrays.stream(text.split("")).mapToInt(Integer::parseInt).toArray();
        files = new ArrayList<>();
        freeSpaces = new ArrayList<>();
        int idx = 0;
        for (int i=0; i < diskMap.length; i+=2) {
            List<String> newFile = new ArrayList<>();
            for (int j=0; j < diskMap[i]; j++) {
                newFile.add(Integer.toString(idx));
            }
            files.add(newFile);
            idx++;
        }
        for (int i=1; i < diskMap.length; i+=2) {
            List<String> newFreeSpace = new ArrayList<>();
            for (int j=0; j < diskMap[i]; j++) {
                newFreeSpace.add(".");
            }
            freeSpaces.add(newFreeSpace);
        }
        List<String> fileSystem = new ArrayList<>();
        int i = 0;
        boolean moreFrees = true;
        boolean moreFiles = true;
        while (moreFrees | moreFiles) {
            if (i < files.size()) {
                fileSystem.addAll(files.get(i));
            } else {
                moreFiles = false;
            }
            if (i < freeSpaces.size()) {
                fileSystem.addAll(freeSpaces.get(i));
            } else {
                moreFrees = false;
            }
            i++;
        }
        int front = 0;
        int back = fileSystem.size()-1;
        while (front < back) {
            if (fileSystem.get(front).equals(".")) {
                while(fileSystem.get(back).equals(".")) {
                    back--;
                }
                if (front < back) {
                    swap(fileSystem, front, back);
                }
            }
            front++;
        }
        long checksum = 0;
        for (int j=0; j < fileSystem.size(); j++) {
            if (fileSystem.get(j).equals(".")) {
                continue;
            }
            checksum += Long.parseLong(fileSystem.get(j)) * j;
        }
        return Long.toString(checksum);
    }

    @Override
    public String partTwo() {
        List<String> fileSystem = new ArrayList<>();
        int i = 0;
        boolean moreFrees = true;
        boolean moreFiles = true;

        // Keep track of starting index of contiguous free blocks and how much space is available
        Map<Integer, Integer> freeSpaceIndex = new TreeMap<>();
        Map<Integer, List<String>> fileIndex = new TreeMap<>();
        while (moreFrees | moreFiles) {
            if (i < files.size()) {
                fileIndex.put(fileSystem.size(), files.get(i));
                fileSystem.addAll(files.get(i));
            } else {
                moreFiles = false;
            }
            if (i < freeSpaces.size()) {
                if (!freeSpaces.get(i).isEmpty()) {
                    freeSpaceIndex.put(fileSystem.size(), freeSpaces.get(i).size());
                }
                fileSystem.addAll(freeSpaces.get(i));
            } else {
                moreFrees = false;
            }
            i++;
        }
        Set<Map.Entry<Integer, List<String>>> fileSet = fileIndex.entrySet();
        // Ordered list of entries mapping file starting index in filesystem to the file string
        List<Map.Entry<Integer, List<String>>> fileList = new ArrayList<>(fileSet);

        // For each File Entry starting from the back
        for (int back=fileList.size()-1; back >=0; back--) {
            // Iterate through all contiguous blocks of free spaces in the filesystem
            for (Map.Entry<Integer, Integer> entry : freeSpaceIndex.entrySet()) {
                // Free space must come before file
                if (entry.getKey() > fileList.get(back).getKey()) {
                    break;
                }
                // If free space has enough space to fit entire file
                if (entry.getValue() >= fileList.get(back).getValue().size()) {
                    // We must swap one free space character with each file character in filesystem
                    int startingIndex = entry.getKey();
                    int swapIndex = fileList.get(back).getKey();
                    for (int j=0; j < fileList.get(back).getValue().size(); j++) {
                        swap(fileSystem, startingIndex+j, swapIndex+j);
                    }
                    // Remove the free space entry if no blocks are left
                    if (entry.getValue() == fileList.get(back).getValue().size()) {
                        freeSpaceIndex.remove(entry.getKey());
                    } else {
                        // Reduce contiguous block of free space by size of file and update index
                        freeSpaceIndex.put(entry.getKey()+fileList.get(back).getValue().size(), entry.getValue() - fileList.get(back).getValue().size());
                        freeSpaceIndex.remove(entry.getKey());
                    }
                    // Add new free space block equivalent to the size of the file
                    freeSpaceIndex.put(fileList.get(back).getKey(), fileList.get(back).getValue().size());
                    break;
                }
            }
        }
        long checksum = 0;
        for (int j=0; j < fileSystem.size(); j++) {
            if (fileSystem.get(j).equals(".")) {
                continue;
            }
            checksum += Long.parseLong(fileSystem.get(j)) * j;
        }
        return Long.toString(checksum);
    }
}
