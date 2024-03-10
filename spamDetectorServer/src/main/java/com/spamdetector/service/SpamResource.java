package com.spamdetector.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.spamdetector.domain.TestFile;
import com.spamdetector.util.SpamDetector;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.logging.Logger;


@Path("/spam")
public class SpamResource {

    //    your SpamDetector Class responsible for all the SpamDetecting logic
    SpamDetector detector = new SpamDetector();

    // logger to log messages
    private static final Logger LOGGER = Logger.getLogger(SpamResource.class.getName());


    SpamResource() throws URISyntaxException, IOException {
//        TODO: load resources, train and test to improve performance on the endpoint calls
        LOGGER.info("Training and testing the model, please wait");

//      TODO: call  this.trainAndTest();
        this.trainAndTest();


    }
    @GET
    @Path("/data")
    @Produces("application/json")
    public Response getSpamResults() throws URISyntaxException, JsonProcessingException {
//       TODO: return the test results list of TestFile, return in a Response object

        List<TestFile> results = this.detector.getTestResults();
        // Get the test results from the SpamDetector object

        // Convert the results to JSON format using Jackson library
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        String json = objectMapper.writeValueAsString(results);

        // Returning JSON response with CORS headers
        return Response.status(200)
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
                .header("Access-Control-Allow-Headers", "Content-Type")
                .header("Content-Type", "application/json")
                .entity(json)
                .build();

    }

    @GET
    @Path("/accuracy")
    @Produces("application/json")
    public Response getAccuracy()
    {
//      TODO: return the accuracy of the detector, return in a Response object
        double accuracy = this.detector.getAccuracy();
        // Returning accuracy as plain text with CORS headers
        return Response.status(200)
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
                .header("Access-Control-Allow-Headers", "Content-Type")
                .header("Content-Type", "application/json")
                .entity(Double.toString(accuracy))
                .build();
    }

    @GET
    @Path("/precision")
    @Produces("application/json")
    public Response getPrecision() {
        //TODO: return the precision of the detector, return in a Response object
        double precision = this.detector.getPrecision();
        // Return precision as plain text with CORS headers
        return Response.status(200)
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
                .header("Access-Control-Allow-Headers", "Content-Type")
                .header("Content-Type", "application/json")
                .entity(Double.toString(precision))
                .build();
    }

    private List<TestFile> trainAndTest() throws URISyntaxException, IOException {
        if (this.detector==null){
            this.detector = new SpamDetector();
        }

//        TODO: load the main directory "data" here from the Resources folder
        //getting mainDirectory to pass a a parameter
        URL directory = SpamDetector.class.getClassLoader().getResource("/data");
        //URI uri = directory.getFile();
//        File mainDirectory = new File(uri);
        File mainDirectory = new File(directory.getFile());
        this.detector.trainAndTest(mainDirectory);

        return this.detector.getTestResults();
    }
}