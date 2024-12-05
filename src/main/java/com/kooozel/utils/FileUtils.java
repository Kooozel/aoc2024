package com.kooozel.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileUtils {
    public <T> List<String> readLines(InputType inputType, Class<T> day) {
        var path = "src/main/resources/" + day.getSimpleName().toLowerCase() + "/" + inputType + ".txt";
        var filePath = Path.of(path);

        try {
            // Read all lines into a List of Strings
            return Files.readAllLines(filePath);
        } catch (IOException e) {
            // Handle exception
            log.error("Error reading file: {}", e.getMessage());
            return null;
        }
    }

    public <T> String readString(InputType inputType, Class<T> day) {
        var path = "src/main/resources/" + day.getSimpleName().toLowerCase() + "/" + inputType + ".txt";
        var filePath = Path.of(path);
        try {
            return Files.readString(filePath);
        } catch (IOException e) {
            log.error("Error reading file: {}", e.getMessage());
            return null;
        }
    }

    public Map<Point, Character> getGridMap(String string) {
        Map<Point, Character> map = new HashMap<>();
        var lines = string.split("\n");

        for (int rowIndex = 0; rowIndex < lines.length; rowIndex++) {
            var line = lines[rowIndex];
            for (int colIndex = 0; colIndex < line.length(); colIndex++) {
                map.put(new Point(rowIndex, colIndex), line.charAt(colIndex));
            }
        }

        return map;
    }
}
