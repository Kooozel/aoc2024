package com.kooozel;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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

        // Get optimal paths
        Set<List<Point>> paths = routes.getPaths();
        System.out.println("Optimal Paths: " + paths.size());

        // Get unique tiles
        Set<Point> uniqueTiles = routes.getUniqueTiles();
        System.out.println("Unique Tiles Count: " + uniqueTiles.size());
        return "";
    }

    @Override
    public String solveB(InputType inputType) {
        return "";
    }

    public record PathInfo(Point point, List<Point> path, int steps, int rotations) {}

    public static class Result {
        private final Set<List<Point>> paths;
        private final Set<Point> uniqueTiles;

        public Result(Set<List<Point>> paths, Set<Point> uniqueTiles) {
            this.paths = paths;
            this.uniqueTiles = uniqueTiles;
        }

        public Set<List<Point>> getPaths() {
            return paths;
        }

        public Set<Point> getUniqueTiles() {
            return uniqueTiles;
        }
    }

    public Result calculateRoutes(Point start, Map<Point, Character> grid, Point end) {
        // Queue holds paths along with their direction changes and steps
        var queue = new LinkedList<PathInfo>();
        var visited = new HashMap<Point, PathInfo>(); // Tracks best state (steps, rotations) for each point
        var optimalPaths = new HashSet<List<Point>>(); // Store all valid paths to the end
        var uniqueTiles = new HashSet<Point>(); // Tracks unique tiles part of the best paths

        var directions = Direction.getBasicDirections();
        queue.add(new PathInfo(start, List.of(start), 0, 0)); // Initialize with the starting point
        visited.put(start, new PathInfo(start, List.of(start), 0, 0)); // Start point has a path length of 0

        while (!queue.isEmpty()) {
            var current = queue.removeFirst();
            var currentPoint = current.point;
            var currentPath = current.path;
            var currentSteps = current.steps;
            var currentRotations = current.rotations;

            // If we've reached the end, continue tracking all valid paths
            if (currentPoint.equals(end)) {
                optimalPaths.add(currentPath);
                uniqueTiles.addAll(currentPath); // Add all tiles in this path to the set
                continue; // Continue exploring for other potential paths
            }

            for (Point direction : directions) {
                var newPoint = currentPoint.plus(direction);

                // Skip invalid points or obstacles
                if (!grid.containsKey(newPoint) || grid.get(newPoint) == '#') {
                    continue;
                }

                // Calculate the number of rotations for this move
                int newRotations = currentRotations;
                if (currentPath.size() > 1) {
                    var prevPoint = currentPath.get(currentPath.size() - 2);
                    if (!isSameDirection(prevPoint, currentPoint, newPoint)) {
                        newRotations++;
                    }
                }

                var newSteps = currentSteps + 1;

                // If this point has been visited with fewer steps and rotations, skip it
                if (visited.containsKey(newPoint)) {
                    var visitedInfo = visited.get(newPoint);
                    if (visitedInfo.steps <= newSteps && visitedInfo.rotations <= newRotations) {
                        continue;
                    }
                }

                // Mark this point as visited with the current state
                var newPath = new ArrayList<>(currentPath);
                newPath.add(newPoint);
                var newPathInfo = new PathInfo(newPoint, newPath, newSteps, newRotations);
                visited.put(newPoint, newPathInfo);
                queue.add(newPathInfo);
            }
        }

        // Return the optimal paths and unique tiles
        return new Result(optimalPaths, uniqueTiles);
    }

    // Helper function to check if three points are in the same direction
    private boolean isSameDirection(Point prev, Point current, Point next) {
        var dx1 = current.x - prev.x;
        var dy1 = current.y - prev.y;
        var dx2 = next.x - current.x;
        var dy2 = next.y - current.y;
        return dx1 == dx2 && dy1 == dy2;
    }





    private Integer calculateRouteScore(List<Point> route) {
        var directionChanges = countDirectionChanges(route);
        return route.size() - 1 + (directionChanges) *1000;
    }

    public static int countDirectionChanges(List<Point> points) {
        if (points == null || points.size() < 2) return 0;
        long prevDx = points.get(1).x - points.get(0).x;
        long prevDy = points.get(1).y - points.get(0).y;
        int directionChanges = 0;

        if (Direction.fromPoint(new Point(prevDx, prevDy)) != Direction.RIGHT) {
            directionChanges++;
        }


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
