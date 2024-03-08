package com.spamdetector.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.File;

/**
 * This class represents a file from the testing data
 * Includes the actual or real class and the predicted class according to the classifier
 * @author CSCI2020U
 */
public class TestFile {
    /**
     * the name of the file this class represents
     */
    @JsonProperty("file")
    private File filename;

    /**
     * the probability of this file belonging to the 'spam' category/class
     */
    @JsonProperty("spamProbability")
    private double spamProbability;

    /**
     * the real class/category of the file; related to the folder it was loaded from 'spam' or 'ham'
     */
    @JsonProperty("actualClass")
    private String actualClass;

    /**
     * the predicted class/category of the file according to the classifier
     */
    @JsonProperty("predictedClass")
    private String predictedClass;

    public TestFile(File filename, String actualClass) {
        this.filename = filename;
        this.actualClass = actualClass;
    }

    /**
     * @return the name of the file
     */
    public File getFilename() {
        return this.filename;
    }

    /**
     * @return the probability of this file being 'spam'
     */
    public double getSpamProbability() {
        return this.spamProbability;
    }

    /**
     * @return the actual/real class of the file
     */
    public String getActualClass() {
        return this.actualClass;
    }

    /**
     * @return the predicted class of the file
     */
    public String getPredictedClass() {
        return this.predictedClass;
    }

    /**
     * Sets the predicted class of the file
     * @param predictedClass the predicted class
     */
    public void setPredictedClass(String predictedClass) {
        this.predictedClass = predictedClass;
    }

    // setter methods for filename, spamProbability, and actualClass
    public void setFilename(File value) { this.filename = value; }
    public void setSpamProbability(double value) { this.spamProbability = value; }
    public void setActualClass(String value) { this.actualClass = value; }
}