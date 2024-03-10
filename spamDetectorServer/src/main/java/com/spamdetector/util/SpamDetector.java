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
public class SpamDetector
{
    //making a testresult variable to store all the testresults in
    private ArrayList<TestFile> testResults;
    public static HashMap<String, Integer> trainHamFreq;
    public static HashMap<String, Integer> trainSpamFreq;


    double accuracy, precision; // optimising, declaring with getters instead of own methods with duplicate code

    // Constructor
    public SpamDetector()
    {
        this.trainHamFreq = new HashMap<>();
        this.trainSpamFreq = new HashMap<>();
    }

    // Get testResults
    public ArrayList<TestFile> getTestResults()
    {
        return testResults;
    }

    // Get precision
    public double getPrecision()
    {
        return precision;
    }

    //  Get accuracy
    public double getAccuracy()
    {
        return accuracy;
    }

    // Set accuracy
    public Double setAccuracy(Double accuracy)
    {
        return this.accuracy = accuracy;
    }

    // Set precision
    public Double setPrecision(Double precision)
    {
        return this.precision = precision;
    }

    // ******************************************************************* TRAIN AND TEST METHOD ***********************************************************************
    //        TODO: main method of loading the directories and files, training and testing the model

    /*
     * Implements the training and testing phase, trains the model by calculating word frequencies,
     * iterates through the test files to calculate the spam probabilities, then creates TestFile
     * objects with probabilities and categorizes them
     */
    public List<TestFile> trainAndTest(File mainDirectory) throws URISyntaxException, IOException
    {
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

        File directorySpamProbability = new File(mainDirectory + "/test/spam");
        File[] filesSpamProbability = directorySpamProbability.listFiles();

        // If spam test directory is not null iterate through each spam test directory and get the current file
        if (filesSpamProbability != null)
        {
            for (int i = 0; i < filesSpamProbability.length - 1; i++)
            {
                File file = filesSpamProbability[i];

                // Check if current item is a file not a directory
                if (file.isFile())
                {
                    //extract words from the current file
                    words = extractWordsFromFile(file);

                    for (String word : words)
                    {
                        // Check if the word is valid using the isWord function
                        if(isWord(word))
                        {
                            // Calculate the probability of the word being spam
                            double probability = calculateProbability(word,trainSpamFreq,trainHamFreq);
                            //add the probability to the list of probabilities
                            probabilities.add(probability);
                        }
                    }

                    // Calculate the total probability of the file being spam
                    Double testProb = totalProb(probabilities);
                    String category = " ";

                    // Determine the category based on total probability
                    if(testProb > 0.5)
                    {
                        category = "Spam";
                    }
                    else
                    {
                        category = "Ham";
                    }

                    TestFile spamFiles = new TestFile(file.getName(), testProb, category);

                    testResults.add(spamFiles);
                    SpamProb.add(testProb);
                    probabilities.clear();
                    words.clear();
                }

            }
        }

        File directoryHamProbability = new File(mainDirectory + "/test/ham");
        File[] filesHamProbability = directoryHamProbability.listFiles();

        // Check to see if there are files in the ham test directory, then iterate through each file and get the current file
        if (filesSpamProbability != null)
        {
            for (int i = 0; i < filesHamProbability.length - 1; i++)
            {
                File file = filesHamProbability[i];

                // Check if the current item is a file not a directory
                if (file.isFile())
                {
                    // Extract words from the current file
                    words = extractWordsFromFile(file);

                    for (String word : words)
                    {

                        // Check if word is valid using the isWord function
                        if(isWord(word))
                        {
                            // Calculate the probability of the word being spam
                            double probability = calculateProbability(word,trainSpamFreq,trainHamFreq);
                            // Add the probability to the list of probabilities
                            probabilities.add(probability);
                        }
                    }

                    // Calculate the total probability of the file being spam
                    Double testProb = totalProb(probabilities);
                    String category = " ";

                    // Determine the category based on total probability
                    if(testProb > 0.5)
                    {
                        category = "Spam";
                    }
                    else
                    {
                        category = "Ham";
                    }

                    TestFile HamFiles = new TestFile(file.getName(), testProb, category);

                    testResults.add(HamFiles);
                    HamProb.add(testProb);
                    probabilities.clear();
                    words.clear();
                }
            }
        }

        for(TestFile results : testResults)
        {
            System.out.println(results.getFilename() + " + " + results.getSpamProbability() + " + " + results.getActualClass());
        }

        // Calculates the precision and accuracy
        calculatePrecisionAndAccuracy(testResults,SpamProb, HamProb);
        return testResults;
    }

    // ********************************PREPARING DATA, TRAINING SPAM AND HAM USING CALCULATE FREQUENCY, EXTRACT WORDS< CHECK IF WORD************************************
    /*
     * This function gets the files from a directory and extracts the words from each file,
     * then updates a map with word frequencies and makes sure each word is counted only once per file,
     * then returns the updated map
     */
    public static Map<String, Integer> calculateFrequency(File directory, HashMap<String, Integer> map) throws IOException
    {
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
            }
        }

        return map;
    }

    /*
     * This function reads words from a file and stores them in an ArrayList, then reads the file line by line
     * and splits it into single words and converts eac word to lowercase then adds them to the ArrayList
     */
    public static ArrayList<String> extractWordsFromFile(File file) throws IOException
    {
        ArrayList<String> wordsList = new ArrayList<>();

        if (file.exists())
        {
            BufferedReader words = new BufferedReader(new FileReader(file));
            wordsList = new ArrayList<>();
            String line = null;

            while ((line = words.readLine()) != null)
            {
                String[] word = line.split("\\s+");

                for (String each_word : word)
                {
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

    // *************************************************************************PROBABILITIES***************************************************************************
    /*
     * This function calculates the probability of a given word being spam, it gets the frequency of the word in both spam and ham,
     * then calculates the probability of the word given it's in spam (PrWiS) and given it's in ham (PrWiH),
     * if not in either then return zero, then returns the probability of the word being in spam divided by the total probability of the word occurring
     */
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

    /*
     * Calculates the precision and accuracy of a spam detector using test results and probabilities
     * of spam or ham categorization. It uses truePositives, trueNegatives, falsePositive and
     * falseNegatives to calculate its accuracy and precision
     */
    public void calculatePrecisionAndAccuracy(ArrayList<TestFile> testResults, ArrayList<Double> spamProb, ArrayList<Double> hamProb) throws IOException
    {
        int truePositives = 0;
        int falsePositives = 0;
        int trueNegatives = 0;
        int falseNegatives = 0;

        for(Double spamProbCounter : spamProb)
        {
            if(spamProbCounter > 0.5)
            {
                truePositives++;
            }

        }

        for(Double hamProbCounter : hamProb)
        {
            if(hamProbCounter > 0.5)
            {
                falsePositives++;
            }
            else
            {
                trueNegatives++;
            }
        }

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

    /*
     * This function calculates the total probability based on a lit of probabilities,
     * it iterates through the probabilities,and fixes them if they're zero to avoid errors during
     * the logarithmic calculations, then calculates the natural logarithm of the odds ratio for each
     * probability, adds them and converts the total back to a probability using the logistic function
     */
    public static double totalProb(ArrayList<Double> probability)
    {
        double n = 0;

        for (double prob : probability)
        {
            if (prob == 0)
            {
                prob = 1e-12;
            }
            n = n + (Math.log(1 - prob) - Math.log(prob));
        }

        double total = 1 / (1 + Math.pow(Math.E, n));
        //System.out.println("Total prob  = " + total);
        return total;
    }

    public static void main(String[] args) throws URISyntaxException, IOException
    {
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