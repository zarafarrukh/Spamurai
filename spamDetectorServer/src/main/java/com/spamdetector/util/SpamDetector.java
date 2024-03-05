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

    public List<TestFile> trainAndTest(File mainDirectory) {
//        TODO: main method of loading the directories and files, training and testing the model

           //return new ArrayList<TestFile>();
        ArrayList<TestFile> testResults = new ArrayList<>();
        testResults.addAll(testing(new File(mainDirectory, "test/ham"), "ham"));
        testResults.addAll(testing(new File(mainDirectory, "test/spam"), "spam"));

        return testResults;
    }

    //tests the program with spam and ham data
    public void training()
    {
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

            File hamDirectory = new File(mainDirectory, "ham");
            File spamDirectory = new File(mainDirectory, "spam");

            Map<String, Integer> spamMap = (Map<String, Integer>) calculateFrequency(spamDirectory);
            Map<String, Integer> hamMap = (Map<String, Integer>) calculateFrequency(hamDirectory);


            for(String key: spamMap.keySet())
            {
                System.out.println(key + " " + spamMap.get(key));
            }

        } catch (URISyntaxException e)
        {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    //will test if the files are ham or spam and then return a list of the files that are ham or spam
    public List<TestFile> testing(File folder, String category)
    {
        ArrayList<TestFile> testResults = new ArrayList<TestFile>();

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
                    try
                    {
                        double spamProb = calculateProbability(file);
                        String findCategory = (spamProb > 0.5) ? "spam" : "ham";

                        TestFile testingFile = new TestFile(file.getName(), findCategory, category);
                        testResults.add(testingFile);
                    } catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }
        return testResults;
    }

    //get words and their occurrences
    //calculate frequency get that for the file path given to it
    public Map<String, Integer> calculateFrequency(File directory) throws IOException {
        Map<String, Integer> map = new HashMap<>();
        File[] files = directory.listFiles();
        if (files != null)
        {
            for (File file : files)
            {
                if(file.isFile())
                {
                    Set<String> sentence = extractWordsFromFile(file);

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
    public Set<String> extractWordsFromFile(File file) throws IOException {
        Set<String> wordsList = null;
        if (file.exists()) {
            BufferedReader words = new BufferedReader(new FileReader(file));
            wordsList = new HashSet<>();
            String line = null;
            while ((line = words.readLine()) != null) {
                String[] word = line.split("\\s+");
                for (String each_word : word) {
                    wordsList.add(each_word.toLowerCase());
                }
            }
        }
        return wordsList;
    }

    // Check if the input string is a word, removes punctuation and special characters
    private boolean isWord(String word)
    {
        return word.matches("[a-zA-Z]+");
    }

    //***************************Training Spam and Ham******************************************************************
    //trains spam data by reading from spam files and recording word frequency
    public void trainSpam(File file, HashMap<String, Integer> SpamMap)
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
                        SpamMap.put(word, SpamMap.getOrDefault(word, 0) + 1);
                    }
                }
            }
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }


    public void trainHam(File file, HashMap<String, Integer> HamMap)
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
                        HamMap.put(word, HamMap.getOrDefault(word, 0) + 1);
                    }
                }
            }
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }


    //*********************************Probabilities******************************************************************
    //will give the probability that a file is a spam file in the testing phase
    public double calculateProbability(File file) throws IOException
    {
        Set<String> words = extractWordsFromFile(file);
        HashMap<String, Integer> spamFreqMap = new HashMap<String, Integer>();
        HashMap<String, Integer> hamFreqMap = new HashMap<String, Integer>();

        double spamProb = 0.0;
        double hamProb = 0.0;

        for (String word : words)
        {

            int spamCount = spamFreqMap.getOrDefault(word, 0);
            int hamCount = hamFreqMap.getOrDefault(word, 0);

            spamProb += Math.log((spamCount + 1.0) / (spamFreqMap.size() + 2.0));
            hamProb += Math.log((hamCount + 1.0) / (hamFreqMap.size() + 2.0));

        }

        spamProb = Math.pow(Math.E, spamProb);
        hamProb = Math.pow(Math.E, hamProb);

        return spamProb / (spamProb + hamProb);
    }


    public static void main(String[] args)
    {


        // Create an instance of SpamDetector
        SpamDetector spamDetector = new SpamDetector();

        // Call the training method to demonstrate terminal output
        spamDetector.training();
    }

}