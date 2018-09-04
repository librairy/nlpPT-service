package org.librairy.service.nlp.service;

import dictionary.DictionaryLoadException;
import lemma.LemmatizeException;
import lemma.Lemmatizer;
import org.librairy.service.nlp.annotators.PosTagger;
import org.librairy.service.nlp.annotators.SentenceDetector;
import org.librairy.service.nlp.annotators.Tokenizer;
import org.librairy.service.nlp.facade.model.Annotation;
import org.librairy.service.nlp.facade.model.Form;
import org.librairy.service.nlp.facade.model.PoS;
import org.librairy.service.nlp.facade.model.Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;
import rank.WordRankingLoadException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 */

public class LemProService {

    private static final Logger LOG = LoggerFactory.getLogger(LemProService.class);

    private final String resourceFolder;

    private SentenceDetector sentenceDetector;
    private PosTagger posTagger;
    private Tokenizer tokenizer;
    private Lemmatizer lemmatizer;

    public LemProService(String resourceFolder){
        this.resourceFolder = resourceFolder;
    }

    public void setup(){

        sentenceDetector = new SentenceDetector(resourceFolder);
        posTagger  = new PosTagger(resourceFolder);
        tokenizer   = new Tokenizer(resourceFolder);
        try {
            lemmatizer  = new Lemmatizer();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String tokens(String text, List<PoS> filter, Form form)  {

        String[] sentences = sentenceDetector.annotate(text);

        List<String[]> tokens = Arrays.stream(sentences).map(sentence -> tokenizer.annotate(sentence)).collect(Collectors.toList());

        List<String> textTokens = new ArrayList<>();

        if ((filter.isEmpty()) && (form.equals(Form.RAW))) return tokens.stream().flatMap(l -> Arrays.stream(l)).collect(Collectors.joining(" "));

        for (String[] sentenceTokens : tokens){

            String[] tags = posTagger.annotate(sentenceTokens);

            List<Integer> valids = IntStream.range(0, sentenceTokens.length).filter(i -> !filter.isEmpty() & filter.contains(PoSTranslator.toPoSTag(tags[i]))).boxed().collect(Collectors.toList());

            if (form.equals(Form.RAW)){
                textTokens.addAll(valids.stream().map(i -> sentenceTokens[i]).collect(Collectors.toList()));
                continue;
            }

            try {
                String[] lemmas = lemmatizer.lemmatize(sentenceTokens, tags);
                textTokens.addAll(valids.stream().map(i -> lemmas[i]).collect(Collectors.toList()));
            } catch (LemmatizeException e) {
                throw new RuntimeException(e);
            }

        }

        return textTokens.stream().collect(Collectors.joining(" "));

    }

    public List<Annotation> annotations(String text, List<PoS> filter)  {
        String[] sentences = sentenceDetector.annotate(text);

        List<String[]> tokens = Arrays.stream(sentences).map(sentence -> tokenizer.annotate(sentence)).collect(Collectors.toList());

        List<Annotation> annotations = new ArrayList<>();

        long offset = 0l;

        for (String[] sentenceTokens : tokens){

            String[] tags = posTagger.annotate(sentenceTokens);

            List<Integer> valids = IntStream.range(0, sentenceTokens.length).filter(i -> !filter.isEmpty() & filter.contains(PoSTranslator.toPoSTag(tags[i]))).boxed().collect(Collectors.toList());

            try {
                String[] lemmas = lemmatizer.lemmatize(sentenceTokens, tags);

                for(int i=0;i<sentenceTokens.length;i++){

                    Annotation annotation = new Annotation();
                    annotation.setOffset(offset++);
                    annotation.setToken(new Token(sentenceTokens[i], lemmas[i], null, PoSTranslator.toPoSTag(tags[i]), null));

                    if (valids.contains(i)) annotations.add(annotation);

                }


            } catch (LemmatizeException e) {
                throw new RuntimeException(e);
            }

        }

        return annotations;
    }

}
