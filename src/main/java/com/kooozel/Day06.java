package com.kooozel;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import com.kooozel.utils.Direction;
import com.kooozel.utils.FileUtils;
import com.kooozel.utils.InputType;
import com.kooozel.utils.Pair;
import com.kooozel.utils.Point;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class Day06 implements Day {

    private final FileUtils fileUtils;

    private Map<Point, Character> parseFile(InputType inputType) {
        String fileContent = fileUtils.readString(inputType, Day06.class);
        return fileUtils.getGridMap(fileContent);
    }

    @Override
    public String solveA(InputType inputType) {
        var grid = parseFile(inputType);
        var startPosition = grid.entrySet().stream()
            .filter(point -> point.getValue() == '^')
            .findFirst()
            .map(Map.Entry::getKey)
            .orElseThrow(RuntimeException::new);

//        var controlSet = getPoints(startPosition, grid);

        return "";
    }

    private boolean getPoints(Point startPosition, Map<Point, Character> grid) {
        var currentPosition = startPosition;
        var currentDirection = Direction.UP;
        var controlSet = new HashSet<Pair<Point, Direction>>();
        controlSet.add(new Pair<>(startPosition, Direction.UP));
        var sum = 0;

        while (currentPosition != null) {
            var result = getNextPositionAndDirection(currentPosition, currentDirection, grid);
            currentPosition = result.newPosition;
            currentDirection = result.newDirection;

            if (currentPosition == null) {
                break;
            }

            if (sum > grid.entrySet().size()) {
                return true;
            }
            sum++;
        }

        return false;
    }

    private MovementResult getNextPositionAndDirection(
        Point currentPosition,
        Direction currentDirection,
        Map<Point, Character> grid) {
        var nextPoint = currentPosition.plus(currentDirection);
        var c = grid.getOrDefault(nextPoint, null);

        if (c == null) {
            return new MovementResult(null, currentDirection); // End of grid
        }

        if (c == '#') {
            currentDirection = getNewDirection(currentDirection);
            return getNextPositionAndDirection(currentPosition, currentDirection, grid);
        }

        return new MovementResult(nextPoint, currentDirection);
    }

    private record MovementResult(Point newPosition, Direction newDirection) {

    }

    private Direction getNewDirection(Direction currentDirection) {
        var directions = List.of(Direction.UP, Direction.RIGHT, Direction.DOWN, Direction.LEFT);
        var indexOfDirection = directions.indexOf(currentDirection);
        if (indexOfDirection == 3) {
            return Direction.UP;
        }
        return directions.get(indexOfDirection + 1);
    }

    @Override
    public String solveB(InputType inputType) {
        var grid = parseFile(inputType);
        var startPosition = grid.entrySet()
            .stream()
            .filter(point -> point.getValue() == '^')
            .findFirst()
            .map(Map.Entry::getKey)
            .orElseThrow(RuntimeException::new);

        // Set to track positions that cause cycles
        var cycleCausingPositions = new HashSet<Point>();

        // Simulate guard's movement and try adding obstacles
        for (var obstaclePoint : grid.keySet()) {
            // Skip if the point is not a valid position to place an obstruction
            if (grid.get(obstaclePoint) != '.') {
                continue;
            }

            // Create a new grid with the potential obstacle
            var newGrid = createObstacle(grid, obstaclePoint);

            // Check if adding this obstacle causes a cycle
            if (isCycle(startPosition, newGrid)) {
                cycleCausingPositions.add(obstaclePoint);
            }
        }

        return String.valueOf(cycleCausingPositions.size());
    }

    private Map<Point, Character> createObstacle(Map<Point, Character> grid, Point obstaclePoint) {
        var newGrid = new HashMap<>(grid);
        newGrid.put(obstaclePoint, '#'); // Add the obstacle
        return newGrid;
    }


    private boolean isCycle(Point start, Map<Point, Character> grid) {
        return getPoints(start, grid);
    }
}
