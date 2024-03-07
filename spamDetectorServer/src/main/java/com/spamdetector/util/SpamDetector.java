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
    public static Map<String, Integer> trainHamFreq;
    public static Map<String, Integer> trainSpamFreq;


    double accuracy, precision; // optimising, declaring with getters instead of own methods with duplicate code
    String category, findCategory;
    // Constructor
    public SpamDetector() {
        this.trainHamFreq = new TreeMap<>();
        this.trainSpamFreq = new TreeMap<>();
    }

    public ArrayList<TestFile> getTestResults() {
        return testResults;
    }
    public double getAccuracy() {
        return accuracy;
    }
    public double getPrecision() {
        return precision;
    }
    public String getActualCategory()
    {
        return category;
    }
    public String getFindingCategory()
    {
        return findCategory;
    }
    //************************************************************Main Train and Test****************************************************************************
    public List<TestFile> trainAndTest(File mainDirectory) throws URISyntaxException, IOException {
//        TODO: main method of loading the directories and files, training and testing the model

        //rtesting word frequency;
        ArrayList<TestFile> testResults = new ArrayList<>();
//        testResults.addAll(testing(new File(mainDirectory, "test/ham"), "ham"));
//        testResults.addAll(testing(new File(mainDirectory, "test/spam"), "spam"));

        //initialize array lists of words and probabilities to put into Prob map
        ArrayList<String> words = new ArrayList<>();
        ArrayList<Double> probabilities = new ArrayList<>();


        File hamDirectory = new File(mainDirectory, "ham");
        File spamDirectory = new File(mainDirectory, "spam");

        //training spam and ham
        trainSpamFreq = trainSpam(spamDirectory);
        trainHamFreq = trainHam(hamDirectory);

        HashMap<String,Double> probMap = new HashMap<>();

        //go through files and words in spam and store the word and respective probability in hashmap
        File[] SpamFiles =  new File(mainDirectory + "/test/spam").listFiles();
        //loop over all files
        for(int i = 0; i < SpamFiles.length - 1; i++)
        {
            File file = SpamFiles[i];
            if(file.isFile())
            {
                words = extractWordsFromFile(file);

                for(String word : words)
                {
                    double probability = calculateProbability(word);
                    probMap.put(word, probability);
                }

            }
        }

        //go through files and words in spam and store the word and respective probability in hashmap
        File[] HamFiles =  new File(mainDirectory + "/test/ham").listFiles();
        //loop over all files
        for(int i = 0; i < HamFiles.length - 1; i++)
        {
            File file = HamFiles[i];
            if(file.isFile())
            {
                words = extractWordsFromFile(file);

                for(String word : words)
                {
                    double probability = calculateProbability(word);
                    probMap.put(word, probability);
                }

            }
        }

        for(String key : probMap.keySet())
        {
            System.out.println(key + " " + probMap.get(key));
        }

        return testResults;
    }

    //will test if the files are ham or spam and then return a list of the files that are ham or spam
//    public List<TestFile> testing(File folder, String category)
//    {
//        ArrayList<TestFile> testResults = new ArrayList<TestFile>();
//
//        if(!folder.exists())
//        {
//            System.err.println("Testing directory doesn't exist");
//            return testResults;
//        }
//
//        File[] files = folder.listFiles();
//        if (files != null)
//        {
//            for (File file : files)
//            {
//                if (file.isFile())
//                {
//                    try
//                    {
//                        double spamProb = calculateProbability(file);
//                        findCategory = (spamProb > 0.5) ? "spam" : "ham";
//
//                        TestFile testingFile = new TestFile(file.getName(), findCategory, category);
//                        testResults.add(testingFile);
//                    } catch (IOException e)
//                    {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }
//        return testResults;
//    }

    //***************************************************Preparing Data, Calculate Frequency, Extract Words, Check if WOrd****************************************
    //get words and their occurrences
    //calculate frequency get that for the file path given to it
    public static Map<String, Integer> calculateFrequency(File directory) throws IOException {
        Map<String, Integer> map = new HashMap<>();
        File[] files = directory.listFiles();
        if (files != null)
        {
            for (File file : files)
            {
                if(file.isFile())
                {
                    ArrayList<String> sentence = extractWordsFromFile(file);

                    for (String word : sentence)
                    {
                        if(isWord(word))
                        {
                            map.put(word, map.getOrDefault(word, 0) + 1);
                        }

                    }
                }
            }
        }
        return map;
    }

    //uses buffer reader to read line by line and store words in a simple hashset and return that to  calculateFrequency function
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

    //***************************Training Spam and Ham******************************************************************
    //trains spam data by reading from spam files and recording number of files the word appeared in
    public static Map<String, Integer> trainSpam(File file) throws FileNotFoundException {

            Set<String> uniqueWords = new HashSet<>();
            Scanner scanner = new Scanner(file); //switched over from buffered reader for variety and ease of use as Scanner is better

            while (scanner.hasNext()) {
                String word = scanner.next().toLowerCase();

                if (isWord(word))
                {
                    if (!uniqueWords.contains(word))
                    {
                        uniqueWords.add(word);
                        trainSpamFreq.put(word, trainSpamFreq.getOrDefault(word, 0) + 1);
                    }
                }
            }
            scanner.close();


        return trainSpamFreq;
    }


    public static Map<String, Integer>  trainHam(File file)
    {
        try {
            Set<String> uniqueWords = new HashSet<>();
            Scanner scanner = new Scanner(file); //switched over from buffered reader for variety and ease of use as Scanner is better

            while (scanner.hasNext()) {
                String word = scanner.next().toLowerCase();

                if (isWord(word))
                {
                    if (!uniqueWords.contains(word))
                    {
                        uniqueWords.add(word);
                        trainHamFreq.put(word, trainHamFreq.getOrDefault(word, 0) + 1);
                    }
                }
            }
            scanner.close();
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
        return trainHamFreq;
    }


    //*********************************Probabilities******************************************************************
    //will give the probability that a file is a spam file in the testing phase
    public double calculateProbability(String word) throws IOException
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


    public static void main(String[] args) throws URISyntaxException, IOException {
        // Create an instance of SpamDetector
        SpamDetector spamDetector = new SpamDetector();

         //Call the training method to demonstrate terminal output
         //spamDetector.training();
//
         URL directoryPath = SpamDetector.class.getClassLoader().getResource("\\data\\test");
         URI uri = directoryPath.toURI();
         File mainDirectory = new File(uri);

          if(directoryPath == null) {System.err.println("Directory does not Exist");
            return;}

         spamDetector.trainAndTest(mainDirectory);
    }

}
