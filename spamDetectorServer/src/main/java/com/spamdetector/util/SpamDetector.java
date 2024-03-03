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

        return new ArrayList<TestFile>();
    }

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
        }

    }

    //get words and their occurences
    //calculate frequency get that for the file path given to it
    public Map<String, Integer> calculateFrequency(File directory)
    {
        Map<String, Integer> map = new HashMap<>();
        File[] files = directory.listFiles();
        if (files != null)
        {
            for (File file : files)
            {
                if(file.isFile())
                {
                    Set<String> sentence = extractWordsFromFile(file);

                    for (String word : sentence) {
                        map.put(word, map.getOrDefault(word, 0) + 1);
                    }
                }
            }
        }
        return map;
    }

    //uses buffer reader to read line by line and store words in a simple hashset and return that to  calculateFrequency function
    public Set<String> extractWordsFromFile(File file)
    {
        Set<String> words = new HashSet<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file)))
        {
            String line;
            while ((line = reader.readLine()) != null)
            {
                String[] tokens = line.split("\\s+");
                for (String token : tokens) {
                    words.add(token.toLowerCase());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return words;
    }

    public static void main(String[] args)
    {


        // Create an instance of SpamDetector
        SpamDetector spamDetector = new SpamDetector();

        // Call the training method to demonstrate terminal output
        spamDetector.training();
    }

}