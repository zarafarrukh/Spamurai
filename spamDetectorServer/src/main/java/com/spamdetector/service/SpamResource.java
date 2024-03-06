package com.spamdetector.service;

import com.spamdetector.domain.TestFile;
import com.spamdetector.util.SpamDetector;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;

import java.io.File;
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


    SpamResource() throws URISyntaxException {
//        TODO: load resources, train and test to improve performance on the endpoint calls
        LOGGER.info("Training and testing the model, please wait");

//      TODO: call  this.trainAndTest();
        this.trainAndTest();


    }
    @GET
    @Produces("application/json")
    public Response getSpamResults() throws URISyntaxException
    {
//       TODO: return the test results list of TestFile, return in a Response object

        List<TestFile> results = detector.getTestResults();

        return Response.ok(results).build();

    }

    @GET
    @Path("/accuracy")
    @Produces("application/json")
    public Response getAccuracy()
    {
//      TODO: return the accuracy of the detector, return in a Response object
       // double accuracy = detector.getAccuracy();
        return Response.ok().build();

    }

    @GET
    @Path("/precision")
    @Produces("application/json")
    public Response getPrecision() {
              //TODO: return the precision of the detector, return in a Response object
        //double precision = detector.getPrecision();
        return Response.ok().build();

    }

    private List<TestFile> trainAndTest() throws URISyntaxException {
        if (this.detector==null){
            this.detector = new SpamDetector();
        }

//        TODO: load the main directory "data" here from the Resources folder
        //getting mainDirectory to pass a a parameter
//        URL directory = SpamDetector.class.getClassLoader().getResource("/data").getFile();
//        URI uri = directory.toURI();
//        File mainDirectory = new File(uri);
        File mainDirectory = new File("SpamDetector.class.getClassLoader().getResource(\"/data\").getFile()");
        return this.detector.trainAndTest(mainDirectory);
    }
}