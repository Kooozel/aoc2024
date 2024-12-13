package com.kooozel;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;

import com.kooozel.utils.FileUtils;
import com.kooozel.utils.InputType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class Day09 implements Day {

    private final FileUtils fileUtils;

    private String parseFile(InputType i) {
        return fileUtils.readString(i, Day09.class);
    }

    @Override
    public String solveA(InputType inputType) {
        var newList = getIntegers(inputType);

        var run = true;
        while (run) {
            moveFileBlock(newList);
            if (isDone(newList)) {
                run = false;
            }
        }
        var sum = getSum(newList);
        return String.valueOf(sum);
    }

    private ArrayList<Integer> getIntegers(InputType inputType) {
        var list = getDiskMaps(inputType);
        var sumSize = list.stream().map(DiskMap::getSize).reduce(0, Integer::sum);
        var newList = new ArrayList<Integer>(sumSize);

        return getIntegers(list, newList);
    }

    private static ArrayList<Integer> getIntegers(ArrayList<DiskMap> list, ArrayList<Integer> newList) {
        for (DiskMap diskMap: list) {
            var diskList = new ArrayList<Integer>();
            for (int i = 0; i < diskMap.size; i++) {
                if (diskMap.isFile) {
                    diskList.add(diskMap.getId());
                } else {
                    diskList.add(null);
                }
            }
            newList.addAll(diskList);
        }
        return newList;
    }

    private ArrayList<DiskMap> getDiskMaps(InputType inputType) {
        var input = parseFile(inputType);
        var list = new ArrayList<DiskMap>();
        for (int i = 0; i < input.length(); i++) {
            var size = Integer.valueOf(String.valueOf(input.toCharArray()[i]));
            var disk = new DiskMap(i / 2, size, isEven(i));
            list.add(disk);
        }
        return list;
    }

    private BigInteger getSum(ArrayList<Integer> newList) {
        var sum = BigInteger.ZERO;
        for (int i = 0; i < newList.size(); i++) {
            Integer i1 = newList.get(i);
            if (i1 != null) {
                sum = sum.add(new BigInteger(String.valueOf(i * i1)));
            }
        }
        return sum;
    }

    private boolean isDone(ArrayList<Integer> r) {
        return r.stream().allMatch(Objects::nonNull);
    }

    private boolean isEven(int number) {
        return number % 2 == 0;
    }

    private void moveFileBlock(ArrayList<Integer> s) {
        int lastDigitIndex = s.size() - 1;
        while (lastDigitIndex >= 0 && s.get(lastDigitIndex) == null) {
            lastDigitIndex--;
        }

        if (lastDigitIndex < 0) {
            return;
        }

        var file = s.get(lastDigitIndex);
        var firstSpace = s.indexOf(null);
        s.remove(firstSpace);
        s.add(firstSpace, file);
        s.remove(lastDigitIndex);
    }

    @AllArgsConstructor
    @Getter
    @Setter
    private class DiskMap {

        private int id;
        private int size;
        private boolean isFile;

        @Override
        public String toString() {
            return isFile ? String.valueOf(id).repeat(size) : ".".repeat(size);
        }
    }

    @Override
    public String solveB(InputType inputType) {
        var newList = getDiskMaps(inputType);

        for (int i = newList.size() - 1; i >= 0; i--) {
            moveWholeFile(newList, i);
        }
        var sum = getSum(getIntegers(newList, new ArrayList<Integer>()));
        return String.valueOf(sum);
    }

    private void moveWholeFile(ArrayList<DiskMap> newList, int i) {

        var file = newList.get(i);
        if (!file.isFile) {
            return;
        }
        newList.stream().filter(el -> !el.isFile && file.size <= el.size).findFirst().ifPresent((el
        ) -> {
            var firstSpace = newList.indexOf(el);
            if (firstSpace > i) {return;}
            var space = newList.get(firstSpace);
            space.setSize(space.getSize() - file.size);
            newList.remove(i);
            newList.add(firstSpace, file);
            newList.add(i, new DiskMap(i, file.getSize(), false));
        });
    }
}
