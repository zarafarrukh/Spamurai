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

    public double getPrecision() {
        return precision;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public Double setAccuracy(Double accuracy)
    {
        return this.accuracy = accuracy;
    }
    public Double setPrecision(Double precision)
    {
        return this.precision = precision;
    }

    //************************************************************Main Train and Test****************************************************************************
    public List<TestFile> trainAndTest(File mainDirectory) throws URISyntaxException, IOException {
//        TODO: main method of loading the directories and files, training and testing the model

        //retesting word frequency;
        ArrayList<TestFile> testResults = new ArrayList<>();

        File hamDirectory = new File(mainDirectory, "\\train\\ham");
        File ham2Directory = new File(mainDirectory, "\\train\\ham2");
        File spamDirectory = new File(mainDirectory, "\\train\\spam");

        //initialize array lists of words and probabilities to put into Prob map
        ArrayList<String> words = new ArrayList<>();
        ArrayList<Double> probabilities = new ArrayList<>();
        ArrayList<Double> SpamProb = new ArrayList<>();
        ArrayList<Double> HamProb = new ArrayList<>();

        //TRAINING HAM AND SPAM
        calculateFrequency(spamDirectory, trainSpamFreq);
        calculateFrequency(hamDirectory, trainHamFreq);
        calculateFrequency(ham2Directory, trainHamFreq);


        testResults.addAll(testing(new File(mainDirectory, "\\test\\ham"), "ham"));
        testResults.addAll(testing(new File(mainDirectory, "\\test\\spam"), "spam"));


//        //go through files and words in spam and store the word and respective probability in hashmap
//        for(String word : trainSpamFreq.keySet())
//        {
//            double probability = calculateProbability(word, "spam") ;
//            probMapSpam.put(word, probability);
//            probabilities.add(probability);
//        }
//
//        for(String word : trainHamFreq.keySet())
//        {
//            double probability = calculateProbability(word, "ham") ;
//            probMapHam.put(word, probability);
//            probabilities.add(probability);
//        }



        File directorySpamProbability = new File(mainDirectory + "/test/spam");
        File[] filesSpamProbability = directorySpamProbability.listFiles();
        if (filesSpamProbability != null) {
            for (int i = 0; i < filesSpamProbability.length - 1; i++) {
                File file = filesSpamProbability[i];
                if (file.isFile()) {
                    // Check each file
                    words = extractWordsFromFile(file);

                    for (String word : words) {
                        if(isWord(word))
                        {
                            double probability = calculateProbability(word,trainSpamFreq,trainHamFreq);
                            probabilities.add(probability);
                        }


                    }
                    SpamProb.add(totalProb(probabilities));
                    probabilities.clear();
                    words.clear();
                }

            }
        }

        File directoryHamProbability = new File(mainDirectory + "/test/ham");
        File[] filesHamProbability = directoryHamProbability.listFiles();
        if (filesSpamProbability != null) {
            for (int i = 0; i < filesHamProbability.length - 1; i++) {
                File file = filesHamProbability[i];
                if (file.isFile()) {
                    // Check each file
                    words = extractWordsFromFile(file);

                    for (String word : words) {
                        if(isWord(word))
                        {
                            double probability = calculateProbability(word,trainSpamFreq,trainHamFreq);
                            probabilities.add(probability);
                        }
                    }
                    HamProb.add(totalProb(probabilities));
                    probabilities.clear();
                    words.clear();
                }

           }
        }


        //TESTING HAM AND SPAM
        calculatePrecisionAndAccuracy(testResults,SpamProb, HamProb);
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
                    TestFile testFile = new TestFile(file, category);
                    testFile.setActualClass(category);
                    testResults.add(testFile);
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
        {for (File file : files)
            {if(file.isFile())
                {ArrayList<String> sentence = extractWordsFromFile(file);
                    for (String word : sentence)
                    {if(isWord(word) && !uniqueWords.contains(word))
                        {
                            uniqueWords.add(word);
                            map.put(word, map.getOrDefault(word, 0) + 1);
                        }
                    }
                }
                //uniqueWords.clear();
            }
        }
        return map;
    }

    //uses buffer reader to read line by line and store words in a simple hashset and return that to calculateFrequency function
    public static ArrayList<String> extractWordsFromFile(File file) throws IOException {
        ArrayList<String> wordsList = new ArrayList<>();
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

    //will give the probability that a file is a spam file in the testing phase
    public double calculateProbability(String word,Map<String, Integer> spamFreqMap, Map<String, Integer> hamFreqMap ) throws IOException
    {

        int spamFilesWithWi =spamFreqMap.getOrDefault(word,0) ;
        int spamFiles = spamFreqMap.size();
        int hamFilesWithWi = hamFreqMap.getOrDefault(word,0);
        int hamFiles = hamFreqMap.size();

        double PrWiS = (double) spamFilesWithWi / spamFiles;
        double PrWiH = (double) hamFilesWithWi / hamFiles;



        //if the word is not in both ham and spam set probability to zero
        if (spamFilesWithWi == 0 && hamFilesWithWi == 0)
        {
            return 0.0;
        }

        double PrSWi = PrWiS / (PrWiS + PrWiH);


        return PrSWi;
    }

    //this will calculate the accuracy and precision based on the predicted class and the actual class of the test files
    public void calculatePrecisionAndAccuracy(ArrayList<TestFile> testResults, ArrayList<Double> spamProb, ArrayList<Double> hamProb) throws IOException {
        int truePositives = 0;
        int falsePositives = 0;
        int trueNegatives = 0;
        int falseNegatives = 0;

        for(Double spamProbCounter : spamProb)
        {
            if(spamProbCounter > 0.5)
            {truePositives++;}

        }
        for(Double hamProbCounter : hamProb)
        {
            if(hamProbCounter > 0.5)
            {falsePositives++;}
            else{trueNegatives++;}
        }

//        for (TestFile file : testResults) {
////            double spamProbability = totalProb(probabilities);
////            String predictedClass = (spamProbability > 0.5) ? "spam" : "ham";
////            System.out.println("Spam Prob : " + spamProbability + " for file : " + file);
////            file.setPredictedClass(predictedClass);
//
//            if (//file.getActualClass().equals("spam") && predictedClass.equals("spam"))
//            {
//                truePositives++;
//            }
//            else if (file.getActualClass().equals("ham") && predictedClass.equals("spam"))
//            {
//                falsePositives++;
//            }
//            else if (file.getActualClass().equals("ham") && predictedClass.equals("ham"))
//            {
//                trueNegatives++;
//            }
//            else if (file.getActualClass().equals("spam") && predictedClass.equals("ham"))
//            {
//                falseNegatives++;
//            }
//        }

        // Calculate accuracy and precision
        if (truePositives + trueNegatives + falsePositives + falseNegatives != 0)
        {
            System.out.println(truePositives   + " + " + trueNegatives  + " / " + hamProb.size() + " + " + spamProb.size());
            accuracy = (double) (truePositives + trueNegatives) / (hamProb.size() + spamProb.size());
        }
        else
        {
            accuracy = 0.0;
        }

        if (truePositives + falsePositives != 0)
        {
            System.out.println(truePositives + " / " + truePositives + " + " + falsePositives);
            precision = (double) truePositives / (truePositives + falsePositives);
        }
        else
        {
            precision = 0.0;
        }
        setAccuracy(accuracy);
        setPrecision(precision);

        System.out.println("Accuracy: " + getAccuracy());
        System.out.println("Precision: " + getPrecision());
    }

    //probability that a file is spam
    public static double totalProb(ArrayList<Double> probability) {
        double n = 0;

        for (double prob : probability) {
            if (prob == 0) {
                prob = 1e-12;
            }
            n = n + (Math.log(1 - prob) - Math.log(prob));
        }

        double total = 1 / (1 + Math.pow(Math.E, n));
        System.out.println("Total prob  = " + total);
        return total;
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
