package com.kooozel;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import com.kooozel.utils.FileUtils;
import com.kooozel.utils.InputType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class Day07 implements Day {

    private final FileUtils fileUtils;

    private List<String> parseFile(InputType inputType) {
        return fileUtils.readLines(inputType, Day07.class);
    }

    private BigInteger getResult(String string) {
        return new BigInteger(string.split(":")[0].trim());
    }

    private List<Integer> getNumbers(String string) {
        return Arrays.stream(string.split(":")[1].trim().split(" ")).map(String::trim).map(Integer::parseInt).toList();
    }

    public static List<String> generateExpressions(List<Integer> numbers) {
        List<String> results = new ArrayList<>();
        generateRecursive(numbers, 0, new StringBuilder(), results);
        return results;
    }

    private static void generateRecursive(
        List<Integer> numbers,
        int index,
        StringBuilder currentExpr,
        List<String> results) {
        // Base case: if we've processed all numbers
        if (index == numbers.size()) {
            results.add(currentExpr.toString());
            return;
        }

        // If it's not the first number, we need to add an operator before the next number
        if (index > 0) {
            StringBuilder temp = new StringBuilder(currentExpr);
            temp.append(" + ");
            generateRecursive(numbers, index + 1, temp.append(numbers.get(index)), results);

            temp = new StringBuilder(currentExpr);
            temp.append(" * ");
            generateRecursive(numbers, index + 1, temp.append(numbers.get(index)), results);

            temp = new StringBuilder(currentExpr);
            temp.append(" || ");
            generateRecursive(numbers, index + 1, temp.append(numbers.get(index)), results);
        } else {
            // For the first number, no operator before it
            generateRecursive(numbers, index + 1, currentExpr.append(numbers.get(index)), results);
        }
    }

    private boolean isValidExpression(String expression, BigInteger target) {
        try {
            var result = evaluateExpression(expression);
            return Objects.equals(result, target);
        } catch (Exception e) {
            return false; // Invalid expression
        }
    }

    public BigInteger evaluateExpression(String expression) {
        // Replace all spaces for safe parsing
        var list  = expression.split(" ");

        // To handle * and + operations, we can use a two-step approach
        // First, handle multiplication
        BigInteger result = new BigInteger(list[0]);
        for (int i = 0; i < list.length; i++) {
            var s = list[i];
            if (Objects.equals(s, "*")) {
                result = result.multiply(new BigInteger(list[i + 1]));
            } else if (Objects.equals(s, "+"))
            {
                result = result.add(new BigInteger(list[i + 1]));
            } else if (Objects.equals(s, "||")) {
                result = new BigInteger(result.toString() + list[i + 1]);
            }
        }
        return result;
    }

    @Override
    public String solveA(InputType inputType) {
        var input = parseFile(inputType);
        var sum = BigInteger.ZERO;
        for (String line : input) {
            var result = getResult(line);
            var numbers = getNumbers(line);

            var expressions = generateExpressions(numbers);

            for (String expression : expressions) {
                if (isValidExpression(expression, result)) {
                    log.info("Valid expression found: {}", expression);
                    sum = sum.add(result);
                    break;
                }
            }
        }
        return String.valueOf(sum);
    }

    @Override
    public String solveB(InputType inputType) {
        return "";
    }
}
