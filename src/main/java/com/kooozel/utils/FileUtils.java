package com.kooozel.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

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
}
