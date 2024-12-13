package com.kooozel;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import com.kooozel.utils.FileUtils;
import com.kooozel.utils.InputType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class Day11 implements Day {

    private final FileUtils fileUtils;

    private String parseFile(InputType inputType) {
        return fileUtils.readString(inputType, Day11.class);
    }

    private Map<Long, Long> mapToHashMap(String input) {
        return Arrays.stream(input.split(" "))
            .map(Long::parseLong)
            .collect(Collectors.groupingBy(
                type -> type,
                Collectors.summingLong(e -> 1)
            ));
    }

    private HashMap<Long, Long> blink(Map<Long, Long> map) {
        var seen = new HashMap<>(map);
        for (Long stone: map.keySet()) {
            long i = map.get(stone);
            if (stone == 0L) {
                seen.compute(1L, (key, value) -> value == null ? i : value + i);
            } else if (stone.toString().length() % 2 == 0) {
                var split = stone.toString().length() / 2;
                var part1 = Long.parseLong(stone.toString().substring(0, split));
                var part2 = Long.parseLong(stone.toString().substring(split));
                seen.compute(part1, (key, value) -> value == null ? i : value + i);
                seen.compute(part2, (key, value) -> value == null ? i : value + i);
            } else {
                seen.compute(stone * 2024, (key, value) -> value == null ? i : value + i);
            }
        }
        map.keySet().forEach(el -> {
            if (seen.containsKey(el)) {
                seen.compute(el, (key, value) -> value - map.get(el));
            }
        });
        return seen;
    }

    @Override
    public String solveA(InputType inputType) {
        var input = parseFile(inputType);
        var map = mapToHashMap(input);

        for (int i = 0; i < 75; i++) {
                map = blink(map);
        }

        var sum = map.values().stream().reduce(0L, Long::sum);
        return String.valueOf(sum);
    }

    @Override
    public String solveB(InputType inputType) {
        return "";
    }

    @Getter
    @AllArgsConstructor
    private class Stone {

        private Type type;
        private Long value;
    }

    private enum Type {
        ZERO,
        ONE,
        EVEN;

        public static Type from(String input) {
            return switch (input) {
                case "0" -> ZERO;
                case "1" -> ONE;
                default -> EVEN;
            };
        }
    }
}
