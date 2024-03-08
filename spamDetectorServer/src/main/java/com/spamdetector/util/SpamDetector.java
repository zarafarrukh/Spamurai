package com.spamdetector.util;

import com.spamdetector.domain.TestFile;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

public class SpamDetector {

    private ArrayList<TestFile> testResults;
    private Map<String, Double> trainHamFreq;
    private Map<String, Double> trainSpamFreq;

    private double accuracy;
    private double precision;

    public SpamDetector() {
        this.trainHamFreq = new HashMap<>();
        this.trainSpamFreq = new HashMap<>();
    }

    public ArrayList<TestFile> getTestResults() {
        return testResults;
    }

    public double getPrecision() {
        return precision;
    }

    public double getAccuracy() {
        return accuracy;
    }

    // Train and Test method
    public List<TestFile> trainAndTest(File mainDirectory) throws URISyntaxException, IOException {
        testResults = new ArrayList<>();
        File hamDirectory = new File(mainDirectory, "train/ham");
        File ham2Directory = new File(mainDirectory, "train/ham2");
        File spamDirectory = new File(mainDirectory, "train/spam");

        calculateFrequency(spamDirectory, trainSpamFreq);
        calculateFrequency(hamDirectory, trainHamFreq);
        calculateFrequency(ham2Directory, trainHamFreq);

        List<TestFile> hamTestResults = testing(new File(mainDirectory, "test/ham"), "ham");
        List<TestFile> spamTestResults = testing(new File(mainDirectory, "test/spam"), "spam");
        testResults.addAll(hamTestResults);
        testResults.addAll(spamTestResults);

        List<Double> spamProb = calculateProbabilities(spamTestResults, trainSpamFreq, trainHamFreq);
        List<Double> hamProb = calculateProbabilities(hamTestResults, trainSpamFreq, trainHamFreq);

        calculatePrecisionAndAccuracy(spamProb, hamProb);

        return testResults;
    }

    public List<TestFile> testing(File folder, String category) throws IOException {
        List<TestFile> testResults = new ArrayList<>();
        if (!folder.exists()) {
            System.err.println("Testing directory doesn't exist");
            return testResults;
        }
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    TestFile testFile = new TestFile(file, category);
                    testFile.setActualClass(category);
                    testResults.add(testFile);
                }
            }
        }
        return testResults;
    }

    public void calculateFrequency(File directory, Map<String, Double> map) throws IOException {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    Set<String> uniqueWords = extractWordsFromFile(file);
                    for (String word : uniqueWords) {
                        map.put(word, map.getOrDefault(word, 0.0) + 1.0);
                    }
                }
            }
        }
    }

    public Set<String> extractWordsFromFile(File file) throws IOException {
        Set<String> words = new HashSet<>();
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] wordTokens = line.split("\\s+");
                    for (String token : wordTokens) {
                        if (isWord(token)) {
                            words.add(token.toLowerCase());
                        }
                    }
                }
            }
        }
        return words;
    }

    private boolean isWord(String word) {
        return word.matches("[a-zA-Z]+");
    }

    public List<Double> calculateProbabilities(List<TestFile> files, Map<String, Double> spamFreqMap, Map<String, Double> hamFreqMap) throws IOException {
        List<Double> probabilities = new ArrayList<>();
        for (TestFile testFile : files) {
            double spamProb = calculateProbability(testFile.getFilename(), spamFreqMap, hamFreqMap);
            probabilities.add(spamProb);
        }
        return probabilities;
    }

    public double calculateProbability(File file, Map<String, Double> spamFreqMap, Map<String, Double> hamFreqMap) throws IOException {
        Set<String> words = extractWordsFromFile(file);

        double spamFiles = spamFreqMap.size();
        double hamFiles = hamFreqMap.size();
        double totalSpamWords = spamFreqMap.values().stream().mapToDouble(Double::doubleValue).sum();
        double totalHamWords = hamFreqMap.values().stream().mapToDouble(Double::doubleValue).sum();

        double prob = Math.log(0.5); // prior probability for spam and ham

        for (String word : words) {
            double spamCount = spamFreqMap.getOrDefault(word, 0.0) + 1.0; // Laplace smoothing
            double hamCount = hamFreqMap.getOrDefault(word, 0.0) + 1.0; // Laplace smoothing

            double wordSpamProb = Math.log(spamCount / (totalSpamWords + spamFiles));
            double wordHamProb = Math.log(hamCount / (totalHamWords + hamFiles));

            prob += (wordSpamProb - wordHamProb);
        }

        return Math.exp(prob);
    }

    public void calculatePrecisionAndAccuracy(List<Double> spamProb, List<Double> hamProb) {
        int truePositives = 0;
        int falsePositives = 0;
        int trueNegatives = 0;
        int falseNegatives = 0;

        for (Double prob : spamProb) {
            if (prob > 0.5) {
                truePositives++;
            } else {
                falseNegatives++;
            }
        }

        for (Double prob : hamProb) {
            if (prob > 0.5) {
                falsePositives++;
            } else {
                trueNegatives++;
            }
        }

        accuracy = (double) (truePositives + trueNegatives) / (hamProb.size() + spamProb.size());
        precision = (double) truePositives / (truePositives + falsePositives);

        System.out.println("Accuracy: " + accuracy);
        System.out.println("Precision: " + precision);
    }

    public static void main(String[] args) throws URISyntaxException, IOException {
        SpamDetector spamDetector = new SpamDetector();
        URL directoryPath = SpamDetector.class.getClassLoader().getResource("data");
        if (directoryPath == null) {
            System.err.println("Directory does not exist");
            return;
        }
        URI uri = directoryPath.toURI();
        File mainDirectory = new File(uri);
        spamDetector.trainAndTest(mainDirectory);
    }
}