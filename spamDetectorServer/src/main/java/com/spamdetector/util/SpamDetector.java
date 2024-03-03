package com.spamdetector.util;

import com.spamdetector.domain.TestFile;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

public class SpamDetector {

    public List<TestFile> trainAndTest(File mainDirectory) {
        // TODO: main method of loading the directories and files, training and testing the model
        return new ArrayList<>();
    }

    public void training() {
        URL directory = SpamDetector.class.getClassLoader().getResource("\\data\\test");
        if(directory == null)
        {
            System.err.println("Directory does not Exist");
            return;
        }

        try
        {
            URI uri = directory.toURI();
            File mainDirectory = new File(uri);

            Map<String, Integer> spamMap = (Map<String, Integer>) calculateFrequency(mainDirectory);
            System.out.println("Testing 1");
            System.out.println(spamMap.get("to"));
            for(String key: spamMap.keySet())
            {
                System.out.println("Testing 2");
                System.out.println(key + " " + spamMap.get(key));
            }
            System.out.println("Testing 3");
        } catch (URISyntaxException e)
        {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Calculate word frequencies from files in the given directory
    public Map<String, Integer> calculateFrequency(File directory) throws IOException {
        Map<String, Integer> wordFrequencyMap = new HashMap<>();
        File[] files = directory.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    extractWordsFromFile(file, wordFrequencyMap);
                }
            }
        }
        return wordFrequencyMap;
    }

    // Extract words from a file and update word frequency map
    public void extractWordsFromFile(File file, Map<String, Integer> wordFrequencyMap) throws FileNotFoundException {
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNext()) {
                String word = scanner.next().toLowerCase();
                if (isWord(word)) {
                    wordFrequencyMap.put(word, wordFrequencyMap.getOrDefault(word, 0) + 1);
                }
            }
        }
    }

    // Check if the input string is a word and not punctuation
    private boolean isWord(String word) {
        return word.matches("[a-zA-Z]+");
    }

    public static void main(String[] args) {
        SpamDetector spamDetector = new SpamDetector();
        spamDetector.training();
    }
}
