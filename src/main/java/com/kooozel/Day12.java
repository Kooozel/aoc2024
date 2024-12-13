package com.kooozel;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.kooozel.utils.Direction;
import com.kooozel.utils.FileUtils;
import com.kooozel.utils.InputType;
import com.kooozel.utils.Point;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Day12 implements Day {

    private final FileUtils fileUtils;

    private Map<Point, Character> parseFile(InputType inputType) {
        return fileUtils.getGridMap(fileUtils.readString(inputType, Day12.class));
    }

    @Override
    public String solveA(InputType inputType) {
        var input = parseFile(inputType);
        var plants = findPlantsAreas(input);

       var gardens = plants.entrySet().stream()
           .flatMap(entry -> entry.getValue().stream()
               .map(i -> {
                   return new Garden(entry.getKey(), calculatePerimeter(i), i.size(), getSides(i, input));
               })
           )
           .toList();

        var price = gardens.stream().map(Garden::newPrice).reduce(0, Integer::sum);

        return String.valueOf(price);
    }

    private Integer getSides(Set<Point> points, Map<Point, Character> input) {
        var corners = 0;
        for (var point: points) {
            var directions = Direction.values();
            var groupedDirections = Arrays.stream(directions)
                .collect(Collectors.partitioningBy(direction -> {
                    var newPoint = point.plus(direction.getPoint());
                    return points.contains(newPoint);
                }));

            corners = getCorners(corners, groupedDirections, true, false);
            if (new HashSet<>(groupedDirections.get(false)).containsAll(List.of(Direction.UP, Direction.LEFT, Direction.UP_LEFT))) {
                corners++;
            }
            if (new HashSet<>(groupedDirections.get(false)).containsAll(List.of(Direction.UP, Direction.RIGHT, Direction.UP_RIGHT))) {
                corners++;
            }
            if (new HashSet<>(groupedDirections.get(false)).containsAll(List.of(Direction.DOWN, Direction.RIGHT, Direction.DOWN_RIGHT))) {
                corners++;
            }
            if (new HashSet<>(groupedDirections.get(false)).containsAll(List.of(Direction.DOWN, Direction.LEFT, Direction.DOWN_LEFT))) {
                corners++;
            }

            corners = getCorners(corners, groupedDirections, false, true);

        }
        return corners;
    }

    private int getCorners(int corners, Map<Boolean, List<Direction>> groupedDirections, boolean b, boolean b2) {
        if (new HashSet<>(groupedDirections.get(b)).containsAll(List.of(Direction.UP, Direction.LEFT))
            && groupedDirections.get(b2).contains(Direction.UP_LEFT)) {
            corners++;
        }
        if (new HashSet<>(groupedDirections.get(b)).containsAll(List.of(Direction.UP, Direction.RIGHT))
            && groupedDirections.get(b2).contains(Direction.UP_RIGHT)) {
            corners++;
        }
        if (new HashSet<>(groupedDirections.get(b)).containsAll(List.of(Direction.DOWN, Direction.LEFT))
            && groupedDirections.get(b2).contains(Direction.DOWN_LEFT)) {
            corners++;
        }
        if (new HashSet<>(groupedDirections.get(b)).containsAll(List.of(Direction.DOWN, Direction.RIGHT))
            && groupedDirections.get(b2).contains(Direction.DOWN_RIGHT)) {
            corners++;
        }
        return corners;
    }

    private Map<Character, List<Set<Point>>> findPlantsAreas(Map<Point, Character> input) {
        var plants = new HashSet<>(input.values());
        var plantAreas = new HashMap<Character, List<Set<Point>>>();
        var visited = new HashSet<Point>();

        for (Character plant : plants) {
            var areas = new ArrayList<Set<Point>>();

            var points = input.entrySet()
                .stream()
                .filter(entry -> entry.getValue() == plant)
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());

            for (Point point : points) {
                if (!visited.contains(point)) {
                    var area = new HashSet<Point>();
                    findConnectedArea(point, points, visited, area);
                    areas.add(area);
                }
            }
            plantAreas.put(plant, areas);
        }

        return plantAreas;
    }

    private void findConnectedArea(Point start, Set<Point> points, Set<Point> visited, Set<Point> area) {
        var stack = new ArrayDeque<Point>();
        stack.push(start);

        while (!stack.isEmpty()) {
            Point current = stack.pop();
            if (visited.contains(current)) continue;
            visited.add(current);
            area.add(current);

            var neighbors = Direction.getBasicDirections()
                .stream()
                .map(current::plus)
                .filter(points::contains)
                .filter(p -> !visited.contains(p))
                .toList();

            stack.addAll(neighbors);
        }
    }


    @Override
    public String solveB(InputType inputType) {
        return "";
    }

    @AllArgsConstructor
    @Getter
    private class Garden {

        private Character name;
        private Integer perimeter;
        private Integer area;
        private Integer sides;

        public Integer fencePrice() {
            return this.perimeter * this.area;
        }

        public Integer newPrice() {
            return this.sides * this.area;
        }
    }

    public static int calculatePerimeter(Set<Point> points) {

       return points.stream().mapToInt(point -> {
                var neighbors = Direction.getBasicDirections()
                    .stream()
                    .map(point::plus)
                    .filter(points::contains).count();

                return 4 -  (int) neighbors;
        }).sum();
    }

}
