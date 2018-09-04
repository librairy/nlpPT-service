package org.librairy.service.nlp.service;

import org.librairy.service.nlp.facade.model.PoS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

/**
 * @author Badenes Olmedo, Carlos <cbadenes@fi.upm.es>
 */
public class PoSTranslator {

    private static final Logger LOG = LoggerFactory.getLogger(PoSTranslator.class);


    public static PoS toPoSTag(String termPoS){
        switch (termPoS.toUpperCase()){
            case "ADJ":     return PoS.ADJECTIVE;
            case "NUM":
            case "PP":
            case "ADV":     return PoS.ADVERB;
            case "ART":     return PoS.ARTICLE;
            case "CONJ":
            case "CONJ-S":
            case "CONJ-C":     return PoS.CONJUNCTION;
            case "PUNC":     return PoS.INTERJECTION;
            case "EC":
            case "N":     return PoS.NOUN;
            case "PROP":
            case "PRP":     return PoS.PREPOSITION;
            case "PRON":
            case "PRON-DET":
            case "PRON-INDP":
            case "PRON-PERS":     return PoS.PRONOUN;
            case "V-FIN":
            case "V-GER":
            case "V-INF":
            case "V-PCP":
            case "VP":
            case "V":     return PoS.VERB;
            default:
                LOG.warn("PoS tag not handled: '" + termPoS+"'");
                return PoS.CONJUNCTION;
        }
    }

    public static List<String> toTermPoS(PoS type){
        switch (type){
            case ADJECTIVE:     return Arrays.asList(new String[]{"adj"});
            case ADVERB:        return Arrays.asList(new String[]{"pp","adv","num"});
            case ARTICLE:       return Arrays.asList(new String[]{"art"});
            case CONJUNCTION:   return Arrays.asList(new String[]{"punc","conj","conj-s","conj-c"});
            case INTERJECTION:  return Arrays.asList(new String[]{"punc"});
            case NOUN:          return Arrays.asList(new String[]{"n","ec"});
            case PREPOSITION:   return Arrays.asList(new String[]{"prop","prp"});
            case PRONOUN:       return Arrays.asList(new String[]{"pron","pron-det","pron-indp","pron-pers"});
            case VERB:          return Arrays.asList(new String[]{"v-fin","v-ger","v-inf","v-pcp","v","vp"});
            default:            return Arrays.asList(new String[]{});
        }
    }
}
