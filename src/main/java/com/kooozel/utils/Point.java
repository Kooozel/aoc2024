package com.kooozel.utils;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import lombok.Getter;

@Getter
public class Point extends Point2D {
    public long x;
    public long y;

    public Point(long x, long y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public double getX() {
        return x;
    }

    @Override
    public double getY() {
        return y;
    }

    @Override
    public void setLocation(double x, double y) {
        this.x = (long) x;
        this.y = (long) y;
    }

    public static Long calculateManhattanDistance(Point point1, Point point2) {
        return Math.abs(point2.x - point1.x) + Math.abs(point2.y - point1.y);
    }

    public Point plus(Direction direction, Long length) {
        long x = this.x;
        long y = this.y;
        switch (direction) {
            case DOWN -> {
                return new Point(x + length, y);
            }
            case RIGHT -> {
                return new Point(x, y + length);
            }
            case UP -> {
                return new Point(x - length, y);
            }
            case LEFT -> {
                return new Point(x, y - length);
            }
        }
        throw new IllegalArgumentException("Wrong Direction");
    }

    public Point plus(Direction direction) {
        return plus(direction, 1L);
    }

    public Point plus(Point direction) {
        return new Point(this.x + direction.x, this.y + direction.y);
    }

    public Point plus(Point direction, Integer length) {
        return new Point(this.x + direction.x * length, this.y + direction.y * length);
    }

    @Override
    public String toString() {
        return "Point{" + "x=" + x + ", y=" + y + '}';
    }

    private static int orientation(Point p, Point q, Point r) {
        var val = (q.y - p.y) * (r.x - q.x) - (q.x - p.x) * (r.y - q.y);
        if (val == 0L) return 0; // Collinear
        return (val > 0L) ? 1 : 2; // Clockwise or Counterclockwise
    }

    public static double distance(Point p1, Point p2) {
        return Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2));
    }


    public static List<Point> convexHull(List<Point> points) {
        List<Point> sortedPoints = getPoints(points);

        if (points.size() < 3) {
            return points;
        }

        if (points.stream().allMatch((el) -> el.getX() == points.get(0).getX()) || points.stream().allMatch((el) -> el.getX() == points.get(0).getX())) {
            return points;
        }

        // Step 3: Build the convex hull using a stack
        Stack<Point> hull = new Stack<>();
        hull.push(sortedPoints.get(0));
        hull.push(sortedPoints.get(1));

        for (int i = 2; i < sortedPoints.size(); i++) {
            Point top = hull.pop();
            while (!hull.isEmpty() && orientation(hull.peek(), top, sortedPoints.get(i)) != 2) {
                top = hull.pop();
            }
            hull.push(top);
            hull.push(sortedPoints.get(i));
        }

        // Convert stack to a list
        return new ArrayList<>(hull);
    }

    private static List<Point> getPoints(List<Point> points) {
        int n = points.size();
        if (n < 3) {
            return  points;
        }

        // Step 1: Find the anchor point
        Point anchor = points.get(0);
        for (Point p : points) {
            if (p.y < anchor.y || (p.y == anchor.y && p.x < anchor.x)) {
                anchor = p;
            }
        }

        // Step 2: Sort points by polar angle with respect to the anchor
        return getSortedPoints(points, anchor);
    }

    private static List<Point> getSortedPoints(List<Point> points, Point anchor) {
        List<Point> sortedPoints = new ArrayList<>(points);
        sortedPoints.sort((p1, p2) -> {
            int orientation = orientation(anchor, p1, p2);
            if (orientation == 0) {
                return java.lang.Double.compare(distance(anchor, p1), distance(anchor, p2));
            }
            return (orientation == 2) ? -1 : 1; // Counterclockwise comes first
        });
        return sortedPoints;
    }

}


