package org.librairy.service.nlp.annotators;

import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
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

public class Tokenizer {

    private static final Logger LOG = LoggerFactory.getLogger(Tokenizer.class);

    private final String resourceFolder;
    private String model;


    public Tokenizer(String resourceFolder){
        this.resourceFolder = resourceFolder;
        model = Paths.get(resourceFolder,"pt-token.bin").toFile().getAbsolutePath();
    }

    public String[] annotate(String text){

        InputStream modelIn;

        try {
            modelIn = new FileInputStream(model);
            TokenizerModel model = new TokenizerModel(modelIn);
            TokenizerME tokenizer = new TokenizerME(model);

            String tokens[] = tokenizer.tokenize(text);

            return tokens;

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
