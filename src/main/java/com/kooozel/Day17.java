package com.kooozel;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.kooozel.utils.FileUtils;
import com.kooozel.utils.InputType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class Day17 implements Day {

    private final FileUtils fileUtils;

    private String parseFile(InputType inputType) {
        return fileUtils.readString(inputType, Day17.class);
    }

    private List<String> getInstructions(String string) {
        return Arrays.stream(string.split("\n\n")[1].trim().split(":")[1].split(",")).map(String::trim).toList();
    }

    private Register parseRegister(String string) {
        var input = Arrays.stream(string.split("\n\n")[0].trim().split("\n"))
            .toList()
            .stream()
            .map(el -> (el.split(":")[1]).trim())
            .toList();
        return Register.builder()
            .a(Integer.parseInt(input.get(0)))
            .b(Integer.parseInt(input.get(1)))
            .c(Integer.parseInt(input.get(2)))
            .build();
    }

    @AllArgsConstructor
    @Getter
    @Setter
    @Builder
    private static class Register {

        private Integer a;
        private Integer b;
        private Integer c;
    }

    @Getter
    @Setter
    @Builder
    private static class Computer {

        private Register register;
        private List<String> instructions;
        private String out;
        private Integer index;

        private void solve() {
            while (true) {
                if (index > instructions.size() -1) {
                    break;
                }
                int result = evaluateInstruction(instructions.get(index), instructions.get(index + 1));
                if (result == -1) {
                    break;
                }
                setIndex(result);
            }
        }

        private int evaluateInstruction(String instruction, String operand) {
            switch (instruction) {
                case "0" -> {
                    log.info("adv");
                    int division = (int) ((this.register.a) / Math.pow(
                        2,
                        parseComboOperand(operand)));
                    register.setA(division);
                }
                case "1" -> {
                    log.info("bxl");
                    int result = this.register.b ^ Integer.parseInt(operand);
                    register.setB(result);

                }
                case "2" -> {
                    log.info("bst");
                    int result = parseComboOperand(operand) % 8;
                    register.setB(result);
                }
                case "3" -> {
                    log.info("jnz");
                    if (this.register.a != 0) {
                        log.info("Jump to {}", operand);
                        return Integer.parseInt(operand);
                    }
                }
                case "4" -> {
                    log.info("bxc");
                    int result = this.register.b ^ this.register.c;
                    register.setB(result);
                }
                case "5" -> {
                    log.info("out");
                    int result = parseComboOperand(operand) % 8;
                    log.info("Result {}", result);
                    setOut(getOut() == null ? String.valueOf(result) : getOut() + "," + result);
                }
                case "6" -> {
                    log.info("bdv");
                    int division = (int) ((this.register.a) / Math.pow(
                        2,
                        parseComboOperand(operand)));
                    register.setB(division);
                }
                case "7" -> {
                    log.info("cdv");
                    int division = (int) ((this.register.a) / Math.pow(
                        2,
                        parseComboOperand(operand)));
                    register.setC(division);
                }
                default -> {
                    return -1;
                }

            }
            return index + 2;
        }

        private Integer parseComboOperand(String input) {
            switch (input) {
                case "0", "1", "2", "3" -> {
                    return Integer.parseInt(input);
                }
                case "4" -> {
                    log.info("Return A");
                    return this.register.a;
                }
                case "5" -> {
                    log.info("Return B");
                    return this.register.b;
                }
                case "6" -> {
                    log.info("Return C");
                    return this.register.c;
                }
                case "7" -> {
                    throw new IllegalStateException("Unexpected value: " + input);
                }
                default -> throw new IllegalStateException("Unexpected value: " + input);
            }
        }
    }

    @Override
    public String solveA(InputType inputType) {
        var instructions = getInstructions(parseFile(inputType));
        var register = parseRegister(parseFile(inputType));

        var computer = Computer.builder()
            .instructions(instructions)
            .register(register)
            .index(0)
            .build();

        computer.solve();

        return computer.getOut();
    }

    @Override
    public String solveB(InputType inputType) {
        var instructions = getInstructions(parseFile(inputType));
        var register = parseRegister(parseFile(inputType));

        var index = 100000;
        String join = String.join(",", instructions);
        while (true) {
            register.setA(index);

            var computer = Computer.builder()
                .instructions(instructions)
                .register(register)
                .index(0)
                .build();

            computer.solve();
            if (Objects.equals(computer.getOut(), join)) {
                break;
            }
            index++;
            log.info("Index: {}", index);
        }
        return String.valueOf(index);
    }
}
