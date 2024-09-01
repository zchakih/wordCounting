package org.example;

public class Main {

    public static final String PATH = "src/main/resources/french_text.txt";
    public static final int TRIAGE_SIZE = 10;

    public static void main(String[] args) {
        new DataProcessor().processAndDisplay(PATH, TRIAGE_SIZE);
    }
}