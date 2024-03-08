package com.spamdetector.util;

import com.spamdetector.domain.TestFile;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;


/**
 * TODO: This class will be implemented by you
 * You may create more methods to help you organize you strategy and make you code more readable
 */
public class SpamDetector {

    //making a testresult variable to store all the testresults in
    private ArrayList<TestFile> testResults;
    public static HashMap<String, Integer> trainHamFreq;
    public static HashMap<String, Integer> trainSpamFreq;


    double accuracy, precision; // optimising, declaring with getters instead of own methods with duplicate code

    // Constructor
    public SpamDetector() {
        this.trainHamFreq = new HashMap<>();
        this.trainSpamFreq = new HashMap<>();
    }

    public ArrayList<TestFile> getTestResults() {
        return testResults;
    }

    //************************************************************Main Train and Test****************************************************************************
    public List<TestFile> trainAndTest(File mainDirectory) throws URISyntaxException, IOException {
//        TODO: main method of loading the directories and files, training and testing the model

        //retesting word frequency;
        ArrayList<TestFile> testResults = new ArrayList<>();

        File hamDirectory = new File(mainDirectory, "\\train\\ham");
        File spamDirectory = new File(mainDirectory, "\\train\\spam");

        //initialize array lists of words and probabilities to put into Prob map
        ArrayList<String> words = new ArrayList<>();
        ArrayList<Double> probabilities = new ArrayList<>();

        //TRAINING HAM AND SPAM
        calculateFrequency(spamDirectory, trainSpamFreq);
        calculateFrequency(hamDirectory, trainHamFreq);

        testResults.addAll(testing(new File(mainDirectory, "\\test\\ham"), "ham"));
        testResults.addAll(testing(new File(mainDirectory, "\\test\\spam"), "spam"));

        HashMap<String,Double> probMapSpamAndHam = new HashMap<>();
        ArrayList<String> wordList = new ArrayList<>(); //to avoid looping over same words

        //go through files and words in spam and store the word and respective probability in hashmap
        for(String word : trainSpamFreq.keySet())
        {

            double probability = calculateProbability(word, "spam") ;
            probMapSpamAndHam.put(word, probability);
            wordList.add(word);
        }

        for(String word : trainHamFreq.keySet())
        {
            if(!wordList.contains(word))
            {
                double probability = calculateProbability(word, "ham");
                probMapSpamAndHam.put(word, probability);
                wordList.add(word);
            }
        }

        for(String key : probMapSpamAndHam.keySet())
        {
              System.out.println(key + " " + probMapSpamAndHam.get(key));


        }

        //TESTING HAM AND SPAM

        return testResults;
    }

    //will test if the files are ham or spam and then return a list of the files that are ham or spam
    public List<TestFile> testing(File folder, String category) throws IOException
    {
        ArrayList<TestFile> testResults = new ArrayList<>();

        if(!folder.exists())
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
                    double spamProb = calculateFileProbability(file, category);
                    String findCategory = (spamProb > 0.5) ? "spam" : "ham";
                    TestFile testingFile = new TestFile(file.getName(), findCategory);
                    testingFile.setPredictedClass(category);
                    testResults.add(testingFile);
                }
            }
        }
        return testResults;
    }

    //***************************************************Preparing Data, TRAIN HAM AND SPAM using Calculate Frequency, Extract Words, Check if WOrd****************************************
    //get words and their occurrences
    //calculate frequency get that for the file path given to it, used to train
    public static Map<String, Integer> calculateFrequency(File directory, HashMap<String, Integer> map) throws IOException {
        File[] files = directory.listFiles();
        ArrayList<String> uniqueWords = new ArrayList<>();

        if (files != null)
        {
            for (File file : files)
            {
                if(file.isFile())
                {
                    ArrayList<String> sentence = extractWordsFromFile(file);
                    for (String word : sentence)
                    {
                        if(isWord(word) && !uniqueWords.contains(word))
                        {
                                uniqueWords.add(word);
                                map.put(word, map.getOrDefault(word, 0) + 1);
                        }
                    }
                }
                uniqueWords.clear();
            }
        }
        return map;
    }

    //uses buffer reader to read line by line and store words in a simple hashset and return that to calculateFrequency function
    public static ArrayList<String> extractWordsFromFile(File file) throws IOException {
        ArrayList<String> wordsList = null;
        if (file.exists()) {
            BufferedReader words = new BufferedReader(new FileReader(file));
            wordsList = new ArrayList<>();
            String line = null;
            while ((line = words.readLine()) != null) {
                String[] word = line.split("\\s+");
                for (String each_word : word) {
                    wordsList.add(each_word.toLowerCase());
                }
            }
            words.close();
        }
        return wordsList;
    }

    // Check if the input string is a word, removes punctuation and special characters
    private static boolean isWord(String word)
    {
        return word.matches("[a-zA-Z]+");
    }

    //*********************************Probabilities******************************************************************

    //this function will give the probability that a file is spam or ham based on the occurrence of words in the file
    public double calculateFileProbability(File file, String category) throws IOException
    {
        double logSpam = 0.0;
        double logHam = 0.0;
        List<String> words = extractWordsFromFile(file);

        for (String word : words)
        {
            double probability = calculateProbability(word, category);

            if (category.equals("spam"))
            {
                logSpam += Math.log(probability);
            }
            else
            {
                logHam += Math.log(probability);
            }
        }

        return (double) 1.0 / (1.0 + Math.exp(logHam - logSpam));
    }

    //will give the probability that a file is a spam file in the testing phase
    public double calculateProbability(String word, String category) throws IOException
    {
        Map<String, Integer> spamFreqMap = trainSpamFreq;
        Map<String, Integer> hamFreqMap = trainHamFreq;

        int spamFilesWithWi =spamFreqMap.getOrDefault(word,0) ;
        int spamFiles = trainSpamFreq.size();
        int hamFilesWithWi = hamFreqMap.getOrDefault(word,0);
        int hamFiles = trainHamFreq.size();

        double PrWiS = (double) spamFilesWithWi / spamFiles;
        double PrWiH = (double) hamFilesWithWi / hamFiles;

        double PrSWi = PrWiS / (PrWiS + PrWiH);

        return PrSWi;
    }

    //this will calculate the accuracy and precision based on the predicted class and the actual class of the test files
    public void calculatePrecisionAndAccuracy(ArrayList<TestFile> testResults) throws IOException {
        int truePositives = 0;
        int falsePositives = 0;
        int trueNegatives = 0;
        int falseNegatives = 0;

        for (TestFile file : testResults) {
            double spamProbability = calculateProbability(file.getFilename(), "spam");
            String predictedClass = (spamProbability > 0.5) ? "spam" : "ham";
            file.setPredictedClass(predictedClass);

            if (file.getActualClass().equals("spam") && predictedClass.equals("spam")) {
                truePositives++;
            } else if (file.getActualClass().equals("ham") && predictedClass.equals("spam")) {
                falsePositives++;
            } else if (file.getActualClass().equals("ham") && predictedClass.equals("ham")) {
                trueNegatives++;
            } else if (file.getActualClass().equals("spam") && predictedClass.equals("ham")) {
                falseNegatives++;
            }
        }

        // Calculate accuracy and precision
        accuracy = (double) (truePositives + trueNegatives) / (truePositives + trueNegatives + falsePositives + falseNegatives);
        precision = (double) truePositives / (truePositives + falsePositives);

        System.out.println("Accuracy: " + accuracy);
        System.out.println("Precision: " + precision);
    }

    public static void main(String[] args) throws URISyntaxException, IOException {
        // Create an instance of SpamDetector
        SpamDetector spamDetector = new SpamDetector();

        URL directoryPath = SpamDetector.class.getClassLoader().getResource("\\data");
        URI uri = directoryPath.toURI();
        File mainDirectory = new File(uri);

        if(directoryPath == null)
        {
            System.err.println("Directory does not Exist");
            return;
        }

        spamDetector.trainAndTest(mainDirectory);
    }

}
