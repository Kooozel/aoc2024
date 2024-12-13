package com.kooozel;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.kooozel.utils.FileUtils;
import com.kooozel.utils.InputType;
import com.kooozel.utils.Point;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Day08 implements Day {

    private final FileUtils fileUtils;

    private Map<Point, Character> parseFile(InputType inputType) {
        return fileUtils.getGridMap(fileUtils.readString(inputType, Day08.class));
    }

    private Set<Character> getUniqueChars(Map<Point, Character> input) {
        return new HashSet<>(input.values()).stream().filter(el -> !el.equals('.')).collect(Collectors.toSet());
    }

    @Override
    public String solveA(InputType inputType) {
        var input = parseFile(inputType);
        var setChars = getUniqueChars(input);
        var controlSet = new HashSet<Point>();
        for (Character c: setChars) {
            var points = input.entrySet().stream().filter(el -> el.getValue() == c).map(Map.Entry::getKey).toList();
           controlSet.addAll(getPointsOnLine(points, input).keySet());
        }
        return String.valueOf(controlSet.size());
    }

    @Override
    public String solveB(InputType inputType) {
        return "";
    }

    public Map<Point, Character> getPointsOnLine(List<Point> linePoints, Map<Point, Character> grid) {
        if (linePoints.size() < 2) {
            throw new IllegalArgumentException("At least two points are needed to define lines.");
        }

        return grid.entrySet()
            .stream()
            .filter(entry -> {
                Point point = entry.getKey();
                for (int i = 0; i < linePoints.size(); i++) {
                    for (int j = i + 1; j < linePoints.size(); j++) {
                        if (isPointOnLine(linePoints.get(i), linePoints.get(j), point)) {
                            return true;
                        }
                    }
                }
                return false;
            })
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private boolean isDistanceTwoTimes(Point p1, Point p2, Point checked) {
        var d1 = Point.calculateManhattanDistance(p1, checked);
        var d2 = Point.calculateManhattanDistance(p2, checked);
        if (d1 > d2) {
            return d1 == d2 * 2;
        } else {
            return d2 == d1 * 2;
        }
    }

    private boolean isPointOnLine(Point p1, Point p2, Point checked) {
        var dx = p2.x - p1.x;
        var dy = p2.y - p1.y;

        return (dx * (checked.y - p1.y)) == (dy * (checked.x - p1.x));
    }
}
