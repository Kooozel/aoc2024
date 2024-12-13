package com.kooozel;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

import com.kooozel.utils.FileUtils;
import com.kooozel.utils.InputType;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Day13 implements Day {
    private final FileUtils fileUtils;

    private List<ClawMachine> parseFile(InputType inputType) {
        return Arrays.stream(fileUtils.readString(inputType, Day13.class).split("\n\n")).map(string -> {
            var list = Arrays.stream(string.split("\n")).map(part -> {
                return part.split(":")[1].trim();
            }).toList();
            return new ClawMachine(new Button(list.get(0)), new Button(list.get(1)), new Button(list.get(2)));

        }).toList();
    }


    @Override
    public String solveA(InputType inputType) {
        var input = parseFile(inputType);
        var sum = input.stream().map(ClawMachine::solveEquations).filter(Objects::nonNull).reduce(0L, Long::sum);
        return String.valueOf(sum);
    }

    @Override
    public String solveB(InputType inputType) {
        return "";
    }

    @AllArgsConstructor
    public class ClawMachine {
        private Button buttonA;
        private Button buttonB;
        private Button prize;

        public Long solveEquations() {
            var a1 = buttonA.x;
            var a2 = buttonA.y;
            var b1 = buttonB.x;
            var b2 = buttonB.y;
            var c1 = prize.x + 10000000000000L;
            var c2 = prize.y + 10000000000000L;

            double determinant = a1 * b2 - a2 * b1;
            if (determinant == 0) {
                return null;
            }

            double x = (c1 * b2 - b1 * c2) / determinant;
            double y = (a1 * c2 - c1 * a2) / determinant;

            if (x % 1 != 0 || y % 1 != 0) {
                return null;
            }

//            if (x > 100 || y > 100) {
//                return null;
//            }

            if (x < 0 || y < 0) {
                return null;
            }


          return (long) ((long) 3 * x + y);
        }

    }

    @AllArgsConstructor
    public class Button {
        private Long x;
        private Long y;

        public Button(String button) {
            var input = Pattern.compile("\\d+")
                .matcher(button)
                .results()  // Stream<MatchResult>
                .map(MatchResult::group)
                .map(Long::parseLong)// Get the matched string
                .toList();
            this.x = input.get(0);
            this.y = input.get(1);
        }
    }

    public Button parseButton(String button) {
        var input = Pattern.compile("\\d+")
            .matcher(button)
            .results()  // Stream<MatchResult>
            .map(MatchResult::group)
            .map(Long::parseLong)// Get the matched string
            .toList();
        return new Button(input.get(0), input.get(1));
    }
}
