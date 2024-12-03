package com.kooozel;

import java.util.concurrent.TimeUnit;

import com.kooozel.utils.FileUtils;
import com.kooozel.utils.InputType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
@RequiredArgsConstructor
@Slf4j
public class Main {

    private static final MeterRegistry meterRegistry = new SimpleMeterRegistry();

    public static void main(String[] args) {
        var solve = new Day03(new FileUtils());

        Timer solveATimer = Timer.builder("day02.solveA.execution.time")
            .description("Time taken to solve part A")
            .register(meterRegistry);
        Timer solveBTimer = Timer.builder("day02.solveB.execution.time")
            .description("Time taken to solve part B")
            .register(meterRegistry);

        try {
            log.info("Starting to solve Part A...");
            String resultA = solveATimer.record(() -> solve.solveA(InputType.TEST));
            double timeA = solveATimer.totalTime(TimeUnit.SECONDS);
            log.info("Result for Part A: {}", resultA);
            log.info("Execution time for Part A: {} seconds", timeA);
        } catch (Exception e) {
            log.error("Error while solving Part A", e);
        }

        try {
            log.info("Starting to solve Part B...");
            String resultB = solveBTimer.record(() -> solve.solveB(InputType.INPUT));
            double timeB = solveBTimer.totalTime(TimeUnit.SECONDS);
            log.info("Result for Part B: {}", resultB);
            log.info("Execution time for Part B: {} seconds", timeB);
        } catch (Exception e) {
            log.error("Error while solving Part B", e);
        }
    }
}