package com.kooozel;

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
public class Day06 implements Day{
    private final FileUtils fileUtils;

    private Map<Point, Character> parseFile(InputType inputType) {
        String fileContent = fileUtils.readString(inputType, Day06.class);
        return fileUtils.getGridMap(fileContent);
    }



    @Override
    public String solveA(InputType inputType) {
        var grid = parseFile(inputType);
        var startPosition = grid.entrySet().stream().filter(point -> point.getValue() == '^').findFirst().map(Map.Entry::getKey).orElseThrow(
            RuntimeException::new);

        var controlSet = getPoints(startPosition, grid);

        return String.valueOf(controlSet.size());
    }

    private HashSet<Point> getPoints(Point startPosition, Map<Point, Character> grid) {
        var currentPosition = startPosition;
        var currentDirection = Direction.UP;
        var controlSet = new HashSet<Point>();
        controlSet.add(startPosition);
        while (currentPosition != null) {
            var nextPoint = currentPosition.plus(currentDirection);
            var  c = grid.getOrDefault(nextPoint, null);
            if (c == null) { break;}
            if (c == '#') {
                currentDirection = getNewDirection(currentDirection);
                nextPoint = currentPosition.plus(currentDirection);
            }
            currentPosition = nextPoint;

            controlSet.add(currentPosition);
            log.info(currentPosition.toString());
        }
        return controlSet;
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
        var startPosition = grid.entrySet().stream().filter(point -> point.getValue() == '^').findFirst().map(Map.Entry::getKey).orElseThrow(
            RuntimeException::new);

        var currentPosition = startPosition;
        var currentDirection = Direction.UP;
        var way = new HashSet<Pair<Point, Direction>>();
        var obstacles = new HashSet<Point>();
        way.add(new Pair<>(currentPosition, currentDirection));
        while (currentPosition != null) {
            var nextPoint = currentPosition.plus(currentDirection);
            var  c = grid.getOrDefault(nextPoint, null);
            if (c == null) { break;}
            if (c == '#') {
                log.info("Change Direction");
                currentDirection = getNewDirection(currentDirection);
                nextPoint = currentPosition.plus(currentDirection);
            }
            currentPosition = nextPoint;

            var e = new Pair<>(currentPosition, currentDirection);
            checkGrid(grid, way, new Pair<>(currentPosition, currentDirection), obstacles);
            way.add(e);
            log.info(currentPosition.toString());
        }
        return String.valueOf(obstacles.size());
    }

    private void checkGrid(
        Map<Point, Character> grid,
        HashSet<Pair<Point, Direction>> controlSet,
        Pair<Point, Direction> o, HashSet<Point> obstacles) {
        var nextPosition = o.key();
        var currentDirection = getNewDirection(o.value());
        var microSet = new HashSet<Pair<Point, Direction>>();
        while (nextPosition != null) {
            nextPosition = nextPosition.plus(currentDirection);
            var c = grid.getOrDefault(nextPosition, null);
            if (c == null) {
                break;
            }
            if (c == '#') {
                currentDirection = getNewDirection(currentDirection);
                nextPosition = nextPosition.plus(currentDirection);
            }

            Pair<Point, Direction> o1 = new Pair<>(nextPosition, currentDirection);
            if (controlSet.contains(o1)) {
                obstacles.add(o.key().plus(o.value()));
                break;
            }

            if (!microSet.add(o1)) {
                obstacles.add(o1.key());
                break;
            }
        }
    }
}
