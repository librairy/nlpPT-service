package org.librairy.service.nlp.annotators;

import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import org.librairy.service.nlp.facade.model.PoS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 */

public class PosTagger {

    private static final Logger LOG = LoggerFactory.getLogger(PosTagger.class);

    private final String resourceFolder;
    private String model;

    public PosTagger(String resourceFolder){
        this.resourceFolder = resourceFolder;
        model = Paths.get(resourceFolder,"pt-pos-perceptron.bin").toFile().getAbsolutePath();
    }

    public String[] annotate(String[] sentenceWords){

        InputStream modelIn;
        try {
            modelIn = new FileInputStream(model);
            POSModel model = new POSModel(modelIn);
            POSTaggerME tagger = new POSTaggerME(model);

            String[] tags = tagger.tag(sentenceWords);

            return tags;

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
