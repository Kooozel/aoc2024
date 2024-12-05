package com.kooozel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import com.kooozel.utils.FileUtils;
import com.kooozel.utils.InputType;
import com.kooozel.utils.Pair;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Day05 implements Day {
    private final FileUtils fileUtils;

    private String[] parseFile(InputType inputType) {
        return fileUtils.readString(inputType, Day05.class).split("\n\n");
    }

    private List<Pair<Integer, Integer>> getRules(String input) {
        return Arrays.stream(input.split("\n"))
            .map(el -> el.split("\\|"))
            .map(parts -> new Pair<>(Integer.parseInt(parts[0]), Integer.parseInt(parts[1])))
            .toList();
    }

    public boolean isValid(List<Pair<Integer, Integer>> rules, List<Integer> update) {
        List<Integer> reversedList = new ArrayList<>(update);
        Collections.reverse(reversedList);

        Set<Integer> controlSet = new HashSet<>();
        for (int number : reversedList) {
            List<Integer> filteredKeys = rules.stream()
                .filter(rule -> Objects.equals(rule.value(), number))
                .map(Pair::key)
                .toList();

            if (filteredKeys.stream().anyMatch(controlSet::contains)) {
                return false;
            }
            controlSet.add(number);
        }
        return true;
    }

    private Integer getMiddleNumber(List<Integer> input) {
        return input.get(input.size()/2);
    }

    @Override
    public String solveA(InputType inputType) {
        var input = parseFile(inputType);
        var pageOrderRules = getRules(input[0]);
        var update = Arrays.stream(input[1].split("\n")).map(line -> Arrays.stream(line.split(",")).map(Integer::parseInt).toList())
            .filter(order -> isValid(pageOrderRules, order)).map(this::getMiddleNumber).reduce(0, Integer::sum);
        return String.valueOf(update);
    }

    @Override
    public String solveB(InputType inputType) {
        var input = parseFile(inputType);
        var pageOrderRules = getRules(input[0]);

        var update = Arrays.stream(input[1].split("\n")).map(line -> Arrays.stream(line.split(",")).map(Integer::parseInt).toList())
            .filter(order -> !isValid(pageOrderRules, order)).map(i -> sortedByRules(pageOrderRules, i)).map(this::getMiddleNumber).reduce(0, Integer::sum);
        return String.valueOf(update);
    }

    public List<Integer> sortedByRules(List<Pair<Integer, Integer>> rules, List<Integer> update) {
            List<Integer> controlSet = new ArrayList<>();
            for (int number : update) {
                var index = rules.stream()
                    .filter(rule -> Objects.equals(rule.key(), number))
                    .map(Pair::value)
                    .map(controlSet::indexOf)
                    .filter(i -> i != -1)
                    .min(Integer::compare)
                    .orElse(-1);

                if (index != -1) {
                    controlSet.add(index, number); // Insert before the existing related value
                } else {
                    controlSet.add(number); // Append to the end
                }

            }
            return controlSet;
    }
}
