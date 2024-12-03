package com.kooozel;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

import com.kooozel.utils.FileUtils;
import com.kooozel.utils.InputType;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Day02 implements Day {

    private final FileUtils fileUtils;

    private List<List<Integer>> parseInput(InputType inputType) {
        return fileUtils.readLines(inputType, Day02.class).stream()
            .map(line -> Arrays.stream(line.trim().split("\\s+"))
                .map(Integer::valueOf)
                .toList())
            .toList();
    }

    public String solveA(InputType inputType) {
        return String.valueOf(parseInput(inputType).stream()
            .filter(this::isValid)
            .count());
    }

    private boolean isValid(List<Integer> integers) {
        // Check if sorted ascending or descending
        var isSorted = integers.equals(integers.stream().sorted().toList()) ||
            integers.equals(integers.stream().sorted(Comparator.reverseOrder()).toList());
        if (!isSorted) {
            return false;
        }

        // Validate consecutive differences
        return IntStream.range(0, integers.size() - 1)
            .allMatch(i -> {
                int diff = Math.abs(integers.get(i) - integers.get(i + 1));
                return diff >= 1 && diff <= 3;
            });
    }

    public String solveB(InputType inputType) {
        return String.valueOf(parseInput(inputType).stream()
            .mapToInt(line -> isValid(line) ||
                IntStream.range(0, line.size())
                    .anyMatch(i -> isValid(removeElement(line, i))) ? 1 : 0)
            .sum());
    }

    private List<Integer> removeElement(List<Integer> list, int index) {
        return IntStream.range(0, list.size())
            .filter(i -> i != index)
            .mapToObj(list::get)
            .toList();
    }
}
