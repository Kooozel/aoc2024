package com.kooozel.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Direction {
    UP(new Point(-1, 0)),
    LEFT(new Point(0, -1)),
    RIGHT(new Point(0, 1)),
    DOWN(new Point(1, 0)),
    UP_LEFT(new Point(-1, -1)),
    UP_RIGHT(new Point(-1, 1)),
    DOWN_LEFT(new Point(1, -1)),
    DOWN_RIGHT(new Point(1, 1));

    private final Point point;

    private static final Map<Point, Direction> POINT_DIRECTION_MAP;

    static {
        POINT_DIRECTION_MAP = Stream.of(Direction.values())
            .collect(Collectors.toUnmodifiableMap(Direction::getPoint, direction -> direction));
    }

    public static Direction fromPoint(Point point) {
        return POINT_DIRECTION_MAP.get(point);
    }


    public static Direction from(String direction) {
        return switch (direction) {
            case "U" -> UP;
            case "D" -> DOWN;
            case "R" -> RIGHT;
            case "L" -> LEFT;
            default -> throw new IllegalArgumentException("Invalid direction: " + direction);
        };
    }

    public static List<Point> getAllDirections() {
        return Stream.of(Direction.values()).map(Direction::getPoint).toList();
    }

    public static List<Point> getCorners() {
        return Stream.of(UP_RIGHT,UP_LEFT,DOWN_LEFT,DOWN_RIGHT).map(Direction::getPoint).toList();
    }

    public static List<Point> getBasicDirections() {
        return Stream.of(UP,DOWN,LEFT,RIGHT).map(Direction::getPoint).toList();
    }
}

