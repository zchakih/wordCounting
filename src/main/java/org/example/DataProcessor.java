package org.example;

import lombok.Getter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class DataProcessor {
    @Getter
    Map<String, Integer> map = new HashMap<>();

    /**
     * Process reading a file and display the triaged words by frequency
     * @param path The uri of file
     * @param triageSize The triage size that can be displayed
     */
    public void processAndDisplay(String path, int triageSize){
        try (Stream<String> lines = Files.lines(Path.of(path))){
            Stream<String> preparedLines = prepareLines(lines);
            addWordAndCount(preparedLines);
            Map<String,Integer> sorted = sortAndTriage(triageSize);
            display(sorted);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Prepare the read lines by splitting in words and convert to lower case, so that the case sensitivity can be ignored
     * @param lines stream of read lines
     * @return Stream of words
     */
    private Stream<String> prepareLines(Stream<String> lines) {
        return lines.parallel().flatMap(line -> Arrays.stream(line.split("\\s+"))
                .map(String::toLowerCase));
    }

    /**
     * Sort the map entries by value in descending order and limit the result
     * @param triageSize the triage size to be limited
     */
    private Map<String, Integer> sortAndTriage(int triageSize) {
        return map.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(triageSize)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));
    }

    /**
     * Add the read words in the map.
     * Check if the map contains the word and increment the frequency
     * @param words words from each line as string
     */
    private void addWordAndCount(Stream<String> words) {
        words.parallel().forEach(word -> {
            if (map.containsKey(word)) {
                map.put(word, map.get(word) + 1);
            } else {
                map.put(word, 1);
            }
        });
    }

    /**
     * Display data and frequency
     * @param map The processed map to be displayed
     */
    private void display(Map<String, Integer> map) {
        map.forEach((key, value) -> System.out.println(key + ": " + value));
    }
}
