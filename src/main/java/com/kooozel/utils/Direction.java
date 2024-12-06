package com.kooozel.utils;

import java.util.ArrayList;
import java.util.List;

public enum Direction {
    UP, LEFT, RIGHT, DOWN;

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
        List<Point> directions = new ArrayList<>();

        directions.add(new Point(0, 1));  // RIGHT
        directions.add(new Point(0, -1)); // LEFT
        directions.add(new Point(1, 0));  // DOWN
        directions.add(new Point(-1, 0)); // UP

        directions.add(new Point(-1, -1)); // UP-LEFT
        directions.add(new Point(-1, 1));  // UP-RIGHT
        directions.add(new Point(1, -1));  // DOWN-LEFT
        directions.add(new Point(1, 1));   // DOWN-RIGHT

        return directions;
    }

    public static List<Point> getCorners() {
        List<Point> directions = new ArrayList<>();
        directions.add(new Point(-1, -1)); // UP-LEFT
        directions.add(new Point(-1, 1));  // UP-RIGHT
        directions.add(new Point(1, -1));  // DOWN-LEFT
        directions.add(new Point(1, 1));   // DOWN-RIGHT

        return directions;
    }
}

