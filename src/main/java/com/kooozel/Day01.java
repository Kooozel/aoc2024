package com.kooozel;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;

import com.kooozel.utils.FileUtils;
import com.kooozel.utils.InputType;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Day01 implements Day {
    private final FileUtils fileUtils;

    public String solveA(InputType inputType) {
        var input = fileUtils.readLines(inputType, Day01.class);

        var list1 = new ArrayList<Integer>();
        var list2 = new ArrayList<Integer>();

        for (String line: input) {
            var numbers = line.trim().split("\\s+");

            list1.add(Integer.parseInt(numbers[0]));
            list2.add(Integer.parseInt(numbers[1]));
        }
        var sum = 0;

        list1.sort(Comparator.naturalOrder());
        list2.sort(Comparator.naturalOrder());

        for (int i = 0; i < list1.size(); i++) {
            sum += Math.abs(list1.get(i) - list2.get(i));
        }

        return String.valueOf(sum);
    }

    public String solveB(InputType inputType) {
        var input = fileUtils.readLines(inputType, Day01.class);

        var list1 = new ArrayList<Integer>();
        var list2 = new ArrayList<Integer>();

        for (String line: input) {
            var numbers = line.trim().split("\\s+");

            list1.add(Integer.parseInt(numbers[0]));
            list2.add(Integer.parseInt(numbers[1]));
        }
        var sum = 0;

        for (Integer number:list1) {
            sum +=  list2.stream().filter(el -> Objects.equals(el, number)).toList().size() * number;
        }

        return String.valueOf(sum);
    }
}
