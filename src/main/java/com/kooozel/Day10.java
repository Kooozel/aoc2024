package com.kooozel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import com.kooozel.utils.Direction;
import com.kooozel.utils.FileUtils;
import com.kooozel.utils.InputType;
import com.kooozel.utils.Pair;
import com.kooozel.utils.Point;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class Day10 implements Day {

    private final FileUtils fileUtils;

    public Map<Point, TrailPoint> parseFile(InputType inputType) {
        return fileUtils.getGridMap(fileUtils.readString(inputType, Day10.class))
            .entrySet()
            .stream()
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                entry -> new TrailPoint(Integer.parseInt(String.valueOf(
                    entry.getValue() == '.' ? "99" : entry.getValue())), 0, 0)
            ));
    }

    @Override
    public String solveA(InputType inputType) {
        var input = parseFile(inputType);
        var trailHeads = input.entrySet()
            .stream()
            .filter(entry -> entry.getValue().isTrailHead())
            .collect(Collectors.toSet());
        trailHeads.forEach(entry -> calculateRoutes(entry, input));
        var sum = trailHeads.stream().map(Map.Entry::getValue).map(TrailPoint::getScore).reduce(0, Integer::sum);
        var totalRating = trailHeads.stream().map(Map.Entry::getValue).map(TrailPoint::getRating).reduce(0, Integer::sum);
        log.info("TOTAL ROUTES: {}", totalRating);
        return sum.toString();
    }

    @Override
    public String solveB(InputType inputType) {
        return "";
    }

    @AllArgsConstructor
    @Getter
    @Setter
    public class TrailPoint {

        private int height;
        private Integer score;
        private Integer rating;

        public boolean isTrailHead() {
            return this.height == 0;
        }

        public void incrementScore() {
            this.score++;
        }

        public void incrementRating(Integer integer) {
            this.rating += integer;
        }
    }

    public void calculateRoutes(Map.Entry<Point, TrailPoint> pointTrailPointEntry, Map<Point, TrailPoint> grid) {
        var queue = new LinkedList<Pair<Point, Integer>>();
        var seen = new HashSet<Point>();
        var routePaths = new HashMap<Point, Set<List<Point>>>();

        var directions = Direction.getBasicDirections();
        queue.add(new Pair<>(pointTrailPointEntry.getKey(), pointTrailPointEntry.getValue().height));
        routePaths.put(pointTrailPointEntry.getKey(), new HashSet<>(Set.of(List.of(pointTrailPointEntry.getKey()))));
        while (!queue.isEmpty()) {
            var currentPair = queue.removeFirst();
            var startPoint = currentPair.key();
            var startHeight = currentPair.value();

            if (startHeight == 9) {
                pointTrailPointEntry.getValue().incrementScore();
                log.info("Found 9");
                var sum = routePaths.get(startPoint).size();
                log.info("routes {}", sum);
                pointTrailPointEntry.getValue().incrementRating(sum);
            }

            directions.forEach(direction -> {
                var newPoint = startPoint.plus(direction);
                    if (!grid.containsKey(newPoint)) {
                        return;
                    }
                    if (grid.get(newPoint).height == startHeight + 1) {
                        //add to queue
                        Pair<Point, Integer> e = new Pair<>(newPoint, startHeight + 1);
                        if (!queue.contains(e)) {
                            queue.add(e);
                        }

                        routePaths.computeIfAbsent(newPoint, k -> new HashSet<>());
                        for (List<Point> path : routePaths.get(startPoint)) {
                            var newPath = new ArrayList<>(path);
                            newPath.add(newPoint);
                            routePaths.get(newPoint).add(newPath);
                        }
                        log.info(newPoint.toString());
                    }

                    seen.add(newPoint);
                }

            );
        }
    }
}
