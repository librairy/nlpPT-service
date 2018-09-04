package org.librairy.service.nlp.annotators;

import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;

/**
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 */

public class SentenceDetector {

    private static final Logger LOG = LoggerFactory.getLogger(SentenceDetector.class);

    private final String resourceFolder;
    private final String model;


    public SentenceDetector(String resourceFolder){
        this.resourceFolder = resourceFolder;
        model = Paths.get(resourceFolder,"pt-sent.bin").toFile().getAbsolutePath();
    }

    public String[] annotate(String text){

        InputStream modelIn;
        try {
            modelIn = new FileInputStream(model);
            SentenceModel model = new SentenceModel(modelIn);
            SentenceDetectorME sentenceDetector = new SentenceDetectorME(model);

            String sentences[] = sentenceDetector.sentDetect(text);

            return sentences;

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
