package com.kooozel;

import java.util.List;
import java.util.Objects;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

import com.kooozel.utils.FileUtils;
import com.kooozel.utils.InputType;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Day03 implements Day {

    private final FileUtils fileUtils;

    private List<String> parseFile(InputType inputType, String regex) {
        return Pattern.compile(regex)
            .matcher(fileUtils.readString(inputType, Day03.class))
            .results()  // Stream<MatchResult>
            .map(MatchResult::group) // Get the matched string
            .toList();
    }

    private List<Integer> parseInstruction(String input) {
        return Pattern.compile("\\d+").matcher(input).results().map(MatchResult::group).map(Integer::valueOf).toList();
    }

    @Override
    public String solveA(InputType inputType) {
        return String.valueOf(parseFile(inputType, "mul\\(\\d+,\\d+\\)").stream()
            .map(this::parseInstruction)
            .map(list -> list.stream()
                .reduce(1, (a, b) -> a * b))
            .reduce(0, Integer::sum));
    }

    @Override
    public String solveB(InputType inputType) {
        var regex = "do\\(\\)|mul\\(\\d+,\\d+\\)|don't\\(\\)";
        var parsed = parseFile(inputType, regex);
        var isEnabled = true;
        var sum = 0;
        for (String s : parsed) {
            if (Objects.equals(s, "do()")) {
                isEnabled = true;
            } else if (Objects.equals(s, "don't()")) {
                isEnabled = false;
            } else {
                if (isEnabled) {
                    sum += parseInstruction(s).stream().reduce(1, (a, b) -> a * b);
                }
            }
        }
        return String.valueOf(sum);
    }
}
