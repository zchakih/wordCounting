package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

public class MainQuick {

    public static final String PATH = "src/main/resources/french_text.txt";
    public static final int TRIAGE_SIZE = 10;

    public static void main(String[] args) {
        try (Stream<String> lines = Files.lines(Path.of(PATH))
                .parallel()) {
            Map<String, Integer> map = new HashMap<>();

            lines.parallel().forEach(line -> Arrays.stream(line.split("\\s+"))
                    .map(String::toLowerCase)
                    .parallel()
                    .forEach(word -> {
                        if(map.containsKey(word)){
                            map.put(word, map.get(word) + 1);
                        }else {
                            map.put(word, 1);
                        }
                    }));
            map.entrySet()
                    .stream()
                    .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                    .limit(TRIAGE_SIZE)
                    .forEach(System.out::println);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}