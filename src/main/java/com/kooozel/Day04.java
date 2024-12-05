package com.kooozel;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.kooozel.utils.FileUtils;
import com.kooozel.utils.InputType;
import com.kooozel.utils.Point;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static com.kooozel.utils.Direction.getAllDirections;

@RequiredArgsConstructor
@Slf4j
public class Day04 implements Day {

    private final FileUtils fileUtils;
    private Map<Point, Character> parseFile(InputType inputType) {
        String fileContent = fileUtils.readString(inputType, Day04.class);
        return fileUtils.getGridMap(fileContent);
    }

    @Override
    public String solveA(InputType inputType) {
        var gridMap = parseFile(inputType);
        var wordCount = new AtomicInteger();

        gridMap.forEach((currentPoint, currentValue) -> {
            if (isMatchingTarget(gridMap, currentPoint, 'X')) {
                findMatchingWords(gridMap, currentPoint, wordCount);
            }
        });

        return wordCount.toString();
    }

    private boolean isMatchingTarget(Map<Point, Character> gridMap, Point point, Character target) {
        return target.equals(gridMap.get(point));
    }

    private void findMatchingWords(Map<Point, Character> gridMap, Point startPoint, AtomicInteger wordCount) {
        getAllDirections().forEach(direction -> {
            if (matchesWord(gridMap, startPoint, direction)) {
                wordCount.incrementAndGet();
            }
        });
    }

    private boolean matchesWord(Map<Point, Character> gridMap, Point startPoint, Point direction) {
        return isMatchingTarget(gridMap, startPoint.plus(direction), 'M')
            && isMatchingTarget(gridMap, startPoint.plus(direction, 2), 'A')
            && isMatchingTarget(gridMap, startPoint.plus(direction, 3), 'S');
    }

    @Override
    public String solveB(InputType inputType) {
        var gridMap = parseFile(inputType);
        var a = gridMap.entrySet().stream().filter(entry -> entry.getValue().equals('A')).toList();
        var target = List.of('M', 'A', 'S');
        AtomicInteger words = new AtomicInteger();
        a.forEach(point -> {
                var startPoint = point.getKey();
                var diagonal1 = List.of(
                    gridMap.getOrDefault(startPoint.plus(new Point(-1, -1)), '.'),
                    point.getValue(),
                    gridMap.getOrDefault(startPoint.plus(new Point(1, 1)), '.'));
                var diagonal2 = List.of(
                    gridMap.getOrDefault(startPoint.plus(new Point(-1, 1)), '.'),
                    point.getValue(),
                    gridMap.getOrDefault(startPoint.plus(new Point(1, -1)), '.'));

                if (diagonal1.containsAll(target) && diagonal2.containsAll(target)) {
                    words.getAndIncrement();
                }
            }
        );
        return words.toString();
    }
}
