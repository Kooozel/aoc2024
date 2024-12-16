package com.kooozel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.kooozel.utils.Direction;
import com.kooozel.utils.FileUtils;
import com.kooozel.utils.InputType;
import com.kooozel.utils.Point;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class Day16 implements Day {

    private final FileUtils fileUtils;

    private Map<Point, Character> parseFile(InputType inputType) {
        return fileUtils.getGridMap(fileUtils.readString(inputType, Day16.class));
    }

    @Override
    public String solveA(InputType inputType) {
        var input = parseFile(inputType);
        var start = input.entrySet().stream().filter(e -> e.getValue() == 'S').map(Map.Entry::getKey).findFirst().get();
        var end = input.entrySet().stream().filter(e -> e.getValue() == 'E').map(Map.Entry::getKey).findFirst().get();

        var routes = calculateRoutes(start, input, end);
        var sum = routes.stream().map(this::calculateRouteScore).toList();
        return "";
    }

    @Override
    public String solveB(InputType inputType) {
        return "";
    }

    public Set<List<Point>> calculateRoutes(Point start, Map<Point, Character> grid, Point end) {
        var queue = new LinkedList<Point>();
        var seen = new HashSet<Point>();
        var routePaths = new HashMap<Point, Set<List<Point>>>();

        var directions = Direction.getBasicDirections();
        queue.add(start);
        routePaths.put(start, new HashSet<>(Set.of(List.of(start))));
        while (!queue.isEmpty()) {
            var current = queue.removeFirst();

            if (current.equals(end)) {
                return routePaths.get(end);
            }

            directions.forEach(direction -> {
                    var newPoint = current.plus(direction);
                    if (!grid.containsKey(newPoint)) {
                        return;
                    }
                    if (grid.get(newPoint) != '#') {
                        //add to queue
                        if (!queue.contains(newPoint) && !seen.contains(newPoint)) {
                            queue.add(newPoint);
                        }

                        routePaths.computeIfAbsent(newPoint, k -> new HashSet<>());
                        for (List<Point> path : routePaths.get(current)) {
                            var newPath = new ArrayList<>(path);
                            newPath.add(newPoint);
                            routePaths.get(newPoint).add(newPath);
                        }
                    }
                    seen.add(current);
                }

            );
        }
        return null;
    }

    private Integer calculateRouteScore(List<Point> route) {
        var directionChanges = countDirectionChanges(route);
        return route.size() + directionChanges*1000;
    }

    public static int countDirectionChanges(List<Point> points) {
        if (points == null || points.size() < 2) return 0;

        int directionChanges = 0;
        long prevDx = points.get(1).x - points.get(0).x;
        long prevDy = points.get(1).y - points.get(0).y;

        for (int i = 1; i < points.size() - 1; i++) {
            long dx = points.get(i + 1).x - points.get(i).x;
            long dy = points.get(i + 1).y - points.get(i).y;

            if (dx != prevDx || dy != prevDy) {
                directionChanges++;
            }

            prevDx = dx;
            prevDy = dy;
        }

        return directionChanges;
    }

}
