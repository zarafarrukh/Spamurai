package com.spamdetector.util;

import com.spamdetector.domain.TestFile;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

public class SpamDetector
{

    // Instance variables
    private ArrayList<TestFile> testResults;
    private final Map<String, Double> trainHamFreq;
    private final Map<String, Double> trainSpamFreq;

    private double accuracy;
    private double precision;

    // Constructor
    public SpamDetector()
    {
        this.trainHamFreq = new HashMap<>();
        this.trainSpamFreq = new HashMap<>();
    }

    // Gets testresults
    public ArrayList<TestFile> getTestResults()
    {
        return testResults;
    }

    // Gets precisiom
    public double getPrecision()
    {
        return precision;
    }

    // Gets accuracy
    public double getAccuracy()
    {
        return accuracy;
    }

    // *********************************************************************TRAIN AND TEST METHOD**********************************************************************
    /*
     * Initializes testResults to store test results. calculates word frequencies from training data,
     * test the spamdetector on test data and computes the probabilities as well as estimate the accuracy and precision
     */
    public List<TestFile> trainAndTest(File mainDirectory) throws URISyntaxException, IOException
    {
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

    // TESTING METHOD 
    /*
     * Tests the spam detector on test data files by categorizing them based on the category provided,
     *  generates TestFile objects for each file in the directory with the indicated category,
     * returns thee the list of TestFile objects that show the test results
     */
    public List<TestFile> testing(File folder, String category) throws IOException
    {
        List<TestFile> testResults = new ArrayList<>();

        if (!folder.exists())
        {
            System.err.println("Testing directory doesn't exist");
            return testResults;
        }

        File[] files = folder.listFiles();

        if (files != null)
        {
            for (File file : files)
            {
                if (file.isFile())
                {
                    TestFile testFile = new TestFile(file, category);
                    testFile.setActualClass(category);
                    testResults.add(testFile);
                }
            }
        }

        return testResults;
    }

    // *************************************Preparing Data, TRAIN HAM AND SPAM using Calculate Frequency, Extract Words, Check if WOrd**********************************
    /*
     * Computes word frequency in files within a given directory, proccesses each file,
     * extracting unique words and updating the frequency map accordingly
     */
    public void calculateFrequency(File directory, Map<String, Double> map) throws IOException
    {
        File[] files = directory.listFiles();

        if (files != null) {
            for (File file : files)
            {
                if (file.isFile())
                {
                    Set<String> uniqueWords = extractWordsFromFile(file);
                    for (String word : uniqueWords) {
                        map.put(word, map.getOrDefault(word, 0.0) + 1.0);
                    }
                }
            }
        }
    }

    /*
     * This function extracts valid alphabetic words from a file and cconverts them
     * to lowercase and stores them in a set. It makes sure the word is valid usingthe
     * isWord function and then returns the set of extracted words
     */
    public Set<String> extractWordsFromFile(File file) throws IOException
    {
        Set<String> words = new HashSet<>();

        if (file.exists())
        {
            try (BufferedReader reader = new BufferedReader(new FileReader(file)))
            {
                String sentence;
                while ((sentence = reader.readLine()) != null)
                {
                    String[] wordTokens = sentence.split("\\s+");
                    for (String word : wordTokens)
                    {
                        if (isWord(word))
                        {
                            words.add(word.toLowerCase());
                        }
                    }
                }
            }
        }

        return words;
    }

    // Checks for both upper and lower case alphabet characters
    private boolean isWord(String word)
    {
        return word.matches("[a-zA-Z]+");
    }

    // ***********************************************************************PROBABILITIES***************************************************************************
    /*
     * Determines the probability of each test file being classified as spam,
     * iterates through the tesst files and calculates their spam probability
     * using the calculateProbability function and then adds the results to a list,
     * and then returns the list of spam probabilities
     */
    public List<Double> calculateProbabilities(List<TestFile> files, Map<String, Double> spamFreqMap, Map<String, Double> hamFreqMap) throws IOException
    {
        List<Double> probabilities = new ArrayList<>();
        for (TestFile testFile : files)
        {
            double spamProb = calculateProbability(testFile.getFilename(), spamFreqMap, hamFreqMap);
            probabilities.add(spamProb);
        }

        return probabilities;
    }

    /*
     * This function determines the chances of a file being spam,
     * proccesses the file and counts the occurrences of its words,
     * then adjusts the probabilities using laplace smoothing,
     * then it compares the probabilities of spam and ham and calculates the total probability of the file being spam
     */
    public double calculateProbability(File file, Map<String, Double> spamFreqMap, Map<String, Double> hamFreqMap) throws IOException
    {
        Set<String> words = extractWordsFromFile(file);

        double spamFiles = spamFreqMap.size();
        double hamFiles = hamFreqMap.size();

        double totalSpamWords = spamFreqMap.values().stream().mapToDouble(Double::doubleValue).sum();
        double totalHamWords = hamFreqMap.values().stream().mapToDouble(Double::doubleValue).sum();

        double prob = Math.log(0.5); // prior probability for spam and ham

        for (String word : words)
        {
            double spamCount = spamFreqMap.getOrDefault(word, 0.0) + 1.0; // Laplace smoothing
            double hamCount = hamFreqMap.getOrDefault(word, 0.0) + 1.0; // Laplace smoothing

            double wordSpamProb = Math.log(spamCount / (totalSpamWords + spamFiles));
            double wordHamProb = Math.log(hamCount / (totalHamWords + hamFiles));

            prob += (wordSpamProb - wordHamProb);
        }

        return Math.exp(prob);
    }

    /*
     * This function calculates the accuracy and precision, it adds the truePositives, falsePositives,
     * trueNegatives, and falseNegatives based on the categorization of spam and ham files,
     * then calculates the accuracy and precision using these
     */
    public void calculatePrecisionAndAccuracy(List<Double> spamProb, List<Double> hamProb)
    {
        double truePositives = 0.0;
        double falsePositives = 0.0;
        double trueNegatives = 0.0;
        double falseNegatives = 0.0;

        for (Double prob : spamProb)
        {
            if (prob > 0.5)
            {
                truePositives++;
            }
            else
            {
                falseNegatives++;
            }
        }

        for (Double prob : hamProb)
        {
            if (prob > 0.5)
            {
                falsePositives++;
            }
            else
            {
                trueNegatives++;
            }
        }

        accuracy = (double) (truePositives + trueNegatives) / (hamProb.size() + spamProb.size());
        precision = (double) truePositives / (truePositives + falsePositives);

        System.out.println("Accuracy: " + accuracy);
        System.out.println("Precision: " + precision);
    }

    public static void main(String[] args) throws URISyntaxException, IOException
    {
        //Creates new instance of spamDetector
        SpamDetector spamDetector = new SpamDetector();

        // Gets the URL for the directory with the data
        URL directoryPath = SpamDetector.class.getClassLoader().getResource("data");

        // If the directory path is null print the following statement
        if (directoryPath == null)
        {
            System.err.println("Directory does not exist");
            return;
        }

        // Convert URL to a URI
        URI uri = directoryPath.toURI();
        // Creates a file object to show the main directory
        File mainDirectory = new File(uri);
        //use the main directory to train and test the spam detector
        spamDetector.trainAndTest(mainDirectory);
    }
}